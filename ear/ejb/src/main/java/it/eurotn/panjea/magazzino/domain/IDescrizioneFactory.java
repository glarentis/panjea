package it.eurotn.panjea.magazzino.domain;

import it.eurotn.panjea.magazzino.domain.descrizionilingua.IDescrizioneLingua;

/**
 * @author fattazzo
 *
 */
public interface IDescrizioneFactory {

    /**
     * Restituisce l'istanza di una nuova descrizione lingua.
     * 
     * @return IDescrizioneLingua
     */
    IDescrizioneLingua createDescrizioneLingua();
}
