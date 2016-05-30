package it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.vending.rest.manager.palmari.exception.ImportazioneException;

@Local
public interface PalmariImportaManager {
    /**
     *
     * @param codiceOperatore
     *            codice operatore
     * @param contenutoFile
     *            contenuto del file
     * @return ?
     * @throws eccezione
     *             durante l'importazione
     */
    String importa(String codiceOperatore, byte[] contenutoFile) throws ImportazioneException;
}
