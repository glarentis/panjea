package it.eurotn.panjea.magazzino.domain;

import it.eurotn.panjea.magazzino.domain.descrizionilingua.IDescrizioneLingua;

/**
 * @author fattazzo
 *
 */
public interface IDescrizioneEstesaFactory {

    /**
     * Restituisce l'istanza di una nuova descrizione lingua estesa.
     * 
     * @return IDescrizioneLingua
     */
    IDescrizioneLingua createDescrizioneLinguaEstesa();
}
