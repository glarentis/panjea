/**
 * 
 */
package it.eurotn.panjea.tesoreria.service.exception;

/**
 * L' Exception indica che l'entità per la lista di rate non è la stessa.
 * 
 * @author leonardo
 */
public class EntitaRateNonCoerentiException extends Exception {

	private static final long serialVersionUID = -2521634938075421827L;

	/**
	 * Costruttore di default.
	 * 
	 * @param message
	 *            il messaggio da visualizzare per l'exception
	 */
	public EntitaRateNonCoerentiException(final String message) {
		super(message);
	}

}
