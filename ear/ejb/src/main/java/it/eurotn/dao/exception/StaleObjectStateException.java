package it.eurotn.dao.exception;

/**
 * Viene sollevata al momento di un errore del versionamento di un oggetto.
 *
 * @author Aracno
 * @version 1.0, 10-apr-2006
 *
 */
public class StaleObjectStateException extends DAOException {

  private static final long serialVersionUID = 1L;

  /**
   * 
   * Costruttore.
   * 
   * @param cause
   *          cause
   */
  public StaleObjectStateException(final Throwable cause) {
    super(cause);
  }

}
