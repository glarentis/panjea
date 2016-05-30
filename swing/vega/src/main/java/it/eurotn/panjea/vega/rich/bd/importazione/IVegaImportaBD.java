package it.eurotn.panjea.vega.rich.bd.importazione;

import java.util.Date;

import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

public interface IVegaImportaBD {

    /**
     *
     * @param dataFine
     * @param tipiDocumento
     *            tipiDoc da importare
     * @param date
     *            data inizio importazione
     * @return stringa contenente eventuali errori di validazione dati. Empty se le fatture possono
     *         essere importate.
     */
    @AsyncMethodInvocation
    String importa(Date dataInizio, Date dataFine, String tipiDocumento);
}
