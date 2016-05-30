/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.exception;

/**
 * 
 * 
 * @author Aracno
 * @version 1.0, 22/set/06
 * 
 */
public class BeniAmmortizzabiliException extends Exception {

	private static final long serialVersionUID = -940589034473868872L;

	/**
	 * Costruttore di default.
	 */
	public BeniAmmortizzabiliException() {
		super();
	}

	/**
	 * @param message
	 *            messaggio dell'eccezione.
	 */
	public BeniAmmortizzabiliException(final String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 *            messaggio dell'eccazione
	 * @param cause
	 *            eccazzione
	 */
	public BeniAmmortizzabiliException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param cause
	 *            eccezione
	 */
	public BeniAmmortizzabiliException(final Throwable cause) {
		super(cause);
	}

}
