/**
 * 
 */
package it.eurotn.panjea.anagrafica.service.exception;

import it.eurotn.panjea.anagrafica.domain.CambioValuta;

/**
 * Eccezione rilanciata quando non viene trovato un oggetto {@link CambioValuta}
 * valido.
 * 
 * @author adriano
 * @version 1.0, 22/ott/2008
 * 
 */
public class CambioValutaNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore.
	 */
	public CambioValutaNotFoundException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public CambioValutaNotFoundException(final String message) {
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
	public CambioValutaNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public CambioValutaNotFoundException(final Throwable cause) {
		super(cause);
	}

}
