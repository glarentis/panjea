package it.eurotn.panjea.vending.manager.evadts.importazioni.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.panjea.vending.manager.evadts.importazioni.ImportazioneFileEvaDtsResult;

@Local
public interface ImportazioneEvaDtsManager {

    /**
     * Importa le rilevazione EVA DTS contenute nel file. Se ci sono degli errori non viene importata nessuna
     * rilevazione.
     *
     * @param fileName
     *            nome del file
     * @param fileContent
     *            contenuto del file
     * @param evaDtsImportFolder
     *            definizione della directory di importazione
     * @return risultato dell'importazione del file
     */
    ImportazioneFileEvaDtsResult importaEvaDts(String fileName, byte[] fileContent,
            EvaDtsImportFolder evaDtsImportFolder);

    /**
     * Importa le rilevazione EVA DTS contenute nel file.
     *
     * @param fileName
     *            nome del file
     * @param fileContent
     *            contenuto del file
     * @param evaDtsImportFolder
     *            definizione della directory di importazione
     * @param forzaImportazione
     *            importa tutte le rilevazioni che non hanno errori
     * @return risultato dell'importazione del file
     */
    ImportazioneFileEvaDtsResult importaEvaDts(String fileName, byte[] fileContent,
            EvaDtsImportFolder evaDtsImportFolder, boolean forzaImportazione);
}
