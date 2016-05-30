/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.eurotn.panjea.contabilita.service.exception;

/**
 * Eccezione usata per la creazione dei documenti dal calendario dei corrispettivi
 * quando viene lanciata la creazione dei documenti dal calendario devo avere una struttura contabile
 * altrimenti le righe contabili non possono essere create.
 * @author Leonardo
 */
public class StrutturaContabileAssenteException extends Exception {

        /**
	 * 
	 */
	private static final long serialVersionUID = -2820801222021733839L;

		/**
	 * @param message
	 */
	public StrutturaContabileAssenteException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public StrutturaContabileAssenteException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public StrutturaContabileAssenteException(String message, Throwable cause) {
		super(message, cause);
	}
}
