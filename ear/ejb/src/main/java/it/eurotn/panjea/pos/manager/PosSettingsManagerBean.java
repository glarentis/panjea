/**
 * 
 */
package it.eurotn.panjea.pos.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.pos.domain.PosSettings;
import it.eurotn.panjea.pos.manager.interfaces.PosSettingsManager;
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
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.PosSettingsManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PosSettingsManager")
public class PosSettingsManagerBean implements PosSettingsManager {

	private static Logger logger = Logger.getLogger(PosSettingsManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext context;

	@Override
	public PosSettings caricaPosSettings() {
		logger.debug("--> Enter caricaPosSettings");

		Query query = panjeaDAO.prepareNamedQuery("PosSettings.caricaAll");
		query.setParameter("codiceAzienda", getAzienda());

		PosSettings posSettings = null;

		// carico il settings, se non lo trovo ne creo uno di default
		try {
			posSettings = (PosSettings) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> PosSettings non trovato, ne creo uno nuovo");
			posSettings = new PosSettings();
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento del pos settings", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaPosSettings");
		return posSettings;
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
	public PosSettings salvaPosSettings(PosSettings posSettings) {
		logger.debug("--> Enter salvaPosSettings");

		PosSettings posSettingsSalvati = null;

		if (posSettings.getCodiceAzienda() == null) {
			posSettings.setCodiceAzienda(getAzienda());
		}

		try {
			posSettingsSalvati = panjeaDAO.save(posSettings);
		} catch (Exception e) {
			logger.error("--> errore durante il salvataggio dei settings.", e);
			throw new RuntimeException("errore durante il salvataggio dei settings.", e);
		}

		logger.debug("--> Exit salvaPosSettings");
		return posSettingsSalvati;
	}

}
