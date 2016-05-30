/**
 * 
 */
package it.eurotn.panjea.anagrafica.service.exception;

/**
 * Avverte del fatto che non e' stato impostato un rapporto bancario di
 * regolamentazione per il rapporto bancario corrente.
 * 
 * @author Leonardo
 */
public class RapportoBancarioRegolamentazioneAssenteException extends Exception {

	private static final long serialVersionUID = -7363229128193066372L;

	/**
	 * 
	 */
	public RapportoBancarioRegolamentazioneAssenteException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public RapportoBancarioRegolamentazioneAssenteException(final String message) {
		super(message);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 */
	public RapportoBancarioRegolamentazioneAssenteException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Costruttore.
	 * 
	 * @param cause
	 *            causa
	 */
	public RapportoBancarioRegolamentazioneAssenteException(final Throwable cause) {
		super(cause);
	}

}
