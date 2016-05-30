package it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;

@Local
public interface LettureCassaGenerator {

    /**
     * Crea la movimentazione cassa in base alla lettura.
     *
     * @param lettura
     *            lettura
     */
    void creaMovimentazioneCassa(LetturaSelezionatrice lettura);
}
