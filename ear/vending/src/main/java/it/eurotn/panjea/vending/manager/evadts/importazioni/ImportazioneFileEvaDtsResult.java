package it.eurotn.panjea.vending.manager.evadts.importazioni;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;

public class ImportazioneFileEvaDtsResult implements Serializable {

    private static final long serialVersionUID = 2819358840320228353L;

    private String filePath;
    private String fileName;

    private Map<Integer, String> errorImport = new HashMap<>();

    private int numeroRilevazioniPresenti = 0;

    private EvaDtsImportFolder evaDtsImportFolder;

    private boolean importazioneForzata = false;

    /**
     * Aggiunge un errore di importazione.
     *
     * @param numeroRilevazione
     *            numero della rilevazione che ha dato errore
     * @param errore
     *            errore generato
     */
    public void aggiungiErrore(Integer numeroRilevazione, String errore) {
        errorImport.put(numeroRilevazione, errore);
    }

    /**
     * @return the errorImport
     */
    public Map<Integer, String> getErrorImport() {
        return Collections.unmodifiableMap(errorImport);
    }

    /**
     * @return the evaDtsImportFolder
     */
    public EvaDtsImportFolder getEvaDtsImportFolder() {
        return evaDtsImportFolder;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @return the numeroRilevazioniPresenti
     */
    public int getNumeroRilevazioniPresenti() {
        return numeroRilevazioniPresenti;
    }

    /**
     * @return numero rilevazioni importate
     */
    public int getRilevazioniImportate() {
        int rilImportate = numeroRilevazioniPresenti;

        if (!errorImport.isEmpty()) {
            if (numeroRilevazioniPresenti == errorImport.size()) {
                rilImportate = 0;
            } else {
                rilImportate -= importazioneForzata ? errorImport.size() : rilImportate;
            }
        }

        return rilImportate;
    }

    /**
     * @return the importazioneForzata
     */
    public boolean isImportazioneForzata() {
        return importazioneForzata;
    }

    /**
     * @param evaDtsImportFolder
     *            the evaDtsImportFolder to set
     */
    public void setEvaDtsImportFolder(EvaDtsImportFolder evaDtsImportFolder) {
        this.evaDtsImportFolder = evaDtsImportFolder;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @param filePath
     *            the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * @param importazioneForzata
     *            the importazioneForzata to set
     */
    public void setImportazioneForzata(boolean importazioneForzata) {
        this.importazioneForzata = importazioneForzata;
    }

    /**
     * @param numeroRilevazioniPresenti
     *            the numeroRilevazioniPresenti to set
     */
    public void setNumeroRilevazioniPresenti(int numeroRilevazioniPresenti) {
        this.numeroRilevazioniPresenti = numeroRilevazioniPresenti;
    }
}
