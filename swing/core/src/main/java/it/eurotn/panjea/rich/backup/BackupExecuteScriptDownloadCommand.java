package it.eurotn.panjea.rich.backup;

import it.eurotn.rich.dialog.MessageAlert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

public class BackupExecuteScriptDownloadCommand extends ApplicationWindowAwareCommand {

    private class BackupTask extends SwingWorker<String, String> {

        private File file;

        /**
         * @param file
         *            file
         */
        public BackupTask(final File file) {
            this.file = file;
        }

        @Override
        protected String doInBackground() throws Exception {
            // Path pathBackupFile = Paths.get("/home/giangi/backup.log");
            // if (!Files.exists(pathBackupFile, LinkOption.NOFOLLOW_LINKS)) {
            // MessageDialog dialog = new MessageDialog("Errore nel creare il backup",
            // "Il percorso del file non esiste.");
            // dialog.showDialog();
            // }
            try {
                publish("#################### Esegue ed scarica il backup del database in una destinazione locale personalizzata #################################################");
                String startDateTimeStamp = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date());
                publish(startDateTimeStamp);
                publish("Backup in corso...");
                URL servlet = new URL(getServerAddress() + SCRIPT_ADDRESS);
                URLConnection conn = servlet.openConnection();
                conn.setDoOutput(true);
                try (BufferedInputStream bufferedReader = new BufferedInputStream(conn.getInputStream());
                        FileOutputStream fileOutputStreamChecksum = new FileOutputStream(file)) {
                    byte[] buf = new byte[1024];
                    long count = 0;
                    int len;
                    while ((len = bufferedReader.read(buf)) > 0) {
                        count += buf.length;
                        fileOutputStreamChecksum.write(buf, 0, len);
                    }
                    publish("Scaricati " + count + " byte");
                    publish("Backup completato!");
                    publish("Backup salvato in " + file.getAbsolutePath());
                    String endDateTimeStamp = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date());
                    publish(endDateTimeStamp);
                    MessageAlert alert = new MessageAlert(new DefaultMessage("Backup completato", Severity.INFO), 5000);
                    alert.showAlert();
                } catch (IOException e) {
                    logger.debug("errore nel richiamare lo script di backup " + getServerAddress() + SCRIPT_ADDRESS, e);
                    publish("errore nel richiamare lo script di backup " + getServerAddress() + SCRIPT_ADDRESS);
                    publish((e != null ? e.getMessage() : e + ""));
                }
            } catch (MalformedURLException e1) {
                logger.error(
                        "Url non corretto per richiamare lo script di backup " + getServerAddress() + SCRIPT_ADDRESS,
                        e1);
                publish("Url non corretto per richiamare lo script di backup " + getServerAddress());
                publish((e1 != null ? e1.getMessage() : e1 + ""));
                throw new RuntimeException(e1);
            } catch (IOException e1) {
                logger.error("errore nel richiamare lo script di backup " + getServerAddress() + SCRIPT_ADDRESS, e1);
                publish("errore nel richiamare lo script di backup " + getServerAddress() + SCRIPT_ADDRESS);
                publish((e1 != null ? e1.getMessage() : e1 + ""));
                throw new RuntimeException(e1);
            } finally {
                logger.debug("Errore eof, non importa");
            }
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            StringBuilder sb = new StringBuilder();
            for (String string : chunks) {
                sb.append(string);
                sb.append("\n");
            }
            BackupExecuteScriptDownloadCommand.this.firePropertyChange(PROPERTY_CHANGE_LOG, "", sb.toString());
            super.process(chunks);
        }
    }

    public static final String PROPERTY_CHANGE_LOG = "propertychangelog";

    private static final String SCRIPT_ADDRESS = "ant?pathfile=downloadbackup&task=scaricaBackup";

    /**
     * Costruttore.
     */
    public BackupExecuteScriptDownloadCommand() {
        super("backupExecuteScriptDownloadCommand");
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        final JFileChooser fileBackupChooser = new JFileChooser();
        int returnVal = fileBackupChooser.showOpenDialog(Application.instance().getActiveWindow().getControl());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File file = fileBackupChooser.getSelectedFile();
            if (file.exists()) {
                ConfirmationDialog dialog = new ConfirmationDialog("Conferma eliminazione file",
                        "Il file verrà sovrascritto. Continuare ?") {
                    @Override
                    protected void onConfirm() {
                        file.delete();
                    }
                };
                dialog.showDialog();
            }
            new BackupTask(file).execute();
        }
    }

    /**
     * ritprna l'url del server del cliente.
     * 
     * @return server url
     */
    public String getServerAddress() {
        String serverUrl;
        try {
            serverUrl = getSettingsManager().getUserSettings().getString("java_naming_provider_url");
        } catch (SettingsException e) {
            throw new RuntimeException(e);
        }
        if (serverUrl.isEmpty()) {
            serverUrl = "http://192.168.4.61";
        }
        serverUrl = "http://".concat(serverUrl).concat(":8090/");
        return serverUrl;
    }

    /**
     * @return settingsManager
     */
    private SettingsManager getSettingsManager() {
        SettingsManager settings = (SettingsManager) Application.instance().getApplicationContext()
                .getBean("settingManagerLocal");
        return settings;
    }

}
