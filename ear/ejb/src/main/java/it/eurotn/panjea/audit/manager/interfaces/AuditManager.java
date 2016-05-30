package it.eurotn.panjea.audit.manager.interfaces;

import java.util.Date;

import javax.ejb.Local;

import it.eurotn.panjea.audit.envers.RevInf;

/**
 * @author fattazzo
 *
 */
@Local
public interface AuditManager {

    /**
     * Cancella tutti i dati di audit e revinf precedenti alla data indicata.
     * 
     * @param data
     *            di riferimento
     */
    void cancellaAuditPrecedente(Date data);

    /**
     * Restituisce il numero di tutte le {@link RevInf}presenti.
     * 
     * @return numero di {@link RevInf} presenti
     */
    Integer caricaNumeroRevInf();
}
