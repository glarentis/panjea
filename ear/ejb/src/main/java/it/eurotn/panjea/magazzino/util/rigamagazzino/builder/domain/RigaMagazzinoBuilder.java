package it.eurotn.panjea.magazzino.util.rigamagazzino.builder.domain;

import java.util.List;
import java.util.Map;

import it.eurotn.panjea.magazzino.domain.RigaMagazzino;

public class RigaMagazzinoBuilder {

    /**
     * Trasforma una riga magazzino generica nella riga di dominio corretta e la inserisce nei risultati.
     * 
     * @param rigaMagazzinoResult
     *            risultati da elaborare
     * @param result
     *            risultati
     * @param righeComposte
     *            mappa contenente le righe composite
     */
    public void fillResult(RigaMagazzino rigaMagazzinoResult, List<RigaMagazzino> result,
            Map<String, RigaMagazzino> righeComposte) {
        result.add(rigaMagazzinoResult);
    }
}
