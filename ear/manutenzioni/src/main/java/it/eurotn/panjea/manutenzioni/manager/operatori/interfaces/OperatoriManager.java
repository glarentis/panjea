package it.eurotn.panjea.manutenzioni.manager.operatori.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.manager.operatori.ParametriRicercaOperatori;

@Local
public interface OperatoriManager extends CrudManager<Operatore> {

    /**
     *
     * @param codiceOperatore
     *            codice dell'operatore
     * @return operatore con il codice richiest. NULL se non lo trovo
     */
    Operatore caricaByCodice(String codiceOperatore);

    /**
     * Ricerca gli operatori in base ai parametri di ricerca.
     *
     * @param parametri
     *            parametri di ricerca
     * @return operatori caricati
     */
    List<Operatore> ricercaOperatori(ParametriRicercaOperatori parametri);

    /**
     * Sostuisce l'operatore.<br>
     * Al nuovo operatore vengono assegnate le installazioni di quello vecchio.<br>
     * Se il nuovo operatore non ha un mezzo di trasporto verrà assegnato quello dell'operatore da sostituire e tolto da
     * quest'ultimo.
     *
     * @param idOperatoreDaSostituire
     *            operatore da sostituire
     * @param idOperatore
     *            operatore di destinazione
     * @param sostituisciTecnico
     *            sostituisce solo l'operatore come tecnico
     * @param sostituisciCaricatore
     *            sostituisce solo l'operatore come caricatore
     */
    void sostituisciOperatore(Integer idOperatoreDaSostituire, Integer idOperatore, boolean sostituisciTecnico,
            boolean sostituisciCaricatore);
}