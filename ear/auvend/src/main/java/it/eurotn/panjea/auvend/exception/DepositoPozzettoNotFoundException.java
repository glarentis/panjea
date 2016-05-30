/**
 * 
 */
package it.eurotn.panjea.auvend.exception;

/**
 * Exception sollevata se il Deposito pozzetto di AuVend non è presente in Panjea.
 * 
 * @author adriano
 * @version 1.0, 21/gen/2009
 * 
 */
public class DepositoPozzettoNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	private String codicePozzetto;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param codicePozzetto
	 *            codicepozzetto
	 */
	public DepositoPozzettoNotFoundException(final String message, final String codicePozzetto) {
		super(message);
		this.codicePozzetto = codicePozzetto;
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 * @param codicePozzetto
	 *            codice pozzetto
	 */
	public DepositoPozzettoNotFoundException(final String message, final String codicePozzetto, final Throwable cause) {
		super(message, cause);
		this.codicePozzetto = codicePozzetto;
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 * @param codicePozzetto
	 *            codice pozzetto
	 */
	public DepositoPozzettoNotFoundException(final Throwable cause, final String codicePozzetto) {
		super(cause);
		this.codicePozzetto = codicePozzetto;
	}

	/**
	 * @return Returns the codicePozzetto.
	 */
	public String getCodicePozzetto() {
		return codicePozzetto;
	}

}
