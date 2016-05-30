package it.eurotn.panjea.rich.bd;

import java.util.List;

import org.springframework.security.Authentication;

import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Permesso;
import it.eurotn.panjea.sicurezza.domain.Ruolo;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.sicurezza.service.exception.SicurezzaServiceException;

/**
 *
 *
 * @author giangi
 * @version 1.0, 24/ott/06
 *
 */
public interface ISicurezzaBD {

    /**
     * Cambia la password dell'utente loggato con la nuova password fornita.
     *
     * @param oldPassword
     *            vecchia password
     * @param newPassword
     *            nuova password
     */
    void cambiaPasswordUtenteLoggato(String oldPassword, String newPassword);

    /**
     * Cancella i dati mail.
     *
     * @param datiMail
     *            dati da cancellare
     */
    void cancellaDatiMail(DatiMail datiMail);

    /**
     * Cancella il ruolo scelto.
     *
     * @param ruolo
     *            il ruolo da cancellare
     * @return true se l'operazione è andata a buon fine
     */
    boolean cancellaRuolo(Ruolo ruolo);

    /**
     * Cancella l'utente scelto.
     *
     * @param utente
     *            l'utente da cancellare
     * @return true se l'operazione è andata a buon fine
     */
    boolean cancellaUtente(Utente utente);

    /**
     *
     * @return lista delle aziende disponibili
     */
    List<String> caricaAziendeDeployate();

    /**
     * Carica i dati mail.
     *
     * @param datiMail
     *            id dei dati da caricare
     * @return dati caricati
     */
    DatiMail caricaDatiMail(DatiMail datiMail);

    /**
     * Carica i dati mail dell'utente.
     *
     * @param idUtente
     *            id utente
     * @return dati mail caricati
     */
    List<DatiMail> caricaDatiMail(Integer idUtente);

    /**
     * Carica tutti i permessi disponibili.
     *
     * @return List<Permesso>
     */
    List<Permesso> caricaPermessi();

    /**
     * Carica i ruoli dell' utente loggato associati all'azienda di quell'utente.
     *
     * @return List<Ruolo>
     */
    List<Ruolo> caricaRuoliAziendaCorrente();

    /**
     * Carica un ruolo.
     *
     * @param idRuolo
     *            Codice del ruolo da caricare
     * @return ruolo caricato
     */
    Ruolo caricaRuolo(Integer idRuolo);

    /**
     * Carica un utente.
     *
     * @param idUtente
     *            id dell'utente da caricare
     * @return Domain Object dell'utente caricato
     */
    Utente caricaUtente(Integer idUtente);

    /**
     * Carica un utente attraverso il suo userName.
     *
     * @param codiceUtente
     *            nome utente
     * @return utente
     */
    Utente caricaUtente(String codiceUtente);

    /**
     * Carica la lista di tutti gli utenti.
     *
     * @param valueSearch
     *            .
     * @param fieldSearch
     *            .
     *
     * @return lista degli utenti presenti compresi quelli disabilitati
     */
    List<Utente> caricaUtenti(String fieldSearch, String valueSearch);

    /**
     * Esegue il flush della security cache di JBoss.
     */
    void flushCache();

    /**
     * Carica la password dell'utente loggato.
     *
     * @return String
     */
    String getCredential();

    /**
     * Esegue il login lato server.
     *
     * @return Principal loggato con le credenziali impostate nel loginContext
     */
    Authentication login();

    /**
     * Salva i dati mail.
     *
     * @param datiMail
     *            dati da salvare
     * @return dati salvati
     */
    DatiMail salvaDatiMail(DatiMail datiMail);

    /**
     * Salva un ruolo.
     *
     * @param ruolo
     *            il ruolo da salvare
     * @return ruolo salvato
     * @throws SicurezzaServiceException
     */
    Ruolo salvaRuolo(Ruolo ruolo);

    /**
     * Salva il ruolo allegando i permessi.
     *
     * @param ruolo
     *            il ruolo da salvare
     * @param permessiDaAggiungere
     *            i permessi da rimuovere al ruolo esistente
     * @param permessiDaRimuovere
     *            i permessi da associare al ruolo esistente
     * @return Ruolo salvato
     */
    Ruolo salvaRuolo(Ruolo ruolo, List<Permesso> permessiDaAggiungere, List<Permesso> permessiDaRimuovere);

    /**
     * Salva l'utente con i ruoli ad esso associati.
     *
     * @param utente
     *            l'utente da salvare
     * @param ruoliDaAggiungere
     *            i ruoli da aggiungere all'utente
     * @param ruoliDaRimuovere
     *            i ruoli da rimuovere dall' utente
     * @return Utente salvato
     */
    Utente salvaUtente(Utente utente, List<Ruolo> ruoliDaAggiungere, List<Ruolo> ruoliDaRimuovere);
}