package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno.StatoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaAssegnoManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaPagamentiManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.panjea.tesoreria.service.exception.EntitaRateNonCoerentiException;
import it.eurotn.panjea.tesoreria.util.AssegnoDTO;
import it.eurotn.panjea.tesoreria.util.ImmagineAssegno;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAssegni;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.AreaAssegnoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaAssegnoManager")
public class AreaAssegnoManagerBean implements AreaAssegnoManager {

	private static Logger logger = Logger.getLogger(AreaAssegnoManagerBean.class.getName());

	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private DocumentiManager documentiManager;

	@EJB
	private AreaTesoreriaManager areaTesoreriaManager;

	@EJB
	private AreaPagamentiManager areaPagamentiManager;

	@Override
	public void assegnaRapportoBancarioAssegni(RapportoBancarioAzienda rapportoBancarioAzienda,
			List<AreaAssegno> areeAssegno) {
		logger.debug("--> Enter assegnaRapportoBancarioAssegni");

		for (AreaAssegno area : areeAssegno) {

			try {
				area = panjeaDAO.load(AreaAssegno.class, area.getId());
				area.setRapportoBancarioAzienda(rapportoBancarioAzienda);
				panjeaDAO.save(area);
			} catch (Exception e) {
				logger.error("--> errore durante l'assegnazione del rapporto bancario all'area assegno.", e);
				throw new RuntimeException("errore durante l'assegnazione del rapporto bancario all'area assegno.", e);
			}
		}

		logger.debug("--> Exit assegnaRapportoBancarioAssegni");
	}

	/**
	 * Calcola l'importo totale di una lista di pagamenti.
	 * 
	 * @param pagamenti
	 *            la lista di cui calcolare il totale
	 * @return Importo totale della lista di pagamenti
	 */
	private Importo calcolaTotale(List<Pagamento> pagamenti) {
		// CALCOLO IL TOTALE DEL DOCUMENTO
		Importo totale = new Importo();
		// Estraggo il primo pagamento per la prima entità per utilizzare i dati di codiceValuta e tassoDi cambio che
		// sono ugulali per tutti i pagamenti
		Pagamento pagamentoRiferimento = pagamenti.iterator().next();
		totale.setCodiceValuta(pagamentoRiferimento.getImporto().getCodiceValuta());
		totale.setTassoDiCambio(pagamentoRiferimento.getImporto().getTassoDiCambio());
		for (Pagamento pagamento : pagamenti) {
			// Sommo l'importo in valuta
			totale.setImportoInValuta(totale.getImportoInValuta().add(pagamento.getImporto().getImportoInValuta()));
			// All'importo sommo anche l'importo forzato
			totale.setImportoInValuta(totale.getImportoInValuta().add(
					pagamento.getImportoForzato().getImportoInValuta()));
			// Sommo l'importo in valuta azienda
			totale.setImportoInValutaAzienda(totale.getImportoInValutaAzienda().add(
					pagamento.getImporto().getImportoInValutaAzienda()));
			// All'importo sommo anche l'importo in valuta forzato
			totale.setImportoInValutaAzienda(totale.getImportoInValutaAzienda().add(
					pagamento.getImportoForzato().getImportoInValutaAzienda()));
		}
		return totale;
	}

	@Override
	public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate) {
		logger.debug("--> Enter cancellaAreaTesoreria");
		try {
			// controllo se i pagamenti hanno la data, in caso sia valorizzata, non posso cancellare l'seegno dato che
			// esiste un accredito legato.
			AreaAssegno areaAssegno = (AreaAssegno) caricaAreaTesoreria(areaTesoreria.getId());
			if (areaAssegno.getPagamenti().size() > 0) {
				Pagamento p = areaAssegno.getPagamenti().iterator().next();
				if (p.getDataPagamento() != null) {
					DocumentiCollegatiPresentiException documentiPresentiException = new DocumentiCollegatiPresentiException(
							"Impossibile cancellare l'assegno, esiste il documento di accredito collegato");
					throw new RuntimeException(
							"Impossibile cancellare l'assegno, esiste il documento di accredito collegato",
							documentiPresentiException);
				}
			}

			// l'area contabile di area assegno viene cancellata dalla cancellazione dell'area accredito assegno
			panjeaDAO.delete(areaAssegno);

			// cancella l'immagine assegno collegata
			cancellaImmagineAssegno(areaAssegno);

			if (deleteAreeCollegate) {
				documentiManager.cancellaDocumento(areaAssegno.getDocumento());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaAreaTesoreria");
	}

	/**
	 * Cancella le immagini (fronte, retro) dell'assegno scelto.
	 * 
	 * @param areaAssegno
	 *            l'assegno di cui cancellare le immagini
	 */
	private void cancellaImmagineAssegno(AreaAssegno areaAssegno) {
		// carico l'immagine assegno se esiste la chiave ImmagineAssegno.PATH_FOLDER_ASSEGNI
		String folderAssegni = null;
		try {
			Preference preference = caricaPreference(ImmagineAssegno.PATH_FOLDER_ASSEGNI);
			folderAssegni = preference.getValore();
		} catch (PreferenceNotFoundException e) {
			// non faccio nulla sul caricamento se non esiste la chiave
			logger.warn("preference non trovata per la key: " + ImmagineAssegno.PATH_FOLDER_ASSEGNI);
		}
		if (folderAssegni != null) {
			ImmagineAssegno immagineAssegno = new ImmagineAssegno();
			immagineAssegno.setAreaAssegno(areaAssegno);
			try {
				immagineAssegno.cancella(folderAssegni);
				areaAssegno.setImmagineAssegno(immagineAssegno);
			} catch (Exception e) {
				logger.warn("Errore nel caricare l'immagine assegno", e);
			}
		}
	}

	@Override
	public AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException {
		logger.debug("--> Enter caricaAreaTesoreria");

		AreaAssegno areaAssegno = null;
		Query query = panjeaDAO.prepareNamedQuery("AreaAssegno.carica");
		query.setParameter("paramIdAreaAssegno", idAreaTesoreria);
		try {
			areaAssegno = (AreaAssegno) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.error("--> area assegno non trovata con id " + idAreaTesoreria);
			throw e;
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dell'area assegno", e);
			throw new RuntimeException("Errore durante il caricamento dell'area assegno", e);
		}
		logger.debug("--> Exit caricaAreaTesoreria");
		return areaAssegno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaAssegno> caricaAreeAssegno(AreaAccreditoAssegno areaAccreditoAssegno) {
		logger.debug("--> Enter caricaAreaTesoreria");

		List<AreaAssegno> results = null;
		Query query = panjeaDAO.prepareNamedQuery("AreaAssegno.caricaByAreaAccredito");
		query.setParameter("paramIdAreaAccreditoAssegno", areaAccreditoAssegno.getId());
		try {
			results = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dell'area assegno", e);
			throw new RuntimeException("Errore durante il caricamento dell'area assegno", e);
		}

		logger.debug("--> Exit caricaAreaTesoreria");
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AssegnoDTO> caricaAssegni(ParametriRicercaAssegni parametriRicercaAssegni) {
		logger.debug("--> Enter caricaAssegni");
		Map<String, Object> valueParametri = new TreeMap<String, Object>();

		StringBuilder hqlString = new StringBuilder();
		hqlString.append("select distinct ");
		hqlString.append("a.id as idAssegno, ");
		hqlString.append("td.id as idTipoDocumento, ");
		hqlString.append("td.codice as codiceTipoDocumento, ");
		hqlString.append("td.descrizione as descrizioneTipoDocumento, ");
		hqlString.append("td.tipoEntita as tipoEntita, ");
		hqlString.append("doc.id as idDocumento, ");
		hqlString.append("doc.dataDocumento as dataDocumento, ");
		hqlString.append("doc.codice as numeroDocumento,");
		hqlString.append("doc.codiceAzienda as codiceAzienda,");
		hqlString.append("rappBanc.id as idEntitaDocumento, ");
		hqlString.append("rappBanc.descrizione as descrizioneEntitaDocumento, ");
		hqlString.append("doc.totale as totaleDocumento, ");
		hqlString.append("a.numeroAssegno as numeroAssegno, ");
		hqlString.append("a.abi as abi, ");
		hqlString.append("a.cab as cab, ");
		hqlString.append("enRata.id as idEntitaRata, ");
		hqlString.append("enRata.codice as codiceEntitaRata, ");
		hqlString.append("anEntRata.denominazione as denominazioneEntitaRata, ");
		hqlString.append("max(pags.dataPagamento) as maxDataPagamento, ");
		hqlString.append("rappAssegno as rapportoBancarioAzienda ");
		hqlString.append("from AreaAssegno a ");
		hqlString.append("inner join a.documento doc ");
		hqlString.append("inner join doc.tipoDocumento td ");
		hqlString.append("left join doc.rapportoBancarioAzienda rappBanc ");
		hqlString.append("inner join a.pagamenti pags inner join pags.rata r ");
		hqlString.append("left join r.areaRate ar left join ar.documento docRata ");
		hqlString.append("left join a.rapportoBancarioAzienda rappAssegno ");
		hqlString.append("left join docRata.entita enRata left join enRata.anagrafica anEntRata ");

		hqlString.append("where doc.codiceAzienda=:paramCodiceAzienda ");
		valueParametri.put("paramCodiceAzienda", getJecPrincipal().getCodiceAzienda());

		if (parametriRicercaAssegni.getDataDocumento().getDataIniziale() != null) {
			hqlString.append("and doc.dataDocumento<=:paramDataIniziale ");
			valueParametri.put("paramDataIniziale",
					PanjeaEJBUtil.getDateTimeToZero(parametriRicercaAssegni.getDataDocumento().getDataIniziale()));
		}
		if (parametriRicercaAssegni.getDataDocumento().getDataFinale() != null) {
			hqlString.append("and doc.dataDocumento>=:paramDataFinale ");
			valueParametri.put("paramDataFinale",
					PanjeaEJBUtil.getDateTimeToZero(parametriRicercaAssegni.getDataDocumento().getDataFinale()));
		}
		if (parametriRicercaAssegni.getTipoAreaPartita() != null) {
			hqlString.append("and td.id=:paramIdTipoDocumento ");
			valueParametri.put("paramIdTipoDocumento", parametriRicercaAssegni.getTipoAreaPartita().getTipoDocumento()
					.getId());
		}
		if (parametriRicercaAssegni.getEntita() != null) {
			hqlString.append("and enRata.id=:paramIdEntitaRata ");
			valueParametri.put("paramIdEntitaRata", parametriRicercaAssegni.getEntita().getId());
		}
		if (parametriRicercaAssegni.getNumeroAssegno() != null) {
			hqlString.append("and a.numeroAssegno like :paramNumeroAssegno ");
			valueParametri.put("paramNumeroAssegno", parametriRicercaAssegni.getNumeroAssegno());
		}
		if (parametriRicercaAssegni.getStatiAssegno() != null) {
			Set<StatoAssegno> stati = parametriRicercaAssegni.getStatiAssegno();
			if (stati.size() == 1) {
				StatoAssegno stato = stati.iterator().next();
				if (stato.equals(StatoAssegno.CHIUSO)) {
					hqlString.append("and pags.dataPagamento is not null ");
				} else if (stato.equals(StatoAssegno.IN_LAVORAZIONE)) {
					hqlString.append("and pags.dataPagamento is null ");
				}
			}

		}
		hqlString.append("group by a.id");

		Query query = panjeaDAO.prepareQuery(hqlString.toString());
		((QueryImpl) query).getHibernateQuery().setResultTransformer(Transformers.aliasToBean((AssegnoDTO.class)));
		Set<String> set = valueParametri.keySet();
		for (String key : set) {
			Object value = valueParametri.get(key);
			if (value instanceof Date) {
				Date valueDate = (Date) value;
				query.setParameter(key, valueDate, TemporalType.DATE);
			} else {
				query.setParameter(key, valueParametri.get(key));
			}
		}
		List<AssegnoDTO> result = new ArrayList<AssegnoDTO>();
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento degli assegni", e);
			throw new RuntimeException("--> Errore durante il caricamento degli assegni", e);
		}
		logger.debug("--> Exit caricaAssegni");
		return result;
	}

	@Override
	public AreaAssegno caricaImmagineAssegno(AreaAssegno areaAssegno) {
		// carico l'immagine assegno se esiste la chiave ImmagineAssegno.PATH_FOLDER_ASSEGNI
		String folderAssegni = null;
		try {
			Preference preference = caricaPreference(ImmagineAssegno.PATH_FOLDER_ASSEGNI);
			folderAssegni = preference.getValore();
		} catch (PreferenceNotFoundException e) {
			// non faccio nulla sul caricamento se non esiste la chiave
			logger.warn("preference non trovata per la key: " + ImmagineAssegno.PATH_FOLDER_ASSEGNI);
		}
		if (folderAssegni != null) {
			ImmagineAssegno immagineAssegno = new ImmagineAssegno();
			immagineAssegno.setAreaAssegno(areaAssegno);
			try {
				immagineAssegno.carica(folderAssegni);
				areaAssegno.setImmagineAssegno(immagineAssegno);
			} catch (Exception e) {
				logger.warn("Errore nel caricare l'immagine assegno", e);
			}
		}
		return areaAssegno;
	}

	/**
	 * Carica la preference secondo la chiave richiesta.
	 * 
	 * @param key
	 *            la chiave da caricare
	 * @return Preference
	 * @throws PreferenceNotFoundException
	 *             PreferenceNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private Preference caricaPreference(String key) throws PreferenceNotFoundException {
		logger.debug("--> Enter caricaPreference key " + key);
		javax.persistence.Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
		query.setParameter("paramChiave", key);
		List<Preference> preferences = null;
		try {
			preferences = panjeaDAO.getResultList(query);
		} catch (ObjectNotFoundException onf) {
			logger.error("--> nessuna preference trovata per la preference " + key);
			throw new PreferenceNotFoundException(key);
		} catch (Exception e) {
			logger.error("--> errore ricerca preference con key " + key, e);
			throw new RuntimeException(e);
		}
		if (preferences.size() == 0) {
			throw new PreferenceNotFoundException(key);
		}
		Preference preference = preferences.get(0);
		return preference;
	}

	@Override
	public AreaAssegno creaAreaAssegno(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			List<Pagamento> pagamenti) throws DocumentoDuplicateException, EntitaRateNonCoerentiException {

		// Verifico che non sia già presente un'area assegno con numero assegno - abi - cab richiesti
		Query query = panjeaDAO
				.prepareQuery("select count(aa.id) from AreaAssegno aa where aa.numeroAssegno = :numeroAssegno and aa.abi = :abi and aa.cab = :cab");
		query.setParameter("numeroAssegno", parametriCreazioneAreaChiusure.getNumeroAssegno());
		query.setParameter("abi", parametriCreazioneAreaChiusure.getAbi());
		query.setParameter("cab", parametriCreazioneAreaChiusure.getCab());
		Long areePresenti;
		try {
			areePresenti = (Long) panjeaDAO.getSingleResult(query);
		} catch (Exception e1) {
			logger.error("--> errore durnate il caricamento delle aree assegno.", e1);
			throw new RuntimeException("errore durnate il caricamento delle aree assegno.", e1);
		}

		if (areePresenti.intValue() > 0) {
			throw new DocumentoDuplicateException(" assegno " + parametriCreazioneAreaChiusure.getNumeroAssegno()
					+ " abi " + parametriCreazioneAreaChiusure.getAbi() + " cab "
					+ parametriCreazioneAreaChiusure.getCab());
		}

		// Verifico se l'entità è sempre la stessa altrimenti blocco tutto avvertendo l'utente che i dati non sono
		// corretti (le rate sono di clienti diversi)
		Integer idEntita = null;
		for (Pagamento pagamento : pagamenti) {
			EntitaLite entita = pagamento.getRata().getAreaRate().getDocumento().getEntita();
			Integer idNew = entita.getId();
			if (idEntita != null && idEntita.compareTo(idNew) != 0) {
				throw new EntitaRateNonCoerentiException("La lista di rate non è della stessa entità");
			}
			idEntita = new Integer(idNew.intValue());
		}

		Importo totaleDocumento = calcolaTotale(pagamenti);

		Documento doc = new Documento();
		doc.setCodiceAzienda(getJecPrincipal().getCodiceAzienda());
		doc.setDataDocumento(parametriCreazioneAreaChiusure.getDataDocumento());
		doc.setTipoDocumento(parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoDocumento());
		doc.setEntita(null);
		doc.setRapportoBancarioAzienda(null);
		doc.setTotale(totaleDocumento);
		doc.setContrattoSpesometro(null);

		Documento docSalvato;
		docSalvato = documentiManager.salvaDocumento(doc);

		AreaAssegno areaAssegno = new AreaAssegno();

		areaAssegno.setDocumento(docSalvato);
		areaAssegno.setNumeroAssegno(parametriCreazioneAreaChiusure.getNumeroAssegno());
		areaAssegno.setAbi(parametriCreazioneAreaChiusure.getAbi());
		areaAssegno.setCab(parametriCreazioneAreaChiusure.getCab());
		areaAssegno.setCodicePagamento(null);
		areaAssegno.setSpeseIncasso(parametriCreazioneAreaChiusure.getSpeseIncasso());
		areaAssegno.setTipoAreaPartita(parametriCreazioneAreaChiusure.getTipoAreaPartita());

		AreaAssegno assegnoSalvato = (AreaAssegno) areaTesoreriaManager.salvaAreaTesoreria(areaAssegno);

		for (Pagamento pagamento : pagamenti) {
			pagamento.setAreaChiusure(assegnoSalvato);
			areaPagamentiManager.salvaPagamento(pagamento);
		}

		// l'area contabile dall'area assegno la genero in un secondo momento, alla creazione dell'area accredito
		// assegno.

		// salvo l'immagine assegno se presente; deve essere definita la preference con chiave
		// ImmagineAssegno.PATH_FOLDER_ASSEGNI se esiste l'immagine.
		ImmagineAssegno immagineAssegno = parametriCreazioneAreaChiusure.getImmagine();
		try {
			if (immagineAssegno != null) {
				immagineAssegno.setAreaAssegno(assegnoSalvato);
				Preference pref = caricaPreference(ImmagineAssegno.PATH_FOLDER_ASSEGNI);
				immagineAssegno.salva(pref.getValore());
			}
		} catch (Exception e) {
			logger.error("Errore nel salvare l'immagine assegno", e);
			throw new RuntimeException("Errore nel salvare l'immagine assegno", e);
		}

		return assegnoSalvato;
	}

	@Override
	public List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria) {
		// l'area assegno non ha area emissione effetti quindi ritorno una lista vuota
		return new ArrayList<AreaEffetti>();
	}

	/**
	 * recupera {@link JecPrincipal} dal {@link SessionContext}.
	 * 
	 * @return utente loggato
	 */
	private JecPrincipal getJecPrincipal() {
		logger.debug("--> Enter getJecPrincipal");
		return (JecPrincipal) context.getCallerPrincipal();
	}

}
