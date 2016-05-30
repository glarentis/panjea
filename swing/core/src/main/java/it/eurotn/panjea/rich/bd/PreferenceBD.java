package it.eurotn.panjea.rich.bd;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.domain.Preference;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * @author Leonardo
 *
 */
public class PreferenceBD implements IPreferenceBD {

    public static final String BEAN_ID = "preferenceBD";

    private static final Logger LOGGER = Logger.getLogger(PreferenceBD.class);

    private PreferenceService preferenceService;

    @Override
    public void cancellaPreference(Preference preference) {
        LOGGER.debug("--> Enter cancellaPreference");
        try {
            preferenceService.cancellaPreference(preference);
        } catch (Exception e) {
            LOGGER.error("--> errore in cancellazione Preference", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        LOGGER.debug("--> Exit cancellaPreference");
    }

    @Override
    public Preference caricaPreference(String key) {
        LOGGER.debug("--> Enter caricaPreference");
        Preference preference = null;
        try {
            preference = preferenceService.caricaPreference(key);
        } catch (Exception e) {
            LOGGER.error("--> errore in carica Preference", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        return preference;
    }

    @Override
    public List<Preference> caricaPreferences() {
        LOGGER.debug("--> Enter caricaPreferences");
        List<Preference> preferences = null;
        try {
            preferences = preferenceService.caricaPreferences();
        } catch (Exception e) {
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        LOGGER.debug("--> Exit caricaPreferences ");
        return preferences;
    }

    @Override
    public Map<String, String> caricaPreferences(String userName) {
        LOGGER.debug("--> Enter caricaPreferences " + userName);
        Map<String, String> preferences = null;
        try {
            preferences = preferenceService.caricaPreferences("");
            LOGGER.debug("--> Preferenze caricate: " + preferences);
        } catch (Exception e) {
            LOGGER.error("---> Errore nel caricare le preferences", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        LOGGER.debug("--> Exit caricaPreferences");
        return preferences;
    }

    @Override
    public Preference salvaPreference(Preference preference) {
        LOGGER.debug("--> Enter salvaPreference");
        Preference preferenceSave = null;
        try {
            preferenceSave = preferenceService.salvaPreference(preference);
        } catch (Exception e) {
            LOGGER.error("--> errore in salvaPreference ", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        LOGGER.debug("--> Exit salvaPreference");
        return preferenceSave;
    }

    @Override
    public void salvaPreferences(Map<String, String> preferences, String username) {
        LOGGER.debug("--> Enter salvaPreferences");
        try {
            preferenceService.salvaPreferences(preferences, username);
        } catch (Exception e) {
            LOGGER.error("---> Errore nel caricare le preferences", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        LOGGER.debug("--> Exit salvaPreferences");
    }

    @Override
    public void setPreferenceService(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

}
