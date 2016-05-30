package it.eurotn.panjea.giroclienti.service.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.giroclienti.util.SchedaGiroClienteStampa;
import it.eurotn.panjea.giroclienti.util.SchedaGiroClientiDTO;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;

@Remote
public interface SchedeGiroClientiService {

    /**
     * Cancella tutte le schede presenti per l'utente.
     *
     * @param utente
     *            utente
     */
    void cancellaSchede(Utente utente);

    /**
     * Restituisce le date della scheda attuale dell'utente.
     *
     * @param idUtente
     *            id utente
     * @return date
     */
    Date[] caricaDateSchedaSettimanale(Integer idUtente);

    /**
     * Carica una {@link RigaGiroCliente}.
     *
     * @param idRiga
     *            id riga
     * @return riga caricata
     */
    RigaGiroCliente caricaRigaGiroCliente(Integer idRiga);

    /**
     * Carica tutte le righe giro cliente presenti per l'utente e il giorno specificato.
     *
     * @param giorno
     *            giorno
     * @param utente
     *            utente
     * @return righe presenti
     */
    List<RigaGiroCliente> caricaRigheGiroCliente(Giorni giorno, Utente utente);

    /**
     * Carica tutte le righe giro cliente in base ai parametri specificati.
     *
     * @param params
     *            parametri
     * @return righe
     */
    List<RigaGiroCliente> caricaRigheGiroCliente(Map<Object, Object> params);

    /**
     * Carica le schede giro per la settimana in corso.
     *
     * @param idUtente
     *            id utente
     * @param giorno
     *            giorno
     * @return scheda cliente
     */
    SchedaGiroClientiDTO caricaSchedaSettimana(Integer idUtente, Giorni giorno);

    /**
     * Carica le schede giro cliente per la stampa.
     *
     * @param params
     *            parametri
     * @return schede caricate
     */
    List<SchedaGiroClienteStampa> caricaSchedeStampa(Map<Object, Object> params);

    /**
     * Crea la testata dell'area ordine delle riga giro cliente.
     *
     * @param idRigaGiroCliente
     *            id riga giro cliente
     */
    void creaAreaOrdineGiroCliente(Integer idRigaGiroCliente);

    /**
     * Salva una {@link RigaGiroCliente}.
     *
     * @param rigaGiroCliente
     *            riga da salvare
     * @return riga salvata
     */
    RigaGiroCliente salvaRigaGiroCliente(RigaGiroCliente rigaGiroCliente);
}
