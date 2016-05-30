/**
 * 
 */
package it.eurotn.panjea.lotti.manager.filter;

import it.eurotn.panjea.lotti.domain.Lotto;

/**
 * @author fattazzo
 * 
 */
public class CodiceLottoStartingWithFilterPredicate implements CodiceLottoFilterPredicate {

	@Override
	public boolean apply(Lotto lotto, String codiceLotto) {
		return codiceLotto == null || lotto.getCodice().startsWith(codiceLotto);
	}

}
