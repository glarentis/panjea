/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.exception;

/**
 * @author Leonardo
 * 
 */
public class VenditaInAnnoSimulatoException extends Exception {

	private static final long serialVersionUID = 5370417230193438835L;

	/**
	 * 
	 */
	public VenditaInAnnoSimulatoException() {
		super();
	}

	/**
	 * 
	 * @param message
	 *            messaggio dell'eccezione
	 */
	public VenditaInAnnoSimulatoException(final String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 *            messaggio dell'eccezione
	 * @param cause
	 *            eccezione
	 */
	public VenditaInAnnoSimulatoException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param cause
	 *            eccezione
	 */
	public VenditaInAnnoSimulatoException(final Throwable cause) {
		super(cause);
	}

}
