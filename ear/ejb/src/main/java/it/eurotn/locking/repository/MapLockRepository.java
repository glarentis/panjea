package it.eurotn.locking.repository;

import java.util.Collection;

import it.eurotn.locking.ILock;

/**
 * Classe per gestire il repository dei documenti attraverso una <code>Map</code><BR>
 * .
 *
 * @author Aracno
 * @version 1.0, 17-mar-2006
 *
 */
public class MapLockRepository implements ILockRepository {

  /**
   * 
   * Costruttore.
   */
  public MapLockRepository() {
  }

  @Override
  public void clearLocks() {
    MapLock.getInstance().clear();
  }

  @Override
  public ILock getLock(ILock lock) {
    return MapLock.getInstance().get(lock.getKeyLock());
  }

  @Override
  public Collection<ILock> getLocks() {
    return MapLock.getInstance().values();
  }

  @Override
  public void removeLock(ILock lock) {
    MapLock.getInstance().remove(lock.getKeyLock());
  }

  @Override
  public void writeLock(ILock lock) {
    MapLock.getInstance().put(lock.getKeyLock(), lock);
  }
}
