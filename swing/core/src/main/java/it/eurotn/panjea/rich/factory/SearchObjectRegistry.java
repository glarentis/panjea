/**
 * 
 */
package it.eurotn.panjea.rich.factory;

import it.eurotn.rich.search.AbstractSearchObject;

/**
 * Interface per recuperare <code>AbstractSearchObject</code> data in input la proprieta' su cui effettuare la ricerca.
 * 
 * @author adriano
 * @version 1.0, 19/ott/06
 * 
 */
public interface SearchObjectRegistry {

    /**
     * 
     * @param propertySearch
     *            classe della proprietà per la quale creare il searchObject
     * @return Ritorna un istanza di {@link AbstractSearchObject} legata alla proprietà interessata.
     */
    AbstractSearchObject getSearchObject(Class<?> propertySearch);

    /**
     * Posso legare al form una searchObject per una proprietà stringa.<br/>
     * Ad esempio si veda in DatiAccessoriForm la search areaMagazzino.tipoPorto. Se la proprietà è di tipo stringa la
     * chiave mappata in panjea-search-context.xml è il path
     * 
     * @param formPropertyPath
     *            formPropertyPath del formModel per il quale creare il searchObject
     * @return Ritorna un istanza di {@link AbstractSearchObject} legata alla proprietà interessata.
     */
    AbstractSearchObject getSearchObject(String formPropertyPath);

}
