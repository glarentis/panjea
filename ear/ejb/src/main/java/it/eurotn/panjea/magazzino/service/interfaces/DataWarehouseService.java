package it.eurotn.panjea.magazzino.service.interfaces;

import javax.ejb.Remote;

import it.eurotn.panjea.magazzino.manager.interfaces.DataWarehouseManager;

@Remote
public interface DataWarehouseService {

    /**
     * Sincronizza tutte le anagrafiche ( articoli, clienti, depositi ).
     */
    void sincronizzaAnagrafiche();

    /**
     * Sincronizza la dimensione data del datawarehouse.
     */
    void sincronizzaDimensionedata();

    /**
     * Sincronizza gli attributi del DMS
     */
    void sincronizzaDMS();

    /**
     * @see DataWarehouseManager#aggiornaMovimenti(java.util.Date)
     * @param dataAggiornamento
     *            .
     */
    void sincronizzaMovimenti(java.util.Date dataAggiornamento);
}
