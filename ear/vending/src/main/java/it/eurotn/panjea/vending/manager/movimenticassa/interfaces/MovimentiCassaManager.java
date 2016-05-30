package it.eurotn.panjea.vending.manager.movimenticassa.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.MovimentoCassa;

@Local
public interface MovimentiCassaManager extends CrudManager<MovimentoCassa> {

    /**
     * Cancella tutti i movimenti della cassa.
     *
     * @param cassaId
     *            id cassa
     */
    void cancellaByCassa(Integer cassaId);

    /**
     * Carica tutti gli oggetti presenti.
     *
     * @param includiChiusure
     *            include i movimenti di chiusura
     * @return oggetti caricati
     */
    List<MovimentoCassa> caricaAll(boolean includiChiusure);
}