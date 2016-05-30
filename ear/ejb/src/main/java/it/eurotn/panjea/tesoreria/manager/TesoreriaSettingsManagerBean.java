package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.TesoreriaSettings;
import it.eurotn.panjea.tesoreria.manager.interfaces.TesoreriaSettingsManager;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.TesoreriaSettingsManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TesoreriaSettingsManagerBean")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "tesoreriaSettings")
public class TesoreriaSettingsManagerBean implements TesoreriaSettingsManager {
	private static Logger logger = Logger.getLogger(TesoreriaSettingsManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public TesoreriaSettings caricaSettings() {
		Query query = panjeaDAO.prepareNamedQuery("TesoreriaSettings.caricaAll");
		TesoreriaSettings tesoreriaSettings = null;
		try {
			tesoreriaSettings = (TesoreriaSettings) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			tesoreriaSettings = salva(new TesoreriaSettings());
		} catch (Exception e) {
			logger.error("-->errore nel caricare le settings di tesoreria", e);
			throw new RuntimeException(e);
		}
		return tesoreriaSettings;
	}

	@Override
	public TesoreriaSettings salva(TesoreriaSettings tesoreriaSettings) {
		try {
			return panjeaDAO.save(tesoreriaSettings);
		} catch (DAOException e) {
			logger.error("-->errore nel salvare le settinsg della tesoreria", e);
			throw new RuntimeException(e);
		}
	}

}
