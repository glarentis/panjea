package it.eurotn.panjea.magazzino.manager.manutenzionelistino.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.exception.ArticoliDuplicatiManutenzioneListinoException;
import it.eurotn.panjea.magazzino.exception.ListinoManutenzioneNonValidoException;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriAggiornaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.RigaManutenzioneListino;

/**
 * @author Leonardo
 */
@Local
public interface ManutenzioneListinoManager {

    /**
     * Dati {@link ParametriAggiornaManutenzioneListino} inserisce aggiorna la versione listino
     * scelta con le sue righe.<br/>
     * Nota che il valore viene arrotondato al numero decimali scelto a questa fase del processo,
     * cioe' quando viene aggiornata/inserita la rigaListino.
     *
     * @param parametriAggiornaManutenzioneListino
     *            i parametri per aggiornare/creare la versione listino
     * @throws ArticoliDuplicatiManutenzioneListinoException
     *             sollevata se esistono articoli duplicati
     */
    void aggiornaListinoDaManutenzione(ParametriAggiornaManutenzioneListino parametriAggiornaManutenzioneListino)
            throws ArticoliDuplicatiManutenzioneListinoException;

    /**
     * Cancella le {@link RigaManutenzioneListino} passate; tutte se la lista e' null.
     *
     * @param righeManutenzioneListino
     *            le righe da cancellare
     */
    void cancellaRigheManutenzioneListino(List<RigaManutenzioneListino> righeManutenzioneListino);

    /**
     * Carica le {@link RigaManutenzioneListino} presenti.
     *
     * @return List<RigaManutenzioneListino>
     */
    List<RigaManutenzioneListino> caricaRigheManutenzioneListino();

    /**
     * Dati i {@link ParametriRicercaManutenzioneListino} ricerca e inserisce le
     * {@link RigaManutenzioneListino} associate ai parametri.
     *
     * @param parametriRicercaManutenzioneListino
     *            ParametriRicercaManutenzioneListino da cui trovare ed inserire le righe
     * @throws ListinoManutenzioneNonValidoException
     *             sollevata nel caso in cui il listino dei parametri manutenzione non sia valido
     *             con le eventuali righe manutenzione già presenti
     */
    void inserisciRigheRicercaManutenzioneListino(
            ParametriRicercaManutenzioneListino parametriRicercaManutenzioneListino)
                    throws ListinoManutenzioneNonValidoException;

    /**
     * Salva una rigaManutenzione listino.
     *
     * @param rigaManutenzioneListino
     *            la riga da salvare
     * @return {@link RigaManutenzioneListino} salvate
     */
    List<RigaManutenzioneListino> salvaRigaManutenzioneListino(RigaManutenzioneListino rigaManutenzioneListino);
}
