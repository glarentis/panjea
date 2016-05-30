/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.exception;

/**
 * @author Leonardo
 * 
 */
public class VenditaInAnnoConsolidatoException extends Exception {

	private static final long serialVersionUID = -5289630998058930207L;

	/**
	 * 
	 */
	public VenditaInAnnoConsolidatoException() {
		super();
	}

	/**
	 * 
	 * @param message
	 *            messaggio dell'eccezione
	 */
	public VenditaInAnnoConsolidatoException(final String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 *            messaggio dell'eccezione
	 * @param cause
	 *            eccezione
	 */
	public VenditaInAnnoConsolidatoException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param cause
	 *            eccezione
	 */
	public VenditaInAnnoConsolidatoException(final Throwable cause) {
		super(cause);
	}

}
