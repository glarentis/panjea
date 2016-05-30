/**
 * 
 */
package it.eurotn.panjea.contabilita.service.exception;

import it.eurotn.panjea.anagrafica.domain.RapportoBancario;

/**
 * Avverte del fatto che non e' stato impostato un conto per il rapporto bancario corrente.
 * 
 * @author Leonardo
 */
public class ContoRapportoBancarioAssenteException extends Exception {

	private static final long serialVersionUID = -2597921626624123087L;
	private RapportoBancario rapportoBancario = null;

	/**
	 * Costruttore.
	 */
	public ContoRapportoBancarioAssenteException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param rapportoBancario
	 *            rapportoBancario
	 */
	public ContoRapportoBancarioAssenteException(final RapportoBancario rapportoBancario) {
		super();
		this.rapportoBancario = rapportoBancario;
	}

	/**
	 * @param message
	 *            message
	 */
	public ContoRapportoBancarioAssenteException(final String message) {
		super(message);
	}

	/**
	 * @param message
	 *            message
	 * @param cause
	 *            cause
	 */
	public ContoRapportoBancarioAssenteException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 *            cause
	 */
	public ContoRapportoBancarioAssenteException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @return the rapportoBancario
	 */
	public RapportoBancario getRapportoBancario() {
		return rapportoBancario;
	}

	/**
	 * @param rapportoBancario
	 *            the rapportoBancario to set
	 */
	public void setRapportoBancario(RapportoBancario rapportoBancario) {
		this.rapportoBancario = rapportoBancario;
	}

}
