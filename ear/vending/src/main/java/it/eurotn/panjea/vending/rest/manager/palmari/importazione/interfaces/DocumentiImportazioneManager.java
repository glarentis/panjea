package it.eurotn.panjea.vending.rest.manager.palmari.importazione.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.vending.rest.manager.palmari.exception.ImportazioneException;
import it.eurotn.panjea.vending.rest.manager.palmari.importazione.xml.ImportazioneXml;

@Local
public interface DocumentiImportazioneManager {
    /**
     * Importa il file xml ricevuto dal palmare
     *
     * @param codiceOperatore
     *            codice operatore
     * @param importazioneXml
     *            dati dell'xml
     * @return log importazione
     * @throws ImportazioneException
     *             rilanciata se ci sono errori
     */
    String importa(String codiceOperatore, ImportazioneXml importazioneXml) throws ImportazioneException;
}
