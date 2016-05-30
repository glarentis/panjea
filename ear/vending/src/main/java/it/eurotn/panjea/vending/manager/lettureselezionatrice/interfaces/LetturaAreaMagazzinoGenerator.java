package it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;

@Local
public interface LetturaAreaMagazzinoGenerator {

    /**
     * Aggiorna l'area magazzino con i dati della lettura.
     *
     * @param areaRifornimento
     *            area rifornimento
     * @param lettura
     *            lettura selezionatrice
     * @return area aggiornata
     */
    AreaMagazzino aggiornaAreaMagazzino(AreaRifornimento areaRifornimento, LetturaSelezionatrice lettura);
}
