package it.eurotn.dao.exception;

/**
 * Rilanciata quando un oggetto non esiste nel database.
 *
 * @author Aracno
 * @version 1.0, 7-apr-2006
 *
 */
public class ObjectNotFoundException extends DAOException {
  private static final long serialVersionUID = 1L;

  /**
   * Costruttore.
   */
  public ObjectNotFoundException() {
  }

  /**
   * Costruttore.
   * 
   * @param ex
   *          cause
   */
  public ObjectNotFoundException(final Throwable ex) {
    super(ex);
  }
}
