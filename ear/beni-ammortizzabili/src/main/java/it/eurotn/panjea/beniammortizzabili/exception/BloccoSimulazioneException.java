/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.exception;

import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;

/**
 * Eccezione rilanciata quando è impossibile bloccare {@link Simulazione}.
 * 
 * @author adriano
 * @version 1.0, 19/ott/07
 * 
 */
public class BloccoSimulazioneException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 *            messaggio dell'eccezione.
	 */
	public BloccoSimulazioneException(final String message) {
		super(message);
	}

	/**
	 * @param message
	 *            messaggio dell'eccezione.
	 * @param cause
	 *            eccezione.
	 */
	public BloccoSimulazioneException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 *            eccezione.
	 */
	public BloccoSimulazioneException(final Throwable cause) {
		super(cause);
	}

}
