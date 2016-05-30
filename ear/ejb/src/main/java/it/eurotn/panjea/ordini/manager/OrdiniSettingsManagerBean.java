/**
 *
 */
package it.eurotn.panjea.ordini.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.ordini.domain.OrdiniSettings;
import it.eurotn.panjea.ordini.manager.interfaces.OrdiniSettingsManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

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
 * Manager per la gestione di <code>OrdiniSettings</code>.
 *
 * @author fattazzo
 */
@Stateless(name = "Panjea.OrdiniSettingsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.OrdiniSettingsManager")
public class OrdiniSettingsManagerBean implements OrdiniSettingsManager {

	private static Logger logger = Logger.getLogger(OrdiniSettingsManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@Override
	public OrdiniSettings caricaOrdiniSettings() {
		logger.debug("--> Enter caricaOrdiniSettings");

		Query query = panjeaDAO.prepareNamedQuery("OrdiniSettings.caricaAll");
		query.setParameter("codiceAzienda", getAzienda());

		OrdiniSettings ordiniSettings = null;

		// carico il settings, se non lo trovo ne creo uno di default
		try {
			ordiniSettings = (OrdiniSettings) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> OrdiniSettings non trovato, ne creo uno nuovo");
			ordiniSettings = new OrdiniSettings();
			ordiniSettings.setCodiceAzienda(getAzienda());

			ordiniSettings = salvaOrdiniSettings(ordiniSettings);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento degli ordini settings", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaOrdiniSettings");
		return ordiniSettings;
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
	public OrdiniSettings salvaOrdiniSettings(OrdiniSettings ordiniSettings) {
		logger.debug("--> Enter salvaOrdiniSettings");

		try {
			ordiniSettings = panjeaDAO.save(ordiniSettings);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio di OrdiniSettings", e);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit salvaOrdiniSettings");
		return ordiniSettings;
	}

}
