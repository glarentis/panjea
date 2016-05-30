package it.eurotn.panjea.magazzino.util.rigamagazzino.builder.dto;

import java.util.List;
import java.util.Map;

import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;

public interface RigaMagazzinoDTOBuilder {

    /**
     * Trasforma una riga result contenente i dati di una riga generica di magazzino nel dto corretto e la inserisce nei
     * risultati.
     * 
     * @param rigaMagazzinoDTOResult
     *            risultati da elaborare
     * @param result
     *            risultati
     * @param righeComposte
     *            mappa contenente le righe composite
     */
    void fillResult(RigaMagazzinoDTOResult rigaMagazzinoDTOResult, List<RigaMagazzinoDTO> result,
            Map<String, RigaMagazzinoDTO> righeComposte);
}
