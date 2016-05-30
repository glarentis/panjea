package it.eurotn.panjea.spedizioni.exception;

public class SpedizioniVettoreException extends Exception {

	private static final long serialVersionUID = -2805573599958490508L;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio dell'eccezione
	 */
	public SpedizioniVettoreException(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio dell'eccezione
	 * @param cause
	 *            causa
	 */
	public SpedizioniVettoreException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
