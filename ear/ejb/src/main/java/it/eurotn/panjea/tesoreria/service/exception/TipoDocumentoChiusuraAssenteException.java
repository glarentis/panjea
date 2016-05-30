package it.eurotn.panjea.tesoreria.service.exception;

public class TipoDocumentoChiusuraAssenteException extends Exception {

	private static final long serialVersionUID = 8721187957867460804L;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio dell'eccezione
	 */
	public TipoDocumentoChiusuraAssenteException(final String message) {
		super(message);
	}

}
