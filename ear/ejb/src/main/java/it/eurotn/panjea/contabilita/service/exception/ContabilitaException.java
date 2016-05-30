package it.eurotn.panjea.contabilita.service.exception;

public class ContabilitaException extends Exception {

	private static final long serialVersionUID = 5699742216187437439L;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio dell'eccezione
	 */
	public ContabilitaException(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio dell'eccezione
	 * @param cause
	 *            causa dell'eccezione
	 */
	public ContabilitaException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa dell'eccezione
	 */
	public ContabilitaException(final Throwable cause) {
		super(cause);
	}

}
