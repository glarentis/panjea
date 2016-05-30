package it.eurotn.panjea.pagamenti.service.exception;

public class CodicePagamentoNonTrovatoException extends Exception {

	private static final long serialVersionUID = -5090981546683907606L;

	/**
	 * Costruttore.
	 */
	public CodicePagamentoNonTrovatoException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public CodicePagamentoNonTrovatoException(final String message) {
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
	public CodicePagamentoNonTrovatoException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public CodicePagamentoNonTrovatoException(final Throwable cause) {
		super(cause);
	}

}
