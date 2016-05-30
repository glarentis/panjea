package it.eurotn.panjea.rich.bd;

import java.util.Map;

import it.eurotn.entity.EntityBase;

/**
 * @author fattazzo
 *
 */
public interface ICodiceBD {

    /**
     * Restituisce le variabili valorizzate in base all'entity di riferimento per la generazione del codice.
     * 
     * @param entity
     *            entity
     * @return variabili
     */
    Map<String, String> creaVariabiliCodice(EntityBase entity);

    /**
     * Genera il codice in base al pattern specificato e usando i valori delle variabili contenute nella mappa.
     * 
     * @param pattern
     *            pattern di generazione
     * @param mapVariabili
     *            mappa delle variabili
     * @return codice generato
     */
    String generaCodice(String pattern, Map<String, String> mapVariabili);

}
