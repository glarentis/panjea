package it.eurotn.panjea.settings;

import org.jboss.annotation.ejb.Management;

@Management
public interface SettingsServerMBean {

    /**
     * Lifecycle.
     *
     * @throws Exception
     *             eccezione generica
     */
    void create() throws Exception;

    /**
     * Lifecycle.
     *
     * @throws Exception
     */
    void destroy();

    /**
     *
     * @param codiceAzienda
     *            codice Azienda per le settings
     * @return settings dell'azienda
     */
    AziendaSettings getAziendaSettings(String codiceAzienda);

    /**
     *
     * @param key
     *            chiave da ricercare
     * @return valore del settings. Se criptato descripta il valore
     */
    String getProperty(String key);

    boolean isDmsEnable();

    /**
     * Lifecycle.
     *
     * @throws Exception
     *             eccezione generica
     */
    void start() throws Exception;

    /**
     * Lifecycle.
     *
     */
    void stop();
}
