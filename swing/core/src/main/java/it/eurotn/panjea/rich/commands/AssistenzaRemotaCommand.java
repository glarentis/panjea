package it.eurotn.panjea.rich.commands;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.MessageDialog;

/**
 * Command per l'assistenza remota.
 *
 * @author leonardo
 */
public class AssistenzaRemotaCommand extends ApplicationWindowAwareCommand {

    private static Logger logger = Logger.getLogger(AssistenzaRemotaCommand.class);

    public static final String COMMAND_ID = "assistenzaRemotaCommand";

    @Override
    protected void doExecuteCommand() {
        try {
            String osName = System.getProperty("os.name");
            osName = osName.toLowerCase();
            // se non è windows avverto che l'assistenza non è disponibile ed esco
            if (osName.indexOf("windows") == -1) {
                // se non trovo la string windows nella property os.name, loggo a error in modo da
                // spedire una email con
                // l'os.name corrente
                logger.error("Assistenza remota command, os.name " + osName);
                new MessageDialog("Attenzione!", "Assistenza remota disponibile solamente per sistemi Windows !")
                        .showDialog();
                return;
            }

            InputStream inputStream = AssistenzaRemotaCommand.class.getClassLoader().getResourceAsStream("europa.exe");
            if (inputStream != null) {
                // crea un file exe temporaneo
                File exeTmp = File.createTempFile("europa", ".exe");
                OutputStream out = new FileOutputStream(exeTmp);

                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }

                inputStream.close();
                out.flush();
                out.close();

                // segna il file exe come da cancellare dopo l'esecuzione
                exeTmp.deleteOnExit();

                Runtime run = Runtime.getRuntime();
                try {
                    run.exec(exeTmp.getAbsolutePath());
                } catch (Exception e) {
                    MessageDialog dialog = new MessageDialog("Errore", e.getMessage());
                    dialog.showDialog();
                }
            } else {
                logger.error("--> Risorsa europavnc.exe non trovata");
            }
        } catch (Exception e) {
            logger.error("Errore nell'esecuzione di AssistenzaRemotaCommand", e);
        }
    }

}
