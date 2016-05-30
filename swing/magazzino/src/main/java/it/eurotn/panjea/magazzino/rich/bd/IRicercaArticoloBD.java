package it.eurotn.panjea.magazzino.rich.bd;

import java.util.List;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;

public interface IRicercaArticoloBD {

    /**
     * Ricerca ottimizzata per il caricamento degli articoli. La ricerca restituisce istanze di {@link ArticoloRicerca}
     * avvalorando solo le proprietà id,codice e descrizione.
     *
     * @param parametriRicercaArticolo
     *            parametri di ricerca
     * @return articoli trovati
     */
    List<ArticoloRicerca> ricercaArticoliSearchObject(ParametriRicercaArticolo parametriRicercaArticolo);
}
