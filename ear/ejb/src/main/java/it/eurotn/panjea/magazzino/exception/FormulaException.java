package it.eurotn.panjea.magazzino.exception;

public class FormulaException extends RuntimeException {
	private static final long serialVersionUID = -6180586754952772086L;

	protected String formula;

	/**
	 *
	 * @param formula
	 *            formula che ha generato l'errore
	 * @param message
	 *            messaggio di errore
	 */
	public FormulaException(final String formula, final String message) {
		super(message);
		this.formula = formula;
	}

	/**
	 * @return Returns the formula.
	 */
	public String getFormula() {
		return formula;
	}

}
