package it.eurotn.panjea.vending.manager.arearifornimento.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;

@Local
public interface AreaRifornimentoAggiornaManager {

    /**
     * Aggiorna l'area di rifornimento con i dati dell'installazione.
     *
     * @param areaRifornimento
     *            area rifornimento
     * @param installazione
     *            installazione
     * @return area rifornimento aggiornata
     */
    AreaRifornimento aggiornaDatiInstallazione(AreaRifornimento areaRifornimento, Installazione installazione);

    /**
     * Aggiorna l'area di rifornimento con i dati del distributore.
     *
     * @param areaRifornimento
     *            area rifornimento
     * @param idDistributore
     *            id distributore
     * @return area rifornimento aggiornata
     */
    AreaRifornimento aggiornaDistributore(AreaRifornimento areaRifornimento, Integer idDistributore);

    /**
     * Aggiorna l'area di rifornimento con i dati dell'entita.
     *
     * @param areaRifornimento
     *            area rifornimento
     * @param idEntita
     *            id entita
     * @return area rifornimento aggiornata
     */
    AreaRifornimento aggiornaEntita(AreaRifornimento areaRifornimento, Integer idEntita);

    /**
     * Aggiorna l'area di rifornimento con i dati della sede entita.
     *
     * @param areaRifornimento
     *            area rifornimento
     * @param sedeEntita
     *            sede entita
     * @return area rifornimento aggiornata
     */
    AreaRifornimento aggiornaSedeEntita(AreaRifornimento areaRifornimento, SedeEntita sedeEntita);
}
