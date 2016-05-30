package it.eurotn.locking;

import java.io.Serializable;
import java.util.Date;

/**
 * Classe lock basata su JAAS e il framework JECAUthentication Per estrarre lo userName utilizza
 * JAAS e l'aliasDB lo recupera da JECPrincipal
 *
 * Un istanza di questa classe identifica un oggetto documento bloccato.
 *
 * @author Gianluca
 * @version 1.0, 30-mag-2005
 *
 */
public class DefaultLock implements ILock, Serializable {

  private static final long serialVersionUID = 3257002155247416881L;
  private String userName;
  private String classObj;
  private java.util.Date timeStart;
  private Object keyObj;
  private String codiceAzienda;
  private String keyLock;
  private Integer version;

  /**
   * Costruttore.
   */
  public DefaultLock() {
    super();
  }

  /**
   * 
   * Costruttore.
   * 
   * @param classObj
   *          classe dell'oggetto
   * @param keyObj
   *          chiave dell'oggetto
   * @param userName
   *          utente che ha generato il lock
   * @param codiceAzienda
   *          azienda dove generare il lock
   * @param versione
   *          versione dell'oggetto da bloccare
   */
  public DefaultLock(final String classObj, final Object keyObj, final String userName,
      final String codiceAzienda, final Integer versione) {

    this.userName = userName;
    this.codiceAzienda = codiceAzienda;

    this.keyLock = codiceAzienda + classObj + keyObj.toString();
    this.timeStart = new Date();
    this.classObj = classObj;
    this.keyObj = keyObj;
    this.version = versione;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    DefaultLock other = (DefaultLock) obj;
    if (classObj == null) {
      if (other.classObj != null) {
        return false;
      }
    } else if (!classObj.equals(other.classObj)) {
      return false;
    }
    if (codiceAzienda == null) {
      if (other.codiceAzienda != null) {
        return false;
      }
    } else if (!codiceAzienda.equals(other.codiceAzienda)) {
      return false;
    }
    if (keyLock == null) {
      if (other.keyLock != null) {
        return false;
      }
    } else if (!keyLock.equals(other.keyLock)) {
      return false;
    }
    return true;
  }

  @Override
  public String getClassObj() {
    return classObj;
  }

  @Override
  public String getKeyLock() {
    return keyLock;
  }

  @Override
  public Object getKeyObj() {
    return keyObj;
  }

  @Override
  public java.util.Date getTimeStart() {
    return timeStart;
  }

  @Override
  public String getUserName() {
    return userName;
  }

  @Override
  public int getVersion() {
    return version;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((classObj == null) ? 0 : classObj.hashCode());
    result = prime * result + ((codiceAzienda == null) ? 0 : codiceAzienda.hashCode());
    result = prime * result + ((keyLock == null) ? 0 : keyLock.hashCode());
    return result;
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("LockDefault[");
    buffer.append("classObj = ").append(classObj);
    buffer.append(" keyObj = ").append(keyObj);
    buffer.append(" timeStart = ").append(timeStart);
    buffer.append(" userName = ").append(userName);
    buffer.append(" aliasDB = ").append(codiceAzienda);
    buffer.append(" versione = ").append(version);
    buffer.append("]");
    return buffer.toString();
  }
}
