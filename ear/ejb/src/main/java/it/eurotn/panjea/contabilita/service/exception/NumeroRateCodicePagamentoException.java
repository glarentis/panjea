/**
 * 
 */
package it.eurotn.panjea.contabilita.service.exception;

/**
 * 
 * @author adriano
 * @version 1.0, 30/apr/07
 * 
 */
public class NumeroRateCodicePagamentoException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 558382779055436361L;

	/**
	 * @param message
	 */
	public NumeroRateCodicePagamentoException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NumeroRateCodicePagamentoException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NumeroRateCodicePagamentoException(String message, Throwable cause) {
		super(message, cause);
	}

}
