/**
 * 
 */
package it.eurotn.panjea.rich.file;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.domain.rendicontazione.DatiSpedizione;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * @author fattazzo
 * 
 */
public class FTPFileTransport extends AbstractFileTransport {

    private static Logger logger = Logger.getLogger(FTPFileTransport.class);

    /**
     * Verifica che tutti i dati per la spedizione siano presenti.
     * 
     * @param datiSpedizione
     *            dati spedizione
     * @return <code>true</code> se validi
     */
    private boolean isDatiSpedizioneValid(DatiSpedizione datiSpedizione) {
        boolean result = true;

        result = result && datiSpedizione != null && !StringUtils.isEmpty(datiSpedizione.getIndirizzoFTP())
                && !StringUtils.isEmpty(datiSpedizione.getUserName());

        if (!"anonymous".equals(datiSpedizione.getUserName())) {
            result = result && !StringUtils.isEmpty(datiSpedizione.getPassword());
        }

        return result;
    }

    @Override
    public boolean send(List<byte[]> files, DatiSpedizione datiSpedizione, Map<String, Object> parametri) {

        if (!isDatiSpedizioneValid(datiSpedizione)) {
            return false;
        }

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(datiSpedizione.getIndirizzoFTP());
            if (!ftpClient.login(datiSpedizione.getUserName(), datiSpedizione.getPassword())) {
                ftpClient.logout();
                GenericException exception = new GenericException("Errore durante l'autenticazione al server FTP.");
                PanjeaSwingUtil.checkAndThrowException(exception);
                return false;
            }
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                GenericException exception = new GenericException("Errore durante la connessione al server FTP.");
                PanjeaSwingUtil.checkAndThrowException(exception);
                return false;
            }
            int numeratoreFile = 1;
            for (byte[] bs : files) {
                InputStream in = new ByteArrayInputStream(bs);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

                String fileName = datiSpedizione.getNomeFile();
                // Se ho dei parametri richiesti di default attacco il valore
                if (parametri != null && parametri.containsKey("data")) {
                    DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                    fileName = fileName + df.format(parametri.get("data"));
                }
                fileName = getFileName(fileName, numeratoreFile, datiSpedizione);
                if (!StringUtils.isEmpty(datiSpedizione.getRemoteDirFTP())) {
                    fileName = "/" + datiSpedizione.getRemoteDirFTP() + "/" + fileName;
                }
                boolean store = ftpClient.storeFile(fileName, in);
                in.close();

                if (!store) {
                    return false;
                }

                numeratoreFile++;
            }
        } catch (Exception e) {
            GenericException exception = new GenericException("Errore durante la connessione al server FTP.");
            PanjeaSwingUtil.checkAndThrowException(exception);
        } finally {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("--> Errore nel chiudere la connessione al server FTP.");
                }
            }
        }

        return true;
    }
}
