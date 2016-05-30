package it.eurotn.panjea.magazzino.manager.interfaces.articolo;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.domain.Articolo;

@Local
public interface DistintaBaseCopiaManager {

    /**
     * Copia una distinta con configurazione base da un articolo ad un altro. Se l'articolo di
     * destinazione ha già una conf. base i componenti vengono aggiunti.
     *
     * @param distintaOrigine
     *            .
     * @param distintaDestinazione
     *            .
     */
    void copiaDistinta(Articolo distintaOrigine, Articolo distintaDestinazione, boolean copiaConfigurazioni);

}
