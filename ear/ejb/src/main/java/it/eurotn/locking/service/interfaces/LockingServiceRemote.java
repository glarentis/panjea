/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.eurotn.locking.service.interfaces;

import it.eurotn.locking.ILock;
import it.eurotn.locking.exception.LockFoundException;
import it.eurotn.locking.exception.LockNotFoundException;
import it.eurotn.locking.exception.LockStaleObjectException;

import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;

/**
 * 
 * @author adriano
 */
@Remote
public interface LockingServiceRemote {

	/**
	 * Restituisce tutti i documenti bloccati.
	 * 
	 * @return Lista di oggetti <code>ILock</code>
	 */
	Collection<ILock> getLockAll();

	/**
	 * @return i lock dell'utente corrente.
	 */
	List<ILock> getLockByLoggedUser();

	/**
	 * Retituisce una lista di <code>ILock</code> di un utente. <BR>
	 * Se non esiste nessun documento bloccato per l'utente la lista sarà vuota
	 * 
	 * @return Lista di oggetti <code>ILock</code>
	 * @param userName
	 *            Nome dell'utente
	 */
	List<ILock> getLockByUser(String userName);

	/**
	 * Blocca un oggetto.
	 * 
	 * @param classObj
	 *            classe dell'oggetto da bloccare
	 * @param keyObj
	 *            chiave dell'oggetto
	 * @param versione
	 *            versioe bloccata
	 * @return lock creato
	 * @throws it.eurotn.locking.exception.LockFoundException
	 *             lanciata quando un oggetto già bloccato
	 * @throws it.eurotn.locking.exception.LockStaleObjectException
	 *             lanciata quando ho un oggetto bloccato con versione diversa
	 */
	ILock lock(java.lang.String classObj, java.lang.Object keyObj, java.lang.Integer versione)
			throws it.eurotn.locking.exception.LockFoundException, it.eurotn.locking.exception.LockStaleObjectException;

	/**
	 * Esegue il lock di un documento.
	 * 
	 * @return <code>Lock</code> del documento
	 * @param userName
	 *            Utente che esegue il lock
	 * @param aliasDB
	 *            alias del database del documento
	 * @param classObj
	 *            Classe del documento
	 * @param keyObj
	 *            Chiave del documento
	 * @param versione
	 *            Versione del documento che voglio bloccare. Null se non ho la versione. Altrimenti verifico la
	 *            versione e lancio una StaleObject se la versione è cambiata
	 * @throws it.eurotn.locking.exception.LockFoundException
	 *             lanciata quando un oggetto già bloccato
	 * @throws LockStaleObjectException
	 *             rilanciata se ho una versione bloccata differente
	 */
	ILock lock(String userName, String aliasDB, String classObj, Object keyObj, Integer versione)
			throws it.eurotn.locking.exception.LockFoundException, LockStaleObjectException;

	/**
	 * Rilascia il lock di un documento.
	 * 
	 * @throws it.eurotn.locking.exception.LockNotFoundException
	 * 
	 * @param lock
	 *            <code>ILock</code> da rilasciare
	 * @throws LockNotFoundException
	 *             lock non trovato
	 * @throws LockFoundException
	 *             lanciata quando un oggetto già bloccato
	 */
	void release(it.eurotn.locking.ILock lock) throws it.eurotn.locking.exception.LockNotFoundException,
			LockFoundException;

	/**
	 * 
	 * Rilascia tutti i documenti bloccati.
	 * 
	 */
	void releaseAll();

	/**
	 * Rilascia tutti i documenti bloccati da un utente.
	 * 
	 * @param userName
	 *            Nome dell'utente
	 */
	void releaseByUser(String userName);

}
