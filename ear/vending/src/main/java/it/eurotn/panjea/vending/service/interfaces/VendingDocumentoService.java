package it.eurotn.panjea.vending.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.RigaLetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.manager.arearifornimento.ParametriRicercaAreeRifornimento;
import it.eurotn.panjea.vending.manager.evadts.importazioni.ImportazioneFileEvaDtsResult;
import it.eurotn.panjea.vending.manager.evadts.rilevazioni.ParametriRicercaRilevazioniEvaDts;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.RisultatiChiusuraLettureDTO;

@Remote
public interface VendingDocumentoService {

    /**
     * Aggiorna l'area di rifornimento con i dati dell'installazione.
     *
     * @param areaRifornimento
     *            area rifornimento
     * @param installazione
     *            installazione
     * @return area rifornimento aggiornata
     */
    AreaRifornimento aggiornaDatiInstallazione(AreaRifornimento areaRifornimento, Installazione installazione);

    /**
     * Aggiorna l'area di rifornimento con i dati dell'articolo.
     *
     * @param areaRifornimento
     *            area rifornimento
     * @param idDistributore
     *            id distributore
     * @return area rifornimento aggiornata
     */
    AreaRifornimento aggiornaDistributore(AreaRifornimento areaRifornimento, Integer idDistributore);

    /**
     * Aggiorna l'area di rifornimento con i dati dell'entita.
     *
     * @param areaRifornimento
     *            area rifornimento
     * @param idEntita
     *            id entita
     * @return area rifornimento aggiornata
     */
    AreaRifornimento aggiornaEntita(AreaRifornimento areaRifornimento, Integer idEntita);

    /**
     * Aggiorna l'area di rifornimento con i dati della sede entita.
     *
     * @param areaRifornimento
     *            area rifornimento
     * @param sedeEntita
     *            sede entita
     * @return area rifornimento aggiornata
     */
    AreaRifornimento aggiornaSedeEntita(AreaRifornimento areaRifornimento, SedeEntita sedeEntita);

    /**
     * Cancella un {@link AreaRifornimento}.
     *
     * @param id
     *            id AreaRifornimento da cancellare
     */
    void cancellaAreaRifornimento(Integer id);

    /**
     * Cancella una {@link LetturaSelezionatrice}.
     *
     * @param id
     *            id lettura da cancellare
     */
    void cancellaLetturaSelezionatrice(Integer id);

    /**
     * Cancella un {@link RilevazioneEvaDts}.
     *
     * @param id
     *            id RilevazioneEvaDts da cancellare
     */
    void cancellaRilevazioneEvaDts(Integer id);

    /**
     * Carica tutti i {@link AreaRifornimento} presenti.
     *
     * @return {@link AreaRifornimento} caricati
     */
    List<AreaRifornimento> caricaAreaRifornimento();

    /**
     * Carica un {@link AreaRifornimento} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link AreaRifornimento} caricato
     */
    AreaRifornimento caricaAreaRifornimentoById(Integer id);

    /**
     * Carica una {@link LetturaSelezionatrice} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link LetturaSelezionatrice} caricata
     */
    LetturaSelezionatrice caricaLetturaSelezionatriceById(Integer id);

    /**
     * Carica un {@link RilevazioneEvaDts} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link RilevazioneEvaDts} caricato
     */
    RilevazioneEvaDts caricaRilevazioneEvaDtsById(Integer id);

    /**
     * Carica tutti i {@link RilevazioneEvaDts} presenti.
     *
     * @return {@link RilevazioneEvaDts} caricati
     */
    List<RilevazioneEvaDts> caricaRilevazioniEvaDts();

    /**
     * Chiude le letture valide associando o creando il rifornimento corrispondente.
     *
     * @param letture
     *            letture da chiudere
     * @return resoconto della chiusura delle letture
     */
    RisultatiChiusuraLettureDTO chiudiLettureSelezionatrice(List<LetturaSelezionatrice> letture);

    /**
     * Importa le rilevazione EVA DTS contenute nel file.
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

    /**
     * Esegue la ricerca delle aree preventivo presenti.
     *
     * @param parametri
     *            parametri di ricerca
     * @return aree di rifornimento presenti
     */
    List<AreaRifornimento> ricercaAreeRifornimento(ParametriRicercaAreeRifornimento parametri);

    /**
     * @param idLettura
     *            id lettura
     * @return carica tutte le letture della selezionatrice
     */
    List<LetturaSelezionatrice> ricercaLettureSelezionatrice(Integer idLettura);

    /**
     * Carica tutte le righe di lettura che corrispondono al progressivo richiesto.
     *
     * @param progressivo
     *            progressivo
     * @return righe caricate
     */
    List<RigaLetturaSelezionatrice> ricercaRigheLetturaSelezionatrice(Integer progressivo);

    /**
     * Esegue la ricerca delle rilevazioni Eva-DTS in base ai parametri di ricerca.
     *
     * @param parametri
     *            parametri di ricerca
     * @return rilevazioni trovate
     */
    List<RilevazioneEvaDts> ricercaRilevazioniEvaDts(ParametriRicercaRilevazioniEvaDts parametri);

    /**
     * Salva un {@link AreaRifornimento}.
     *
     * @param areaRifornimento
     *            {@link AreaRifornimento} da salvare
     * @return {@link AreaRifornimento} salvato
     */
    AreaRifornimento salvaAreaRifornimento(AreaRifornimento areaRifornimento);

    /**
     * Salva una {@link LetturaSelezionatrice}.
     *
     * @param letturaSelezionatrice
     *            {@link LetturaSelezionatrice} da salvare
     * @return {@link LetturaSelezionatrice} salvata
     */
    LetturaSelezionatrice salvaLetturaSelezionatrice(LetturaSelezionatrice letturaSelezionatrice);

    /**
     * Salva un {@link RilevazioneEvaDts}.
     *
     * @param rilevazioneEvaDts
     *            {@link RilevazioneEvaDts} da salvare
     * @return {@link RilevazioneEvaDts} salvato
     */
    RilevazioneEvaDts salvaRilevazioneEvaDts(RilevazioneEvaDts rilevazioneEvaDts);

}
