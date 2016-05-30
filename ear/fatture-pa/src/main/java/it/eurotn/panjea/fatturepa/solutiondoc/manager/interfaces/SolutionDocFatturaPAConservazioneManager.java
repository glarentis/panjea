package it.eurotn.panjea.fatturepa.solutiondoc.manager.interfaces;

import javax.ejb.Local;

@Local
public interface SolutionDocFatturaPAConservazioneManager {

    /**
     * Esegue la conservazione sostitutiva per tutte le fattura PA che ancora non l'hanno eseguita.
     */
    void conservaXMLFatturePA();

}
