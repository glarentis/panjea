package it.eurotn.panjea.rich;

import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;

public class LogSender extends TimerTask {

    private Timer timerSend;
    private static Logger logger = Logger.getLogger(LogSender.class);
    private SettingsManager settingsManager = null;

    /**
     * Costruttore.
     */
    public LogSender() {
        boolean debugMode = false;
        try {
            debugMode = getSettingsManager().getUserSettings().getBoolean("debugMode");
        } catch (SettingsException e) {
            logger.error("Errore nel caricare le settings", e);
        }
        if (timerSend == null && !debugMode) {
            timerSend = new Timer("sendLogTimer", true);
            timerSend.schedule(this, 0, 3600000);
        }
    }

    /**
     * Aggiunge un file allo stream zip con il nome file scelto.
     *
     * @param stream
     *            lo stream zip
     * @param file
     *            il file da aggiungere
     * @param nomeFile
     *            il nome da impostare al file aggiunto
     */
    private void addFile(ZipOutputStream stream, File file, String nomeFile) {
        try {
            byte[] buf = new byte[1024];
            FileInputStream in = new FileInputStream(file);
            stream.putNextEntry(new ZipEntry(nomeFile));

            // Transfer bytes from the file to the ZIP file
            int len;
            while ((len = in.read(buf)) > 0) {
                stream.write(buf, 0, len);
            }

            // Complete the entry
            stream.closeEntry();
            in.close();
        } catch (FileNotFoundException e) {
            logger.error("errore nell'aggiungere il file " + nomeFile + " allo zip", e);
        } catch (IOException e) {
            logger.error("errore nell'aggiungere il file " + nomeFile + " allo zip", e);
        } catch (Exception e) {
            logger.error("errore nell'aggiungere il file " + nomeFile + " allo zip", e);
        }
    }

    /**
     * @return .
     */
    private String getServerAddress() {
        try {
            if (getSettingsManager().getUserSettings().getBoolean("debugMode")) {
                return "http://192.168.4.253:8080/panjea/downloadlog";
            } else {
                return "http://eurotn.it:9999/panjea/downloadlog";
            }
        } catch (SettingsException e) {
            return "http://eurotn.it:9999/panjea/downloadlog";
        }
    }

    /**
     * @return SettingsManager
     */
    private SettingsManager getSettingsManager() {
        if (settingsManager == null) {
            settingsManager = (SettingsManager) ApplicationServicesLocator.services().getService(SettingsManager.class);
        }
        return settingsManager;
    }

    @Override
    public void run() {
        try {
            Path pathLog = PanjeaSwingUtil.getHome().resolve("log.txt");
            Path pathLogToSend = PanjeaSwingUtil.getHome().resolve("logToSend.txt");
            Path pathLogZipToSend = PanjeaSwingUtil.getHome().resolve("logToSend.zip");

            if (!Files.exists(pathLog)) {
                return;
            }

            Files.move(pathLog, pathLogToSend, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });

            // zip non ancora creato
            File fileZip = PanjeaSwingUtil.getHome().resolve("logToSend.zip").toFile();
            // creo il file zip, con java 7 c'è un bug per i sistemi windows quindi uso il vecchio sistema
            FileOutputStream fout = new FileOutputStream(fileZip);
            ZipOutputStream zout = new ZipOutputStream(fout);
            addFile(zout, pathLogToSend.toFile(), "log.txt");
            zout.finish();

            Files.delete(pathLogToSend);

            String url = getServerAddress();
            HttpURLConnection conn = null;
            URL servlet = new URL(url);
            conn = (HttpURLConnection) servlet.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // dice alla connection che voglio uno streaming, passando -1 significa che non conosco le dimensioni
            conn.setChunkedStreamingMode(-1);

            conn.connect();
            OutputStream dos = conn.getOutputStream();
            Files.copy(fileZip.toPath(), dos);
            dos.flush();
            dos.close();
            InputStream inStream = conn.getInputStream();
            IOUtils.copy(inStream, System.err);
            inStream.close();
        } catch (Exception e) {
            logger.error("-->errore nello spedire il file di zip", e);
        }
    }
}