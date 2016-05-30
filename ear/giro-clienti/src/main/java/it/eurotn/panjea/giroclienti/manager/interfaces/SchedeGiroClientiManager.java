package it.eurotn.panjea.giroclienti.manager.interfaces;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.util.Giorni;

@Local
public interface SchedeGiroClientiManager {

    /**
     * Cancella le righe giro cliente per l'utente e il giorno indicato.
     *
     * @param idUtente
     *            id utente
     * @param giorno
     *            giorno
     */
    void cancellaRigheGiroCliente(Integer idUtente, Giorni giorno);

    /**
     * Cancella tutte le schede presenti per l'utente.
     *
     * @param utente
     *            utente
     */
    void cancellaSchede(Utente utente);

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
     * @param idUtente
     *            id utente
     * @return righe presenti
     */
    List<RigaGiroCliente> caricaRigheGiroCliente(Giorni giorno, Integer idUtente);

    /**
     * Carica tutte le righe giro cliente in base ai parametri specificati.
     *
     * @param params
     *            parametri
     * @return righe
     */
    List<RigaGiroCliente> caricaRigheGiroCliente(Map<Object, Object> params);

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
