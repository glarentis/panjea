/**
 * 
 */
package it.eurotn.panjea.magazzino.service.exception;

/**
 * Eccezione sollevata quanto lo sconto risulta essere non valido.
 * 
 * @author fattazzo
 * 
 */
public class ScontoNotValidException extends Exception {

	private static final long serialVersionUID = -8382460040326570771L;

	public ScontoNotValidException(String message) {
		super(message);
	}
}
