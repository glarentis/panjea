/**
 *
 */
package it.eurotn.rich.settings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.springframework.core.io.Resource;
import org.springframework.richclient.settings.AbstractSettings;
import org.springframework.richclient.settings.Settings;

/**
 *
 *
 * @author Aracno
 * @version 1.0, 14-giu-2006
 *
 */
public class PropertiesSettings extends AbstractSettings {

    private Properties properties;

    private Resource fileSettings;

    public PropertiesSettings(Properties properties, Resource location) {
        super(null, "");
        this.properties = properties;
        this.fileSettings = location;
    }

    public PropertiesSettings(Settings parent, String name) {
        super(parent, name);

    }

    @Override
    public String[] getKeys() {
        return properties.keySet().toArray(new String[0]);
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    protected boolean internalContains(String key) {
        return properties.containsKey(key);
    }

    @Override
    protected Settings internalCreateChild(String key) {
        return new PropertiesSettings(this, key);
    }

    @Override
    protected String internalGet(String key) {
        return properties.getProperty(key);
    }

    @Override
    protected String[] internalGetChildSettings() {
        return new String[0];
    }

    @Override
    protected void internalRemove(String key) {
        properties.remove(key);

    }

    @Override
    protected void internalRemoveSettings() {

    }

    @Override
    protected void internalSet(String key, String value) {
        properties.setProperty(key, value);

    }

    @Override
    public void load() throws IOException {

    }

    @Override
    public void save() throws IOException {
        try (OutputStream outputStream = new FileOutputStream(fileSettings.getFile())) {
            properties.store(outputStream, "");
        }

    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
