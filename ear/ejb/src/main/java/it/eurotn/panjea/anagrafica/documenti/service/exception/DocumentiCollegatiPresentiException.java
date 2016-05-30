/**
 * 
 */
package it.eurotn.panjea.anagrafica.documenti.service.exception;

/**
 * Exception usata per segnalare la presenza di diversi documenti collegati tra loro
 * 
 * @author Leonardo
 */
public class DocumentiCollegatiPresentiException extends Exception {

	private static final long serialVersionUID = 3369535520560253520L;

	/**
	 * 
	 */
	public DocumentiCollegatiPresentiException() {
		super();
	}

	/**
	 * @param message
	 */
	public DocumentiCollegatiPresentiException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DocumentiCollegatiPresentiException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public DocumentiCollegatiPresentiException(Throwable cause) {
		super(cause);
	}

}
