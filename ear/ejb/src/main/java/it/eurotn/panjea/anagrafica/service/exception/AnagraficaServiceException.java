/**
 * 
 */
package it.eurotn.panjea.anagrafica.service.exception;

public class AnagraficaServiceException extends Exception {

	private static final long serialVersionUID = 3257846563014652727L;

	/**
	 * Costruttore.
	 */
	public AnagraficaServiceException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public AnagraficaServiceException(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            message
	 * @param cause
	 *            causa
	 */
	public AnagraficaServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public AnagraficaServiceException(final Throwable cause) {
		super(cause);
	}

}
