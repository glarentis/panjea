package it.eurotn.panjea.partite.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.TipoDocumentoManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoOperazione;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite;
import it.eurotn.panjea.partite.manager.interfaces.TipiAreaPartitaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.ArrayList;
import java.util.List;

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
 * Manager per la gestione di {@link TipoAreaPartita} .
 * 
 * @author adriano
 * @version 1.0, 08/lug/08
 */
@Stateless(name = "Panjea.TipiAreaPartiteManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipiAreaPartiteManager")
public class TipiAreaPartitaManagerBean implements TipiAreaPartitaManager {

	private static Logger logger = Logger.getLogger(TipiAreaPartitaManagerBean.class);

	@Resource
	private SessionContext sessionContext;

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@EJB
	private TipoDocumentoManager tipoDocumentoManager;

	@Override
	public void cancellaTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		logger.debug("--> Enter cancellaTipoAreaPartita");
		try {
			panjeaDAO.delete(tipoAreaPartita);
		} catch (VincoloException e) {
			logger.error("--> errore vincolo in cancellaTipoAreaPartita", e);
			throw new RuntimeException(e);
		} catch (DAOException e) {
			logger.error("--> errore dao in cancellaTipoAreaPartita", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaTipoAreaPartita");
	}

	@Override
	public void cancellaTipoDocumentoBase(TipoDocumentoBasePartite tipoDocumentoBase) {
		logger.debug("--> Enter cancellaTipoDocumentoBase");
		try {
			panjeaDAO.delete(tipoDocumentoBase);
		} catch (Exception e) {
			logger.error("--> errore nella cancellazione del tipo documento base", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaTipoDocumentoBase");

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoAreaPartita> caricaTipiAreaPartitaGenerazioneRate(TipoPartita tipoPartita) {
		Query query = panjeaDAO.prepareNamedQuery("TipoAreaPartita.caricaGenerazioneRateByTipoPartita");
		query.setParameter("paramTipoPartita", tipoPartita);
		List<TipoAreaPartita> partite = null;
		try {
			partite = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("-->errore nel caricare i tipiPartita ghe generano rate " + tipoPartita, e);
			throw new RuntimeException(e);
		}
		return partite;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoAreaPartita> caricaTipiAreaPartitaPerPagamenti(boolean loadTipiDocumentoDisabilitati) {
		logger.debug("--> Enter caricaTipiAreaPartita");
		List<TipoAreaPartita> tipiAreaPartita;
		Query query = panjeaDAO.prepareNamedQuery("TipoAreaPartita.caricaPerPagamentiByAzienda");
		query.setParameter("paramCodiceAzienda", getPrincipal().getCodiceAzienda());
		if (loadTipiDocumentoDisabilitati) {
			query.setParameter("paramTuttiTipi", 1);
		} else {
			query.setParameter("paramTuttiTipi", 0);
		}
		try {
			tipiAreaPartita = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore DAO in caricaTipiAreaPartita", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipiAreaPartita");
		return tipiAreaPartita;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoAreaPartita> caricaTipiAreaPartitaPerPagamenti(String fieldSearch, String valueSearch,
			TipoPartita tipoPartita, boolean loadTipiDocumentoDisabilitati, boolean escludiTipiAreaPartiteDistinta) {
		logger.debug("--> Enter caricaTipiAreaPartitaPerPagamenti");
		StringBuffer sb = new StringBuffer();
		sb.append("from TipoAreaPartita t join fetch t.tipoDocumento td ");
		sb.append("where ");
		sb.append("(  t.tipoOperazione=:paramTipoOperazioneChiusura or ");
		sb.append("   t.tipoOperazione=:paramTipoOperazioneGestioneDistinta or ");
		sb.append("   t.tipoOperazione=:paramTipoOperazioneAnticipoFatture or ");
		sb.append("   t.tipoOperazione=:paramTipoOperazioneChiusuraAnticipoFatture or ");
		sb.append("   t.tipoOperazione=:paramTipoOperazioneGestioneAssegno) ");
		if (!loadTipiDocumentoDisabilitati) {
			sb.append("and (td.abilitato = true )");
		}
		if (tipoPartita != null) {
			sb.append(" and t.tipoPartita = :paramTipoPartita");
		}

		if (valueSearch != null) {
			sb.append(" and t.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}

		sb.append(" order by t.").append(fieldSearch);

		Query query = panjeaDAO.prepareQuery(sb.toString());

		if (tipoPartita != null) {
			query.setParameter("paramTipoPartita", tipoPartita);
		}
		query.setParameter("paramTipoOperazioneChiusura", TipoAreaPartita.TipoOperazione.CHIUSURA);
		query.setParameter("paramTipoOperazioneGestioneDistinta", TipoAreaPartita.TipoOperazione.GESTIONE_DISTINTA);
		query.setParameter("paramTipoOperazioneAnticipoFatture", TipoAreaPartita.TipoOperazione.ANTICIPO_FATTURE);
		query.setParameter("paramTipoOperazioneChiusuraAnticipoFatture",
				TipoAreaPartita.TipoOperazione.CHIUSURA_ANTICIPO_FATTURE);
		query.setParameter("paramTipoOperazioneGestioneAssegno", TipoAreaPartita.TipoOperazione.GESTIONE_ASSEGNO);

		List<TipoAreaPartita> result;
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore DAO in caricaTipiAreaPartitaPerPagamenti", e);
			throw new RuntimeException(e);
		}

		if (escludiTipiAreaPartiteDistinta) {
			result.removeAll(getTipiAreaPartitaDistinta());
		}
		logger.debug("--> Exit caricaTipiAreaPartitaPerPagamenti " + result.size());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDocumentoBasePartite> caricaTipiDocumentoBase() {
		logger.debug("--> Enter caricaTuttiTipoDocumentoBase");
		Query query = panjeaDAO.prepareNamedQuery("TipoDocumentoBasePartite.caricaByAzienda");
		query.setParameter("paramCodiceAzienda", getJecPrincipal().getCodiceAzienda());
		List<TipoDocumentoBasePartite> tipi;
		try {
			tipi = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTuttiTipoDocumentoBase");
		return tipi;
	}

	@Override
	public TipoAreaPartita caricaTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		logger.debug("--> Enter caricaTipoAreaPartita");
		TipoAreaPartita tipoAreaPartitaCaricata;
		try {
			tipoAreaPartitaCaricata = panjeaDAO.load(TipoAreaPartita.class, tipoAreaPartita.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore e in caricaTipoAreaPartita", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipoAreaPartita");
		return tipoAreaPartitaCaricata;
	}

	@Override
	public TipoAreaPartita caricaTipoAreaPartitaPerTipoDocumento(TipoDocumento tipoDocumento) {
		logger.debug("--> Enter caricaTipoAreaPartitaPerTipoDocumento");
		TipoAreaPartita tipoAreaPartita;
		Query query = panjeaDAO.prepareNamedQuery("TipoAreaPartita.caricaByTipoDocumento");
		query.setParameter("paramTipoDocumentoId", tipoDocumento.getId());
		try {
			tipoAreaPartita = (TipoAreaPartita) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> TipoAreaPartita non trovato restituisco un TipoAreaPartita vuoto");
			tipoAreaPartita = new TipoAreaPartita();
		} catch (DAOException e) {
			logger.error("--> errore DAO in caricaTipoAreaPartitaPerTipoDocumento", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipoAreaPartitaPerTipoDocumento");
		return tipoAreaPartita;
	}

	@Override
	public TipoDocumentoBasePartite caricaTipoDocumentoBase(
			it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione tipoOperazione)
			throws TipoDocumentoBaseException {
		logger.debug("--> Enter caricaTipoDocumentoBase");
		Query query = panjeaDAO.prepareNamedQuery("TipoDocumentoBasePartite.caricaByTipoOperazione");
		query.setParameter("paramCodiceAzienda", getJecPrincipal().getCodiceAzienda());
		query.setParameter("paramTipoOperazione", tipoOperazione);
		TipoDocumentoBasePartite tipoDocumentoBase;
		try {
			tipoDocumentoBase = (TipoDocumentoBasePartite) panjeaDAO.getSingleResult(query);
		} catch (it.eurotn.dao.exception.ObjectNotFoundException e) {
			throw new TipoDocumentoBaseException(new String[] { "Tipo Operazione " + tipoOperazione.name() });
		} catch (Exception e) {
			logger.error("--> errore nel caricamento dei tipiDocumentoBasePartite per il tipoOperazione "
					+ tipoOperazione.name(), e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipoDocumentoBase");
		return tipoDocumentoBase;
	}

	/**
	 * @return codice azienda dell'utente loggato
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	/**
	 * recupera {@link JecPrincipal} dal {@link SessionContext}.
	 * 
	 * @return {@link SessionContext}
	 */
	private JecPrincipal getJecPrincipal() {
		logger.debug("--> Enter getJecPrincipal");
		return (JecPrincipal) sessionContext.getCallerPrincipal();
	}

	/**
	 * @return utente loggato
	 */
	private JecPrincipal getPrincipal() {
		return (JecPrincipal) sessionContext.getCallerPrincipal();
	}

	/**
	 * Carica dalla definizione dei tipi documento base dell'area partite tutti i tipi area partita del giro della
	 * distinta, escludendo l'area emissione effetti.<br>
	 * I tipi caricati saranno quindi: DISTINTA, ACCREDITO,INSOLUTO,ANTICIPO e ACCONTO
	 * 
	 * @return list di {@link TipoAreaPartita} caricati
	 */
	private List<TipoAreaPartita> getTipiAreaPartitaDistinta() {
		List<TipoAreaPartita> listTipi = new ArrayList<TipoAreaPartita>();

		try {
			TipoDocumentoBasePartite distintaBancaria = caricaTipoDocumentoBase(it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione.DISTINTA_BANCARIA);
			listTipi.add(distintaBancaria.getTipoAreaPartita());
		} catch (TipoDocumentoBaseException e) {
			logger.warn("--> non ho un tipo documento base per la distinta bancaria");
		}
		try {
			TipoDocumentoBasePartite accreditoValuta = caricaTipoDocumentoBase(it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione.VALUTA_PAGAMENTI);
			listTipi.add(accreditoValuta.getTipoAreaPartita());
		} catch (TipoDocumentoBaseException e) {
			logger.warn("--> non ho un tipo documento base per laccredito valuta");
		}
		try {
			TipoDocumentoBasePartite accreditoAssegno = caricaTipoDocumentoBase(it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione.ACCREDITO_ASSEGNO);
			listTipi.add(accreditoAssegno.getTipoAreaPartita());
		} catch (TipoDocumentoBaseException e) {
			logger.warn("--> non ho un tipo documento base per laccredito assegno");
		}
		try {
			TipoDocumentoBasePartite tipoInsoluto = caricaTipoDocumentoBase(it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione.INSOLUTO);
			listTipi.add(tipoInsoluto.getTipoAreaPartita());
		} catch (TipoDocumentoBaseException e) {
			logger.warn("--> non ho un tipo documento base per l'insoluto");
		}
		try {
			TipoDocumentoBasePartite tipoAnticipo = caricaTipoDocumentoBase(it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione.ANTICIPO);
			listTipi.add(tipoAnticipo.getTipoAreaPartita());
		} catch (TipoDocumentoBaseException e) {
			logger.warn("--> non ho un tipo documento base per l'anticipo");
		}
		try {
			TipoDocumentoBasePartite tipoAccontoCliente = caricaTipoDocumentoBase(it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione.ACCONTO_CLIENTE);
			listTipi.add(tipoAccontoCliente.getTipoAreaPartita());
		} catch (TipoDocumentoBaseException e) {
			logger.warn("--> non ho un tipo documento base per l'acconto cliente");
		}
		try {
			TipoDocumentoBasePartite tipoAccontoFornitore = caricaTipoDocumentoBase(it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione.ACCONTO_FORNITORE);
			listTipi.add(tipoAccontoFornitore.getTipoAreaPartita());
		} catch (TipoDocumentoBaseException e) {
			logger.warn("--> non ho un tipo documento base per l'acconto fornitore");
		}
		try {
			TipoDocumentoBasePartite tipoUtilizzoAccontoCliente = caricaTipoDocumentoBase(it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione.UTILIZZO_ACCONTO_CLIENTE);
			listTipi.add(tipoUtilizzoAccontoCliente.getTipoAreaPartita());
		} catch (TipoDocumentoBaseException e) {
			logger.warn("--> non ho un tipo documento base per l'utilizzo acconto cliente");
		}
		try {
			TipoDocumentoBasePartite tipoUtilizzoAccontoFornitore = caricaTipoDocumentoBase(it.eurotn.panjea.partite.domain.TipoDocumentoBasePartite.TipoOperazione.UTILIZZO_ACCONTO_FORNITORE);
			listTipi.add(tipoUtilizzoAccontoFornitore.getTipoAreaPartita());
		} catch (TipoDocumentoBaseException e) {
			logger.warn("--> non ho un tipo documento base per l'utilizzo acconto fornitore");
		}

		return listTipi;
	}

	@Override
	public TipoAreaPartita salvaTipoAreaPartita(TipoAreaPartita tipoAreaPartita)
			throws ModificaTipoAreaConDocumentoException {
		logger.debug("--> Enter salvaTipoAreaPartita");
		TipoAreaPartita tipoAreaPartitaSalvata;
		try {
			// se sono in modifica di un tipoAreaPartita esistente allora
			// verifico quali proprieta' ho modificato e se
			// ho modificato proprieta' che ne modificano la struttura,
			// controllo se ci sono documenti del tipoAreaPartita
			// lanciando una eccezione in caso positivo
			if (tipoAreaPartita.getId() != null) {
				verificaModificaTipoAreaContabile(tipoAreaPartita);
			}

			tipoAreaPartitaSalvata = panjeaDAO.save(tipoAreaPartita);
		} catch (ModificaTipoAreaConDocumentoException e) {
			throw e;
		} catch (Exception e) {
			logger.error("--> errore in salvaTipoAreaPartita", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaTipoAreaPartita");
		return tipoAreaPartitaSalvata;
	}

	@Override
	public TipoDocumentoBasePartite salvaTipoDocumentoBase(TipoDocumentoBasePartite tipoDocumentoBase) {
		logger.debug("--> Enter salvaTipoDocumentoBase");
		TipoDocumentoBasePartite tipoDocumentoBaseSalvato;
		/* Inizializza il codice azienda in caso di nuovo TipoDocumentoBase */
		if (tipoDocumentoBase.isNew()) {
			tipoDocumentoBase.setCodiceAzienda(getAzienda());
		}
		try {
			tipoDocumentoBaseSalvato = panjeaDAO.save(tipoDocumentoBase);
		} catch (Exception e) {
			logger.error("--> errore nel salvataggio del tipo documento base", e);
			throw new RuntimeException(e);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit salvaTipoDocumentoBase " + tipoDocumentoBaseSalvato.getId());
		}
		return tipoDocumentoBaseSalvato;
	}

	/**
	 * Verifica se la modifica del tipo area partita pu� essere eseguita, in caso negativo viene lanciata una eccezione
	 * di ModificaTipoAreaConDocumentoException.
	 * 
	 * @param tipoAreaPartita
	 *            il tipo documento modificato di cui verificare le modifiche
	 * @throws AnagraficaServiceException
	 * @throws ModificaTipoAreaConDocumentoException
	 *             rilanciata in caso la modifica non possa essere effettuata
	 */
	private void verificaModificaTipoAreaContabile(TipoAreaPartita tipoAreaPartita)
			throws ModificaTipoAreaConDocumentoException {
		logger.debug("--> Enter verificaModificaTipoAreaContabile");
		TipoAreaPartita tipoAreaPartitaOld = caricaTipoAreaPartita(tipoAreaPartita);

		// sono di seguito verificate le proprieta' che non devono assolutamente
		// variare

		// tipo Operazione
		TipoOperazione tipoOperazioneOld = tipoAreaPartitaOld.getTipoOperazione();
		TipoOperazione tipoOperazione = tipoAreaPartita.getTipoOperazione();

		// tipo Partita
		TipoPartita tipoPartitaOld = tipoAreaPartitaOld.getTipoPartita();
		TipoPartita tipoPartita = tipoAreaPartita.getTipoPartita();

		// verifico se una delle proprieta' e' stata cambiata
		if (!tipoOperazioneOld.equals(tipoOperazione) || !tipoPartitaOld.equals(tipoPartita)) {
			// se e' cambiata verifico se ho gia' inserito documenti di questo
			// tipo
			boolean hasDocuments = tipoDocumentoManager.hasDocuments(tipoAreaPartita.getTipoDocumento());
			// se ho gia' documenti non posso modificare il tipoDocumento
			if (hasDocuments) {
				logger.debug("--> trovati documenti del tipo documento scelto "
						+ tipoAreaPartita.getTipoDocumento().getCodice());
				throw new ModificaTipoAreaConDocumentoException();
			}
		}
		logger.debug("--> Exit verificaModificaTipoAreaContabile");
	}
}
