/**
 *
 */
package it.eurotn.rich.settings;

import it.eurotn.panjea.rich.bd.IPreferenceBD;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author Leonardo
 *
 */
public class PanjeaBdSettings extends PropertiesSettings {

    private static Logger logger = Logger.getLogger(PanjeaBdSettings.class);

    private String userName;

    private IPreferenceBD preferenceBD;

    /**
     *
     * Costruttore.
     *
     * @param properties
     *            properties
     * @param userName
     *            userName
     * @param preferenceBD
     *            preferenceBD
     */
    public PanjeaBdSettings(final Properties properties, final String userName, final IPreferenceBD preferenceBD) {
        super(properties, null);
        this.userName = userName;
        this.preferenceBD = preferenceBD;
    }

    /**
     * @return the preferenceBD
     */
    public IPreferenceBD getPreferenceBD() {
        return preferenceBD;
    }

    @Override
    public void load() throws IOException {
    }

    @Override
    public void save() throws IOException {
        logger.debug("--> Enter save");
        Map<String, String> preferences = new HashMap<String, String>();
        Set<Object> keys = getProperties().keySet();
        for (Object object : keys) {
            String key = (String) object;
            String value = (String) getProperties().get(key);

            preferences.put(key, value);
        }
        preferenceBD.salvaPreferences(preferences, userName);
    }

    /**
     * @param preferenceBD
     *            the preferenceBD to set
     */
    public void setPreferenceBD(IPreferenceBD preferenceBD) {
        this.preferenceBD = preferenceBD;
    }

}
