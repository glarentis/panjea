/**
 * 
 */
package it.eurotn.panjea.contabilita.manager.interfaces;

import it.eurotn.panjea.contabilita.service.exception.FormulaException;

import java.math.BigDecimal;
import java.util.Map;

import javax.ejb.Local;

/**
 * Manager per la gestione delle formule.
 * 
 * @author fattazzo
 * @version 1.0, 07/set/07
 */
@Local
public interface FormuleManager {

	/**
	 * 
	 * @param formula
	 *            formula
	 * @param map
	 *            map
	 * @param nDecimali
	 *            numero decimali
	 * @return BigDecimal
	 * @throws FormulaException
	 *             se non trova la formula
	 */
	BigDecimal calcola(String formula, Map<String, BigDecimal> map, int nDecimali) throws FormulaException;
}
