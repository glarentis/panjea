package it.eurotn.panjea.magazzino.service.exception;

/**
 * Eccezione generica per il magazzino.
 * 
 * @author giangi
 * 
 */
public class MagazzinoException extends RuntimeException {

	private static final long serialVersionUID = 8035985455143940612L;

	/**
	 * Costruttore.
	 * 
	 * @param e
	 *            eccezione che ha generato l'eccezione
	 */
	public MagazzinoException(final Exception e) {
		super(e);
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio dell'eccezione
	 * @param e
	 *            eccezione che ha generato l'eccezione
	 */
	public MagazzinoException(final String message, final Exception e) {
		super(message, e);
	}

}
