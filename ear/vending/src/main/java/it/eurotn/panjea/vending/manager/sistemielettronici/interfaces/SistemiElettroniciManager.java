package it.eurotn.panjea.vending.manager.sistemielettronici.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.SistemaElettronico;
import it.eurotn.panjea.vending.manager.sistemielettronici.ParametriRicercaSistemiElettronici;

@Local
public interface SistemiElettroniciManager extends CrudManager<SistemaElettronico> {

    /**
     * Esegue la ricerca dei sistemi elettronici.
     *
     * @param parametri
     *            parametri di ricerca
     * @return risultati
     */
    List<SistemaElettronico> ricercaSistemiElettronici(ParametriRicercaSistemiElettronici parametri);
}