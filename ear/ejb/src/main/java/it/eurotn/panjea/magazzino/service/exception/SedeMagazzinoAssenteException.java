package it.eurotn.panjea.magazzino.service.exception;

/**
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class SedeMagazzinoAssenteException extends RuntimeException {

	private static final long serialVersionUID = 7927803995157758305L;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            message
	 */
	public SedeMagazzinoAssenteException(final String message) {
		super(message);
	}

}
