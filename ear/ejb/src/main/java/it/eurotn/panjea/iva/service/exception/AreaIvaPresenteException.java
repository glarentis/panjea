/**
 * 
 */
package it.eurotn.panjea.iva.service.exception;

/**
 * Eccezione sollevata quando in un documento e' presente un'area iva ma il suo tipo documento non la prevede.
 * 
 * @author fattazzo
 * 
 */
public class AreaIvaPresenteException extends RuntimeException {

	private static final long serialVersionUID = 4714608722717272462L;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public AreaIvaPresenteException(final String message) {
		super(message);
	}
}
