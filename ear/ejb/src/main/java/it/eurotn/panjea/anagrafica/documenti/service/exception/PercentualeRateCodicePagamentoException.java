/**
 * 
 */
package it.eurotn.panjea.anagrafica.documenti.service.exception;

/**
 * 
 * @author adriano
 * @version 1.0, 30/apr/07
 * 
 */
public class PercentualeRateCodicePagamentoException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 8442112789265242087L;

	/**
	 * @param message
	 * @param cause
	 */
	public PercentualeRateCodicePagamentoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public PercentualeRateCodicePagamentoException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public PercentualeRateCodicePagamentoException(Throwable cause) {
		super(cause);
	}

}
