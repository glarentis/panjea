/**
 * 
 */
package it.eurotn.panjea.anagrafica.service.exception;

import it.eurotn.panjea.anagrafica.domain.CambioValuta;

/**
 * Eccezione da rilanciare quando l'oggetto {@link CambioValuta} non è valido <br>
 * es: la data di {@link CambioValuta} è troppo anteriore alla data corrente.
 * 
 * @author adriano
 * @version 1.0, 22/ott/2008
 * 
 */
public class CambioValutaNotValidException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore.
	 */
	public CambioValutaNotValidException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public CambioValutaNotValidException(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 */
	public CambioValutaNotValidException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public CambioValutaNotValidException(final Throwable cause) {
		super(cause);
	}

}
