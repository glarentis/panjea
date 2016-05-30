/**
 *
 */
package it.eurotn.panjea.beniammortizzabili2.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.beniammortizzabili2.domain.BeniSettings;
import it.eurotn.panjea.beniammortizzabili2.manager.interfaces.BeniAmmortizzabiliSettingsManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.BeniAmmortizzabiliSettingsManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.BeniAmmortizzabiliSettingsManager")
public class BeniAmmortizzabiliSettingsManagerBean implements BeniAmmortizzabiliSettingsManager {

	private static Logger logger = Logger.getLogger(BeniAmmortizzabiliSettingsManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext context;

	@Override
	public BeniSettings caricaBeniSettings() {
		logger.debug("--> Enter caricaBeniSettings");

		Query query = panjeaDAO.prepareNamedQuery("BeniSettings.caricaAll");
		query.setParameter("codiceAzienda", getAzienda());

		BeniSettings beniSettings = null;

		// carico il settings, se non lo trovo ne creo uno di default
		try {
			beniSettings = (BeniSettings) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> BeniSettings non trovato, ne creo uno nuovo");
			beniSettings = new BeniSettings();
			beniSettings.setCodiceAzienda(getAzienda());
			beniSettings = salvaBeniSettings(beniSettings);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento del beni settings", e);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit caricaBeniSettings");
		return beniSettings;
	}

	/**
	 * Recupera il codice azienda dell'utente autenticato nel context.
	 *
	 * @return String codice azienda
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@Override
	public BeniSettings salvaBeniSettings(BeniSettings beniSettings) {
		logger.debug("--> Enter salvaBeniSettings");

		try {
			beniSettings = panjeaDAO.save(beniSettings);
		} catch (DAOException e) {
			logger.error("--> errore durante il caricamento delle settings dei beni", e);
			throw new RuntimeException("errore durante il caricamento delle settings dei beni", e);
		}

		logger.debug("--> Exit salvaBeniSettings");
		return beniSettings;
	}

}
