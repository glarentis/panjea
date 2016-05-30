/**
 *
 */
package it.eurotn.rich.settings;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsFactory;

/**
 * Factory per create <code>PropertiesSetting</code>
 *
 * @author Aracno
 * @version 1.0, 14-giu-2006
 *
 */
public class PropertiesSettingsFactory extends PropertiesLoaderSupport implements SettingsFactory {

    private static final Logger LOGGER = Logger.getLogger(PropertiesSettingsFactory.class);

    public static final String BEAN_ID = "propertiesSettingsFactory";
    private Properties properties;
    private Resource location;

    @Override
    public Settings createSettings(String key) throws SettingsException {
        Settings settings = null;
        if (properties == null) {
            try {
                properties = mergeProperties();
            } catch (IOException e) {
                e.printStackTrace();
            }
            settings = new PropertiesSettings(this.properties, location);
        }
        return settings;
    }

    /**
     *
     * @return path della cartella di home di panjea (cartella di avvio)
     */
    public Path getHome() {
        try {
            return location.getFile().toPath().getParent();
        } catch (IOException e) {
            System.err.println("errore nel recuperare la posizione della home. Location richiesta " + location);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setLocations(Resource[] locations) {
        // tengo la prima location che trovo. non ho due file properties.
        for (Resource resource : locations) {
            if (resource.exists()) {
                location = resource;
                break;
            }
        }
        super.setLocations(locations);
    }
}
