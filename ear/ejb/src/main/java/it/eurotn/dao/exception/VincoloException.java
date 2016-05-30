package it.eurotn.dao.exception;

/**
 * Eccezione usata per errori sui vincoli del BD.
 *
 * @author Aracno
 * @version 1.0, 29/set/06
 *
 */
public class VincoloException extends DAOException {

  private static final long serialVersionUID = -6718120997717274700L;

  private String parentClass;
  private String vincoloClass;

  /**
   * Costruttore.
   * 
   * @param message
   *          messaggio dell'eccezione.
   */
  public VincoloException(final String message) {
    super(message);
  }

  /**
   * 
   * Costruttore.
   * 
   * @param parentClass
   *          classe che non riesco a cancellare a causa del vincolo
   * @param vincoloClass
   *          classe che vincola la parentClass
   */
  public VincoloException(final String parentClass, final String vincoloClass) {
    super();
    this.parentClass = parentClass;
    this.vincoloClass = vincoloClass;
  }

  /**
   * Costruttore.
   * 
   * @param ex
   *          eccezione generata durante la costruzione della vincolo exception.
   */
  public VincoloException(final Throwable ex) {
    super(ex);
    this.parentClass = null;
    this.vincoloClass = null;
  }

  /**
   * @return Returns the parentClass.
   */
  public String getParentClass() {
    return parentClass;
  }

  /**
   * @return Returns the vincoloClass.
   */
  public String getVincoloClass() {
    return vincoloClass;
  }
}
