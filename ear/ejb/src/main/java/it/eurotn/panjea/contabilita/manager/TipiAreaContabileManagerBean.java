package it.eurotn.panjea.contabilita.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.TipoDocumentoManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.CodiceIvaPrevalente;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.TipiDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.TipoTotalizzazione;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.manager.interfaces.StrutturaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
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
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * roles: cancellaTipoAreaContabile, visualizzaTipoAreaContabile, modificaTipoAreaContabile.
 * 
 * @author adriano
 */
@Stateless(name = "Panjea.TipiAreaContabileManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipiAreaContabileManager")
public class TipiAreaContabileManagerBean implements TipiAreaContabileManager {

	private static Logger logger = Logger.getLogger(TipiAreaContabileManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@IgnoreDependency
	@EJB
	private StrutturaContabileManager strutturaContabileManager;

	@Resource
	private SessionContext context;

	@EJB
	private TipoDocumentoManager tipoDocumentoManager;

	@Override
	public void cancellaCodiceIvaPrevalente(CodiceIvaPrevalente codiceIvaPrevalente) {
		logger.debug("--> Enter cancellaCodiceIvaPrevalente");
		try {
			panjeaDAO.delete(codiceIvaPrevalente);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione del codice iva prevalente", e);
			throw new RuntimeException("Errore durante la cancellazione del codice iva prevalente", e);
		}

		logger.debug("--> Exit cancellaCodiceIvaPrevalente");
	}

	@Override
	public void cancellaTipoAreaContabile(TipoAreaContabile tipoAreaContabile) {
		logger.debug("--> Enter cancellaTipoAreaContabile");
		// devo cancellare se presenti le strutture contabili e le contro
		// partite
		strutturaContabileManager.cancellaStrutturaContabile(tipoAreaContabile);

		try {
			panjeaDAO.delete(tipoAreaContabile);
		} catch (Exception e) {
			logger.error("--> errore cancellaTipoAreaContabile", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaTipoAreaContabile");

	}

	@Override
	public void cancellaTipoDocumentoBase(TipoDocumentoBase tipoDocumentoBase) {
		logger.debug("--> Enter cancellaTipoDocumentoBase");
		try {
			panjeaDAO.delete(tipoDocumentoBase);
		} catch (Exception e) {
			logger.error("--> errore cancellaTipoDocumentoBase", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaTipoDocumentoBase");
	}

	@Override
	public CodiceIvaPrevalente caricaCodiceIvaPrevalente(TipoAreaContabile tipoAreaContabile, EntitaLite entita) {
		logger.debug("--> Enter caricaCodiceIvaPrevalente");

		CodiceIvaPrevalente codiceIvaPrevalente = null;

		Query query;
		// se l'entita' e' nulla cerco il codice iva prevalente per il tipo area
		// contabile
		if (entita == null) {
			query = panjeaDAO.prepareNamedQuery("CodiceIvaPrevalente.ricercaByTipoAreaContabile");
			query.setParameter("paramIdTipoAreaContabile", tipoAreaContabile.getId());
		} else {
			query = panjeaDAO.prepareNamedQuery("CodiceIvaPrevalente.ricercaByTipoAreaContabileEntita");
			query.setParameter("paramIdTipoAreaContabile", tipoAreaContabile.getId());
			query.setParameter("paramIdEntita", entita.getId());
		}

		try {
			codiceIvaPrevalente = (CodiceIvaPrevalente) panjeaDAO.getSingleResult(query);
			if (codiceIvaPrevalente.getCodiceIva() != null) {
				// codice iva collegato e per ventilazione sono lazy, se
				// esistono li inizializzo
				if (codiceIvaPrevalente.getCodiceIva().getCodiceIvaCollegato() != null) {
					codiceIvaPrevalente.getCodiceIva().getCodiceIvaCollegato().getVersion();
				}
				if (codiceIvaPrevalente.getCodiceIva().getCodiceIvaSostituzioneVentilazione() != null) {
					codiceIvaPrevalente.getCodiceIva().getCodiceIvaSostituzioneVentilazione().getVersion();
				}
			}
		} catch (ObjectNotFoundException e) {
			codiceIvaPrevalente = null;
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento del codice iva prevalente.", e);
			throw new RuntimeException("Errore durante il caricamento del codice iva prevalente.", e);
		}

		logger.debug("--> Exit caricaCodiceIvaPrevalente");
		return codiceIvaPrevalente;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoAreaContabile> caricaTipiAreaContabile(String fieldSearch, String valueSearch,
			boolean loadTipiDocumentiDisabilitati) throws ContabilitaException {
		logger.debug("--> Enter caricaTipiAreaContabile");
		StringBuilder sb = new StringBuilder(
				"select tac from TipoAreaContabile tac inner join tac.tipoDocumento td where td.codiceAzienda = :paramCodiceAzienda");
		if (!loadTipiDocumentiDisabilitati) {
			sb.append(" and td.abilitato = true");
		}
		if (valueSearch != null) {
			sb.append(" and tac.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		sb.append(" order by ").append("tac.").append(fieldSearch);
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("paramCodiceAzienda", getAzienda());
		List<TipoAreaContabile> tipiAreaContabile;
		try {
			tipiAreaContabile = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore impossibile recuperare la list di TipoAreaContabile ", e);
			throw new ContabilitaException(e);
		}
		logger.debug("--> Exit caricaTipiAreaContabile");
		return tipiAreaContabile;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDocumento> caricaTipiDocumentiContabili() throws ContabilitaException {
		logger.debug("--> Enter caricaTipiDocumentiContabili");
		Query query = panjeaDAO.prepareNamedQuery("TipoAreaContabile.caricaTipiDocumentiContabili");
		query.setParameter("paramCodiceAzienda", getAzienda());
		List<TipoDocumento> tipiDocumento = null;
		try {
			tipiDocumento = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore in ricerca tipi documenti contabili", e);
			throw new ContabilitaException("ricerca tipi documenti contabili ", e);
		}
		logger.debug("--> Exit caricaTipiDocumentiContabili");
		return tipiDocumento;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDocumentoBase> caricaTipiDocumentoBase() {
		logger.debug("--> Enter caricaTipiDocumentoBase");
		List<TipoDocumentoBase> lista = new ArrayList<TipoDocumentoBase>();
		Query query = panjeaDAO.prepareNamedQuery("TipoDocumentoBase.caricaByAzienda");
		query.setParameter("paramCodiceAzienda", getAzienda());
		try {
			lista = panjeaDAO.getResultList(query);
			logger.debug("--> Exit caricaTipiDocumentoBase " + lista.size());
			return lista;
		} catch (Exception e) {
			logger.error("--> errore, impossibile recuperare la lista di TipoDocumentoBase ", e);
			throw new RuntimeException("impossibile recuperare la lista di TipoDocumentoBase", e);
		}
	}

	@Override
	public TipiDocumentoBase caricaTipiOperazione() throws ContabilitaException {
		logger.debug("--> Enter caricaTipiDocumentoBase");
		List<TipoDocumentoBase> listTipiDocumento = caricaTipiDocumentoBase();
		TipiDocumentoBase tipiDocumentoBase = new TipiDocumentoBase(listTipiDocumento);
		logger.debug("--> Exit caricaTipiDocumentoBase " + tipiDocumentoBase.size());
		return tipiDocumentoBase;
	}

	@Override
	public TipoAreaContabile caricaTipoAreaContabile(Integer id) throws ContabilitaException {
		logger.debug("--> Enter caricaTipoAreaContabile");
		try {
			TipoAreaContabile tipoAreaContabile = panjeaDAO.load(TipoAreaContabile.class, id);
			// inizializzazione degli attributi lazy
			if (tipoAreaContabile.getContoCassa() != null) {
				tipoAreaContabile.getContoCassa().getId();
			}

			logger.debug("--> Exit caricaTipoAreaContabile");
			return tipoAreaContabile;
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore caricaTipoAreaContabile", e);
			throw new ContabilitaException("Impossibile restituire TipoAreaContabile identificata da " + id);
		}
	}

	@Override
	public TipoAreaContabile caricaTipoAreaContabilePerTipoDocumento(Integer idTipoDocumento)
			throws ContabilitaException {
		logger.debug("--> Enter caricaTipoAreaContabilePerTipoDocumento");
		Query query = panjeaDAO.prepareNamedQuery("TipoAreaContabile.caricaByTipoDocumento");
		query.setParameter("paramId", idTipoDocumento);
		TipoAreaContabile tipoAreaContabile = null;
		try {
			tipoAreaContabile = (TipoAreaContabile) panjeaDAO.getSingleResult(query);
			// inizializzazione degli attributi lazy
			if (tipoAreaContabile.getContoCassa() != null) {
				tipoAreaContabile.getContoCassa().getId();
			}
		} catch (ObjectNotFoundException e) {
			logger.debug("--> TipoAreaContabile non trovato");
			tipoAreaContabile = new TipoAreaContabile();
		} catch (DAOException e) {
			throw new ContabilitaException("Impossibile restituire TipoAreaContabile identificata da "
					+ idTipoDocumento, e);
		}
		logger.debug("--> Exit caricaTipoAreaContabilePerTipoDocumento");
		return tipoAreaContabile;
	}

	@Override
	public TipoAreaContabile caricaTipoAreaContabilePerTipoOperazione(TipoOperazioneTipoDocumento tipoOperazione)
			throws TipoDocumentoBaseException, ContabilitaException {
		logger.debug("--> Enter caricaTipoAreaContabilePerTipoOperazione");
		TipoDocumentoBase tipoDocumentoBase = null;
		Query query = panjeaDAO.prepareNamedQuery("TipoDocumentoBase.caricaByTipoOperazione");
		query.setParameter("paramCodiceAzienda", getAzienda());
		query.setParameter("paramTipoOperazione", tipoOperazione);
		try {
			tipoDocumentoBase = (TipoDocumentoBase) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore, TipoDocumentoBase non esiste ", e);
			throw new TipoDocumentoBaseException(new String[] { "Tipo operazione " + tipoOperazione.name() });
		} catch (DAOException e) {
			logger.error("--> errore, impossibile recuperare TipoDocumentoBase  ", e);
			throw new ContabilitaException("Iimpossibile recuperare TipoDocumentoBase", e);
		}
		logger.debug("--> Exit caricaTipoAreaContabilePerTipoOperazione");
		return tipoDocumentoBase.getTipoAreaContabile();
	}

	@Override
	public TipoDocumentoBase caricaTipoDocumentoBase(Integer idTipoDocumentoBase) throws ContabilitaException {
		logger.debug("--> Enter caricaTipoDocumentoBase");
		try {
			TipoDocumentoBase tipoDocumentoBase;
			tipoDocumentoBase = panjeaDAO.load(TipoDocumentoBase.class, idTipoDocumentoBase);
			logger.debug("--> Exit caricaTipoDocumentoBase");
			return tipoDocumentoBase;
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore, impossibile recuperare TipoDocumentoBase ", e);
			throw new ContabilitaException("impossibile recuperare TipoDocumentoBase ", e);
		}
	}

	/**
	 * Restituisce il codice azienda.
	 * 
	 * @return codiceAzienda
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@Override
	public CodiceIvaPrevalente salvaCodiceIvaPrevalente(CodiceIvaPrevalente codiceIvaPrevalente) {
		logger.debug("--> Enter salvaCodiceIvaPrevalente");

		CodiceIvaPrevalente codiceIvaPrevalenteSalvato = null;

		try {
			codiceIvaPrevalenteSalvato = panjeaDAO.save(codiceIvaPrevalente);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del codice iva prevalente.", e);
			throw new RuntimeException("Errore durante il salvataggio del codice iva prevalente.", e);
		}

		logger.debug("--> Exit salvaCodiceIvaPrevalente");
		return codiceIvaPrevalenteSalvato;
	}

	@Override
	public TipoAreaContabile salvaTipoAreaContabile(TipoAreaContabile tipoAreaContabile)
			throws ModificaTipoAreaConDocumentoException {
		logger.debug("--> Exit salvaTipoAreaContabile");
		try {
			// se sono in modifica di un tipoareaContabile esistente allora
			// verifico quali proprieta' ho modificato e se
			// ho modificato proprieta' che ne modificano la struttura,
			// controllo se ci sono documenti del tipoareacontabile
			// lanciando una eccezione in caso positivo
			if (tipoAreaContabile.getId() != null) {
				verificaModificaTipoAreaContabile(tipoAreaContabile);
			}

			TipoAreaContabile tipoAreaContabileSave = panjeaDAO.save(tipoAreaContabile);
			// inizializzazione degli attributi lazy
			if (tipoAreaContabileSave.getContoCassa() != null) {
				tipoAreaContabileSave.getContoCassa().getId();
			}
			logger.debug("--> Exit salvaTipoAreaContabile");
			return tipoAreaContabileSave;
		} catch (ModificaTipoAreaConDocumentoException e) {
			throw e;
		} catch (Exception e) {
			logger.error("--> errore impossibile eseguire il salvataggio di TipoAreaContabile", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public TipoDocumentoBase salvaTipoDocumentoBase(TipoDocumentoBase tipoDocumentoBase) {
		logger.debug("--> Enter salvaTipoDocumentoBase");
		/* Inizializza il codice azienda in caso di nuovo TipoDocumentoBase */
		if (tipoDocumentoBase.isNew()) {
			tipoDocumentoBase.setCodiceAzienda(getAzienda());
		}
		try {
			TipoDocumentoBase tipoDocumentoBase2 = panjeaDAO.save(tipoDocumentoBase);
			logger.debug("--> Exit salvaTipoDocumentoBase");
			return tipoDocumentoBase2;

		} catch (Exception e) {
			logger.error("--> errore, impossibile salvare TipoDocumentoBase ", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Verifica se la modifica del tipo area contabile puo' essere eseguita, in caso negativo viene lanciata una
	 * eccezione di ModificaTipoAreaConDocumentoException.
	 * 
	 * @param tipoAreaContabile
	 *            il tipo area contabile modificato di cui verificare le modifiche
	 * @throws ModificaTipoAreaConDocumentoException
	 *             ci sono dei documenti del tipo documento scelto
	 * @throws ContabilitaException
	 *             errore di contabilita'
	 */
	private void verificaModificaTipoAreaContabile(TipoAreaContabile tipoAreaContabile)
			throws ModificaTipoAreaConDocumentoException, ContabilitaException {
		logger.debug("--> Enter verificaModificaTipoAreaContabile");
		TipoAreaContabile tipoAreaContabileOld = caricaTipoAreaContabile(tipoAreaContabile.getId());

		// sono di seguito verificate le proprieta' che non devono assolutamente
		// variare

		// gestione iva
		GestioneIva gestioneIvaOld = tipoAreaContabileOld.getGestioneIva();
		GestioneIva gestioneIva = tipoAreaContabile.getGestioneIva();

		// registro Iva
		RegistroIva registroIvaOld = tipoAreaContabileOld.getRegistroIva();
		RegistroIva registroIva = tipoAreaContabile.getRegistroIva();
		RegistroIva registroIvaCollegatoOld = tipoAreaContabileOld.getRegistroIvaCollegato();
		RegistroIva registroIvaCollegato = tipoAreaContabile.getRegistroIvaCollegato();

		// tipo Totalizzazione
		TipoTotalizzazione tipoTotalizzazioneOld = tipoAreaContabileOld.getTipoTotalizzazione();
		TipoTotalizzazione tipoTotalizzazione = tipoAreaContabile.getTipoTotalizzazione();

		// verifico se una delle proprieta' e' stata cambiata
		if (!gestioneIvaOld.equals(gestioneIva) || (registroIvaOld != null && !registroIvaOld.equals(registroIva))
				|| (tipoTotalizzazioneOld != null && !tipoTotalizzazioneOld.equals(tipoTotalizzazione))
				|| !gestioneIvaOld.equals(gestioneIva)
				|| (registroIvaCollegatoOld != null && !registroIvaCollegatoOld.equals(registroIvaCollegato))) {
			// se e' cambiata verifico se ho gia' inserito documenti di questo
			// tipo
			boolean hasDocuments = tipoDocumentoManager.hasDocuments(tipoAreaContabile.getTipoDocumento());
			// se ho gia' documenti non posso modificare il tipoDocumento
			if (hasDocuments) {
				logger.debug("--> trovati documenti del tipo documento scelto "
						+ tipoAreaContabile.getTipoDocumento().getCodice());
				throw new ModificaTipoAreaConDocumentoException();
			}
		}
		logger.debug("--> Exit verificaModificaTipoAreaContabile");
	}
}
