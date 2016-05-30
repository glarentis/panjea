package it.eurotn.locking.exception;

/**
 *
 * Eccezione lanciata quando provo a bloccare un documento con una versione
 * vecchia. Può succedere quando il documento viene modificato e salvato da un
 * altro utente mentre lo sto visionando.
 *
 * @author giangi
 * @version 1.0, 14-apr-2006
 *
 */
public class LockStaleObjectException extends LockException {
	private static final long serialVersionUID = 1L;

}
