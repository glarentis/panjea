package it.eurotn.panjea.ordini.util.rigaordine.builder.dto;

import it.eurotn.panjea.ordini.util.RigaOrdineDTO;

import java.util.List;
import java.util.Map;

public interface RigaOrdineDTOBuilder {

	/**
	 * Trasforma una riga result contenente i dati di una riga generica di ordine nel dto corretto e la inserisce nei
	 * risultati.
	 * 
	 * @param rigaOrdineDTOResult
	 *            risultati da elaborare
	 * @param result
	 *            risultati
	 * @param righeComposte
	 *            mappa contenente le righe composite
	 */
	void fillResult(RigaOrdineDTOResult rigaOrdineDTOResult, List<RigaOrdineDTO> result,
			Map<String, RigaOrdineDTO> righeComposte);
}
