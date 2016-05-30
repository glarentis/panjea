package it.eurotn.locking.exception;

import it.eurotn.locking.ILock;

/**
 * Eccezione sollevata se un bean è già bloccato.
 *
 * @author Gianluca
 * @version 1.0, 30-mag-2005
 *
 */
public class LockFoundException extends it.eurotn.locking.exception.LockException {

	private static final long serialVersionUID = 3257284716968424496L;

	private final ILock lock;

	/**
	 * Costruttore.
	 *
	 * @param searchLock
	 *            Lock esistente del documento
	 */
	public LockFoundException(final ILock searchLock) {
		lock = searchLock;
	}

	/**
	 *
	 *
	 * @return Lock esistente del documento
	 */
	public it.eurotn.locking.ILock getLock() {
		return this.lock;
	}
}
