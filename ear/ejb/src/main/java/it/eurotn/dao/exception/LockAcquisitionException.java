package it.eurotn.dao.exception;

/**
 * Errore di acquisizione del lock sul db. Ad esempio nel caso di deadlock.
 *
 * @author leonardo
 */
public class LockAcquisitionException extends DAOException {

  private static final long serialVersionUID = -5892247159687382297L;

}
