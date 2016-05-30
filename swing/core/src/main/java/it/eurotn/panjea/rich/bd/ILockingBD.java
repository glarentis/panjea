package it.eurotn.panjea.rich.bd;

import java.util.List;

import it.eurotn.locking.ILock;
import it.eurotn.locking.exception.LockNotFoundException;

/**
 * Interfaccia per il Business Delegate del service per la gestione dei lock
 *
 * @author adriano
 */
public interface ILockingBD {

    /**
     *
     * @return tutti gli oggetto bloccati
     */
    public List<ILock> getLockAll();

    /**
     *
     * @param lockObject
     *            oggetto da bloccare
     * @return handler del lock
     */
    public ILock lock(Object lockObject);

    /**
     *
     * @param lock
     *            handler da rilasciare
     * @return true se rilasciata
     * @throws LockNotFoundException
     *             rilanciata se il lock non esiste
     */
    public boolean release(ILock lock) throws LockNotFoundException;

    /**
     * rilascia tutti gli oggetti bloccati.
     */
    public void releaseAll();

}