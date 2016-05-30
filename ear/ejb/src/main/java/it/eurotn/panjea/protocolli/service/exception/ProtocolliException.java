/**
 * 
 */
package it.eurotn.panjea.protocolli.service.exception;

/**
 * Eccezione sollevata in caso di errore nella gestione protocolli.
 * 
 * @author adriano
 * @version 1.0, 07/mag/07
 */
public class ProtocolliException extends Exception {

	private static final long serialVersionUID = 9151957761547378L;

	/**
	 * @param message
	 *            il messaggio dell'exception
	 */
	public ProtocolliException(final String message) {
		super(message);
	}

	/**
	 * @param message
	 *            il messaggio dell'exception
	 * @param cause
	 *            la causa dell'exception
	 */
	public ProtocolliException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 *            la causa dell'exception
	 */
	public ProtocolliException(final Throwable cause) {
		super(cause);
	}

}
