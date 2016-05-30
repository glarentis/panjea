/**
 * 
 */
package it.eurotn.rich.settings;

import it.eurotn.panjea.rich.bd.IPreferenceBD;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsFactory;

/**
 * Factory dove viene creato un setting legato al business delegate client per la gestione delle preferences.
 * 
 * @author Leonardo
 */
public class PanjeaBdSettingsFactory implements SettingsFactory {

    static Logger logger = Logger.getLogger(PanjeaBdSettingsFactory.class);

    private String key;

    private IPreferenceBD preferenceBD;

    private Properties properties;

    /**
     * Crea il settings legato al preferenceBD. L'utente è sempre una stringa vuota
     */
    @Override
    public Settings createSettings(String paramKey) throws SettingsException {
        logger.debug("--> Enter createSettings " + paramKey);
        paramKey = "";
        if (properties == null) {
            // inizializzo le properties
            properties = new Properties();
            logger.debug("--> Carico le properties da BD");
            Map<String, String> propertiesMap = preferenceBD.caricaPreferences(paramKey);
            logger.debug("--> Trovate " + propertiesMap.size() + " properies");

            // se ci sono delle preferences le aggiungo come properties
            if (propertiesMap.size() > 0) {
                properties.putAll(propertiesMap);
                logger.debug("--> Aggiunte " + properties.size() + " properies");
            }
        }
        return new PanjeaBdSettings(properties, paramKey, preferenceBD);
    }

    public String getKey() {
        return key;
    }

    public IPreferenceBD getPreferenceBD() {
        return preferenceBD;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPreferenceBD(IPreferenceBD preferenceBD) {
        this.preferenceBD = preferenceBD;
    }

}
