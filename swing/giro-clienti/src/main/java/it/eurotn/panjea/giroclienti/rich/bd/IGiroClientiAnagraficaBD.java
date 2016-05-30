package it.eurotn.panjea.giroclienti.rich.bd;

import java.util.Date;
import java.util.List;

import it.eurotn.panjea.giroclienti.domain.GiroClientiSettings;
import it.eurotn.panjea.giroclienti.domain.GiroSedeCliente;
import it.eurotn.panjea.giroclienti.domain.ModalitaCopiaGiroClienti;
import it.eurotn.panjea.util.Giorni;

public interface IGiroClientiAnagraficaBD {

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
     * Carica i settings del giro clienti. Se non esistono ne viene creato uno, salvato e restituito.
     *
     * @return settings
     */
    GiroClientiSettings caricaGiroClientiSettings();

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
     * Salva un {@link GiroClientiSettings}.
     *
     * @param giroClientiSettings
     *            settings da slavare
     * @return settings salvate
     */
    GiroClientiSettings salvaGiroClientiSettings(GiroClientiSettings giroClientiSettings);

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
