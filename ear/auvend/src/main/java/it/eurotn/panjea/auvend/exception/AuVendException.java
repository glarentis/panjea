/**
 * 
 */
package it.eurotn.panjea.auvend.exception;

/**
 * Exception generica per il plugin di AuVend.
 * 
 * @author adriano
 * @version 1.0, 29/dic/2008
 * 
 */
public class AuVendException extends Exception {

	private static final long serialVersionUID = -1655296765786728302L;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public AuVendException(final String message) {
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
	public AuVendException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public AuVendException(final Throwable cause) {
		super(cause);
	}

}
