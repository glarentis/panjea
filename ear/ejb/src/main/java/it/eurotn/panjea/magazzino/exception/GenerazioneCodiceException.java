package it.eurotn.panjea.magazzino.exception;

/**
 * Generata quando non ho dati per generare il codice dell'articolo. I casi possono essere. Maschera e Numeratore vuoto
 * Attributi non presenti o non avvalorati
 * 
 * @author giangi
 * 
 */
public class GenerazioneCodiceException extends Exception {

	private static final long serialVersionUID = -226658849714182185L;

	/**
	 * Costruttore.
	 * 
	 * @param string
	 *            messaggio
	 */
	public GenerazioneCodiceException(final String string) {
		super(string);
	}

}
