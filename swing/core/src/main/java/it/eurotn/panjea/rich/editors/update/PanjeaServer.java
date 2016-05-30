package it.eurotn.panjea.rich.editors.update;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.Properties;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jboss.remoting.CannotConnectException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.service.interfaces.PanjeaMessageService;
import it.eurotn.panjea.anagrafica.util.PanjeaUpdateDescriptor;
import it.eurotn.panjea.anagrafica.util.PanjeaUpdateDescriptor.UpdateOperation;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.editors.update.step.StepUpdate;
import it.eurotn.panjea.rich.editors.update.stream.ProgressInputStream;
import it.eurotn.panjea.rich.editors.update.stream.ProgressOutputStream;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.report.ReportManager;

public class PanjeaServer implements InitializingBean {

    public static final String BEAN_ID = "panjeaServer";
    private static Logger logger = Logger.getLogger(PanjeaServer.class);

    public static final String PROPERTY_STEP = "step";
    public static final String PROPERTY_BYTE_TRASMETTI = "byteTrasmessi";
    public static final String PROPERTY_LOG = "log";

    private StepUpdate statoCorrente;
    private Boolean cancel = false;
    private final PropertyChangeSupport propertyChangeSupport;

    private UpdateSettings updateSettings;

    private SettingsManager settingsManager;

    private PanjeaMessageService panjeaMessageService;

    private ISicurezzaBD sicurezzaBD;

    /**
     * if (checksumFile != null) { checksumFile.delete(); } Costruttore.
     */
    public PanjeaServer() {
        statoCorrente = StepUpdate.WAITING;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Aggiunge un listener al property change.
     *
     * @param listener
     *            listener da aggiungere
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        updateSettings = new UpdateSettings(settingsManager);
    }

    /**
     * Chiama la servlet che aggiorna il file di properties del cliente nel server.
     *
     * @param codiceAzienda
     *            codiceAzienda
     * @return = log del processo
     */
    private String aggiornaProperties(String codiceAzienda) {
        InputStream respons = null;
        StringBuffer sb = null;
        try {
            URL servlet = new URL(updateSettings.getUrlPropertiesUpdateServlet(codiceAzienda));
            URLConnection conn = servlet.openConnection();
            conn.setDoOutput(true);
            respons = conn.getInputStream();
            int ch;
            sb = new StringBuffer();
            while ((ch = respons.read()) != -1) {
                sb.append((char) ch);
                if (sb.length() > 50) {
                    propertyChangeSupport.firePropertyChange(PROPERTY_LOG, "", sb.toString());
                    sb = new StringBuffer();
                }
            }
            if (sb.length() > 0) {
                propertyChangeSupport.firePropertyChange(PROPERTY_LOG, "", sb.toString());
            }
        } catch (EOFException e) {
            logger.error("EOFException", e);
        } catch (IOException e) {
            logger.error("-->errore nell'aggiornare le properties sul server", e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
     * Applica l'update scaricato.
     */
    private void applyUpdate() {
        InputStream respons = null;
        StringBuffer sb = null;
        changeStato(StepUpdate.APPLY_UPDATE);

        String servletUpdate = UpdateSettings.UPDATE_SERVER_APPLY_URL;

        String serverStatus = getServerStatus();
        // se panjeaserver nuovo mi arriva questa stringa
        if (serverStatus.startsWith("<li>")) {
            // devo fermare jboss prima di chiamare la servlet di aggiornamento che decomprime il
            // file panjeaUpdate.zip,
            // altrimenti non posso decomprimere eventuali librerie e file utilizzati da jboss
            stopJboss();
            servletUpdate = UpdateSettings.UPDATE_SERVER_NEW_APPLY_URL;
        }

        try {
            URL servlet = updateSettings.getPanjeaServerAddress(servletUpdate);
            URLConnection conn = servlet.openConnection();
            conn.setDoOutput(true);
            respons = conn.getInputStream();
            int ch;
            sb = new StringBuffer();
            try {
                while ((ch = respons.read()) != -1) {
                    sb.append((char) ch);
                    if (sb.length() > 150) {
                        propertyChangeSupport.firePropertyChange(PROPERTY_LOG, "", sb.toString());
                        sb = new StringBuffer();
                    }
                }
            } catch (IOException ioe) {
                if (ioe != null && ioe.getMessage() != null && ioe.getMessage().indexOf("Premature EOF") != -1) {
                    logger.warn("Errore nel leggere il file di aggiornamento, provo ad ignorare", ioe);
                } else {
                    throw new RuntimeException(ioe);
                }
            }
            if (sb.length() > 0) {
                propertyChangeSupport.firePropertyChange(PROPERTY_LOG, "", sb.toString());
            }
        } catch (RuntimeException e) {
            logger.error("-->errore nell' applicare l'update", e);
            throw e;
        } catch (Exception e) {
            logger.error("-->errore nell' applicare l'update", e);
            throw new PanjeaRuntimeException(e);
        }

        if (serverStatus.startsWith("<li>")) {
            // riavvio jboss dopo aver applicato le differenze
            startJboss();
        }
    }

    /**
     * Richiede di cancellare un eventuale update in esecuzione.
     */
    public synchronized void cancel() {
        cancel = true;
        try {
            PanjeaUpdateDescriptor updateDescriptor = new PanjeaUpdateDescriptor(
                    PanjeaSwingUtil.getUtenteCorrente().getUserName(), 0, UpdateOperation.ABORT);
            panjeaMessageService.sendPanjeaQueueMessage(updateDescriptor, PanjeaUpdateDescriptor.MESSAGE_SELECTOR);
        } catch (CannotConnectException e) {
            logger.warn(
                    "Errore nel lanciare il messaggio di annullamento dell'aggiornamento al server, impossibile connettersi",
                    e);
        }
    }

    /**
     * Cambia lo stato del manager e rilancia la property change.
     *
     * @param statoUpdate
     *            nuovo stato
     */
    private void changeStato(StepUpdate statoUpdate) {
        StepUpdate oldStato = StepUpdate.values()[statoCorrente.ordinal()];
        statoCorrente = statoUpdate;
        propertyChangeSupport.firePropertyChange(PROPERTY_STEP, oldStato, statoCorrente);
    }

    /**
     * Controlla lo stato di cancel e chiama una changeStato con WAITING che è a <code>true</code>.
     *
     * @return cancel
     */
    private boolean checkCancel() {
        if (cancel) {
            changeStato(StepUpdate.WAITING);
        }

        return cancel;
    }

    /**
     * Verifica la disponibilità della connessione verso il server di aggiornamento di Europa
     * Computer.
     *
     * @return true o false
     */
    public boolean checkEuroTnServerConnection() {
        String serverAddress = updateSettings.getUrlEurotnServer();
        URL url;
        try {
            url = new URL(serverAddress);
            URLConnection connection = url.openConnection();
            connection.connect();
        } catch (Exception e) {
            logger.error("Errore nel ecuperare la connessione al server di aggiornamento EUROPA Computer "
                    + updateSettings.getUrlEurotnServer(), e);
            return false;
        }
        return true;
    }

    /**
     * Scarica il file con il checksum.
     *
     * @return file contenente lo zip
     */
    private File downloadChecksum() {
        File checksum = null;
        changeStato(StepUpdate.CHECKSUM_PREPARE);
        URL servlet = updateSettings.getPanjeaServerAddress(UpdateSettings.UPDATE_SERVER_CHECKSUM_URL);
        try {
            checksum = File.createTempFile("checksum", ".zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream is = new ProgressInputStream(servlet, propertyChangeSupport, StepUpdate.CHECKSUM_DOWNLOAD);
                OutputStream oschecksum = new FileOutputStream(checksum)) {
            IOUtils.copyLarge(is, oschecksum);
            // FileUtils.copyInputStreamToFile(is, checksum);
        } catch (IOException e1) {
            logger.error("-->errore nel creare il file temporaneo per scaricare il checksum", e1);
            throw new RuntimeException(e1);
        }
        return checksum;
    }

    /**
     * scarica il file de upload, pasando il clientzip checksum.
     *
     * @param checksum
     *            file de checksum
     * @param codiceAzienda
     *            azienda attuale.
     * @return file di update
     */
    private File downloadUpdate(File checksum, String codiceAzienda) {
        String url = updateSettings.getUrlDownloaUpdateServlet();
        File fileUpdate = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httppost = new HttpPost(url);

            FileBody fileChecksumPart = new FileBody(checksum);
            StringBody aziendaPart = new StringBody(codiceAzienda, ContentType.TEXT_PLAIN);

            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("file1", fileChecksumPart)
                    .addPart("usuario", aziendaPart).build();

            httppost.setEntity(reqEntity);

            changeStato(StepUpdate.CHECKSUM_UPLOAD);

            try (CloseableHttpResponse response = httpclient.execute(httppost)) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        fileUpdate = File.createTempFile("update", ".zip");
                        fileUpdate.deleteOnExit();
                        OutputStream fileUpdateOutputStream = new ProgressOutputStream(new FileOutputStream(fileUpdate),
                                propertyChangeSupport);

                        IOUtils.copy(resEntity.getContent(), fileUpdateOutputStream);
                        EntityUtils.consume(resEntity);
                    }
                }
            }
        } catch (Exception e1) {
            logger.error("-->errore nel spedire il checksum e scaricare l'update dal server", e1);
            throw new RuntimeException(e1);
        }
        return fileUpdate;
    }

    /**
     * @return properties contenente versione e buildid di panjea
     */
    public Properties getApplicationProperties() {

        String propertiesUrl = updateSettings.getVersionPropertiesPath();

        Properties properties = null;
        if (!propertiesUrl.isEmpty()) {
            try {
                URL servlet = new URL(propertiesUrl);
                URLConnection conn = servlet.openConnection();
                conn.setDoOutput(true);

                properties = new Properties();
                properties.load(conn.getInputStream());
            } catch (IOException e) {
                logger.debug("propertiesUrl non raggiungibile");
            }
        }

        return properties;
    }

    /**
     * Data una lista di aziende richiede il file ${azienda}.properties dal server eurotn.it.
     *
     * @param aziende
     *            la lista di aziende di cui cercare il properties
     * @return il primo Properties dell'azienda o null se nessu
     */
    private Properties getRemoteProperties(String cliente) {
        Properties propertiesRemoto = null;
        try {
            URL servlet = new URL(updateSettings.getUrlPropertiesDownloadServlet(cliente));
            InputStream isRemoto = servlet.openStream();
            propertiesRemoto = new Properties();
            propertiesRemoto.load(isRemoto);
            isRemoto.close();
        } catch (Exception e) {
            logger.warn("Errore nel richiedere la risorsa a Eurotn.it");
        }
        return propertiesRemoto;
    }

    /**
     * @return url del server
     */
    public URL getServerAddress() {
        return updateSettings.getPanjeaServerAddress("");
    }

    /**
     * @return stringa con lo stato di jboss
     */
    public String getServerStatus() {
        String status = "";
        try (InputStream in = updateSettings.getPanjeaServerAddress(UpdateSettings.STATE_URL).openStream()) {
            status = IOUtils.toString(in);
        } catch (IOException e) {
            status = "errore nel recuperare lo stato del server <BR>Indirizzo:"
                    + updateSettings.getPanjeaServerAddress(UpdateSettings.STATE_URL);
            logger.warn(status, e);
        }
        return status;
    }

    /**
     * @return Returns the settingsManager.
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    /**
     * @return the sicurezzaBD
     */
    public ISicurezzaBD getSicurezzaBD() {
        return sicurezzaBD;
    }

    /**
     * Verifica se è disponibile un aggiornamento di Panjea dal server Eurotn per il codice azienda
     * corrente.<br>
     * La disponibilità viene controllata comparando il buildid locale con il buildid richiesto al
     * server eurotn di aggiornamento (estratto dal properties dell'azienda).
     *
     * @return Properties remote del cliente. Null se non ho update
     */
    public Properties isUpdateAvailable() {
        Properties updatedProperties = null;
        File localVersionProperties = PanjeaSwingUtil.getHome().resolve("version.properties").toFile();
        File localProperties = PanjeaSwingUtil.getHome().resolve("panjeauser.properties").toFile();
        try (InputStream isVersionLocale = new FileInputStream(localVersionProperties);
                InputStream isPropertiesLocale = new FileInputStream(localProperties)) {
            Properties propertiesLocale = new Properties();
            propertiesLocale.load(isPropertiesLocale);

            Properties versionLocale = new Properties();
            versionLocale.load(isVersionLocale);

            String cliente = ((ReportManager) RcpSupport.getBean("reportManager")).getPathCodiceAzienda();
            if (StringUtils.isEmpty(cliente)) {
                cliente = propertiesLocale.get("aziende").toString().split(";")[0];
            }
            // properties remoto del properties della prima azienda trovato
            Properties remoteProperties = getRemoteProperties(cliente);
            if (remoteProperties != null) {
                String buildidRemoto = remoteProperties.getProperty("buildid");
                String buildidLocale = versionLocale.getProperty("buildid");
                if (!Objects.equals(buildidRemoto, buildidLocale)) {
                    updatedProperties = remoteProperties;
                }
            }
        } catch (Exception e) {
            logger.warn("IOException getPanjeaUpdatedProperties");
        }
        return updatedProperties;
    }

    /**
     * Controlla se il file zip è valido.
     *
     * @param file
     *            file
     * @return true o false
     */
    private boolean isZipValid(final File file) {
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(file);
            return true;
        } catch (ZipException e) {
            logger.error("--> Errore nel file zip per l'aggiornamento", e);
            return false;
        } catch (IOException e) {
            logger.error("--> Errore nel file zip per l'aggiornamento", e);
            return false;
        } finally {
            try {
                if (zipfile != null) {
                    zipfile.close();
                    zipfile = null;
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * Manda un messaggio sulla coda di aggiornamento della partenza e ritardo dell'update
     * dell'applicazione.
     *
     * @param delay
     *            tempo id secondi di ritardo
     */
    public void notifyApplicationUpdate(long delay) {
        try {
            PanjeaUpdateDescriptor updateDescriptor = new PanjeaUpdateDescriptor(
                    PanjeaSwingUtil.getUtenteCorrente().getUserName(), delay, UpdateOperation.START);
            panjeaMessageService.sendPanjeaQueueMessage(updateDescriptor, PanjeaUpdateDescriptor.MESSAGE_SELECTOR);
        } catch (CannotConnectException e) {
            logger.warn("Errore nel notificare l'inizio dell'aggiornamento al server, impossibile connettersi", e);
        }
    }

    /**
     *
     * @param listener
     *            listener da rimuovere
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * @return stringa con lo stato di jboss
     */
    public String serverTasks() {
        InputStream response = null;
        StringBuffer sb = null;
        try {
            URLConnection conn = updateSettings.getPanjeaServerAddress(UpdateSettings.TASK_URL).openConnection();
            conn.setDoOutput(true);
            response = conn.getInputStream();
            int ch;
            sb = new StringBuffer();
            while ((ch = response.read()) != -1) {
                sb.append((char) ch);
            }
        } catch (IOException e) {
            logger.error("serverTasks", e);
        }
        return sb.toString();
    }

    /**
     * @param panjeaMessageService
     *            the panjeaMessageService to set
     */
    public void setPanjeaMessageService(PanjeaMessageService panjeaMessageService) {
        this.panjeaMessageService = panjeaMessageService;
    }

    /**
     * @param settingsManager
     *            setter for settingManager.
     */
    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    /**
     * @param sicurezzaBD
     *            the sicurezzaBD to set
     */
    public void setSicurezzaBD(ISicurezzaBD sicurezzaBD) {
        this.sicurezzaBD = sicurezzaBD;
    }

    /**
     * Start Jboss server.
     */
    private void startJboss() {
        InputStream response = null;
        StringBuffer responseOfServer = null;
        try {
            URLConnection conn = updateSettings.getPanjeaServerAddress(UpdateSettings.UPDATE_SERVER_JBOSS_START)
                    .openConnection();
            conn.setDoOutput(true);
            response = conn.getInputStream();
            int ch;
            responseOfServer = new StringBuffer();
            while ((ch = response.read()) != -1) {
                responseOfServer.append((char) ch);
            }
        } catch (IOException e) {
            logger.error("startJboss", e);
        }
    }

    /**
     * Stop Jboss server.
     */
    private void stopJboss() {
        InputStream response = null;
        StringBuffer responseOfServer = null;
        try {
            URLConnection conn = updateSettings.getPanjeaServerAddress(UpdateSettings.UPDATE_SERVER_JBOSS_STOP)
                    .openConnection();
            conn.setDoOutput(true);
            response = conn.getInputStream();
            int ch;
            responseOfServer = new StringBuffer();
            while ((ch = response.read()) != -1) {
                responseOfServer.append((char) ch);
            }
        } catch (IOException e) {
            logger.error("stopJboss", e);
        }
    }

    /**
     * Inizia l'aggiornamento e notifica i vari eventi tramite cambiamento delle proprietà
     * PROPERTY_STATO E PROPERTY_BYTE_TRASMESSI.
     */
    public void update() {
        File checksumFile = null;
        File updateFile = null;
        try {
            boolean debugMode = settingsManager.getUserSettings().getBoolean("debugMode");
            if (debugMode && ((ReportManager) RcpSupport.getBean("reportManager")).getAziendaStampa().isEmpty()) {
                MessageDialog dialog = new MessageDialog("Attenzione",
                        new DefaultMessage("Impostare l'azienda di stampa per l'aggiornamento", Severity.INFO));
                dialog.showDialog();
                return;
            }

            // il report manager restituisce il codice azienda impostato dall'utente
            // o il codice azienda dell'utente corrente
            String codiceAzienda = ((ReportManager) RcpSupport.getBean("reportManager")).getPathCodiceAzienda();

            cancel = false;

            // Prepara e scarica il file del checksum
            checksumFile = downloadChecksum();

            // Upload del file checksum e download del file di aggiornamento dal server di europa
            if (checkCancel() || !checkEuroTnServerConnection()) {
                return;
            }
            updateFile = downloadUpdate(null, codiceAzienda);
            if (updateFile == null) {
                // nessun aggiornamento da effettuare
                cancel = true;
                changeStato(StepUpdate.NOT_UPDATE_FILE);
            }

            if (!isZipValid(updateFile)) {
                cancel = true;
                changeStato(StepUpdate.UPDATE_FILE_CORRUPTED);
            }

            // Spedisco il file dell'aggiornamento al server di panjea del cliente
            if (checkCancel()) {
                return;
            }
            uploadUpdate(updateFile);

            if (checkCancel()) {
                return;
            }
            // applico gli aggiornamenti
            applyUpdate();
            // aggiorno la versione sul server di eurotn
            updateVersion(codiceAzienda);

        } catch (Exception e) {
            logger.error("-->errore nel recuperare lo stato del server", e);
            throw new PanjeaRuntimeException(e);
        } finally {
            changeStato(StepUpdate.WAITING);
            if (checksumFile != null) {
                checksumFile.delete();
            }
            if (updateFile != null) {
                updateFile.delete();
            }
        }
    }

    /**
     * Aggiorna la versione sul server di euroTn.
     *
     * @param codiceAzienda
     *            azienda attuale.
     */
    private void updateVersion(String codiceAzienda) {
        changeStato(StepUpdate.UPDATE_VERSION);
        InputStream response = null;
        StringBuffer responseOfServer = null;
        try {
            // Se l'aggiornamento è andato a buon fine il server di panjea ha il properties
            // aggiornato.
            // La servlet UPDATE_OK_SERVER_URL controlla che sul properties del server di panjea
            // le proprietà current_version e version siano uguali. Significa che l'aggiornamento ha
            // avuto successo.

            URLConnection conn = updateSettings.getPanjeaServerAddress(UpdateSettings.UPDATE_OK_SERVER_URL)
                    .openConnection();
            conn.setDoOutput(true);
            response = conn.getInputStream();
            int ch;
            responseOfServer = new StringBuffer();
            while ((ch = response.read()) != -1) {
                responseOfServer.append((char) ch);
            }
        } catch (IOException e) {
            logger.error("updateok", e);
        }

        if (responseOfServer != null && responseOfServer.toString().equals("true")) {
            aggiornaProperties(codiceAzienda);
        }
    }

    /**
     * Scarica il file in panjeaServer.
     *
     * @param update
     *            = il file a portare
     */
    private void uploadUpdate(File update) {
        changeStato(StepUpdate.UPDATE_UPLOAD);

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) updateSettings.getPanjeaServerAddress(UpdateSettings.UPDATE_UPLOAD_URL)
                    .openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // dice alla connection che voglio uno streaming, passando -1 significa che non conosco
            // le dimensioni
            conn.setChunkedStreamingMode(-1);
            IOUtils.copy(new FileInputStream(update), conn.getOutputStream());
            //
            // conn.connect();
            // try (OutputStream dos = conn.getOutputStream();
            // FileInputStream fileOutputStreamUpdate = new FileInputStream(update)) {
            //
            // byte[] buf = new byte[LENGHT_BUFFER];
            // long count = 0;
            // int len;
            //
            // while ((len = fileOutputStreamUpdate.read(buf)) != -1) {
            // if (checkCancel()) {
            // break;
            // }
            // count += buf.length;
            // dos.write(buf, 0, len);
            // buf = new byte[LENGHT_BUFFER];
            // propertyChangeSupport.firePropertyChange(PROPERTY_BYTE_TRASMETTI, 0, count);
            // }
            // }
        } catch (Exception e) {
            logger.error("putUpdateFile", e);
        }
    }

}
