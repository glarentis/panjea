package it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.interfaces;

import javax.ejb.Local;

@Local
public interface SqlGenerator {

    /**
     *
     * @param output
     *            output dove scrivere il ddl per l'aggiornamento
     * @param codiceOperatore
     *            codiceoperatore
     */
    String esporta(String codiceOperatore);
}
