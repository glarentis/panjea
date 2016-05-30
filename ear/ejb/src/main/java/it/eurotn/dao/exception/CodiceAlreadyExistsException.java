package it.eurotn.dao.exception;

/**
 * @author Leonardo
 *
 */
public class CodiceAlreadyExistsException extends RuntimeException {

  private static final long serialVersionUID = 1913654698855577134L;

  /**
   *
   * Costruttore.
   */
  public CodiceAlreadyExistsException() {
    super();
  }

  @Override
  public String getMessage() {
    return "Il codice scelto è già stato assegnato";
  }

}
