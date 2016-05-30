package it.eurotn.panjea.preventivi.util.rigapreventivo.builder.dto;

import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;

import java.util.List;
import java.util.Map;

public interface RigaPreventivoDTOBuilder {

	/**
	 * Trasforma una riga result contenente i dati di una riga generica di preventivo nel dto corretto e la inserisce
	 * nei risultati.
	 * 
	 * @param rigaPreventivoDTOResult
	 *            risultati da elaborare
	 * @param result
	 *            risultati
	 * @param righeComposte
	 *            mappa contenente le righe composite
	 */
	void fillResult(RigaPreventivoDTOResult rigaPreventivoDTOResult, List<RigaPreventivoDTO> result,
			Map<String, RigaPreventivoDTO> righeComposte);
}
