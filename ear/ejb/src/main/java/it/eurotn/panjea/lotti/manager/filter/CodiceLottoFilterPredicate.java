/**
 * 
 */
package it.eurotn.panjea.lotti.manager.filter;

import it.eurotn.panjea.lotti.domain.Lotto;

/**
 * @author fattazzo
 * 
 */
public interface CodiceLottoFilterPredicate {

	/**
	 * 
	 * @param lotto
	 *            lotto da testare
	 * @param codiceLotto
	 *            codice lotto da verificare
	 * @return true se il lotto è compatibile con il codice lotto
	 */
	boolean apply(Lotto lotto, String codiceLotto);

}
