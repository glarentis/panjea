/**
 * 
 */
package it.eurotn.panjea.contabilita.service.exception;

/**
 * Eccezione rilanciata se un movimento contabile di chiusura è assente.
 * 
 * @author adriano
 * @version 1.0, 05/set/07
 * 
 */
public class ChiusuraAssenteException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -6931492318321993469L;

	/**
	 * 
	 */
	public ChiusuraAssenteException() {
	}

	/**
	 * @param message
	 */
	public ChiusuraAssenteException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ChiusuraAssenteException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public ChiusuraAssenteException(Throwable cause) {
		super(cause);
	}

}
