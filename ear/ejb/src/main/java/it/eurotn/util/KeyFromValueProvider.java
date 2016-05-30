package it.eurotn.util;

/**
 * Utilizzata per recuperare la chiave di un oggetto per usare nel metodo {@link PanjeaEJBUtil#listToMap}.
 * 
 * @author giangi
 * @version 1.0, 11/mar/2011
 * 
 */
public interface KeyFromValueProvider<T, S> {
	/**
	 * 
	 * @param elem
	 *            oggetto dal quale recuperare la chiave
	 * @return chiave dell'oggetto
	 */
	S keyFromValue(T elem);
}