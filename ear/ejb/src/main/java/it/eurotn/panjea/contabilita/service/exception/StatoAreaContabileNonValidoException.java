/**
 * 
 */
package it.eurotn.panjea.contabilita.service.exception;

import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;

/**
 * Eccezione rilanciata in caso di variazione di {@link StatoAreaContabile} non contemplata dal relativo StateDiagram
 * 
 * @author adriano
 * @version 1.0, 04/ott/07
 * 
 */
public class StatoAreaContabileNonValidoException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -6965540270180480337L;

	/**
	 * @param message
	 * @param cause
	 */
	public StatoAreaContabileNonValidoException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param arg0
	 */
	public StatoAreaContabileNonValidoException(String message) {
		super(message);
	}

	/**
	 * @param arg0
	 */
	public StatoAreaContabileNonValidoException(Throwable cause) {
		super(cause);
	}

}
