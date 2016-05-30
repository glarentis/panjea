package it.eurotn.panjea.vending.manager.prodotticollegati.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;

@Local
public interface ProdottiCollegatiVendingManager {

    /**
     * Carica tutti i prodotti del distributore.
     *
     * @param idDistributore
     *            id distributore
     * @return prodotti caricati
     */
    List<ProdottoCollegato> caricaProdottiCollegatiByDistributore(Integer idDistributore);

    /**
     * Carica tutti i prodotti dell'installaizone.
     *
     * @param idInstallazione
     *            id dell'installazione
     * @return prodotti collegati
     */
    List<ProdottoCollegato> caricaProdottiCollegatiByInstallazione(Integer idInstallazione);

    /**
     * Carica tutti i prodotti del modello.
     *
     * @param idModello
     *            id modello
     * @return prodotti caricati
     */
    List<ProdottoCollegato> caricaProdottiCollegatiByModello(Integer idModello);

    /**
     * Carica tutti i prodotti del tipo modello.
     *
     * @param idTipoModello
     *            id tipo modello
     * @return prodotti caricati
     */
    List<ProdottoCollegato> caricaProdottiCollegatiByTipoModello(Integer idTipoModello);

    /**
     * Esegue la ricerca degli articoli associati all'installazione.
     *
     * @param parametri
     *            parametri di ricerca
     * @return articoli caricati
     */
    List<ArticoloRicerca> ricercaArticoliInstallazione(ParametriRicercaArticolo parametri);
}
