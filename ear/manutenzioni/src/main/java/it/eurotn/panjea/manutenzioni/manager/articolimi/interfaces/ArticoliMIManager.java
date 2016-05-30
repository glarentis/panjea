package it.eurotn.panjea.manutenzioni.manager.articolimi.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;

@Local
public interface ArticoliMIManager extends CrudManager<ArticoloMI> {
    /**
     * Carica l'oggetto in base al suo id e valorizza il campo installazione (se presente)
     *
     * @param id
     *            id
     * @return oggetto caricato
     */
    ArticoloMI caricaByIdConInstallazione(Integer id);

    /**
     *
     * @param parametriRicerca
     *            parametri di ricerca per l'articolo
     * @return lista di articoli con id,codice e descrizione avvalorati
     */
    List<ArticoloMI> ricercaArticoloMI(ParametriRicercaArticoliMI parametriRicerca);

}