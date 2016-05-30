package it.eurotn.panjea.fatturepa.signer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.fatturepa.domain.XMLFatturaPA;

public abstract class AbstractFileSigner implements FileSigner {

    private class FileSignWorker extends SwingWorker<SignResult, Void> {

        private String pin;

        private String fileToSign;

        /**
         * Costruttore.
         *
         * @param fileToSign
         *            file da segnare
         * @param pin
         *            pin per la firma
         */
        public FileSignWorker(final String fileToSign, final String pin) {
            this.fileToSign = fileToSign;
            this.pin = pin;
        }

        @Override
        protected SignResult doInBackground() throws Exception {

            return execSign(fileToSign, pin);
        }

    }

    private String softwareFilePath = null;
    private Path tempSignDir = null;

    private SignResult signResult = null;

    /**
     *
     * @param xmlFilePath
     *            path del file xml da firmare
     * @param pin
     *            pin
     * @return risultati della firma
     */
    protected abstract SignResult execSign(String xmlFilePath, String pin);

    /**
     * @return the softwareFilePath
     */
    public String getSoftwareFilePath() {
        return softwareFilePath;
    }

    /**
     * @return directory temporanea di lavoro per la firma dei file
     */
    protected Path getTempSignDir() {
        return tempSignDir;
    }

    @Override
    public void setSoftwarePath(String path) {
        softwareFilePath = path;
    }

    @Override
    public SignResult signFile(XMLFatturaPA fileXML, String pin) {

        Path xmlPath = null;
        try {
            // salvo tutti i file da firmare nella directory temporanea
            tempSignDir = Files.createTempDirectory("firmaPanjea");
            FileUtils.cleanDirectory(tempSignDir.toFile());

            xmlPath = Paths.get(tempSignDir.toFile() + File.separator + fileXML.getXmlFileName());
            Files.write(xmlPath, fileXML.getXmlFattura().getBytes());

        } catch (Exception e) {
            throw new RuntimeException("Errore durante il salvataggio del file xml", e);
        }

        // se non sono nell'EDT sposto tutto usando uno swingworker perchè devo stare in attesa del termine del processo
        // di firma
        if (SwingUtilities.isEventDispatchThread()) {
            FileSignWorker fileSignWorker = new FileSignWorker(xmlPath.toString(), pin);
            fileSignWorker.execute();
            try {
                signResult = fileSignWorker.get();
            } catch (Exception e) {
                throw new GenericException("Errore durante la firma del file.");
            }
        } else {
            signResult = execSign(xmlPath.toString(), pin);
        }

        return signResult;
    }
}
