package it.eurotn.locking;

import java.util.Date;

/**
 * Interfaccia che deve essere implementata dall'oggetto che descrive il lock del documento.
 *
 * @author Gianluca
 * @version 1.0, 30-mag-2005
 *
 */
public interface ILock {

  /**
   * 
   * @return Classe dell'oggetto bloccato
   */
  String getClassObj();

  /**
   * 
   * @return Chiave univoca del lock
   */
  String getKeyLock();

  /**
   * 
   * @return Chiave dell'oggetto bloccato
   * 
   */
  Object getKeyObj();

  /**
   * 
   * @return Ora in cui il documento è stato bloccato
   */
  Date getTimeStart();

  /**
   * 
   * @return UserName dell'utente che ha eseguito il lock
   * 
   */
  String getUserName();

  /**
   * 
   * @return Versione dell' oggetto per verificare di non lockare l'oggetto con una versione
   *         "datata"
   * 
   */
  int getVersion();
}
