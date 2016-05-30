package it.eurotn.panjea.ordini.util.rigaordine.builder.domain;

import it.eurotn.panjea.ordini.domain.RigaOrdine;

import java.util.List;
import java.util.Map;

public class RigaOrdineBuilder {

	/**
	 * Trasforma una riga ordine generica nella riga di dominio corretta e la inserisce nei risultati.
	 * 
	 * @param rigaOrdineResult
	 *            risultati da elaborare
	 * @param result
	 *            risultati
	 * @param righeComposte
	 *            mappa contenente le righe composite
	 */
	public void fillResult(RigaOrdine rigaOrdineResult, List<RigaOrdine> result, Map<String, RigaOrdine> righeComposte) {
		result.add(rigaOrdineResult);
	};

}
