/**
 * 
 */
package it.eurotn.panjea.contabilita.service.exception;

/**
 * Eccezzione rilanciata a causa di una non corretta validazione o
 * interpretazione di una formula
 * 
 * @author Fattazzo
 *
 */
public class FormulaException extends Exception {

	private static final long serialVersionUID = 1L;

	public FormulaException(String message) {
		super(message);
	}

}
