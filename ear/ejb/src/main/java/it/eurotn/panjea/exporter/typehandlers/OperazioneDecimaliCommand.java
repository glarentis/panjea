package it.eurotn.panjea.exporter.typehandlers;

import java.math.BigDecimal;

/**
 * Esegue l'operazione richiesta.
 * 
 * @author giangi
 * @version 1.0, 14/ott/2011
 * 
 */
public abstract class OperazioneDecimaliCommand<E> {
	/**
	 * 
	 * @param value
	 *            valore da trasformare
	 * @param numDecimali
	 *            numero decimali da utilizzare
	 * @return valore con l'operazione sui decimali effettuata
	 */
	public abstract BigDecimal execute(E value, int numDecimali);
}