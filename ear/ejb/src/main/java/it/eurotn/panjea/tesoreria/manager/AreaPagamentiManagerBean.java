package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoTesoreriaManager;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaPagamentiContabilitaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaPagamentiManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.AreaTesoreriaManager;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AreaPagamentiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaPagamentiManager")
public class AreaPagamentiManagerBean implements AreaPagamentiManager {
	private static Logger logger = Logger.getLogger(AreaPagamentiManagerBean.class.getName());

	@Resource
	protected SessionContext context;

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected DocumentiManager documentiManager;

	@EJB
	protected AreaTesoreriaManager areaTesoreriaManager;

	@EJB
	protected AreaPagamentiContabilitaManager areaPagamentiContabilitaManager;

	@EJB
	protected AreaContabileManager areaContabileManager;

	@EJB
	protected AreaContabileCancellaManager areaContabileCancellaManager;

	@EJB
	private RitenutaAccontoTesoreriaManager ritenutaAccontoTesoreriaManager;

	@Override
	public void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate) {
		logger.debug("--> Enter cancellaAreaTesoreria");

		try {
			areaTesoreria = caricaAreaTesoreria(areaTesoreria.getId());
			if (deleteAreeCollegate) {
				// cancello l'area contabile se esiste
				AreaContabileLite areaContabileLite = areaContabileManager
						.caricaAreaContabileLiteByDocumento(areaTesoreria.getDocumento());
				if (areaContabileLite != null) {
					areaContabileCancellaManager.cancellaAreaContabile(areaTesoreria.getDocumento(), true);
				}
			}

			ritenutaAccontoTesoreriaManager.cancellaRataRitenutaAccontoAssociata((AreaPagamenti) areaTesoreria);

			panjeaDAO.delete(areaTesoreria);

			if (deleteAreeCollegate) {
				// cancello il documento
				documentiManager.cancellaDocumento(areaTesoreria.getDocumento());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit cancellaAreaTesoreria");
	}

	@Override
	public void cancellaPagamento(Pagamento pagamento) {
		logger.debug("--> Enter cancellaPagamento");

		logger.debug("--> Exit cancellaPagamento");

	}

	@Override
	public AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException {
		logger.debug("--> Enter caricaAreaTesoreria");

		AreaPagamenti result = null;
		Query query = panjeaDAO.prepareNamedQuery("AreaPagamenti.carica");
		query.setParameter("paramIdAreaPagamenti", idAreaTesoreria);
		try {
			result = (AreaPagamenti) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.error("--> area pagamenti non trovata id " + idAreaTesoreria);
			throw e;
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dell'area pagamenti", e);
			throw new RuntimeException("Errore durante il caricamento dell'area pagamenti", e);
		}

		logger.debug("--> Exit caricaAreaTesoreria");
		return result;
	}

	@Override
	public List<AreaPagamenti> creaAreaPagamenti(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			List<Pagamento> pagamenti) throws DocumentoDuplicateException {
		logger.debug("--> Enter creaAreaChiusure");

		List<AreaPagamenti> listResult = new ArrayList<AreaPagamenti>();
		Map<Object, List<Pagamento>> pagamentiRaggruppati = null;

		switch (parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoOperazione()) {
		case GESTIONE_DISTINTA:
			pagamentiRaggruppati = organizzaPagamentiPerEntita(pagamenti);
			listResult.add(creaAreaPagamentiConDistinta(pagamentiRaggruppati, parametriCreazioneAreaChiusure));
			break;
		default:
			if (parametriCreazioneAreaChiusure.getTipoAreaPartita().isChiusuraSuPagamentoUnico()) {
				pagamentiRaggruppati = organizzaPagamentiPerPagamentoUnico(pagamenti);
			} else if (parametriCreazioneAreaChiusure.isCompensazioneRate()) {
				pagamentiRaggruppati = organizzaPagamentiPerAnagrafica(pagamenti);
			} else {
				pagamentiRaggruppati = organizzaPagamentiPerEntita(pagamenti);
			}

			listResult = creaAreaPartitaSenzaDistinta(pagamentiRaggruppati, parametriCreazioneAreaChiusure);
			break;
		}

		logger.debug("--> Exit creaAreaChiusure");
		return listResult;
	}

	/**
	 * metodo di utilita' che registra i documenti per pagamenti passivi con distinta.<br>
	 * (Pagamenti caso bonifico, rid, ecc.).<br>
	 * Un documento solo
	 * 
	 * @param pagamentiPerEntita
	 *            pagamenti
	 * @param parametriCreazioneAreaChiusure
	 *            parametri di creazione
	 * @return area pagamenti creata
	 * @throws DocumentoDuplicateException
	 *             DocumentoDuplicateException
	 */
	private AreaPagamenti creaAreaPagamentiConDistinta(Map<Object, List<Pagamento>> pagamentiPerEntita,
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) throws DocumentoDuplicateException {
		logger.debug("--> Enter creaAreaPartitaConDistinta");
		if (parametriCreazioneAreaChiusure.getRapportoBancarioAzienda() == null) {
			throw new IllegalArgumentException("Rapporto bancario NULLO!");
		}

		// CALCOLO IL TOTALE DEL DOCUMENTO
		Importo totaleDocumento = new Importo();
		// Estraggo il primo pagamento per la prima entità per utilizzare i dati di codiceValuta e tassoDi cambio che
		// sono ugulali per tutti i pagamenti
		Pagamento pagamentoRiferimento = pagamentiPerEntita.values().iterator().next().get(0);
		totaleDocumento.setCodiceValuta(pagamentoRiferimento.getImporto().getCodiceValuta());
		totaleDocumento.setTassoDiCambio(pagamentoRiferimento.getImporto().getTassoDiCambio());

		for (List<Pagamento> arrays : pagamentiPerEntita.values()) {
			for (Pagamento pagamento : arrays) {
				// Sommo l'importo in valuta
				totaleDocumento.setImportoInValuta(totaleDocumento.getImportoInValuta().add(
						pagamento.getImporto().getImportoInValuta()));
				// All'importo sommo anche l'importo forzato
				totaleDocumento.setImportoInValuta(totaleDocumento.getImportoInValuta().add(
						pagamento.getImportoForzato().getImportoInValuta()));
				// Sommo l'importo in valuta azienda
				totaleDocumento.setImportoInValutaAzienda(totaleDocumento.getImportoInValutaAzienda().add(
						pagamento.getImporto().getImportoInValutaAzienda()));
				// All'importo sommo anche l'importo in valuta forzato
				totaleDocumento.setImportoInValutaAzienda(totaleDocumento.getImportoInValutaAzienda().add(
						pagamento.getImportoForzato().getImportoInValutaAzienda()));
			}
		}

		Documento doc = new Documento();
		doc.setCodiceAzienda(getJecPrincipal().getCodiceAzienda());
		doc.setDataDocumento(parametriCreazioneAreaChiusure.getDataDocumento());
		doc.setTipoDocumento(parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoDocumento());
		doc.setEntita(null);
		doc.setRapportoBancarioAzienda(parametriCreazioneAreaChiusure.getRapportoBancarioAzienda());
		doc.setTotale(totaleDocumento);
		doc.setContrattoSpesometro(null);
		Documento docSalvato;
		docSalvato = documentiManager.salvaDocumento(doc);
		AreaPagamenti areaPagamenti = new AreaPagamenti();
		areaPagamenti.setDocumento(docSalvato);
		areaPagamenti.setTipoAreaPartita(parametriCreazioneAreaChiusure.getTipoAreaPartita());
		areaPagamenti.setCodicePagamento(null);
		areaPagamenti.setSpeseIncasso(parametriCreazioneAreaChiusure.getSpeseIncasso());
		AreaPagamenti areaPagamentiSalvata;
		areaPagamentiSalvata = (AreaPagamenti) areaTesoreriaManager.salvaAreaTesoreria(areaPagamenti);

		for (Object entita : pagamentiPerEntita.keySet()) {
			// per ogni entita....
			List<Pagamento> pags = pagamentiPerEntita.get(entita);
			// aggiorno rate e salvo pagamenti
			for (Pagamento pagamento : pags) {
				pagamento.setAreaChiusure(areaPagamentiSalvata);
				try {
					pagamento = panjeaDAO.save(pagamento);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			panjeaDAO.getEntityManager().refresh(areaPagamentiSalvata);
		}

		// creo l'area contabile
		try {
			areaPagamentiSalvata = areaPagamentiContabilitaManager.creaAreaContabilePagamentoDistinta(
					areaPagamentiSalvata, parametriCreazioneAreaChiusure);
		} catch (PagamentiException e) {
			logger.error("--> Errore durante la creazione dell'area contabile dell'area pagamenti.", e);
			throw new RuntimeException("Errore durante la creazione dell'area contabile dell'area pagamenti.", e);
		}

		logger.debug("--> Exit creaAreaPartitaConDistinta");
		return areaPagamentiSalvata;
	}

	/**
	 * metodo di utilita' che registra i documenti per pagamenti senza distinta.<br>
	 * Viene crato un documento per ogni entita.
	 * 
	 * @param parametriCreazioneAreaChiusure
	 *            parametri di creazione
	 * @param pagamentiRaggruppati
	 *            pagamenti
	 * @return lista di aree pagamento create
	 * @throws DocumentoDuplicateException
	 *             DocumentoDuplicateException
	 */
	private List<AreaPagamenti> creaAreaPartitaSenzaDistinta(Map<Object, List<Pagamento>> pagamentiRaggruppati,
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) throws DocumentoDuplicateException {
		logger.debug("--> Enter creaAreaPartitaSenzaDistinta");

		Set<AreaPagamenti> areePagamentiSalvate = new HashSet<AreaPagamenti>();

		AreaPagamenti areaPagamento = null;

		// per ogni entita....
		for (Object gruppo : pagamentiRaggruppati.keySet()) {
			List<Pagamento> pagamentiGruppo = pagamentiRaggruppati.get(gruppo);

			Importo totaleDocumento = new Importo();
			// Estraggo il primo pagamento per la prima entità per utilizzare i dati di codiceValuta e tassoDi cambio
			// che sono ugulali per tutti i pagamenti
			Pagamento pagamentoRiferimento = pagamentiGruppo.get(0);
			totaleDocumento.setCodiceValuta(pagamentoRiferimento.getImporto().getCodiceValuta());
			totaleDocumento.setTassoDiCambio(pagamentoRiferimento.getImporto().getTassoDiCambio());

			for (Pagamento pagamento : pagamentiGruppo) {
				// Sommo l'importo in valuta
				totaleDocumento.setImportoInValuta(totaleDocumento.getImportoInValuta().add(
						pagamento.getImporto().getImportoInValuta()));
				// All'importo sommo anche l'importo forzato
				totaleDocumento.setImportoInValuta(totaleDocumento.getImportoInValuta().add(
						pagamento.getImportoForzato().getImportoInValuta()));
				// Sommo l'importo in valuta azienda
				totaleDocumento.setImportoInValutaAzienda(totaleDocumento.getImportoInValutaAzienda().add(
						pagamento.getImporto().getImportoInValutaAzienda()));
				// All'importo sommo anche l'importo in valuta forzato
				totaleDocumento.setImportoInValutaAzienda(totaleDocumento.getImportoInValutaAzienda().add(
						pagamento.getImportoForzato().getImportoInValutaAzienda()));
			}

			Documento doc = null;
			if (parametriCreazioneAreaChiusure.isCompensazioneRate()) {
				doc = getDocumentoAreaPartitaSenzaDistinta(totaleDocumento, parametriCreazioneAreaChiusure);
			} else {
				SedeEntita sedeEntita = pagamentoRiferimento.getRata().getAreaRate().getDocumento().getSedeEntita();
				doc = getDocumentoAreaPartitaSenzaDistinta(gruppo, sedeEntita, totaleDocumento,
						parametriCreazioneAreaChiusure);
			}

			areaPagamento = getAreaPagamentoSenzaDistinta(areaPagamento, doc, parametriCreazioneAreaChiusure);

			for (Pagamento pagamento : pagamentiGruppo) {
				pagamento.setDataPagamento(parametriCreazioneAreaChiusure.getDataDocumento());
				pagamento.setAreaChiusure(areaPagamento);
				salvaPagamento(pagamento);
			}

			panjeaDAO.getEntityManager().refresh(areaPagamento);
			areePagamentiSalvate.add(areaPagamento);
		}
		// creo l'area contabile per le aree pagamenti presenti
		for (AreaPagamenti areaPagamenti : areePagamentiSalvate) {
			try {
				areaPagamenti = areaPagamentiContabilitaManager.creaAreaContabilePagamentoDiretto(areaPagamenti,
						parametriCreazioneAreaChiusure);
			} catch (PagamentiException e) {
				logger.error("--> Errore durante la creazione dell'area contabile dell'area pagamenti.", e);
				throw new RuntimeException("Errore durante la creazione dell'area contabile dell'area pagamenti.", e);
			}
		}

		logger.debug("--> Exit creaAreaPartitaSenzaDistinta");
		List<AreaPagamenti> list = new ArrayList<AreaPagamenti>();
		list.addAll(areePagamentiSalvate);
		return list;
	}

	/**
	 * Restituisce una area pagamento nuova o quella esistente a seconda dell'opzione
	 * tipoAreaPartita.chiusuraSuPagamentoUnico.
	 * 
	 * @param areaPagamento
	 *            l' area pagamento di origine
	 * @param doc
	 *            il documento di origine
	 * @param parametriCreazioneAreaChiusure
	 *            i parametri per scegliere quale areaPagamenti restituire
	 * @return AreaPagamenti
	 */
	private AreaPagamenti getAreaPagamentoSenzaDistinta(AreaPagamenti areaPagamento, Documento doc,
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) {
		boolean chiusuraSuPagamentoUnico = parametriCreazioneAreaChiusure.getTipoAreaPartita()
				.isChiusuraSuPagamentoUnico();
		if (areaPagamento == null || !chiusuraSuPagamentoUnico) {
			areaPagamento = new AreaPagamenti();
			areaPagamento.setDocumento(doc);
			areaPagamento.setTipoAreaPartita(parametriCreazioneAreaChiusure.getTipoAreaPartita());
			areaPagamento.setCodicePagamento(null);
			areaPagamento.setSpeseIncasso(parametriCreazioneAreaChiusure.getSpeseIncasso());
			areaPagamento = (AreaPagamenti) areaTesoreriaManager.salvaAreaTesoreria(areaPagamento);
		}
		return areaPagamento;
	}

	@Override
	public List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria) {
		return new ArrayList<AreaEffetti>();
	}

	/**
	 * Restituisce un documento nuovo.
	 * 
	 * @param totaleDocumento
	 *            il totale per il documento
	 * @param parametriCreazioneAreaChiusure
	 *            i parametri per comporre il documento
	 * @return Documento
	 * @throws DocumentoDuplicateException
	 *             DocumentoDuplicateException
	 */
	private Documento getDocumentoAreaPartitaSenzaDistinta(Importo totaleDocumento,
			ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure) throws DocumentoDuplicateException {

		return getDocumentoAreaPartitaSenzaDistinta(null, null, totaleDocumento, parametriCreazioneAreaChiusure);
	}

	/**
	 * Restituisce un documento nuovo.
	 * 
	 * @param entita
	 *            l'entità per il documento
	 * @param sedeEntita
	 *            la sede entità del documento della rata
	 * @param totaleDocumento
	 *            il totale per il documento
	 * @param parametriCreazioneAreaChiusure
	 *            i parametri per comporre il documento
	 * @return Documento
	 * @throws DocumentoDuplicateException
	 *             DocumentoDuplicateException
	 */
	private Documento getDocumentoAreaPartitaSenzaDistinta(Object entita, SedeEntita sedeEntita,
			Importo totaleDocumento, ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure)
			throws DocumentoDuplicateException {

		Documento doc = new Documento();
		doc.setCodiceAzienda(getJecPrincipal().getCodiceAzienda());
		doc.setDataDocumento(parametriCreazioneAreaChiusure.getDataDocumento());
		doc.setTipoDocumento(parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoDocumento());
		doc.setEntita(null);
		doc.setContrattoSpesometro(null);

		TipoEntita tipoEntita = parametriCreazioneAreaChiusure.getTipoAreaPartita().getTipoDocumento().getTipoEntita();
		switch (tipoEntita) {
		case AZIENDA:
			// documento di pagamento relativo all'azienda
			doc.setRapportoBancarioAzienda(null);
			break;
		case BANCA:
			// documento di pagamento relativo ad un rapporto bancario (non
			// ci dovrebbe essere ... ma non si sa mai)
			if (parametriCreazioneAreaChiusure.getRapportoBancarioAzienda() == null) {
				throw new IllegalArgumentException("Rapporto bancario NULLO!");
			}
			doc.setRapportoBancarioAzienda(parametriCreazioneAreaChiusure.getRapportoBancarioAzienda());
			break;
		case CLIENTE:
		case FORNITORE:
		case VETTORE:
			if (entita instanceof EntitaLite) {
				doc.setEntita((EntitaLite) entita);
			}
			if (sedeEntita != null) {
				doc.setSedeEntita(sedeEntita);
			}
			break;
		default:
			break;
		}
		// il totale documento deve essere sempre positivo
		doc.setTotale(totaleDocumento.abs());

		doc = documentiManager.salvaDocumento(doc);
		return doc;
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

	/**
	 * Metodo di utilita' che serve a raggruppare i pagamenti per ogni anagrafica.
	 * 
	 * @param pagamenti
	 *            pagamenti da raggruppare
	 * @return pagamenti per anagrafica
	 */
	private Map<Object, List<Pagamento>> organizzaPagamentiPerAnagrafica(List<Pagamento> pagamenti) {
		logger.debug("--> Enter raggruppaPagamenti");
		Map<Object, List<Pagamento>> raggruppamenti = new HashMap<Object, List<Pagamento>>();
		for (Pagamento pagamento : pagamenti) {

			TipoEntita tipoEntitaPagamento = pagamento.getRata().getAreaRate().getDocumento().getTipoDocumento()
					.getTipoEntita();

			switch (tipoEntitaPagamento) {
			case CLIENTE:
			case FORNITORE:
				// Cliente o Fornitore
				AnagraficaLite anagrafica = pagamento.getRata().getAreaRate().getDocumento().getEntita()
						.getAnagrafica();

				List<Pagamento> pags = new ArrayList<Pagamento>();
				if (raggruppamenti.containsKey(anagrafica)) {
					pags = raggruppamenti.get(anagrafica);
				}
				pags.add(pagamento);
				raggruppamenti.put(anagrafica, pags);
				break;
			case AZIENDA:
				// HACK creo un nuovo oggetto senza caricarlo dato che non sono necessari dati particolari per la
				// creazione del documento di pagamento
				AziendaLite aziendaLite = new AziendaLite();
				aziendaLite.setId(1);
				aziendaLite.setCodice(getJecPrincipal().getCodiceAzienda());

				List<Pagamento> pagsAzienda = new ArrayList<Pagamento>();
				if (raggruppamenti.containsKey(aziendaLite)) {
					pagsAzienda = raggruppamenti.get(aziendaLite);
				}
				pagsAzienda.add(pagamento);
				raggruppamenti.put(aziendaLite, pagsAzienda);
				break;
			default:
				break;
			}
		}
		logger.debug("--> Exit raggruppaPagamenti");
		return raggruppamenti;
	}

	/**
	 * Metodo di utilita' che serve a raggruppare i pagamenti per ogni entita.
	 * 
	 * @param pagamenti
	 *            pagamenti da raggruppare
	 * @return pagamenti per entita'
	 */
	private Map<Object, List<Pagamento>> organizzaPagamentiPerEntita(List<Pagamento> pagamenti) {
		logger.debug("--> Enter raggruppaPagamenti");
		Map<Object, List<Pagamento>> raggruppamenti = new HashMap<Object, List<Pagamento>>();
		for (Pagamento pagamento : pagamenti) {

			TipoEntita tipoEntitaPagamento = pagamento.getRata().getAreaRate().getDocumento().getTipoDocumento()
					.getTipoEntita();

			switch (tipoEntitaPagamento) {
			case CLIENTE:
			case FORNITORE:
				// Cliente o Fornitore
				EntitaLite entita = pagamento.getRata().getAreaRate().getDocumento().getEntita();

				List<Pagamento> pags = new ArrayList<Pagamento>();
				if (raggruppamenti.containsKey(entita)) {
					pags = raggruppamenti.get(entita);
				}
				pags.add(pagamento);
				raggruppamenti.put(entita, pags);
				break;
			case AZIENDA:
				// HACK creo un nuovo oggetto senza caricarlo dato che non sono necessari dati particolari per la
				// creazione del documento di pagamento
				AziendaLite aziendaLite = new AziendaLite();
				aziendaLite.setId(1);
				aziendaLite.setCodice(getJecPrincipal().getCodiceAzienda());

				List<Pagamento> pagsAzienda = new ArrayList<Pagamento>();
				if (raggruppamenti.containsKey(aziendaLite)) {
					pagsAzienda = raggruppamenti.get(aziendaLite);
				}
				pagsAzienda.add(pagamento);
				raggruppamenti.put(aziendaLite, pagsAzienda);
				break;
			default:
				break;
			}
		}
		logger.debug("--> Exit raggruppaPagamenti");
		return raggruppamenti;
	}

	/**
	 * Restituisce una mappa con un unica chiave, alla quale vengono assegnati tutti i pagamenti.
	 * 
	 * @param pagamenti
	 *            parametri
	 * @return mappa
	 */
	private Map<Object, List<Pagamento>> organizzaPagamentiPerPagamentoUnico(List<Pagamento> pagamenti) {
		Map<Object, List<Pagamento>> pagamentiRaggruppati;
		pagamentiRaggruppati = new HashMap<Object, List<Pagamento>>();
		pagamentiRaggruppati.put(new Object(), pagamenti);
		return pagamentiRaggruppati;
	}

	@Override
	public Pagamento salvaPagamento(Pagamento pagamento) {
		logger.debug("--> Enter salvaPagamento");
		Pagamento pagamentoSalvato;
		try {
			pagamentoSalvato = panjeaDAO.save(pagamento);

			ritenutaAccontoTesoreriaManager.aggiornaRataRitenutaAcconto(pagamentoSalvato);
		} catch (Exception e) {
			logger.error("--> Errore in salvaPagamento", e);
			throw new RuntimeException(e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit salvaPagamento " + pagamentoSalvato.getId());
		}
		return pagamentoSalvato;
	}
}
