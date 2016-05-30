package it.eurotn.panjea.manutenzioni.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.manager.operatori.ParametriRicercaOperatori;

@Remote
public interface OperatoriService {

    /**
     * Cancella un {@link Operatore}.
     *
     * @param id
     *            id Operatore da cancellare
     */
    void cancellaOperatore(Integer id);

    /**
     * Carica un {@link Operatore} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link Operatore} caricato
     */
    Operatore caricaOperatoreById(Integer id);

    /**
     * Carica tutti i {@link Operatore} presenti.
     *
     * @return {@link Operatore} caricati
     */
    List<Operatore> caricaOperatori();

    /**
     * Ricerca gli operatori in base ai parametri di ricerca.
     *
     * @param parametri
     *            parametri di ricerca
     * @return operatori caricati
     */
    List<Operatore> ricercaOperatori(ParametriRicercaOperatori parametri);

    /**
     * Salva un {@link Operatore}.
     *
     * @param operatore
     *            {@link Operatore} da salvare
     * @return {@link Operatore} salvato
     */
    Operatore salvaOperatore(Operatore operatore);

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
