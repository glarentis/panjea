package it.eurotn.panjea.manutenzioni.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;
import it.eurotn.panjea.manutenzioni.domain.ManutenzioneSettings;
import it.eurotn.panjea.manutenzioni.domain.UbicazioneInstallazione;
import it.eurotn.panjea.manutenzioni.manager.articolimi.interfaces.ParametriRicercaArticoliMI;

@Remote
public interface ManutenzioneAnagraficaService {

    /**
     * Cancella un {@link ArticoloMI}.
     *
     * @param id
     *            id ArticoloMI da cancellare
     */
    void cancellaArticoloMI(Integer id);

    /**
     * Cancella un {@link UbicazioneInstallazione}.
     *
     * @param id
     *            id UbicazioneInstallazione da cancellare
     */
    void cancellaUbicazioneInstallazione(Integer id);

    /**
     * Carica tutti i {@link ArticoloMI} presenti.
     *
     * @return {@link ArticoloMI} caricati
     */
    List<ArticoloMI> caricaArticoliMI();

    /**
     * Carica l'oggetto in base al suo id e valorizza il campo installazione (se presente)
     *
     * @param id
     *            id
     * @return oggetto caricato
     */
    ArticoloMI caricaArticoloByIdConInstallazione(Integer id);

    /**
     * Carica un {@link ArticoloMI} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link ArticoloMI} caricato
     */
    ArticoloMI caricaArticoloMIById(Integer id);

    /**
     * Carica il settings delle manutenzioni.<br/>
     * Se non esiste ne crea uno, lo salva e lo restituisce.
     *
     * @return <code>ManutenzioneSettings</code> caricato
     */
    ManutenzioneSettings caricaManutenzioniSettings();

    /**
     * Carica un {@link UbicazioneInstallazione} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link UbicazioneInstallazione} caricato
     */
    UbicazioneInstallazione caricaUbicazioneInstallazioneById(Integer id);

    /**
     * Carica tutti i {@link UbicazioneInstallazione} presenti.
     *
     * @return {@link UbicazioneInstallazione} caricati
     */
    List<UbicazioneInstallazione> caricaUbicazioniInstallazione();

    /**
     *
     * @param parametriRicerca
     *            parametri di ricerca per l'articolo
     * @return lista di articoli con id,codice e descrizione avvalorati
     */
    List<ArticoloMI> ricercaArticoloMI(ParametriRicercaArticoliMI parametriRicerca);

    /**
     * Salva un {@link ArticoloMI}.
     *
     * @param articoloMI
     *            {@link ArticoloMI} da salvare
     * @return {@link ArticoloMI} salvato
     */
    ArticoloMI salvaArticoloMI(ArticoloMI articoloMI);

    /**
     * Salva un {@link ManutenzioneSettings}.
     *
     * @param manutenzioneSettings
     *            settings da salvare
     * @return <code>ManutenzioneSettings</code> salvato
     */
    ManutenzioneSettings salvaManutenzioneSettings(ManutenzioneSettings manutenzioneSettings);

    /**
     * Salva un {@link UbicazioneInstallazione}.
     *
     * @param ubicazioneInstallazione
     *            {@link UbicazioneInstallazione} da salvare
     * @return {@link UbicazioneInstallazione} salvato
     */
    UbicazioneInstallazione salvaUbicazioneInstallazione(UbicazioneInstallazione ubicazioneInstallazione);

}
