package it.eurotn.panjea.fatturepa.service.interfaces;

import javax.ejb.Remote;

@Remote
public interface ConservazioneSostitutivaService {

    /**
     * Esegue la conservazione sostitutiva per tutte le fattura PA che ancora non l'hanno eseguita.
     */
    void conservaXMLFatturePA();
}
