package it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.interfaces;

import java.io.OutputStream;

public interface SqlGeneratorStream extends SqlGenerator {

    /**
     *
     * @param output
     *            output dove scrivere il ddl per l'aggiornamento
     * @param codiceOperatore
     *            codiceoperatore
     */
    void esporta(OutputStream output, String codiceOperatore);
}
