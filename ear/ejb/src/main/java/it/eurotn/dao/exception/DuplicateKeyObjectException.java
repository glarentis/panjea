package it.eurotn.dao.exception;

/**
 * Viene sollevata al momento di una constraint violation.
 *
 * @author Aracno
 * @version 1.0, 13-apr-2006
 *
 */
public class DuplicateKeyObjectException extends DAOException {

  private static final long serialVersionUID = 1L;
  private String[] propertiesCostraintNames;

  /**
   *
   * Costruttore.
   *
   * @param propertiesCostraintNames
   *          proprietà che hanno causato la duplicateKey
   */
  public DuplicateKeyObjectException(final String[] propertiesCostraintNames) {
    this.propertiesCostraintNames = propertiesCostraintNames;
  }

  /**
   * @return Returns the propertiesCostraintNames.
   */
  public String[] getPropertiesCostraintNames() {
    return propertiesCostraintNames;
  }
}
