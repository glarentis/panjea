package it.eurotn.dao.exception;

/**
 * Eccezione generata la layer DAO.
 * 
 * @author LErik
 * @version 1.0, 22-lug-2005
 * 
 */
public class DAOException extends Exception {

	private static final long serialVersionUID = -302533773004301982L;

	/**
	 * 
	 * Costruttore.
	 */
	public DAOException() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param message
	 *            messaggio per l'eccezione
	 */
	public DAOException(final String message) {
		super(message);
	}

	/**
	 * @param ex
	 *            eccezione wrappata
	 */
	public DAOException(final Throwable ex) {
		super(ex);
	}

}
