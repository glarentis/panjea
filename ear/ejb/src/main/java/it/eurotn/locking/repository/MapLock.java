package it.eurotn.locking.repository;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import it.eurotn.locking.ILock;

/**
 * Classe che wrappa una mappa in un singleton.
 *
 * @author Aracno
 * @version 1.0, 17-mar-2006
 *
 */
public final class MapLock extends ConcurrentHashMap<String, ILock>implements Serializable {
  private static Logger logger = Logger.getLogger(MapLock.class);
  private static final long serialVersionUID = 5953630401309921853L;
  private static volatile MapLock locks = null;

  /**
   *
   * @return Singleton
   */
  public static MapLock getInstance() {
    if (locks == null) {
      logger.debug("--> CREO LA CLASSE SINGLETON MAP LOCK");
      locks = new MapLock();
    }
    return locks;
  }

  /**
   *
   * Costruttore.
   */
  private MapLock() {
  }

}
