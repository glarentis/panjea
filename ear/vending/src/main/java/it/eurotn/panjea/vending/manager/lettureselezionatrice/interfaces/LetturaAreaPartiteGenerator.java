package it.eurotn.panjea.vending.manager.lettureselezionatrice.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.partite.domain.AreaPartite;

@Local
public interface LetturaAreaPartiteGenerator {

    /**
     * Area l'area partite dall'area magazzino.
     *
     * @param areaMagazzino
     *            area magazzino
     * @return area partite
     */
    AreaPartite creaAreaPartite(AreaMagazzino areaMagazzino);
}
