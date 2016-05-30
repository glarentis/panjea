/**
 * 
 */
package it.eurotn.panjea.contabilita.service.exception;

/**
 * Eccezione rilanciata in presenza di movimenti contabili non stampati a giornale
 * 
 * @author adriano
 * @version 1.0, 06/set/07
 * 
 */
public class DocumentiNonStampatiGiornaleException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3521379464786411539L;

	/**
	 * @param message
	 * @param cause
	 */
	public DocumentiNonStampatiGiornaleException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public DocumentiNonStampatiGiornaleException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DocumentiNonStampatiGiornaleException(Throwable cause) {
		super(cause);
	}

	
	
}
