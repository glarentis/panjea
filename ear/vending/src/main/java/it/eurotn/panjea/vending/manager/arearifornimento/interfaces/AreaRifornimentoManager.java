package it.eurotn.panjea.vending.manager.arearifornimento.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;
import it.eurotn.panjea.vending.manager.arearifornimento.ParametriRicercaAreeRifornimento;

@Local
public interface AreaRifornimentoManager extends CrudManager<AreaRifornimento> {

    /**
     * Esegue la ricerca delle aree preventivo presenti.
     *
     * @param parametri
     *            parametri di ricerca
     * @return aree di rifornimento presenti
     */
    List<AreaRifornimento> ricercaAreeRifornimento(ParametriRicercaAreeRifornimento parametri);
}