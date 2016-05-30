package it.eurotn.panjea.vega.rich.bd.importazione;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

import com.jidesoft.utils.Base64;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public class RestClient {
    private static final Logger LOGGER = Logger.getLogger(RestClient.class);
    private String baseClientUrl;

    public String execute(String restService) {
        StringBuilder result = new StringBuilder();
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(baseClientUrl + restService);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // conn.setRequestProperty("Accept", "application/html");
            conn.addRequestProperty("Authorization", getCredential());
            if (conn.getResponseCode() != 200) {
                LOGGER.error("-->errore nel cercare di chiaramare vega rest con il service. Cod errore : "
                        + conn.getResponseCode() + " url " + baseClientUrl + restService);
                return conn.getResponseMessage();
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line = "";
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            }
        } catch (IOException e1) {
            LOGGER.error("-->errore nel creare l'url per il restService. Url=" + baseClientUrl + restService, e1);
            throw new PanjeaRuntimeException(
                    "-->errore nel creare l'url per il restService. Url=" + baseClientUrl + restService, e1);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        ;
        return result.toString();
    }

    /**
     * @return Returns the baseClientUrl.
     */
    public String getBaseClientUrl() {
        return baseClientUrl;
    }

    private String getCredential() {
        String rawUser = PanjeaSwingUtil.getUtenteCorrente().getName();
        String rawPass = "pnj_adm";
        String rawCred = rawUser + ":" + rawPass;
        String myCred = Base64.encodeBytes(rawCred.getBytes());// encodeBase64String(rawCred.getBytes());
        return "Basic " + myCred;
    }

    /**
     * @param baseClientUrl
     *            The baseClientUrl to set.
     */
    public void setBaseClientUrl(String baseClientUrl) {
        this.baseClientUrl = baseClientUrl;
        if ("http://:8080/panjea-vega-rest".equals(baseClientUrl)) {
            this.baseClientUrl = "http://localhost:8080/panjea-vega-rest";
        }
    }

}
