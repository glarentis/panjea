package it.eurotn.panjea.anagrafica.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.codice.generator.interfaces.LastCodiceGenerator;
import it.eurotn.dao.exception.CodiceAlreadyExistsException;
import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficheManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.ContattiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.EntitaManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.anagrafica.service.exception.SedeAnagraficaOrphanException;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita.FieldSearch;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.EntitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.EntitaManager")
public class EntitaManagerBean implements EntitaManager {
	private static Logger logger = Logger.getLogger(EntitaManagerBean.class);
	@Resource
	private SessionContext sessionContext;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private SediEntitaManager sediEntitaManager;

	@EJB
	private AnagraficheManager anagraficaManager;

	@EJB
	private ContattiManager contattiManager;

	@IgnoreDependency
	@EJB
	private LastCodiceGenerator lastCodiceGenerator;

	@Override
	public void cambiaSedePrincipaleEntita(SedeEntita sedeEntita, TipoSedeEntita tipoSedeEntita) {
		logger.debug("--> Enter cambiaSedePrincipaleEntita");
		try {
			// recupera il riferimento di Entita
			Entita entita = sedeEntita.getEntita();
			// recupera la sede entita principale corrente
			SedeEntita sedeEntitaPrincipaleEsistente = sediEntitaManager.caricaSedePrincipaleEntita(entita);
			// sostituisci il tipo sede entita alla sede entita principale
			// esistente
			sedeEntitaPrincipaleEsistente.setTipoSede(tipoSedeEntita);
			panjeaDAO.save(sedeEntitaPrincipaleEsistente);

			// aggiorno la sede anagrafica di Entita
			SedeEntita se = sediEntitaManager.salvaSedeEntita(sedeEntita);
			entita = se.getEntita();
			// assegna la nuova sede entita principale
			entita.getAnagrafica().setSedeAnagrafica(se.getSede());
			panjeaDAO.save(entita);
		} catch (Exception e) {
			logger.error("--> errore, impossibile salvare la nuova sede entita principale ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cambiaSedePrincipaleEntita");
	}

	@Override
	public void cancellaAnagraficheOrfane() throws AnagraficaServiceException {
		logger.debug("--> Enter cancellaAnagraficheOrfane");
		Query query = panjeaDAO.prepareNamedQuery("Anagrafica.cancellaOrfane");
		try {
			panjeaDAO.executeQuery(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaEntita");
	}

	@Override
	public void cancellaEntita(Entita entita) throws AnagraficaServiceException {
		logger.debug("--> Enter cancellaEntita");
		if (entita.isNew() || entita.getAnagrafica().isNew()) {
			logger.error("--> Impossibile cancellare un Entita con id entita o id anagrafica nullo");
			throw new IllegalArgumentException("Impossibile cancellare un Entita con id entita o id anagrafica nullo");
		}
		logger.debug("--> cancellazione di Contatti");
		contattiManager.cancellaContattiPerEntita(entita);
		entita = caricaEntita(entita.getEntitaLite(), false);
		Set<SedeEntita> sedi = entita.getSedi();
		for (SedeEntita sedeEntita : sedi) {
			try {
				sediEntitaManager.cancellaSedeEntita(sedeEntita, true);
			} catch (SedeAnagraficaOrphanException e) {
				logger.warn("--> Lanciato l'errore in un punto non previsto.");
			}
		}
		try {
			panjeaDAO.delete(entita);
			cancellaAnagraficheOrfane();
			logger.debug("--> eseguita cancellazione dell'entit�");
		} catch (DAOException e) {
			logger.error("--> Impossibile eliminare l'entit� con id = " + entita.getId(), e);
			throw new RuntimeException(e);
		}
		try {
			anagraficaManager.eliminaSediAnagraficheOrfane();
		} catch (Exception e) {
			logger.error("--> impossibile cancellare le sedi entita anagrafiche! errore comando ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaEntita");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Agente> caricaAgentiSenzaCapoArea() {
		logger.debug("--> Enter caricaAgentiSenzaCapoArea");
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("ent.id,ent.version,ent.codice,anag.denominazione ");
		sb.append("from anag_entita ent ");
		sb.append("inner join anag_anagrafica anag on anag.id=ent.anagrafica_id ");
		sb.append("where ent.TIPO_ANAGRAFICA='A' ");
		// Query query =
		// panjeaDAO.prepareNamedQuery("Agente.caricaSenzaCapoarea");
		SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString(), Agente.class);
		List<Agente> agenti = new ArrayList<Agente>();

		try {
			agenti = query.list();
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento degli agenti senza capo area", e);
			throw new RuntimeException("errore durante il caricamento degli agenti senza capo area", e);
		}

		return agenti;
	}

	@Override
	public List<EntitaLite> caricaClienti(String codice, String descrizione) {

		ParametriRicercaEntita parametri = new ParametriRicercaEntita();
		parametri.setTipoEntita(TipoEntita.CLIENTE);
		parametri.setFieldSearch(codice != null ? FieldSearch.CODICE : FieldSearch.DESCRIZIONE);
		parametri.setCodice(codice);
		parametri.setDescrizione(descrizione);

		return ricercaEntita(parametri);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClienteLite> caricaClientiConCodiceEsternoDaVerificare() {
		logger.debug("--> Enter caricaClientiConCodiceEsternoDaVerificare");
		Query query = panjeaDAO.prepareQuery("select c from ClienteLite c where c.codiceEsterno like '#*'");
		List<ClienteLite> clientiDaVerificare = null;
		try {
			clientiDaVerificare = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore nel cercare i clienti da verificare", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaClientiConCodiceEsternoDaVerificare");
		return clientiDaVerificare;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EntitaLite> caricaEntita(AnagraficaLite anagraficaLite) {
		logger.debug("--> Enter caricaEntita");

		Query query = panjeaDAO.prepareNamedQuery("EntitaLite.caricaByAnagrafica");
		query.setParameter("paramIdAnagrafica", anagraficaLite.getId());

		List<EntitaLite> entita = new ArrayList<EntitaLite>();

		try {
			entita = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento delle entita legate all'anagrafica.", e);
			throw new RuntimeException("errore durante il caricamento delle entita legate all'anagrafica.", e);
		}

		logger.debug("--> Exit caricaEntita");
		return entita;
	}

	@Override
	public Entita caricaEntita(Entita entita, Boolean caricaLazy) throws AnagraficaServiceException {
		logger.debug("--> Enter caricaEntita");
		Integer idEntita = entita.getId();
		Entita entitaLoad = null;
		try {
			entitaLoad = panjeaDAO.load(Entita.class, idEntita);
		} catch (DAOException e) {
			throw new AnagraficaServiceException(e);
		}
		if (!caricaLazy) {
			entitaLoad.getSedi().size();
			if (entitaLoad.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getCap() != null) {
				entitaLoad.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getCap().getLocalita().size();
			}
			if (entitaLoad.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getLocalita() != null) {
				entitaLoad.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getLocalita().getCap().size();
			}
		}
		logger.debug("--> Exit caricaEntita");
		// Devo inizializzare l'embedded
		entitaLoad.getBloccoSede();
		return entitaLoad;
	}

	@Override
	public Entita caricaEntita(EntitaLite entitaLite, Boolean caricaLazy) throws AnagraficaServiceException {
		logger.debug("--> Enter caricaEntita");
		Entita entita = null;
		entita = caricaEntita(entitaLite.creaProxyEntita(), caricaLazy);
		logger.debug("--> Exit caricaEntita");
		return entita;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EntitaLite caricaEntita(TipoEntita tipoEntita, Integer codice) {
		EntitaLite result = null;

		String className = "EntitaLite";
		if (tipoEntita != null) {
			className = ((Class<EntitaLite>) ObjectUtils.defaultIfNull(tipoEntita.getClassTipoEntitaLite(),
					EntitaLite.class)).getSimpleName();
		}

		StringBuilder sb = new StringBuilder(
				"select e.id as id ,e.codice as codice, e.version as version, e.assortimentoArticoli as assortimentoArticoli, a.denominazione as denominazione, e.class as tipo, loc.descrizione as descrizioneLocalita,");
		sb.append(
				"a.partiteIVA as partitaIVA, a.codiceFiscale as codiceFiscale,e.fido as fido, sa.indirizzo as indirizzoSede ");
		sb.append(" from ");
		sb.append(className);
		sb.append(" e inner join e.anagrafica a ");
		sb.append(" inner join a.sedeAnagrafica sa ");
		sb.append(" left join sa.datiGeografici.localita loc ");
		sb.append(" where e.anagrafica.codiceAzienda= :paramCodiceAzienda and  ");
		sb.append(" e.codice = :paramCodice ");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		((QueryImpl) query).getHibernateQuery().setResultTransformer(Transformers.aliasToBean((EntitaLite.class)));

		query.setParameter("paramCodiceAzienda", getCurrentPrincipal().getCodiceAzienda());
		query.setParameter("paramCodice", codice);

		try {
			result = (EntitaLite) panjeaDAO.getSingleResult(query);
		} catch (DAOException e) {
			result = null;
		}
		return result;
	}

	@Override
	public EntitaLite caricaEntitaLite(EntitaLite entitaLite) throws AnagraficaServiceException {
		logger.debug("--> Enter caricaEntitaLite");
		EntitaLite entitaLoad;
		try {
			entitaLoad = panjeaDAO.load(EntitaLite.class, entitaLite.getId());
		} catch (ObjectNotFoundException e) {
			throw new AnagraficaServiceException(e);
		}
		logger.debug("--> Exit caricaEntita");
		return entitaLoad;
	}

	/**
	 * @param codiceEsterno
	 *            codiceEsterno
	 * @return Vettore
	 * @throws AnagraficaServiceException
	 *             AnagraficaServiceException
	 */
	@Override
	public Vettore caricaVettorePerCodiceImportazione(Integer codiceEsterno) throws AnagraficaServiceException {
		try {
			logger.debug("--> Enter caricaVettorePerCodiceImportazione");
			Query query = panjeaDAO.prepareNamedQuery("Vettore.caricaByCodiceImportazione");
			query.setParameter("paramCodiceAzienda", getCurrentPrincipal().getCodiceAzienda());
			query.setParameter("paramCodiceImportazione", codiceEsterno);
			Vettore vettore = (Vettore) panjeaDAO.getSingleResult(query);
			logger.debug("--> Exit caricaClientePerCodiceImportazione");
			return vettore;
		} catch (DAOException ex) {
			throw new AnagraficaServiceException(ex);
		}
	}

	@Override
	public Entita confermaClientePotenziale(Integer idEntita) {
		Query query = panjeaDAO.getEntityManager()
				.createNativeQuery("update anag_entita set TIPO_ANAGRAFICA='C',codice=null where id=" + idEntita);
		int updatedRows = 0;
		try {
			updatedRows = panjeaDAO.executeQuery(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}

		// devo aggiornare il codice risalvando l'entita' e associare un conto
		// al cliente confermato nota che per
		// salvare il conto devo avere la versione dell'entita' a 0 la annullo
		// quindi con l'sql
		if (updatedRows == 1) {
			Entita entita = null;
			try {
				entita = panjeaDAO.load(Entita.class, idEntita);
				entita.setCodice(lastCodiceGenerator.nextCodice(entita.getClass(), "anagrafica.codiceAzienda"));
			} catch (ObjectNotFoundException e) {
				logger.warn("--> entita non trovata con id " + idEntita);
				throw new RuntimeException(e);
			}
			// associa il nuovo codice impostato a null dalla query sql
			try {
				entita = panjeaDAO.save(entita);
				entita = caricaEntita(entita, false);
			} catch (Exception e) {
				logger.error("--> errore in confermaClientePotenziale", e);
				throw new RuntimeException(e);
			}

			return entita;
		}
		return null;
	}

	/**
	 * Restituisce il principal corrente.
	 *
	 * @return JecPrincipal
	 */
	private JecPrincipal getCurrentPrincipal() {
		JecPrincipal jecPrincipal = (JecPrincipal) sessionContext.getCallerPrincipal();
		return jecPrincipal;
	}

	/**
	 * Prepara la query per controllare l'univocità dell'entità che si vuole
	 * inserire, quindi non devo trovare nessuna entità con la combinazione
	 * partita iva e codice fiscale.
	 *
	 * @param partitaIva
	 *            partitaIva
	 * @param codiceFiscale
	 *            codiceFiscale
	 * @return la query per caricare una entità con partita iva e codice fiscale
	 *         specificati
	 */
	private String getQueryEntitaUnivoca(String partitaIva, String codiceFiscale) {
		StringBuilder sb = new StringBuilder("select e from Entita e join e.anagrafica a where ");
		sb.append("(");
		sb.append("a.partiteIVA");
		if (partitaIva == null) {
			sb.append(" is null ");
		} else {
			sb.append("=:paramPartiteIVA ");
		}
		sb.append("and a.codiceFiscale");
		if (codiceFiscale == null) {
			sb.append(" is null");
		} else {
			sb.append("=:paramCodiceFiscale");
		}
		sb.append(")");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EntitaLite> ricercaEntita(ParametriRicercaEntita parametriRicercaEntita) {
		logger.debug("--> Enter ricercaEntita");
		String className;
		List<EntitaLite> result = new ArrayList<EntitaLite>();
		className = "EntitaLite";
		if (parametriRicercaEntita.getTipoEntita() != null) {
			switch (parametriRicercaEntita.getTipoEntita()) {
			case CLIENTE:
				className = "ClienteLite";
				break;
			case FORNITORE:
				className = "FornitoreLite";
				break;
			case VETTORE:
				className = "VettoreLite";
				break;
			case AGENTE:
				className = "AgenteLite";
				break;
			case CLIENTE_POTENZIALE:
				className = "ClientePotenzialeLite";
				break;
			default:
				className = "EntitaLite";
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> CLASS NAME " + className);
		}
		String orderBy = "";

		StringBuilder sb = new StringBuilder(
				"select e.id as id ,e.codice as codice, e.version as version, e.assortimentoArticoli as assortimentoArticoli, a.denominazione as denominazione, e.class as tipo, loc.descrizione as descrizioneLocalita,");
		sb.append(
				"sa.indirizzo as indirizzoSede, a.partiteIVA as partitaIVA, a.codiceFiscale as codiceFiscale, a.id as idAnagrafica, sa.id as idSedeAnagrafica, naz.descrizione as descrizioneNazione, ");
		sb.append(
				" cap.descrizione as descrizioneCAP, sa.fax as faxSede, sa.telefono as telefonoSede, liv1.nome as liv1Nome, liv2.nome as liv2Nome, liv3.nome as liv3Nome, ");
		sb.append(
				" liv4.nome as liv4Nome, e.abilitato as abilitato, a.version as versionAnagrafica, sa.version as versionSedeAnagrafica,e.fido as fido ");
		sb.append(" from ");
		sb.append(className);
		sb.append(" e inner join e.anagrafica a ");
		sb.append(" inner join a.sedeAnagrafica sa ");
		sb.append(" left join sa.datiGeografici.livelloAmministrativo1 liv1 ");
		sb.append(" left join sa.datiGeografici.livelloAmministrativo2 liv2 ");
		sb.append(" left join sa.datiGeografici.livelloAmministrativo3 liv3 ");
		sb.append(" left join sa.datiGeografici.livelloAmministrativo4 liv4 ");
		sb.append(" left join sa.datiGeografici.localita loc ");
		sb.append(" left join sa.datiGeografici.nazione naz ");
		sb.append(" left join sa.datiGeografici.cap cap ");
		sb.append(" where e.anagrafica.codiceAzienda= :paramCodiceAzienda ");
		Map<String, Object> valueParametri = new TreeMap<String, Object>();
		valueParametri.put("paramCodiceAzienda", getCurrentPrincipal().getCodiceAzienda());

		if (parametriRicercaEntita.getAbilitato() != null) {
			sb.append(" and e.abilitato=:paramAbilitato ");
			valueParametri.put("paramAbilitato", parametriRicercaEntita.getAbilitato());
		}

		if (parametriRicercaEntita.getTipiEntitaList() != null) {

			StringBuilder tipiEntiListSB = new StringBuilder();
			for (TipoEntita tipoEntita : parametriRicercaEntita.getTipiEntitaList()) {

				if (tipiEntiListSB.length() != 0) {
					tipiEntiListSB.append(" or ");
				}
				switch (tipoEntita) {
				case CLIENTE:
					tipiEntiListSB.append(" e.class = " + ClienteLite.class.getName());
					break;
				case FORNITORE:
					tipiEntiListSB.append(" e.class = " + FornitoreLite.class.getName());
					break;
				case VETTORE:
					tipiEntiListSB.append(" e.class = " + VettoreLite.class.getName());
					break;
				case AGENTE:
					tipiEntiListSB.append(" e.class = " + AgenteLite.class.getName());
					break;
				default:
				}
			}

			sb.append(" and " + tipiEntiListSB.toString());
		}

		if ("ClienteLite".equals(className) && !parametriRicercaEntita.isIncludiEntitaPotenziali()) {
			sb.append(" and e.class != it.eurotn.panjea.anagrafica.domain.ClientePotenziale ");
		}

		switch (parametriRicercaEntita.getFieldSearch()) {
		case CODICE:
			String codice = "";
			if (parametriRicercaEntita.getCodice() != null) {
				codice = parametriRicercaEntita.getCodice();
			}
			sb.append(" and e.codice like cast(:paramCodice as string)");
			valueParametri.put("paramCodice", codice + "%");
			orderBy = " order by e.codice ";
			break;
		case DESCRIZIONE:
			String descrizione = "";
			if (parametriRicercaEntita.getDescrizione() != null) {
				descrizione = parametriRicercaEntita.getDescrizione();
			}
			sb.append(" and e.anagrafica.denominazione like :paramDenominazione ");
			valueParametri.put("paramDenominazione", descrizione.replace("*", "%") + "%");
			orderBy = " order by e.anagrafica.denominazione ";
			break;
		case PARTITAIVA:
			String partitaIva = "";
			if (parametriRicercaEntita.getPartitaIva() != null) {
				partitaIva = parametriRicercaEntita.getPartitaIva();
			}
			sb.append(" and e.anagrafica.partiteIVA like :paramPartIva ");
			valueParametri.put("paramPartIva", partitaIva.replace("*", "%") + "%");
			orderBy = " order by e.anagrafica.partiteIVA ";
			break;
		case CODICEFISCALE:
			String codiceFiscale = "";
			if (parametriRicercaEntita.getCodiceFiscale() != null) {
				codiceFiscale = parametriRicercaEntita.getCodiceFiscale();
			}
			sb.append(" and e.anagrafica.codiceFiscale like :paramCodiceFiscale ");
			valueParametri.put("paramCodiceFiscale", codiceFiscale.replace("*", "%") + "%");
			orderBy = " order by e.anagrafica.codiceFiscale ";
			break;
		default:
			orderBy = " order by e.codice ";
			break;
		}

		sb.append(orderBy);

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("--> Query da eseguire " + sb.toString());
			}
			org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate())
					.createQuery(sb.toString());
			query.setResultTransformer(Transformers.aliasToBean(EntitaLite.class));

			for (Entry<String, Object> entry : valueParametri.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
			result = query.list();
		} catch (Exception e) {
			logger.error("--> Errore durante la ricerca delle entità.", e);
			throw new RuntimeException("Errore durante la ricerca delle entità.", e);
		}
		logger.debug("--> Exit ricercaEntita");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EntitaLite> ricercaEntita(String codiceFiscale, String partitaIva) {
		logger.debug("--> Enter ricercaEntita");
		List<EntitaLite> result = new ArrayList<EntitaLite>();

		StringBuilder sb = new StringBuilder(
				"select e.id as id ,e.codice as codice, e.version as version ,a.denominazione as denominazione, e.class as tipo, loc.descrizione as descrizioneLocalita,");
		sb.append(
				"sa.indirizzo as indirizzoSede, a.partiteIVA as partitaIVA, a.codiceFiscale as codiceFiscale, a.id as idAnagrafica, sa.id as idSedeAnagrafica, naz.descrizione as descrizioneNazione, ");
		sb.append(
				" cap.descrizione as descrizioneCAP, sa.fax as faxSede, sa.telefono as telefonoSede, liv1.nome as liv1Nome, liv2.nome as liv2Nome, liv3.nome as liv3Nome, ");
		sb.append(
				" liv4.nome as liv4Nome, e.abilitato as abilitato, a.version as versionAnagrafica, sa.version as versionSedeAnagrafica,e.fido as fido ");
		sb.append(" from EntitaLite ");
		sb.append(" e inner join e.anagrafica a ");
		sb.append(" inner join a.sedeAnagrafica sa ");
		sb.append(" left join sa.datiGeografici.livelloAmministrativo1 liv1 ");
		sb.append(" left join sa.datiGeografici.livelloAmministrativo2 liv2 ");
		sb.append(" left join sa.datiGeografici.livelloAmministrativo3 liv3 ");
		sb.append(" left join sa.datiGeografici.livelloAmministrativo4 liv4 ");
		sb.append(" left join sa.datiGeografici.localita loc ");
		sb.append(" left join sa.datiGeografici.nazione naz ");
		sb.append(" left join sa.datiGeografici.cap cap ");
		sb.append(" where e.anagrafica.codiceAzienda= :paramCodiceAzienda ");
		Map<String, Object> valueParametri = new TreeMap<String, Object>();
		valueParametri.put("paramCodiceAzienda", getCurrentPrincipal().getCodiceAzienda());

		if (codiceFiscale != null && !codiceFiscale.isEmpty()) {
			sb.append(" and e.anagrafica.codiceFiscale like :paramCodiceFiscale ");
			valueParametri.put("paramCodiceFiscale", codiceFiscale.replace("*", "%") + "%");
		} else {
			sb.append(" and e.anagrafica.codiceFiscale is null ");
		}

		if (partitaIva != null && !partitaIva.isEmpty()) {
			sb.append(" and e.anagrafica.partiteIVA like :paramPartIva ");
			valueParametri.put("paramPartIva", partitaIva.replace("*", "%") + "%");
		} else {
			sb.append(" and e.anagrafica.partiteIVA is null ");
		}

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("--> Query da eseguire " + sb.toString());
			}
			org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate())
					.createQuery(sb.toString());
			query.setResultTransformer(Transformers.aliasToBean(EntitaLite.class));

			for (Entry<String, Object> entry : valueParametri.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
			result = query.list();
		} catch (Exception e) {
			logger.error("--> Errore durante la ricerca delle entità.", e);
			throw new RuntimeException("Errore durante la ricerca delle entità.", e);
		}
		logger.debug("--> Exit ricercaEntita");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EntitaLite> ricercaEntitaSearchObject(ParametriRicercaEntita parametriRicercaEntita) {
		List<EntitaLite> result = new ArrayList<EntitaLite>();

		if (parametriRicercaEntita.getTipoEntita() != null
				&& TipoEntita.BANCA == parametriRicercaEntita.getTipoEntita()) {
			return result;
		}

		String className = "EntitaLite";

		if (parametriRicercaEntita.getTipoEntita() != null) {
			className = ((Class<EntitaLite>) ObjectUtils
					.defaultIfNull(parametriRicercaEntita.getTipoEntita().getClassTipoEntitaLite(), EntitaLite.class))
							.getSimpleName();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> CLASS NAME " + className);
		}
		String orderBy = "";

		StringBuilder sb = new StringBuilder(
				"select e.id as id ,e.codice as codice, e.version as version, e.assortimentoArticoli as assortimentoArticoli, a.denominazione as denominazione, e.class as tipo, loc.descrizione as descrizioneLocalita,");
		sb.append(
				"a.partiteIVA as partitaIVA, a.codiceFiscale as codiceFiscale,e.fido as fido, sa.indirizzo as indirizzoSede ");
		sb.append(" from ");
		sb.append(className);
		sb.append(" e inner join e.anagrafica a ");
		sb.append(" inner join a.sedeAnagrafica sa ");
		sb.append(" left join sa.datiGeografici.localita loc ");
		sb.append(" where e.anagrafica.codiceAzienda= :paramCodiceAzienda ");

		Map<String, Object> valueParametri = new TreeMap<String, Object>();
		valueParametri.put("paramCodiceAzienda", getCurrentPrincipal().getCodiceAzienda());

		if (parametriRicercaEntita.getAbilitato() != null) {
			sb.append(" and e.abilitato=:paramAbilitato ");
			valueParametri.put("paramAbilitato", parametriRicercaEntita.getAbilitato());
		}

		if (parametriRicercaEntita.getTipiEntitaList() != null) {

			StringBuilder tipiEntiListSB = new StringBuilder();
			for (TipoEntita tipoEntita : parametriRicercaEntita.getTipiEntitaList()) {

				if (tipiEntiListSB.length() != 0) {
					tipiEntiListSB.append(" or ");
				}
				tipiEntiListSB.append(" e.class = " + tipoEntita.getClassTipoEntitaLite().getName());
			}

			sb.append(" and (").append(tipiEntiListSB.toString()).append(") ");
		}

		if ("ClienteLite".equals(className) && !parametriRicercaEntita.isIncludiEntitaPotenziali()) {
			sb.append(" and e.class != it.eurotn.panjea.anagrafica.domain.ClientePotenziale ");
		}

		switch (parametriRicercaEntita.getFieldSearch()) {
		case CODICE:
			String codice = "";
			if (parametriRicercaEntita.getCodice() != null) {
				codice = parametriRicercaEntita.getCodice();
			}
			sb.append(" and e.codice like cast(:paramCodice as string)");
			valueParametri.put("paramCodice", codice + "%");
			orderBy = " order by e.codice ";
			break;
		case DESCRIZIONE:
			String descrizione = "";
			if (parametriRicercaEntita.getDescrizione() != null) {
				descrizione = parametriRicercaEntita.getDescrizione();
			}
			sb.append(" and e.anagrafica.denominazione like :paramDenominazione ");
			valueParametri.put("paramDenominazione", descrizione.replace("*", "%") + "%");
			orderBy = " order by e.anagrafica.denominazione ";
			break;
		case PARTITAIVA:
			String partitaIva = "";
			if (parametriRicercaEntita.getPartitaIva() != null) {
				partitaIva = parametriRicercaEntita.getPartitaIva();
			}
			sb.append(" and e.anagrafica.partiteIVA like :paramPartIva ");
			valueParametri.put("paramPartIva", partitaIva.replace("*", "%") + "%");
			orderBy = " order by e.anagrafica.partiteIVA ";
			break;
		case CODICEFISCALE:
			String codiceFiscale = "";
			if (parametriRicercaEntita.getCodiceFiscale() != null) {
				codiceFiscale = parametriRicercaEntita.getCodiceFiscale();
			}
			sb.append(" and e.anagrafica.codiceFiscale like :paramCodiceFiscale ");
			valueParametri.put("paramCodiceFiscale", codiceFiscale.replace("*", "%") + "%");
			orderBy = " order by e.anagrafica.codiceFiscale ";
			break;
		default:
			break;
		}

		sb.append(orderBy);

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("--> Query da eseguire " + sb.toString());
			}
			org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate())
					.createQuery(sb.toString());
			query.setResultTransformer(Transformers.aliasToBean(EntitaLite.class));

			for (Entry<String, Object> entry : valueParametri.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
			result = query.list();
		} catch (Exception e) {
			logger.error("--> Errore durante la ricerca delle entità.", e);
			throw new RuntimeException("Errore durante la ricerca delle entità.", e);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Entita salvaEntita(Entita entita) throws AnagraficaServiceException, AnagraficheDuplicateException {
		logger.debug("--> Enter salvaEntita");

		Entita entitaSave = null;

		// se non ho un codice definito per l'entità da salvare ne assegno uno
		// automatico.
		if (entita.getCodice() == null) {
			entita.setCodice(lastCodiceGenerator.nextCodice(entita.getClass(), "anagrafica.codiceAzienda"));
		}

		/**
		 * se il codice fiscale o la partita iva sono vuote, le setto a null.
		 */
		if ((entita.getAnagrafica().getCodiceFiscale() != null)
				&& (entita.getAnagrafica().getCodiceFiscale().isEmpty())) {
			entita.getAnagrafica().setCodiceFiscale(null);
		}
		if ((entita.getAnagrafica().getPartiteIVA() != null) && (entita.getAnagrafica().getPartiteIVA().isEmpty())) {
			entita.getAnagrafica().setPartiteIVA(null);
		}

		List<Entita> entitaRicerca = null;
		try {
			// se non ho ne partita iva ne codice fiscale, non devo controllare
			// l'univocità
			if (entita.getAnagrafica().getCodiceFiscale() != null || entita.getAnagrafica().getPartiteIVA() != null) {
				Anagrafica anagraficaPresente = null;

				String queryEntitaUnivoca = getQueryEntitaUnivoca(entita.getAnagrafica().getPartiteIVA(),
						entita.getAnagrafica().getCodiceFiscale());

				Query query = panjeaDAO.prepareQuery(queryEntitaUnivoca);
				if (entita.getAnagrafica().getCodiceFiscale() != null) {
					query.setParameter("paramCodiceFiscale", entita.getAnagrafica().getCodiceFiscale());
				}
				if (entita.getAnagrafica().getPartiteIVA() != null) {
					query.setParameter("paramPartiteIVA", entita.getAnagrafica().getPartiteIVA());
				}
				entitaRicerca = panjeaDAO.getResultList(query);
				// Controllo se ho un'entita dello stesso tipo con lo stesso
				// codicefiscale e partitaiva.
				for (Entita entitaPresente : entitaRicerca) {
					anagraficaPresente = entitaPresente.equals(entita) ? null : entitaPresente.getAnagrafica();
					if (entitaPresente.getClass().equals(entita.getClass()) && !entitaPresente.equals(entita)) {
						throw new AnagraficheDuplicateException();
					}
				}
				if (anagraficaPresente != null && !anagraficaPresente.equals(entita.getAnagrafica())) {
					entita.setAnagrafica(anagraficaPresente);
				}
			}

			entita.getAnagrafica().setCodiceAzienda(getCurrentPrincipal().getCodiceAzienda());

			entitaSave = panjeaDAO.save(entita);
			if (entita.isNew()) {
				/*
				 * in caso di nuova entita provvedere in seguito alla creazione
				 * di una sede entita di tipo principale ed effettuare il legame
				 * con la stessa sede anagrafica dell'entita
				 */
				sediEntitaManager.generaSedePrincipaleInEntita(entitaSave);
			}
			if (entitaSave.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getCap() != null) {
				entitaSave.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getCap().getLocalita().size();
			}
			if (entitaSave.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getLocalita() != null) {
				entitaSave.getAnagrafica().getSedeAnagrafica().getDatiGeografici().getLocalita().getCap().size();
			}
			cancellaAnagraficheOrfane();
		} catch (CodiceAlreadyExistsException e) {
			logger.warn("--> il codice scelto e' gia' stato assegnato per l'entita'");
			throw e;
		} catch (AnagraficheDuplicateException e) {
			throw e;
		} catch (Exception e) {
			logger.error("--> errore, impossibile salvare Entita ", e);
			throw new AnagraficaServiceException(e);
		}
		logger.debug("--> Exit salvaEntita");
		return entitaSave;
	}

}
