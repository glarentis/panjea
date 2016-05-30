/**
 * 
 */
package it.eurotn.panjea.auvend.exception;

/**
 * Eccezione per la gestione dell'assenza di un cliente di AuVend in Panjea.
 * 
 * @author adriano
 * @version 1.0, 02/feb/2009
 * 
 */
public class ClienteAuVendNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	private String codiceCliente;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param codiceCliente
	 *            codice cliente
	 */
	public ClienteAuVendNotFoundException(final String message, final String codiceCliente) {
		super(message);
		this.codiceCliente = codiceCliente;
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 * @param codiceCliente
	 *            codice cliente
	 */
	public ClienteAuVendNotFoundException(final String message, final Throwable cause, final String codiceCliente) {
		super(message, cause);
		this.codiceCliente = codiceCliente;
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 * @param codiceCliente
	 *            codice cliente
	 */
	public ClienteAuVendNotFoundException(final Throwable cause, final String codiceCliente) {
		super(cause);
		this.codiceCliente = codiceCliente;
	}

	/**
	 * @return Returns the codiceCliente.
	 */
	public String getCodiceCliente() {
		return codiceCliente;
	}

}
