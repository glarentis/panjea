package it.eurotn.panjea.vending.manager.casse.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.Cassa;

@Local
public interface CasseManager extends CrudManager<Cassa> {

    /**
     * Esegue la chiusura della cassa.
     *
     * @param id
     *            id cassa da chiudere
     */
    void chiudiCassa(Integer id);

    /**
     * Ricerca le casse presenti.
     *
     * @return casse trovate
     */
    List<Cassa> ricercaCasse();
}