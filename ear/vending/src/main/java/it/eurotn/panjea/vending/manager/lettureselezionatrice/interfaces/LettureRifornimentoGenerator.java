package it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;

@Local
public interface LettureRifornimentoGenerator {

    /**
     * Associa il rifornimento alla lettura della selezionatrice.
     *
     * @param letturaSelezionatrice
     *            lettura
     */
    void associaRifornimento(LetturaSelezionatrice letturaSelezionatrice);

    /**
     * Crea il rifornimento per la lettura della selezionatrice.
     *
     * @param letturaSelezionatrice
     *            lettura
     */
    void creaRifornimento(LetturaSelezionatrice letturaSelezionatrice);
}
