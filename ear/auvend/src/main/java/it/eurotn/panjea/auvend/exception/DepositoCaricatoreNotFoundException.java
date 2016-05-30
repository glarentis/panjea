/**
 * 
 */
package it.eurotn.panjea.auvend.exception;

/**
 * Eccezione da sollevare per gestire l'assenza di un deposito caricatore di AuVend.
 * 
 * @author adriano
 * @version 1.0, 23/dic/2008
 * 
 */
public class DepositoCaricatoreNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	private String codiceDeposito;

	/**
	 * Costruttore.
	 * 
	 * @param codiceDeposito
	 *            codice deposito
	 */
	public DepositoCaricatoreNotFoundException(final String codiceDeposito) {
		super();
		this.codiceDeposito = codiceDeposito;
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param codiceDeposito
	 *            codice deposito
	 */
	public DepositoCaricatoreNotFoundException(final String message, final String codiceDeposito) {
		super(message);
		this.codiceDeposito = codiceDeposito;
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 * @param codiceDeposito
	 *            codice deposito
	 */
	public DepositoCaricatoreNotFoundException(final String message, final String codiceDeposito, final Throwable cause) {
		super(message, cause);
		this.codiceDeposito = codiceDeposito;
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 * @param codiceDeposito
	 *            codice deposito
	 */
	public DepositoCaricatoreNotFoundException(final String codiceDeposito, final Throwable cause) {
		super(cause);
		this.codiceDeposito = codiceDeposito;
	}

	/**
	 * @return Returns the codiceDeposito.
	 */
	public String getCodiceDeposito() {
		return codiceDeposito;
	}

}
