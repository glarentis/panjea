/**
 * 
 */
package it.eurotn.panjea.magazzino.service.exception;

/**
 * @author leonardo
 */
public class ConversioneUnitaMisuraAssenteException extends Exception {

	private static final long serialVersionUID = -4270109904060912969L;

	private String unitaMisuraOrigine = null;
	private String unitaMisuraDestinazione = null;

	/**
	 * Costruttore.
	 */
	public ConversioneUnitaMisuraAssenteException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            message
	 */
	public ConversioneUnitaMisuraAssenteException(final String message) {
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
	public ConversioneUnitaMisuraAssenteException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            cause
	 */
	public ConversioneUnitaMisuraAssenteException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @return the unitaMisuraDestinazione
	 */
	public String getUnitaMisuraDestinazione() {
		return unitaMisuraDestinazione;
	}

	/**
	 * @return the unitaMisuraOrigine
	 */
	public String getUnitaMisuraOrigine() {
		return unitaMisuraOrigine;
	}

	/**
	 * @param unitaMisuraDestinazione
	 *            the unitaMisuraDestinazione to set
	 */
	public void setUnitaMisuraDestinazione(String unitaMisuraDestinazione) {
		this.unitaMisuraDestinazione = unitaMisuraDestinazione;
	}

	/**
	 * @param unitaMisuraOrigine
	 *            the unitaMisuraOrigine to set
	 */
	public void setUnitaMisuraOrigine(String unitaMisuraOrigine) {
		this.unitaMisuraOrigine = unitaMisuraOrigine;
	}

}
