package it.eurotn.panjea.magazzino.manager.interfaces;

import javax.ejb.Local;

@Local
public interface DataWarehouseManager {

    /**
     * Cancella tutti i movimenti del dw che hanno il corrispondente movimento con uuid specificato.
     *
     * @param uuid
     *            uuid
     */
    void cancellaMovimenti(String uuid);

    /**
     * Sincronizza tutte le anagrafiche ( articoli, clienti, depositi ).
     */
    void sincronizzaAnagrafiche();

    /**
     * Sincronizza la dimensione data del datawarehouse con un range di 5 anni dalla data attuale.
     */
    void sincronizzaDimensionedata();

    /**
     * Sincronizza gli attributi del DMS
     */
    void sincronizzaDMS();

    /**
     * Sincronizza i movimenti di magazzino con il datawarehouse partendo da una determinata data.
     *
     * @param dataIniziale
     *            Sincronizza i movimenti con data di registrazione >= alla dataIniziale richiesta
     */
    void sincronizzaMovimenti(java.util.Date dataIniziale);

}
