package it.eurotn.panjea.pagamenti.service.exception;

public class CodicePagamentoEsistenteException extends Exception {

	private static final long serialVersionUID = -3323899096668651388L;

	/**
	 * Costruttore.
	 * 
	 */
	public CodicePagamentoEsistenteException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public CodicePagamentoEsistenteException(final String message) {
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
	public CodicePagamentoEsistenteException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public CodicePagamentoEsistenteException(final Throwable cause) {
		super(cause);
	}

}
