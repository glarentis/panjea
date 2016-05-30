/**
 * 
 */
package it.eurotn.panjea.pagamenti.service.exception;

/**
 * Exception per pagamenti.
 * 
 * @author adriano
 * @version 1.0, 19/dic/2008
 * 
 */
public class PagamentiException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore.
	 * 
	 */
	public PagamentiException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public PagamentiException(final String message) {
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
	public PagamentiException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public PagamentiException(final Throwable cause) {
		super(cause);
	}

}
