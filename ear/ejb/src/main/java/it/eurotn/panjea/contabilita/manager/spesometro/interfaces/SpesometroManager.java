package it.eurotn.panjea.contabilita.manager.spesometro.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;

@Local
public interface SpesometroManager {

    /**
     * Carica tutti i documenti utilizzati per la creazione dello spesometro in base ai parametri di creazione.
     * 
     * @param params
     *            parametri
     * @return documenti caricati
     */
    List<DocumentoSpesometro> caricaDocumentiSpesometro(ParametriCreazioneComPolivalente params);

    /**
     * Genera il file dello spesometro per i parametri prescelti.
     * 
     * @param params
     *            i parametri per la generazione dello spesometro
     * @return byte del file generato
     */
    byte[] genera(ParametriCreazioneComPolivalente params);
}
