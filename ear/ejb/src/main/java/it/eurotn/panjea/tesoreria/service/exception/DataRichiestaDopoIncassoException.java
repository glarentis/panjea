/**
 * 
 */
package it.eurotn.panjea.tesoreria.service.exception;

/**
 * @author leonardo
 * 
 */
public class DataRichiestaDopoIncassoException extends Exception {

	private static final long serialVersionUID = 1637360285681675923L;

	/**
	 * Costruttore.
	 */
	public DataRichiestaDopoIncassoException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            message
	 */
	public DataRichiestaDopoIncassoException(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            message
	 * @param cause
	 *            cause
	 */
	public DataRichiestaDopoIncassoException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            cause
	 */
	public DataRichiestaDopoIncassoException(final Throwable cause) {
		super(cause);
	}

}
