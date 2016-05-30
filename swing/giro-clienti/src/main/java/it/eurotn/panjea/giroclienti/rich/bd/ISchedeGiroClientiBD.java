package it.eurotn.panjea.giroclienti.rich.bd;

import java.util.Date;
import java.util.List;

import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.giroclienti.util.SchedaGiroClientiDTO;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;

public interface ISchedeGiroClientiBD {

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
