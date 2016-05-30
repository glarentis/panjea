package it.eurotn.panjea.contabilita.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import it.eurotn.panjea.contabilita.domain.EntitaCointestazione;
import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente;
import it.eurotn.panjea.contabilita.util.ParametriCreazioneComPolivalente.TipologiaDati;

/**
 * @author fattazzo
 *
 */
@Remote
public interface ComunicazionePolivalenteService {

    /**
     * Cancella un {@link EntitaCointestazione}.
     *
     * @param id
     *            id EntitaCointestazione da cancellare
     */
    void cancellaEntitaCointestazione(Integer id);

    /**
     * Carica tutti i documenti utilizzati per la creazione della comunicazione polivalente in base ai parametri di
     * creazione.<br>
     * I documenti vengono caricati utilizzando sempre la {@link TipologiaDati.ANALITICI}.
     *
     * @param params
     *            parametri
     * @return documenti caricati
     */
    List<DocumentoSpesometro> caricaDocumenti(ParametriCreazioneComPolivalente params);

    /**
     * Carica tutti i {@link EntitaCointestazione} presenti.
     *
     * @return {@link EntitaCointestazione} caricati
     */
    List<EntitaCointestazione> caricaEntitaCointestazione();

    /**
     * Carica tutte le {@link EntitaCointestazione} dell'area contabile.
     *
     * @param idAreaContabile
     *            id area contabile
     * @return entita presenti
     */
    List<EntitaCointestazione> caricaEntitaCointestazioneByAreaContabile(Integer idAreaContabile);

    /**
     * Carica un {@link EntitaCointestazione} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link EntitaCointestazione} caricato
     */
    EntitaCointestazione caricaEntitaCointestazioneById(Integer id);

    /**
     * Genera il file dello spesometro per i parametri prescelti.
     *
     * @param params
     *            i parametri per la generazione dello spesometro
     * @return byte del file generato
     */
    byte[] genera(ParametriCreazioneComPolivalente params);

    /**
     * Salva un {@link EntitaCointestazione}.
     *
     * @param entitaCointestazione
     *            {@link EntitaCointestazione} da salvare
     * @return {@link EntitaCointestazione} salvato
     */
    EntitaCointestazione salvaEntitaCointestazione(EntitaCointestazione entitaCointestazione);

}
