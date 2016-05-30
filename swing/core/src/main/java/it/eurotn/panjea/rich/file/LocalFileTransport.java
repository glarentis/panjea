/**
 *
 */
package it.eurotn.panjea.rich.file;

import it.eurotn.panjea.magazzino.domain.rendicontazione.DatiSpedizione;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;

/**
 * @author fattazzo
 *
 */
public class LocalFileTransport extends AbstractFileTransport {

    private static Logger logger = Logger.getLogger(LocalFileTransport.class);

    private List<File> fileCreati;

    private SettingsManager settingsManager = (SettingsManager) Application.services()
            .getService(SettingsManager.class);

    /**
     * Restituisce la directory di salvataggio del file. La directory viene recuperata dal file .properties di panjea,
     * se questa non è settata viene restituita la home dell'utente.
     *
     * @return directory di esportazione
     */
    private String getExportDirectory() {
        String dirExp = null;
        try {
            dirExp = settingsManager.getUserSettings().getString("TipoEsportazione.FILE");
        } catch (SettingsException e) {
            logger.error("-->errore durante il caricamento delle settings", e);
        }

        if (dirExp == null || dirExp.isEmpty()) {
            dirExp = PanjeaSwingUtil.getHome().toString();
        }

        return dirExp;
    }

    /**
     * @return the fileCreati
     */
    public List<File> getFileCreati() {
        return Collections.unmodifiableList(fileCreati);
    }

    @Override
    public boolean send(List<byte[]> files, DatiSpedizione datiSpedizione, Map<String, Object> parametri) {

        fileCreati = new ArrayList<File>();

        String userDir = getExportDirectory();
        String fileName = datiSpedizione.getNomeFile();
        if (fileName == null) {
            fileName = "esportazione.txt";
        }
        // Se ho dei parametri richiesti di default attacco il valore
        if (parametri != null && parametri.containsKey("data")) {
            String extension = "";
            if (fileName.indexOf(".") != -1) {
                extension = fileName.substring(fileName.indexOf("."));
                fileName = fileName.replace(extension, "");
            }
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            fileName = fileName + df.format(parametri.get("data")) + extension;
        }
        JFileChooser fileChooser = new JFileChooser(userDir);
        fileChooser.setSelectedFile(new File(userDir + File.separator + fileName));
        int returnVal = fileChooser.showSaveDialog(Application.instance().getActiveWindow().getControl());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();

            try {
                // aggiorno la directory di salvataggio del tipo operazione
                settingsManager.getUserSettings().setString("TipoEsportazione.FILE", file.getParent());
                settingsManager.getUserSettings().save();
            } catch (Exception e) {
                PanjeaSwingUtil.checkAndThrowException(e);
            }

            int numeratoreFile = 1;
            BufferedOutputStream out = null;
            try {
                for (byte[] flusso : files) {
                    File fileFlusso = getFileName(file, numeratoreFile, datiSpedizione);
                    out = new BufferedOutputStream(new FileOutputStream(fileFlusso));
                    out.write(flusso);
                    out.close();

                    fileCreati.add(fileFlusso);

                    numeratoreFile++;
                }
            } catch (Exception e) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                MessageDialog dialog = new MessageDialog("ATTENZIONE",
                        new DefaultMessage("Errore durante il salvataggio del file.", Severity.ERROR));
                dialog.showDialog();
            }
        }
        return true;
    }

}
