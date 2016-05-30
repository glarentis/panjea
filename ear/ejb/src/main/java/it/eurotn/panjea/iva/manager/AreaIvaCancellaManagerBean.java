/**
 * 
 */
package it.eurotn.panjea.iva.manager;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaCancellaManager;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author Leonardo
 */
@Stateless(name = "Panjea.AreaIvaCancellaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaIvaCancellaManager")
public class AreaIvaCancellaManagerBean implements AreaIvaCancellaManager {

	private static Logger logger = Logger.getLogger(AreaIvaCancellaManagerBean.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;
	/**
	 * @uml.property name="areaIvaManager"
	 * @uml.associationEnd
	 */
	@IgnoreDependency
	@EJB
	private AreaIvaManager areaIvaManager;

	@Deprecated
	@Override
	public void cancellaAreaIva(AreaContabile areaContabile) {
		logger.debug("--> cancellaAreaIva");
		try {
			AreaIva areaIva = areaIvaManager.caricaAreaIva(areaContabile);
			// l'areaIva puo' non esistere o essere gia' stata cancellata
			if (areaIva != null) {
				cancellaAreaIva(areaIva);
			} else {
				if (logger.isDebugEnabled()) {
					logger.warn("--> area iva non trovata per l'area contabile " + areaContabile.getId());
				}
			}
		} catch (Exception ex) {
			logger.error("--> Errore nel cancellare l'area Iva", ex);
			throw new RuntimeException(ex);
		}
		logger.debug("--> cancellaAreaIva");
	}

	@Override
	public void cancellaAreaIva(AreaIva areaIva) {
		cancellaRigheIva(areaIva);
		cancellaAreaIvaNoCheck(areaIva);
	}

	@Override
	public void cancellaAreaIva(Documento documento) {
		AreaIva areaIva = areaIvaManager.caricaAreaIvaByDocumento(documento);
		if (areaIva != null && areaIva.getId() != null && areaIva.getId().intValue() != -1) {
			cancellaAreaIva(areaIva);
		}
	}

	@Override
	public void cancellaAreaIvaNoCheck(AreaIva areaIva) {
		logger.debug("--> Enter cancellaAreaIvaNoCheck");
		try {
			panjeaDAO.delete(areaIva);
		} catch (Exception e) {
			logger.error("--> errore in cancellaAreaIvaNoCheck", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaAreaIvaNoCheck");
	}

	@Override
	public void cancellaRigaIva(RigaIva rigaIva) {
		logger.debug("--> Enter cancellaRigaIva");
		try {
			AreaIva areaIva = rigaIva.getAreaIva();
			areaIva.getRigheIva().remove(rigaIva);
			panjeaDAO.delete(rigaIva);
			areaIvaManager.checkInvalidaAreeCollegate(areaIva);
		} catch (Exception e) {
			logger.error("--> errore in cancellazione RigaIva", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaRigaIva");
	}

	@Override
	public void cancellaRigheIva(AreaIva areaIva) {
		logger.debug("--> Enter cancellaRigheIva");
		Query query = panjeaDAO.prepareNamedQuery("RigaIva.eliminaRigheIvaByAreaIva");
		query.setParameter("paramAreaIva", areaIva);
		query.executeUpdate();
		logger.debug("--> Exit cancellaRigheIva");
	}

}
