package it.eurotn.panjea.giroclienti.manager.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import it.eurotn.panjea.giroclienti.util.SchedaGiroClienteStampa;
import it.eurotn.panjea.giroclienti.util.SchedaGiroClientiDTO;
import it.eurotn.panjea.util.Giorni;

@Local
public interface GiroClientiSchedeManager {

    /**
     * Restituisce le date della scheda attuale dell'utente.
     *
     * @param idUtente
     *            id utente
     * @return date
     */
    Date[] caricaDateSchedaSettimanale(Integer idUtente);

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

}
