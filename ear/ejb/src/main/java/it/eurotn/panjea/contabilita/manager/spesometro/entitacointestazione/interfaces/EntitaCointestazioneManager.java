package it.eurotn.panjea.contabilita.manager.spesometro.entitacointestazione.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.contabilita.domain.EntitaCointestazione;
import it.eurotn.panjea.manager.interfaces.CrudManager;

@Local
public interface EntitaCointestazioneManager extends CrudManager<EntitaCointestazione> {

    /**
     * Cancella tutte le {@link EntitaCointestazione} dell'area contabile.
     *
     * @param idAreaContabile
     *            id area contabile
     */
    void cancellaByAreaContabile(Integer idAreaContabile);

    /**
     * Carica tutte le {@link EntitaCointestazione} dell'area contabile.
     *
     * @param idAreaContabile
     *            id area contabile
     * @return entita presenti
     */
    List<EntitaCointestazione> caricaByAreaContabile(Integer idAreaContabile);
}