/**
 *
 */
package it.eurotn.panjea.lotti.manager.filter;

import it.eurotn.panjea.lotti.domain.Lotto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fattazzo
 * 
 */
public class CodiceLottoFilterExecuter {

	/**
	 * Filtra la lista dei lotti in base al codice lotto passato.
	 * 
	 * @param lotti
	 *            lotti da filtrare
	 * @param codice
	 *            codice
	 * @return lista di lotti filtrata
	 */
	public List<Lotto> filter(List<Lotto> lotti, String codice) {

		List<Lotto> lottiResult = new ArrayList<Lotto>();

		CodiceLottoFilterPredicate filterPredicate = getFilterPredicate(codice);

		// rimuovo i percentuali
		if (codice != null) {
			codice = codice.replaceAll("%", "");
		}

		for (Lotto lotto : lotti) {
			if (filterPredicate.apply(lotto, codice)) {
				lottiResult.add(lotto);
			}
		}

		return lottiResult;
	}

	/**
	 * Restituisce il filtro corretto da utilizzare in base al codice da cercare.
	 * 
	 * @param codice
	 *            codice da cercare
	 * @return predicate
	 */
	private CodiceLottoFilterPredicate getFilterPredicate(String codice) {

		CodiceLottoFilterPredicate filterPredicate = new CodiceLottoStartingWithFilterPredicate();

		if (codice != null && codice.startsWith("%")) {
			filterPredicate = new CodiceLottoContainsFilterPredicate();
		}

		return filterPredicate;
	}

}
