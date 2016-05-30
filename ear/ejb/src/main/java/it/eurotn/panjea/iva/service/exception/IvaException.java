package it.eurotn.panjea.iva.service.exception;

/**
 * 
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class IvaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4267825217957868983L;

	/**
	 * Creates a new instance of <code>IvaException</code> without detail message.
	 */
	public IvaException() {
	}

	/**
	 * Constructs an instance of <code>IvaException</code> with the specified detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public IvaException(final String msg) {
		super(msg);
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param msg
	 *            messaggio
	 * @param cause
	 *            causa
	 */
	public IvaException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public IvaException(final Throwable cause) {
		super(cause);
	}
}
