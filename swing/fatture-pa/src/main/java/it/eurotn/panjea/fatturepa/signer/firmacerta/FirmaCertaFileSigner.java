package it.eurotn.panjea.fatturepa.signer.firmacerta;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import it.eurotn.panjea.fatturepa.signer.AbstractFileSigner;
import it.eurotn.panjea.fatturepa.signer.SignResult;

public class FirmaCertaFileSigner extends AbstractFileSigner {

    private static final Logger LOGGER = Logger.getLogger(FirmaCertaFileSigner.class);

    private static final String VAR_PIN = "Pin";
    private static final String VAR_TARGETDIR = "TargetDir";
    private static final String VAR_LOG_FILE_NAME = "LogFileName";

    @Override
    protected SignResult execSign(String fileToSign, String pin) {

        SignResult sign = new SignResult();
        sign.setFileToSign(fileToSign);
        try {
            // percorso e nome del file di log
            String logFile = getTempSignDir().toString() + File.separator + "Log"
                    + FilenameUtils.getBaseName(fileToSign) + ".txt";

            // cancello un eventuale log se esiste
            Files.deleteIfExists(Paths.get(logFile));

            // creo i parametri per la firma
            String param = new String("\"").concat(VAR_PIN).concat("=").concat(pin).concat(";").concat(VAR_TARGETDIR)
                    .concat("=").concat(getTempSignDir().toString()).concat(";").concat(VAR_LOG_FILE_NAME).concat("=")
                    .concat(logFile).concat("\"");

            Process process = new ProcessBuilder(getSoftwareFilePath(), fileToSign, "0", param).start();

            // aspetto che il processo di firma termini
            process.waitFor();

            // verifico se è stato creato il file firmato
            String fileSigned = getTempSignDir().toString() + File.separator + FilenameUtils.getName(fileToSign)
                    + ".p7m";

            if (Files.exists(Paths.get(fileSigned))) {
                sign.setFileSigned(fileSigned);
            }

            // apro il file di log e carico il contenuto nel signResult
            sign.setSignLog(new String(Files.readAllBytes(Paths.get(logFile))));

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Errore generico durante la firma del file.", e);
            }
            sign.setSignLog("Errore generico durante la firma del file. " + e.getMessage());
        }

        return sign;
    }
}
