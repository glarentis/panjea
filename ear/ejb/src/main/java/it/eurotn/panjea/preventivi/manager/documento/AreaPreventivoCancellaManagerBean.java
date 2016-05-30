package it.eurotn.panjea.preventivi.manager.documento;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.manager.interfaces.DocumentiManager;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.AreaPreventivoCancellaManager;
import it.eurotn.panjea.preventivi.manager.documento.interfaces.RigaPreventivoManager;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.AreaPreventivoCancellaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AreaPreventivoCancellaManager")
public class AreaPreventivoCancellaManagerBean implements AreaPreventivoCancellaManager {

	private static Logger logger = Logger.getLogger(AreaPreventivoCancellaManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected DocumentiManager documentiManager;

	@EJB
	protected RigaPreventivoManager rigaPreventivoManager;

	@EJB
	protected AreaRateManager areaRateManager;

	@Override
	public void cancellaAreaPreventivo(AreaPreventivo areaPreventivo) {
		logger.debug("--> Enter cancellaAreaPreventivo");
		Documento documento = areaPreventivo.getDocumento();
		areaRateManager.cancellaAreaRate(documento);
		try {

			// cancello le righe dell'area
			Query query = panjeaDAO.prepareNamedQuery("RigaPreventivo.cancellaByAreaPreventivo");
			query.setParameter("paramAreaPreventivo", areaPreventivo.getId());
			panjeaDAO.executeQuery(query);

			panjeaDAO.delete(areaPreventivo);
		} catch (DAOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		documentiManager.cancellaDocumento(documento);
		logger.debug("--> Exit cancellaAreaPreventivo");
	}

	@Override
	public AreaPreventivo cancellaRigaPreventivo(RigaPreventivo rigaPreventivo) {
		return rigaPreventivoManager.getDao(rigaPreventivo).cancellaRigaPreventivo(rigaPreventivo);
	}

}
