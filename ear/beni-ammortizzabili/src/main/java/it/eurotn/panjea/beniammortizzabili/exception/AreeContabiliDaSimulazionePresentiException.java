/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.exception;

/**
 * @author fattazzo
 *
 */
public class AreeContabiliDaSimulazionePresentiException extends Exception {

	private static final long serialVersionUID = -4533774426730585647L;

	private Integer annoMovimento;

	/**
	 * Costruttore.
	 *
	 * @param annoMovimento
	 *            anno movimento
	 */
	public AreeContabiliDaSimulazionePresentiException(final Integer annoMovimento) {
		super();
		this.annoMovimento = annoMovimento;
	}

	/**
	 * @return the annoMovimento
	 */
	public Integer getAnnoMovimento() {
		return annoMovimento;
	}
}
