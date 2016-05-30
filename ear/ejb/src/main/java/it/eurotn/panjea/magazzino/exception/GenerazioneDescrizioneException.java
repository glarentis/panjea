package it.eurotn.panjea.magazzino.exception;

/**
 * Errore generato quando mancano i dati per generare la descrizione.
 * 
 * @author giangi
 */
public class GenerazioneDescrizioneException extends Exception {

	private static final long serialVersionUID = -8827599596323216138L;

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio
	 */
	public GenerazioneDescrizioneException(final String message) {
		super(message);
	}

}
