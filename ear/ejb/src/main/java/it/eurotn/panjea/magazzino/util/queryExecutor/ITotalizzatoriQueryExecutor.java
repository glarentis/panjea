package it.eurotn.panjea.magazzino.util.queryExecutor;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Local
public interface ITotalizzatoriQueryExecutor {

    /**
     * La query restituisce una List, i suoi elementi sono array di object di tre elementi: il primo e' la sommatoria
     * del totale riga in valuta, il secondo il totale in valuta azienda e il terzo il tipo articolo riferimento.
     * 
     * @return risultato della query
     */
    List<Object[]> execute();

    /**
     * @return the areaDocumento
     * @uml.property name="areaDocumento"
     * @uml.associationEnd
     */
    IAreaDocumento getAreaDocumento();

    /**
     * @param areaDocumento
     *            the areaDocumento to set
     * @uml.property name="areaDocumento"
     */
    void setAreaDocumento(IAreaDocumento areaDocumento);

    /**
     * @param queryString
     *            the queryString to set
     */
    void setQueryString(String queryString);
}
