/**
 * 
 */
package it.eurotn.panjea.rich.file;

import it.eurotn.panjea.magazzino.domain.rendicontazione.DatiSpedizione;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author fattazzo
 * 
 */
public abstract class AbstractFileTransport implements FileTransport {

    private final DateFormat dateFormat = new SimpleDateFormat("ddMMyy");

    /**
     * Restituisce il nome del file.
     * 
     * @param file
     *            nome del file
     * @param numeroFile
     *            numero del file
     * @param datiSpedizione
     *            dati spedizione
     * @return nome file
     */
    public File getFileName(File file, int numeroFile, DatiSpedizione datiSpedizione) {

        String fileName = file.getName();
        File fileResult = new File(
                file.getParent() + File.separator + getFileName(fileName, numeroFile, datiSpedizione));

        return fileResult;
    }

    /**
     * Restituisce il nome del file.
     * 
     * @param fileName
     *            nome del file
     * @param numeroFile
     *            numero del file
     * @param datiSpedizione
     *            dati spedizione
     * @return nome file
     */
    public String getFileName(String fileName, int numeroFile, DatiSpedizione datiSpedizione) {

        boolean aggiungiSuffisso = datiSpedizione.isApplicaSuffissoAlPrimoFile()
                || (!datiSpedizione.isApplicaSuffissoAlPrimoFile() && numeroFile > 1);

        String suffisso = "";

        switch (datiSpedizione.getSuffissoNomeFile()) {
        case DATA:
            if (aggiungiSuffisso) {
                suffisso = "_" + dateFormat.format(Calendar.getInstance().getTime());
            }
            break;
        case NUMERATORE:
            if (aggiungiSuffisso) {
                suffisso = "_" + new Integer(numeroFile).toString();
            }
            break;
        default:
            // lascio il nome file com'è
            suffisso = "";
            break;
        }

        String extension = "";
        String nomeFile = fileName;
        if (fileName.indexOf(".") != -1) {
            extension = fileName.substring(fileName.indexOf("."));
            nomeFile = fileName.replace(extension, "");
        }

        StringBuilder sb = new StringBuilder(nomeFile);
        sb.append(suffisso);
        sb.append(extension);

        fileName = sb.toString();

        return fileName;
    }
}
