package it.eurotn.locking.repository;

import it.eurotn.locking.ILock;

import java.util.Collection;

/**
 * 
 * Interfaccia del repository del lock.
 * 
 * @author Aracno
 * @version 1.0, 20-mar-2006
 * 
 */
public interface ILockRepository {

	/**
	 * Rimuove tutti i lock nel repository.
	 */
	void clearLocks();

	/**
	 * 
	 * @param lock
	 *            lock da recuperare
	 * @return lock contenuto nel repository. null se non trovato
	 */
	ILock getLock(ILock lock);

	/**
	 * @return collection dei lock presenti nel repository
	 */
	Collection<ILock> getLocks();

	/**
	 * @param lock
	 *            lock da rimuovere nel repository
	 */
	void removeLock(ILock lock);

	/**
	 * Inserisce il lock nel repository.
	 * 
	 * @param lock
	 *            lock da inserire
	 */
	void writeLock(ILock lock);

}