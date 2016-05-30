package it.eurotn.panjea.iva.util;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;

import java.util.List;

import javax.ejb.Local;

@Local
public interface IImponibiliIvaQueryExecutor {

	/**
	 * La query restituisce una List, i suoi elementi sono array di object di due elementi: il primo e' la sommatoria
	 * del totale riga raggruppato per codice iva ed e' di tipo BigDecimal, il secondo e' il codiceIva a cui fa
	 * riferimento.
	 * 
	 * @return risultato della query
	 */
	List<Object[]> execute();

	/**
	 * @param areaDocumento
	 *            the areaDocumento to set
	 */
	void setAreaDocumento(IAreaDocumento areaDocumento);

	/**
	 * @param queryString
	 *            the queryString to set
	 */
	void setQueryString(String queryString);
}
