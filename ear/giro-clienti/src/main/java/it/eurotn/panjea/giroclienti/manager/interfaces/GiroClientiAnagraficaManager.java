package it.eurotn.panjea.giroclienti.manager.interfaces;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.giroclienti.domain.GiroSedeCliente;
import it.eurotn.panjea.giroclienti.domain.ModalitaCopiaGiroClienti;
import it.eurotn.panjea.util.Giorni;

@Local
public interface GiroClientiAnagraficaManager {

    /**
     * Cancella un {@link GiroSedeCliente}.
     *
     * @param giroSedeCliente
     *            giro da cancellare
     */
    void cancellaGiroSedeCliente(GiroSedeCliente giroSedeCliente);

    /**
     * Cancella tutti i {@link GiroSedeCliente} dell'entità.
     *
     * @param idEntita
     *            id entità
     */
    void cancellaGiroSedeCliente(Integer idEntita);

    /**
     * Cancella il {@link GiroSedeCliente} corrispondente ai paramentri.
     *
     * @param idSedeEntita
     *            id sede entità
     * @param giorno
     *            giorno
     * @param ora
     *            ora
     */
    void cancellaGiroSedeCliente(Integer idSedeEntita, Giorni giorno, Date ora);

    /**
     * Carica tutti i giri glienti configurati per il giorno e l'utente richiesto.
     *
     * @param giorno
     *            giorno
     * @param idUtente
     *            id utente
     * @return {@link GiroSedeCliente} caricati
     */
    List<GiroSedeCliente> caricaGiroSedeCliente(Giorni giorno, Integer idUtente);

    /**
     * Carica i giri cliente definiti per l'entità.
     *
     * @param idEntita
     *            id entita
     * @return {@link GiroSedeCliente} presenti
     */
    List<GiroSedeCliente> caricaGiroSedeCliente(Integer idEntita);

    /**
     * Copia il giro clienti del giorno e utente specificato su quello di destinazione.
     *
     * @param idUtente
     *            id utente
     * @param giorno
     *            giorno
     * @param idUtenteDestinazione
     *            utente di destinazione
     * @param giornoDestinazione
     *            giorno di destinazione
     * @param modalitaCopia
     *            tipo di copia
     */
    void copiaGiroSedeClienti(Integer idUtente, Giorni giorno, Integer idUtenteDestinazione, Giorni giornoDestinazione,
            ModalitaCopiaGiroClienti modalitaCopia);

    /**
     * Salva un {@link GiroSedeCliente}.
     *
     * @param giroSedeCliente
     *            giro da salvare
     * @return giro salvato
     */
    GiroSedeCliente salvaGiroSedeCliente(GiroSedeCliente giroSedeCliente);

    /**
     * Salva i {@link GiroSedeCliente}.
     *
     * @param giri
     *            giri da salvare
     */
    void salvaGiroSedeCliente(List<GiroSedeCliente> giri);

}
