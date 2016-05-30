package it.eurotn.panjea.vending.manager.evadts.rilevazioni.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.manager.evadts.rilevazioni.ParametriRicercaRilevazioniEvaDts;

@Local
public interface RilevazioniEvaDtsManager extends CrudManager<RilevazioneEvaDts> {

    /**
     * Esegue la ricerca delle rilevazioni Eva-DTS in base ai parametri di ricerca.
     *
     * @param parametri
     *            parametri di ricerca
     * @return rilevazioni trovate
     */
    List<RilevazioneEvaDts> ricercaRilevazioniEvaDts(ParametriRicercaRilevazioniEvaDts parametri);
}