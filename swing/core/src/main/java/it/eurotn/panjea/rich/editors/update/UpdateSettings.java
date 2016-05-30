package it.eurotn.panjea.rich.editors.update;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;

import it.eurotn.panjea.utils.PanjeaSwingUtil;

//Gestisce i settings (indirizzi e variabili) per l'update
public class UpdateSettings {

    private static Logger logger = Logger.getLogger(UpdateSettings.class);

    public static final String UPDATE_SERVER_CHECKSUM_URL = "/ant?pathfile=zipChecksum";
    public static final String UPDATE_SERVER_JBOSS_STOP = "/ant?pathfile=jboss&task=stopjboss";
    public static final String UPDATE_SERVER_JBOSS_START = "/ant?pathfile=jboss&task=startjboss";
    public static final String UPDATE_SERVER_APPLY_URL = "/ant?pathfile=update";
    public static final String UPDATE_SERVER_NEW_APPLY_URL = "/update";
    public static final String UPDATE_OK_SERVER_URL = "/updateok";
    public static final String UPDATE_UPLOAD_URL = "/save";
    public static final String STATE_URL = "/state";
    public static final String TASK_URL = "/task";

    private String urlEurotnServer;

    private String urlPropertiesDownloadServlet;

    private String urlPropertiesUpdateServlet;

    private String urlDownloaUpdateServlet;

    /**
     * Costruttore.
     *
     * @param settingsManager
     *            settingsManager
     */
    public UpdateSettings(SettingsManager settingsManager) {
        try {
            urlEurotnServer = settingsManager.getUserSettings().getString("url_eurotn_server");
            if (StringUtils.isEmpty(urlEurotnServer)) {
                urlEurotnServer = "http://eurotn.it:9999/panjea";
            }
        } catch (SettingsException e) {
            logger.error("-->errore ", e);
        }

        try {
            urlPropertiesDownloadServlet = settingsManager.getUserSettings()
                    .getString("url_properties_download_servlet");
            if (StringUtils.isEmpty(urlPropertiesDownloadServlet)) {
                urlPropertiesDownloadServlet = "http://eurotn.it:9999/panjea/downloadproperties";
            }
            urlPropertiesDownloadServlet += "?installazione=";
        } catch (SettingsException e) {
            logger.error("-->errore ", e);
        }

        try {
            urlPropertiesUpdateServlet = settingsManager.getUserSettings().getString("url_properties_update_servlet");
            if (StringUtils.isEmpty(urlPropertiesUpdateServlet)) {
                urlPropertiesUpdateServlet = "http://eurotn.it:9999/panjea/aggiornaversionecliente";
            }
            urlPropertiesUpdateServlet += "?installazione=";
        } catch (SettingsException e) {
            logger.error("-->errore ", e);
        }

        try {
            urlDownloaUpdateServlet = settingsManager.getUserSettings().getString("url_download_update_servlet");
            if (StringUtils.isEmpty(urlDownloaUpdateServlet)) {
                urlDownloaUpdateServlet = "http://eurotn.it:9999/panjea/downloadupdate";
            }
        } catch (SettingsException e) {
            logger.error("-->errore ", e);
        }

    }

    /**
     * @param address
     *            url finale del servizio sul server di panje del cilente
     * @return url completo del servizio
     */
    public URL getPanjeaServerAddress(String address) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("http://").append(PanjeaSwingUtil.getServerUrl()).append(":8090").append(address);
        URL result;
        try {
            result = new URL(sb.toString());
        } catch (MalformedURLException e) {
            logger.error("-->errore nel creare l'url " + sb.toString(), e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * @return Returns the urlDownloadownloadServlet.
     */
    public String getUrlDownloaUpdateServlet() {
        return urlDownloaUpdateServlet;
    }

    /**
     * @return Returns the urlEurotnServer.
     */
    public String getUrlEurotnServer() {
        return urlEurotnServer;
    }

    /**
     * @return Returns the urlPropertiesDownloadServlet.
     */
    public String getUrlPropertiesDownloadServlet(String azienda) {
        return urlPropertiesDownloadServlet + azienda;
    }

    /**
     * @return Returns the urlPropertiesUpdateServlet.
     */
    public String getUrlPropertiesUpdateServlet(String azienda) {
        return urlPropertiesUpdateServlet + azienda;
    }

    /**
     *
     * @return properties contenente versione e build id di panjea
     */
    public String getVersionPropertiesPath() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("http://").append(PanjeaSwingUtil.getServerUrl()).append(":8080/panjea/version.properties");
        return sb.toString();
    }

}
