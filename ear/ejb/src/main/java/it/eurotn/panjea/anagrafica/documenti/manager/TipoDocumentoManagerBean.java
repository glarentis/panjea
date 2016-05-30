package it.eurotn.panjea.anagrafica.documenti.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.TipoDocumentoManager;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoPanjea;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoTipoDocumento;
import it.eurotn.panjea.dms.manager.interfaces.DMSAttributiManager;
import it.eurotn.panjea.exception.GenericException;
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

@Stateless(name = "Panjea.TipoDocumentoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipoDocumentoManager")
public class TipoDocumentoManagerBean implements TipoDocumentoManager {
	private static final Logger LOGGER = Logger.getLogger(TipoDocumentoManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@EJB
	private DMSAttributiManager dmsAttributiManager;

	@Override
	public void cancellaTipoDocumento(TipoDocumento tipoDocumento) throws AnagraficaServiceException {
		LOGGER.debug("--> Enter cancellaTipoDocumento");
		try {
			panjeaDAO.delete(tipoDocumento);
		} catch (Exception e) {
			LOGGER.error("--> Errore nel cancellare il tipoDocumento " + tipoDocumento);
			throw new GenericException(e);
		}
		LOGGER.debug("--> Exit cancellaTipoDocumento");

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDocumento> caricaTipiDocumento(String fieldSearch, String valueSearch, boolean caricaNonAbilitati)
			throws AnagraficaServiceException {
		LOGGER.debug("--> Enter caricaTipiDocumento");

		StringBuilder sb = new StringBuilder("from TipoDocumento t where t.codiceAzienda = :codiceAzienda");

		if (!caricaNonAbilitati) {
			sb.append(" and t.abilitato = true ");
		}

		if (valueSearch != null) {
			sb.append(" and t.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}

		sb.append(" order by ").append(fieldSearch);

		String codiceAzienda = ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("codiceAzienda", codiceAzienda);
		List<TipoDocumento> tipiDocumento;
		try {
			tipiDocumento = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			LOGGER.error("--> errore impossibile recuperare la list di Tipidocumento ", e);
			throw new AnagraficaServiceException(e);
		}
		LOGGER.debug("--> Exit caricaTipiDocumento");
		return tipiDocumento;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDocumento> caricaTipiDocumentoPerNoteAutomatiche() {
		LOGGER.debug("--> Enter caricaTipiDocumentoPerNoteAutomatiche");

		List<TipoDocumento> tipiDoc = new ArrayList<TipoDocumento>();

		Query query = panjeaDAO.prepareQuery("select distinct tam.tipoDocumento from TipoAreaMagazzino tam");
		try {
			tipiDoc = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			LOGGER.error("--> errore durante il caricamento dei tipi documento.", e);
			throw new GenericException("errore durante il caricamento dei tipi documento.", e);
		}

		LOGGER.debug("--> Exit caricaTipiDocumentoPerNoteAutomatiche");
		return tipiDoc;
	}

	@Override
	public TipoDocumento caricaTipoDocumento(int idTipoDocumento) throws AnagraficaServiceException {
		try {
			return panjeaDAO.load(TipoDocumento.class, idTipoDocumento);
		} catch (ObjectNotFoundException e) {
			LOGGER.error("--> TipoDocumento non trovato. ID cercato=" + idTipoDocumento);
			throw new AnagraficaServiceException(e);
		}
	}

	@Override
	public TipoDocumento copiaTipoDocumento(String codiceNuovoTipoDocumento, String descrizioneNuovoTipoDocumento,
			TipoDocumento tipoDocumentoParam) {
		LOGGER.debug("--> Enter copiaTipoDocumento");

		TipoDocumento tipoDocumento = null;
		try {
			tipoDocumento = caricaTipoDocumento(tipoDocumentoParam.getId());
		} catch (Exception e) {
			LOGGER.error("--> errore durante il caricamento del tipo documento " + tipoDocumentoParam.getId(), e);
			throw new GenericException(
					"errore durante il caricamento del tipo documento " + tipoDocumentoParam.getId(), e);
		}

		// clono il tipo documento e assegno codice e descrizione nuovi
		TipoDocumento tipoDocumentoClone = (TipoDocumento) tipoDocumento.clone();
		tipoDocumentoClone.setCodice(codiceNuovoTipoDocumento);
		tipoDocumentoClone.setDescrizione(descrizioneNuovoTipoDocumento);

		tipoDocumentoClone = salvaTipoDocumento(tipoDocumentoClone);
		LOGGER.debug("--> Exit copiaTipoDocumento");
		return tipoDocumentoClone;
	}

	/**
	 * 
	 * @return codiceAzienda
	 */
	private String getCodiceAzienda() {
		return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
	}

	/**
	 * @return principal corrente
	 */
	private JecPrincipal getPrincipal() {
		return (JecPrincipal) context.getCallerPrincipal();

	}

	@Override
	public boolean hasDocuments(TipoDocumento tipoDocumento) {
		LOGGER.debug("--> Enter hasDocuments");

		String codiceAzienda = ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
		Query query = panjeaDAO.prepareNamedQuery("Documento.verificaPresenzaDocumentiByTipoDocumento");
		query.setParameter("paramCodiceAzienda", codiceAzienda);
		query.setParameter("paramIdTipoDoc", tipoDocumento.getId());

		// l'id del documento trovato
		Integer id = null;

		// variabile booleana che definisce se ci sono dei documenti con legato
		// il tipo documento passato
		boolean hasDocuments = true;

		try {
			id = (Integer) panjeaDAO.getSingleResult(query);
			LOGGER.debug("--> trovato documento collegato a tipo documento con id " + id);
			// la max ritorna sempre un valore che al massimo se non ci sono
			// record risulta essere null, vedi
			// query Documento.verificaPresenzaDocumentiByTipoDocumento
			hasDocuments = id != null;
		} catch (ObjectNotFoundException e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("--> non ho nessul risultato", e);
			}
			// se non ho risultati non ci sono documenti del tipo documento
			// scelto
			hasDocuments = false;
		} catch (DAOException e) {
			LOGGER.error(
					"--> errore nella ricerca di un documento associato a tipoDocumento id " + tipoDocumento.getId(), e);
			throw new GenericException(e);
		}
		LOGGER.debug("--> Exit hasDocuments " + hasDocuments);
		return hasDocuments;
	}

	@Override
	public TipoDocumento salvaTipoDocumento(TipoDocumento tipoDocumento) {
		LOGGER.debug("--> Enter salvaTipoDocumento");
		TipoDocumento tipoDocumento2;
		try {
			// se il tipo documento e' nuovo imposto il codice azienda
			if (tipoDocumento.isNew()) {
				tipoDocumento.setCodiceAzienda(getPrincipal().getCodiceAzienda());
			} else {
				// se aggiorno un tipo documento esistente verifico se posso portare a termine la
				// modifica
				verificaModificaTipoDocumento(tipoDocumento);
			}

			tipoDocumento2 = panjeaDAO.save(tipoDocumento);
			AttributoAllegatoPanjea att = new AttributoAllegatoTipoDocumento(tipoDocumento.getCodice(),
					tipoDocumento.getDescrizione(), tipoDocumento.getId(), getCodiceAzienda());
			if (dmsAttributiManager != null) {
				// dmsAttributiManager.aggiornaAttributo(TipoAttributo.TIPO_DOC, att);
			}
			LOGGER.debug("--> Exit salvaTipoDocumento");
			return tipoDocumento2;
		} catch (Exception e) {
			LOGGER.error("--> errore in salva TipoDocumento", e);
			throw new GenericException(e);
		}
	}

	/**
	 * Verifica se la modifica del tipo documento può essere eseguita, in caso negativo viene lanciata una eccezione di
	 * ModificaTipoAreaConDocumentoException.
	 * 
	 * @param tipoDocumento
	 *            il tipo documento modificato di cui verificare le modifiche
	 * @throws AnagraficaServiceException
	 *             eccezione generica
	 * @throws ModificaTipoAreaConDocumentoException
	 *             rilanciata quando si cerca di modificare certe proprietà di un tipoDocumento che ha dei documenti
	 *             collegati.
	 */
	private void verificaModificaTipoDocumento(TipoDocumento tipoDocumento) throws AnagraficaServiceException,
			ModificaTipoAreaConDocumentoException {
		LOGGER.debug("--> Enter verificaModificaTipoDocumento");
		TipoDocumento tipoDocumentoOld = caricaTipoDocumento(tipoDocumento.getId());

		// sono di seguito verificate le proprieta' che non devono assolutamente
		// variare

		// classe tipo doc
		String classeTipoDocOld = tipoDocumentoOld.getClasseTipoDocumento();
		String classeTipoDoc = tipoDocumento.getClasseTipoDocumento();

		// tipoEntita'
		TipoEntita tipoEntitaOld = tipoDocumentoOld.getTipoEntita();
		TipoEntita tipoEntita = tipoDocumento.getTipoEntita();

		// righe iva enabled
		boolean gestioneAreaIvaOld = tipoDocumentoOld.isRigheIvaEnable();
		boolean gestioneAreaIva = tipoDocumento.isRigheIvaEnable();

		// nota di credito
		boolean notaCreditoOld = tipoDocumentoOld.isNotaCreditoEnable();
		boolean notaCredito = tipoDocumento.isNotaCreditoEnable();

		// verifico se una delle proprieta' e' stata cambiata
		if (!classeTipoDocOld.equals(classeTipoDoc) || !tipoEntitaOld.equals(tipoEntita)
				|| gestioneAreaIvaOld != gestioneAreaIva || notaCreditoOld != notaCredito) {
			// se e' cambiata verifico se ho gia' inserito documenti di questo
			// tipo
			boolean hasDocuments = hasDocuments(tipoDocumento);
			// se ho gia' documenti non posso modificare il tipoDocumento
			if (hasDocuments) {
				LOGGER.debug("--> trovati documenti del tipo documento scelto " + tipoDocumento.getCodice());
				throw new ModificaTipoAreaConDocumentoException();
			}
		}
		LOGGER.debug("--> Exit verificaModificaTipoDocumento");
	}

}
