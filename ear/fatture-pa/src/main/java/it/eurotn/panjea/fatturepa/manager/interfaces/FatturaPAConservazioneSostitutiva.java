package it.eurotn.panjea.fatturepa.manager.interfaces;

import javax.ejb.Local;

@Local
public interface FatturaPAConservazioneSostitutiva {

    /**
     * Esegue la conservazione sostitutiva per tutte le fattura PA che ancora non l'hanno eseguita.
     */
    void conservaXMLFatturePA();

}