package it.eurotn.panjea.sicurezza.service.exception;

/**
 * Eccezione generica generata dal <code>SicurezzaService</code>.
 * 
 * 
 * @author Aracno
 * @version 1.0, 5-apr-2006
 * 
 */
public class SicurezzaServiceException extends Exception {

	private static final long serialVersionUID = -2194591625038103367L;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public SicurezzaServiceException(final String message) {
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
	public SicurezzaServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public SicurezzaServiceException(final Throwable cause) {
		super(cause);
	}

}
