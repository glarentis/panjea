/**
 * 
 */
package it.eurotn.panjea.offerte.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.offerte.domain.AreaOfferta;
import it.eurotn.panjea.offerte.domain.RigaOfferta;
import it.eurotn.panjea.offerte.manager.interfaces.AreaOffertaCancellaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author Leonardo
 * 
 */
@Stateless(name = "Panjea.AreaOffertaCancellaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaOffertaCancellaManager")
public class AreaOffertaCancellaManagerBean implements AreaOffertaCancellaManager {

	private static Logger logger = Logger.getLogger(AreaOffertaManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private DocumentiManager documentiManager;

	@Override
	public void cancellaAreaOfferta(AreaOfferta areaOfferta) {
		cancellaRigheOfferta(areaOfferta.getId());
		cancellaAreaOffertaNoCheck(areaOfferta);
		cancellaDocumento(areaOfferta.getDocumento());
	}

	@Override
	public void cancellaAreaOffertaNoCheck(AreaOfferta areaOfferta) {
		logger.debug("--> Enter cancellaAreaOffertaNoCheck");
		try {
			panjeaDAO.delete(areaOfferta);
		} catch (Exception e) {
			logger.error("--> errore in cancellaAreaOfferta", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaAreaOffertaNoCheck");
	}

	/**
	 * Cancella il documento.
	 * 
	 * @param documento
	 *            documento da cancellare
	 */
	private void cancellaDocumento(Documento documento) {
		documentiManager.cancellaDocumento(documento);
	}

	@Override
	public void cancellaRigaOfferta(RigaOfferta rigaOfferta) {
		logger.debug("--> Enter cancellaRigaOfferta");
		try {
			panjeaDAO.delete(rigaOfferta);
		} catch (Exception e) {
			logger.error("--> errore in cancellaRigaOfferta", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaRigaOfferta");
	}

	@Override
	public void cancellaRigheOfferta(Integer idAreaOfferta) {
		logger.debug("--> Enter cancellaRigheOfferta");
		Query query = panjeaDAO.prepareNamedQuery("RigaOfferta.cancellaByAreaOfferta");
		query.setParameter("paramIdAreaOfferta", idAreaOfferta);
		try {
			panjeaDAO.executeQuery(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaRigheOfferta");
	}

}
