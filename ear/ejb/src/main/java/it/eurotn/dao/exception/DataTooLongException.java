package it.eurotn.dao.exception;

public class DataTooLongException extends DAOException {

	private static final long serialVersionUID = 3396332677506858059L;
	private String column;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param message
	 *            dell'eccezione
	 */
	public DataTooLongException(final String message) {
		super(message);
		String[] messageTmp = message.split("'");
		column = messageTmp[1];
	}

	/**
	 * 
	 * @return colonna che eccede la lunghezza massima.
	 */
	public String getColumn() {
		return column;
	}

}
