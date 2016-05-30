package it.eurotn.panjea.anagrafica.manager.depositi.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.manager.interfaces.CrudManager;

@Local
public interface TipiDepositoManager extends CrudManager<TipoDeposito> {
    /**
     *
     * @param codice
     *            codice tipo deposito
     * @return tipoDeposito
     */
    TipoDeposito caricaByCodice(String codice);

    /**
     *
     * @param codice
     *            codice tipo deposito
     * @return tipo deposito trovato. Se non esiste viene creato un tipo deposito con il codice
     *         ricercato
     */
    TipoDeposito caricaOCreaByCodice(String codice);
}
