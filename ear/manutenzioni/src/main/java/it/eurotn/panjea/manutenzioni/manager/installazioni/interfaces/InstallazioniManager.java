package it.eurotn.panjea.manutenzioni.manager.installazioni.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.manager.installazioni.ParametriRicercaInstallazioni;

@Local
public interface InstallazioniManager extends CrudManager<Installazione> {
    /**
     * Aggiorna l'articolo installato in base alle {@link RigaInstallazione}
     *
     * @param idInstallazione
     *            per la quale aggiornare l'articolo installato
     */
    void aggiornaArticoloInstallato(int idInstallazione);

    /**
     *
     * @param idArticolo
     *            idArticolo
     * @return carica l'installazione che ha l'articolo passato come parametro. Null se non ho
     *         nessuna installazioen con quell'articolo.
     */
    Installazione caricaByArticolo(Integer idArticolo);

    /**
     *
     * @param codice
     *            codice installazione
     * @return installazione o null se non trovata
     */
    Installazione caricaByCodice(String codice);

    /**
     *
     * @param idEntita
     *            idEntita delle installazioni
     * @return lista delle installazioni associate all'entità
     */
    List<Installazione> ricercaByEntita(Integer idEntita);

    /**
     *
     * @param parametri
     *            parametri ricerca
     * @return lista installazioni
     */
    List<Installazione> ricercaByParametri(ParametriRicercaInstallazioni parametri);

    /**
     *
     * @param idSedeEntita
     *            sedeEntita delle installazioni
     * @return lista delle installazioni associate alla sedeEntita
     */
    List<Installazione> ricercaBySede(Integer idSedeEntita);
}
