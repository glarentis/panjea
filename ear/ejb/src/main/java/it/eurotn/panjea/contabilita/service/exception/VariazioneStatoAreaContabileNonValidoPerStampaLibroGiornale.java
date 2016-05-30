package it.eurotn.panjea.contabilita.service.exception;

/**
 * 
 * Eccezione rilanciata quando viene effettuato un cambio stato non valido relativo alla stampa libro giornale.
 * 
 * @author adriano
 * @version 1.0, 04/ott/07
 * 
 */
public class VariazioneStatoAreaContabileNonValidoPerStampaLibroGiornale extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 4463548764983117429L;

	/**
	 * 
	 * @param message
	 *            messaggio
	 */
	public VariazioneStatoAreaContabileNonValidoPerStampaLibroGiornale(final String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 *            messaggio
	 * @param cause
	 *            causa
	 */
	public VariazioneStatoAreaContabileNonValidoPerStampaLibroGiornale(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * 
	 * @param cause
	 *            causa
	 */
	public VariazioneStatoAreaContabileNonValidoPerStampaLibroGiornale(final Throwable cause) {
		super(cause);
	}

}
