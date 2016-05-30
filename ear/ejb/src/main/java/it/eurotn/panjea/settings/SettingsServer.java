package it.eurotn.panjea.settings;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.SessionContext;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.Service;

import it.eurotn.panjea.exception.GenericException;

@Service(objectName = "panjea:service=Settings")
public final class SettingsServer implements SettingsServerMBean {

    private static final Logger LOGGER = Logger.getLogger(SettingsServer.class);
    private Properties properties = new Properties();
    private Map<String, AziendaSettings> aziendaSetting;

    private boolean testMode = false;
    private Boolean dmsEnable = null;

    @Resource
    private SessionContext sessionContext;

    private void checkDmsEnabled() {
        String bindAddress = System.getProperty("bind.address");
        String earAddress = "http://" + bindAddress
                + ":8080/jmx-console/HtmlAdaptor?action=inspectMBean&name=jboss.j2ee%3Aservice%3DEARDeployment%2Curl%3D%2720logicaldoc.ear%27";
        URL url;
        try {
            url = new URL(earAddress);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            int responseCode = huc.getResponseCode();
            dmsEnable = responseCode == 200;
        } catch (Exception e) {
            LOGGER.error("-->errore nel verificare se il dms esiste", e);
            throw new GenericException("-->errore nel verificare se il dms esiste", e);
        }
    }

    @Override
    public void create() throws Exception {
        aziendaSetting = new HashMap<String, AziendaSettings>();
        URL urlProperties = this.getClass().getResource("/it/eurotn/panjea/settings/settings.properties");
        if (urlProperties != null) {
            properties.load(urlProperties.openStream());

            testMode = Boolean.parseBoolean(properties.getProperty("testMode", "false"));

            if (testMode) {
                aziendaSetting.put("test", createTestModeAziendaSettings());
            } else {
                String aziende = properties.get("aziende").toString();
                String[] aziendeSplit = aziende.split(";");
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                for (String azienda : aziendeSplit) {
                    // licenza
                    // data scadenza
                    StringBuilder sb = new StringBuilder(azienda).append(".").append("license");
                    String licenseString = StringUtils.defaultString((String) properties.get(sb.toString()), "E");

                    // data scadenza
                    sb = new StringBuilder(azienda).append(".").append("scadenza");
                    String dataScadenzaString = (String) properties.get(sb.toString());

                    // numero utenti
                    sb = new StringBuilder(azienda).append(".").append("nUtenti");
                    String numUtentiValue = (String) ObjectUtils.defaultIfNull(properties.get(sb.toString()), "-1");
                    int numUtenti = -1;
                    if (NumberUtils.isNumber(numUtentiValue)) {
                        numUtenti = Integer.parseInt(numUtentiValue);
                    }
                    Date dataScadenza = null;
                    if (dataScadenzaString != null) {
                        dataScadenza = sdf.parse(dataScadenzaString);
                    }
                    aziendaSetting.put(azienda, new AziendaSettings(azienda, dataScadenza, numUtenti, licenseString));
                }
            }
        } else {
            // non esiste il properties.
            testMode = true;
            aziendaSetting.put("test", createTestModeAziendaSettings());
        }
    }

    private AziendaSettings createTestModeAziendaSettings() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 10);
        return new AziendaSettings("test", calendar.getTime(), 100, "E");
    }

    @Override
    public void destroy() {
    }

    @Override
    public AziendaSettings getAziendaSettings(String codiceAzienda) {
        if (testMode) {
            return aziendaSetting.get("test");
        }
        return aziendaSetting.get(codiceAzienda);
    }

    @Override
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public boolean isDmsEnable() {
        if (dmsEnable == null) {
            checkDmsEnabled();
        }
        return dmsEnable;
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() {
    }

}
