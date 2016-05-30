package it.eurotn.panjea.intra.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.intra.domain.IntraSettings;
import it.eurotn.panjea.intra.manager.interfaces.IntraSettingsManager;
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

@Stateless(name = "Panjea.IntraSettingsManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.IntraSettingsManagerBean")
public class IntraSettingsManagerBean implements IntraSettingsManager {

	private static Logger logger = Logger.getLogger(IntraSettingsManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;
	@Resource
	private SessionContext context;

	@Override
	public IntraSettings caricaIntraSettings() {
		logger.debug("--> Enter caricaContabilitaSettings");

		Query query = panjeaDAO.prepareNamedQuery("IntraSettings.caricaAll");
		query.setParameter("codiceAzienda", getAzienda());

		IntraSettings intraSettings = null;

		// carico il settings, se non lo trovo ne creo uno di default
		try {
			intraSettings = (IntraSettings) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> ContabilitaSettings non trovato, ne creo uno nuovo");
			// se non trovo i settings impostati scelgo io di default i parametri obbligatori
			// usati nell'applicazione
			intraSettings = new IntraSettings();
			intraSettings.setCodiceAzienda(getAzienda());
			intraSettings = salvaIntraSettings(intraSettings);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento del contabilita settings", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaContabilitaSettings");
		return intraSettings;
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
	public IntraSettings salvaIntraSettings(IntraSettings intraSettingsToSave) {
		logger.debug("--> Enter salvaContabilitaSettings");

		IntraSettings intraSettingsSalvato;
		try {
			intraSettingsSalvato = panjeaDAO.save(intraSettingsToSave);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio di ContabilitaSettings", e);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit salvaContabilitaSettings");
		return intraSettingsSalvato;
	}

}
