/**
 * 
 */
package it.eurotn.panjea.offerte.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.offerte.domain.TipoAreaOfferta;
import it.eurotn.panjea.offerte.manager.interfaces.TipiAreaOffertaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

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
 * @author Leonardo
 * 
 */
@Stateless(name = "Panjea.TipiAreaOffertaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipiAreaOffertaManager")
public class TipiAreaOffertaManagerBean implements TipiAreaOffertaManager {

	private static Logger logger = Logger.getLogger(TipiAreaOffertaManagerBean.class.getName());

	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void cancellaTipoAreaOfferta(Integer idTipoAreaOfferta) {
		TipoAreaOfferta tipoAreaOfferta = new TipoAreaOfferta();
		tipoAreaOfferta.setId(idTipoAreaOfferta);
		try {
			panjeaDAO.delete(tipoAreaOfferta);
		} catch (Exception e) {
			logger.error("--> errore in cancellaTipoAreaOfferta", e);
			throw new RuntimeException("--> errore in cancellaTipoAreaOfferta", e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoAreaOfferta> caricaTipiAreaOfferta(boolean loadTipiDocumentoDisabilitati) {
		logger.debug("--> Enter caricaTipiAreaOfferta");
		Query query = panjeaDAO.prepareNamedQuery("TipoAreaOfferta.caricaAll");
		query.setParameter("paramCodiceAzienda", getAzienda());
		if (loadTipiDocumentoDisabilitati) {
			query.setParameter("paramTuttiTipi", 1);
		} else {
			query.setParameter("paramTuttiTipi", 0);
		}
		List<TipoAreaOfferta> tipiAreaContabile;
		try {
			tipiAreaContabile = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore nel recuperare la lista di TipoAreaOfferta ", e);
			throw new RuntimeException("--> errore nel recuperare la lista di TipoAreaOfferta ", e);
		}
		logger.debug("--> Exit caricaTipiAreaOfferta");
		return tipiAreaContabile;
	}

	@Override
	public TipoAreaOfferta caricaTipoAreaOfferta(Integer idTipoAreaOfferta) {
		logger.debug("--> Enter caricaTipoAreaOfferta");
		TipoAreaOfferta tipoAreaOfferta;
		try {
			tipoAreaOfferta = panjeaDAO.load(TipoAreaOfferta.class, idTipoAreaOfferta);
		} catch (ObjectNotFoundException e) {
			logger.error("ObjectNotFoundException per caricaTipoAreaOfferta");
			throw new RuntimeException(e);
		} catch (Exception e) {
			logger.error("Errore nel caricare il tipoAreaOfferta", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipoAreaOfferta");
		return tipoAreaOfferta;
	}

	@Override
	public TipoAreaOfferta caricaTipoAreaOffertaPerTipoDocumento(Integer idTipoDocumento) {
		logger.debug("--> Enter caricaTipoAreaContabilePerTipoDocumento");
		Query query = panjeaDAO.prepareNamedQuery("TipoAreaOfferta.caricaByTipoDocumento");
		query.setParameter("paramId", idTipoDocumento);
		TipoAreaOfferta tipoAreaOfferta = null;
		try {
			tipoAreaOfferta = (TipoAreaOfferta) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> TipoAreaOfferta non trovato per il tipo documento con id " + idTipoDocumento);
			tipoAreaOfferta = new TipoAreaOfferta();
		} catch (Exception e) {
			logger.error("--> errore nella caricaTipoAreaOffertaPerTipoDocumento", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipoAreaContabilePerTipoDocumento");
		return tipoAreaOfferta;
	}

	/**
	 * @return codice azienda del principal loggato
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@Override
	public TipoAreaOfferta salvaTipoAreaOfferta(TipoAreaOfferta tipoAreaOfferta) {
		logger.debug("--> Exit salvaTipoAreaContabile");
		try {
			TipoAreaOfferta tipoAreaOffertaSave = panjeaDAO.save(tipoAreaOfferta);
			logger.debug("--> Exit salvaTipoAreaContabile");
			return tipoAreaOffertaSave;
		} catch (Exception e) {
			logger.error("--> errore impossibile eseguire il salvataggio di TipoAreaOfferta", e);
			throw new RuntimeException("--> errore impossibile eseguire il salvataggio di TipoAreaOfferta", e);
		}
	}

}
