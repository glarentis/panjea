package it.eurotn.panjea.tesoreria.service.exception;

public class AccontoUtilizzatoException extends Exception {

	private static final long serialVersionUID = -8396784036935854601L;

	/**
	 * Costruttore.
	 */
	public AccontoUtilizzatoException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            il messaggio dell'exception
	 */
	public AccontoUtilizzatoException(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            il messaggio dell'exception
	 * @param cause
	 *            la causa dell'exception
	 */
	public AccontoUtilizzatoException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            la causa dell'exception
	 */
	public AccontoUtilizzatoException(final Throwable cause) {
		super(cause);
	}

}
