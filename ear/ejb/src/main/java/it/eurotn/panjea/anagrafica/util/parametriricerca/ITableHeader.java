package it.eurotn.panjea.anagrafica.util.parametriricerca;

import java.util.List;

/**
 * 
 * Utilizzato da JideTableWidget per inserire in stampa un header sopra la tabella. <br/>
 * Utlizzare il metodo setTableHeader di JideTableWidget.
 * 
 * @author giangi
 * @version 1.0, 19/ago/2010
 * 
 */
public interface ITableHeader {

	/**
	 * Restituisce una lista di valori per l'header della tabella.
	 * 
	 * @return lista di valori
	 */
	List<TableHeaderObject> getHeaderValues();
}
