package it.eurotn.panjea.vending.manager.distributore.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.manager.distributore.ParametriRicercaDistributore;

@Local
public interface DistributoreManager extends CrudManager<Distributore> {

    /**
     * Carica tutti i distributori in base ai parametri di ricerca
     *
     * @param parametri
     *            parametri di ricerca
     * @return distributori caricati
     */
    List<Distributore> ricercaDistributori(ParametriRicercaDistributore parametri);

}
