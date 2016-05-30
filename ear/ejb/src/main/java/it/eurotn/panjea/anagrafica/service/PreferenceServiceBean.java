package it.eurotn.panjea.anagrafica.service;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * SessionBean per Preference.
 * 
 * @author adriano
 * @version 1.0, 19/dic/07
 */
@Stateless(name = "Panjea.PreferenceService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.PreferenceService")
public class PreferenceServiceBean implements PreferenceService {

	private static Logger logger = Logger.getLogger(PreferenceServiceBean.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	@RolesAllowed("strumenti")
	public void cancellaPreference(Preference preference) throws AnagraficaServiceException {
		logger.debug("--> Enter cancellaPreference");
		if (preference.isNew()) {
			logger.error("--> Impossibile eliminare la preference. ID nullo.");
			throw new AnagraficaServiceException("Impossibile eliminare la preference. ID nullo.");
		}

		try {
			panjeaDAO.delete(preference);
			logger.debug("--> Cancellata la preference con id " + preference.getId());
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaPreference");
	}

	@Override
	@SuppressWarnings("unchecked")
	public Preference caricaPreference(String key) throws PreferenceNotFoundException {
		logger.debug("--> Enter caricaPreference key " + key);
		javax.persistence.Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerChiave");
		query.setParameter("paramChiave", key);
		List<Preference> preferences = null;
		try {
			preferences = panjeaDAO.getResultList(query);
		} catch (ObjectNotFoundException onf) {
			logger.error("--> nessuna preference trovata per la preference " + key);
			throw new PreferenceNotFoundException(key);
		} catch (Exception e) {
			logger.error("--> errore ricerca preference con key " + key, e);
			throw new RuntimeException(e);
		}
		if (preferences.size() == 0) {
			throw new PreferenceNotFoundException(key);
		}
		Preference preference = preferences.get(0);
		return preference;
	}

	/**
	 * Carica le preferenze dell'utente per la chiave specificata.
	 * 
	 * @param key
	 *            chiave
	 * @param userName
	 *            utente
	 * @throws DAOException
	 *             eccezione generica
	 * @return preferenze caricate
	 */
	private Preference caricaPreference(String key, String userName) throws DAOException {
		logger.debug("--> Enter caricaPreference");
		Preference preference = null;
		try {
			Session hibernateSession = (Session) panjeaDAO.getEntityManager().getDelegate();
			Criteria criteria = hibernateSession.createCriteria(Preference.class);

			preference = (Preference) criteria.add(Restrictions.eq(Preference.PROP_CHIAVE, key))
					.add(Restrictions.eq(Preference.PROP_NOME_UTENTE, userName)).uniqueResult();
		} catch (HibernateException ex) {
			logger.error("--> errore nel caricare la preference con chiave " + key + " e nome utente " + userName, ex);
			throw new DAOException(ex);
		}
		logger.debug("--> Exit caricaPreference");
		return preference;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Preference> caricaPreferences() {
		logger.debug("--> Enter caricaPreferences");
		javax.persistence.Query query = panjeaDAO.prepareNamedQuery("Preference.caricaAll");
		List<Preference> preferences = null;
		try {
			preferences = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore nel caricamento delle preference.", e);
			throw new RuntimeException("errore nel caricamento delle preference.", e);
		}
		logger.debug("--> Exit caricaPreferences");
		return preferences;
	}

	@Override
	@SuppressWarnings("unchecked")
	@RolesAllowed("strumenti")
	public Map<String, String> caricaPreferences(String userName) {
		logger.debug("--> Enter caricaPreferences " + userName);
		Map<String, String> preferencesMap = new HashMap<String, String>();
		javax.persistence.Query query = panjeaDAO.prepareNamedQuery("Preference.caricaPerUtente");
		query.setParameter("paramNomeUtente", userName);
		List<Preference> preferences;
		try {
			preferences = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore, impossibile recuperare Preference ", e);
			throw new RuntimeException(e);
		}
		for (Preference preference : preferences) {
			preferencesMap.put(preference.getChiave(), preference.getValore());
		}
		logger.debug("--> Exit caricaPreferences " + preferencesMap.size());
		return preferencesMap;
	}

	@Override
	@RolesAllowed("strumenti")
	public Preference salvaPreference(Preference preference) {
		logger.debug("--> Enter salvaPreference");
		Preference preferenceSave = null;

		try {
			preferenceSave = panjeaDAO.save(preference);
		} catch (DAOException e) {
			logger.error("--> Errore durante il salvataggio della preference.");
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaPreference");
		return preferenceSave;
	}

	@Override
	@RolesAllowed("strumenti")
	public void salvaPreferences(Map<String, String> preferences, String userName) {
		Set<String> keyset = preferences.keySet();
		for (String key : keyset) {
			String value = preferences.get(key);

			// ricerca preference esistente
			Preference preference = null;
			try {
				preference = caricaPreference(key, userName);
				// preference =
				// PreferenceDAO.getInstance().caricaPreference(key,userName);
			} catch (Exception e) {
				logger.error("--> errore nel caricare la preference " + key + " ," + userName, e);
				throw new RuntimeException("--> errore nel caricare la preference " + key + " ," + userName, e);
			}
			// se non esiste ne creo una nuova
			if (preference == null) {
				preference = new Preference();
				preference.setChiave(key);
				preference.setNomeUtente(userName);
			}
			preference.setValore(value);

			// salvo la preference
			try {
				preference = panjeaDAO.save(preference);
			} catch (DAOException e) {
				logger.error("--> Errore durante il salvataggio della preference.");
				throw new RuntimeException(e);
			}
		}

	}

}
