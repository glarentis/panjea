package it.eurotn.panjea.sicurezza.service.interfaces;

import it.eurotn.dao.exception.DuplicateKeyObjectException;
import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.panjea.sicurezza.domain.Permesso;
import it.eurotn.panjea.sicurezza.domain.Ruolo;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.sicurezza.service.exception.SicurezzaServiceException;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Remote;

@Remote
public interface SicurezzaService {

	/**
	 * Associa un permesso ad un ruolo.
	 *
	 * @param ruolo
	 *            ruolo di riferimento
	 * @param permesso
	 *            permesso da associare
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 * @throws DuplicateKeyObjectException
	 */
	void associaPermessoARuolo(Ruolo ruolo, Permesso permesso) throws SicurezzaServiceException;

	/**
	 * Associa un ruolo ad un utente.
	 *
	 * @param utente
	 *            utente al quale verra' associato il ruolo
	 * @param ruolo
	 *            ruolo che sara' associato all'utente
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 */
	void associaRuoloAUtente(Utente utente, Ruolo ruolo) throws SicurezzaServiceException;

	/**
	 * Cambia la password dell'utente loggato con la nuova password fornita.
	 *
	 * @param oldPassword
	 *            vecchia password
	 * @param newPassword
	 *            nuova password
	 * @throws SicurezzaServiceException
	 *             sollevata se <code>oldPassword</code> non corrisponde a quella dell'utente
	 */
	void cambiaPasswordUtenteLoggato(String oldPassword, String newPassword) throws SicurezzaServiceException;

	/**
	 * Cancella i dati mail.
	 *
	 * @param datiMail
	 *            dati da cancellare
	 */
	void cancellaDatiMail(DatiMail datiMail);

	/**
	 * Cancella un ruolo.
	 *
	 * @param ruolo
	 *            ruolo da salvare
	 */
	void cancellaRuolo(Ruolo ruolo);

	/**
	 * Cancella un utente.
	 *
	 * @param utente
	 *            utente da cancellare
	 */
	void cancellaUtente(Utente utente);

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
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 */
	List<DatiMail> caricaDatiMail(Integer idUtente) throws SicurezzaServiceException;

	/**
	 * Carica tutti i permessi.
	 *
	 * @return lista di <code>PermessoVO</code> trovati
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 */
	java.util.List<Permesso> caricaPermessi() throws SicurezzaServiceException;

	/**
	 * Carica tutti i permessi di un determinato modulo.
	 *
	 * @param modulo
	 *            Modulo filtro per il caricamento dei permessi
	 * @return lista di <code>Permesso</code> trovati
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 */
	java.util.List<Permesso> caricaPermessi(java.lang.String modulo) throws SicurezzaServiceException;

	/**
	 * Carica tutti i ruoli.
	 *
	 * @return lista di <code>Ruolo</code>
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 * @throws RemoteException
	 *             eccezione generica
	 */
	java.util.List<Ruolo> caricaRuoli() throws java.rmi.RemoteException, SicurezzaServiceException;

	/**
	 * Carica tutti i ruoli filtrati per l'azienda dell'utente loggato.
	 *
	 * @return lista di <code>RuoloVO</code>
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 */
	java.util.List<Ruolo> caricaRuoliAziendaCorrente() throws SicurezzaServiceException;

	/**
	 * Carica un ruolo.
	 *
	 * @param idRuolo
	 *            Codice del ruolo da caricare
	 * @return ruolo caricato
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 * @throws RemoteException
	 *             eccezione generica
	 */
	Ruolo caricaRuolo(java.lang.Integer idRuolo) throws SicurezzaServiceException, java.rmi.RemoteException;

	/**
	 * Carica un utente.
	 *
	 * @param idUtente
	 *            id dell'utente da caricare
	 * @return Domain Object dell'utente caricato
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 */
	Utente caricaUtente(java.lang.Integer idUtente) throws SicurezzaServiceException;

	/**
	 * Carica un utente attraverso il suo userName.
	 *
	 * @param userName
	 *            nome utente
	 * @return utente
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 */
	Utente caricaUtente(String userName) throws SicurezzaServiceException;

	/**
	 * Carica la lista di tutti gli utenti.
	 *
	 * @param valueSearch
	 *            .
	 * @param fieldSearch
	 *            .
	 *
	 * @return List<Utente>
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 * @throws RemoteException
	 *             eccezione generica
	 */
	java.util.List<Utente> caricaUtenti(String fieldSearch, String valueSearch) throws java.rmi.RemoteException,
	SicurezzaServiceException;

	/**
	 * Carica tutta la lista degli utenti disponibili per il POS.
	 *
	 * @return utenti caricati
	 */
	List<Utente> caricaUtentiPos();

	/**
	 * Esegue il flush della security cache di JBoss.
	 *
	 * @throws RemoteException
	 *             eccezione generica
	 */
	void flushCache() throws java.rmi.RemoteException;

	/**
	 * Restituisce la password dell' utente loggato.
	 *
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 * @return password
	 * @throws RemoteException
	 *             eccezione generica
	 */
	java.lang.String getCredential() throws SicurezzaServiceException, java.rmi.RemoteException;

	/**
	 * Restituisce il JECPrincipal con i suoi ruoli.
	 *
	 * @return principal
	 * @throws RemoteException
	 *             eccezione generica
	 */
	it.eurotn.security.JecPrincipal login() throws java.rmi.RemoteException;

	/**
	 * Rimuove un permesso da un ruolo.
	 *
	 * @param ruolo
	 *            ruolo di riferimento
	 * @param permesso
	 *            Permesso da rimuovere
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 */
	void rimuoviPermessoDaRuolo(Ruolo ruolo, Permesso permesso) throws SicurezzaServiceException;

	/**
	 * Rimuove un ruolo da un utente.
	 *
	 * @param utente
	 *            utente da cui rimuovere il ruolo
	 * @param ruolo
	 *            ruolo da rimuovere
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 * @throws DuplicateKeyObjectException
	 *             eccezione generica
	 * @throws RemoteException
	 *             eccezione generica
	 */
	void rimuoviRuoloDaUtente(Utente utente, Ruolo ruolo) throws SicurezzaServiceException,
	it.eurotn.dao.exception.DuplicateKeyObjectException, java.rmi.RemoteException;

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
	 *            ruolo da salvare
	 * @return ruolo salvato
	 */
	Ruolo salvaRuolo(Ruolo ruolo);

	/**
	 * Salva il ruolo con i permessi associati.
	 *
	 * @param ruolo
	 *            ruolo da salvare
	 * @param permessiAggiunti
	 *            i permessi da aggiungere al ruolo
	 * @param permessiRimossi
	 *            i permessi da rimuovere dal ruolo
	 * @return Ruolo
	 */
	Ruolo salvaRuolo(Ruolo ruolo, java.util.List<Permesso> permessiAggiunti, java.util.List<Permesso> permessiRimossi);

	/**
	 * Salva un utente.
	 *
	 * @param utente
	 *            utente da salvare
	 * @return Value Object dell'utente salvato
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 * @throws RemoteException
	 *             eccezione generica
	 */
	Utente salvaUtente(Utente utente) throws SicurezzaServiceException, java.rmi.RemoteException;

	/**
	 * Salva un utente con i ruoli legati.
	 *
	 * @param utente
	 *            utente da salvare
	 * @param ruoliAggiunti
	 *            i ruoli da aggiungere all'utente
	 * @param ruoliRimossi
	 *            i ruoli da rimuovere dall' utente
	 * @return utente salvato
	 * @throws RemoteException
	 *             eccezione generica
	 * @throws SicurezzaServiceException
	 *             eccezione generica
	 */
	Utente salvaUtente(Utente utente, java.util.List<Ruolo> ruoliAggiunti, java.util.List<Ruolo> ruoliRimossi)
			throws SicurezzaServiceException, java.rmi.RemoteException;

}
