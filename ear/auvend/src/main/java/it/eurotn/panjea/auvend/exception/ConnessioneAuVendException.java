/**
 * 
 */
package it.eurotn.panjea.auvend.exception;

/**
 * Eccezione per l'errore nella connessione e nel recupero dei dati da AuVend.
 * 
 * @author adriano
 * @version 1.0, 19/gen/2009
 * 
 */
public class ConnessioneAuVendException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public ConnessioneAuVendException(final String message) {
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
	public ConnessioneAuVendException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public ConnessioneAuVendException(final Throwable cause) {
		super(cause);
	}

}
