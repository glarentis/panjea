package it.eurotn.panjea.rich.bd;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.eurotn.locking.IDefProperty;
import it.eurotn.locking.ILock;
import it.eurotn.locking.exception.LockFoundException;
import it.eurotn.locking.exception.LockNotFoundException;
import it.eurotn.locking.service.interfaces.LockingServiceRemote;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

/**
 * @author Leonardo
 */
public class LockingBD implements ILockingBD {

    private static final Logger LOGGER = Logger.getLogger(LockingBD.class);

    LockingServiceRemote lockingService;

    @Override
    public List<ILock> getLockAll() {
        try {
            return (List<ILock>) lockingService.getLockAll();
        } catch (Exception e) {
            LOGGER.error("---> Errore nel caricare tutti gli elementi lockati", e);
            PanjeaSwingUtil.checkAndThrowException(e);
            return new ArrayList<>();
        }
    }

    /**
     * @return Returns the lockingService.
     */
    public LockingServiceRemote getLockingService() {
        return lockingService;
    }

    @Override
    public ILock lock(Object lockObject) {
        LOGGER.debug("---> Enter lock");
        ILock lock = null;

        try {
            Integer id = null;
            Integer version = null;
            String classDomainObject = null;

            if (lockObject instanceof IDefProperty) {
                IDefProperty object = (IDefProperty) lockObject;
                id = object.getId();
                version = object.getVersion();
                classDomainObject = object.getDomainClassName();
            } else {
                LOGGER.error("--> L'oggetto che si sta cercando di bloccare non � istanza di IDefProperty");
                throw new IllegalAccessException(
                        "L'oggetto che si sta cercando di bloccare non � istanza di IDefProperty");
            }

            lock = lockingService.lock(classDomainObject, id, version);

        } catch (LockFoundException e) {
            LOGGER.error("---> Lock sull'oggetto gia' presente");
            PanjeaSwingUtil.checkAndThrowException(e);
        } catch (Exception e) {
            LOGGER.error("---> Errore nel lock dell' oggetto", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        LOGGER.debug("---> Exit lock");
        return lock;
    }

    @Override
    public boolean release(ILock lock) throws LockNotFoundException {
        LOGGER.debug("---> Enter release");
        try {
            lockingService.release(lock);
            LOGGER.debug("---> Exit release");
            return true;
        } catch (LockNotFoundException e) {
            LOGGER.warn("---> Errore nel rilasciare il lock LockNotFoundException");
            throw e;
        } catch (Exception e) {
            LOGGER.error("---> Errore nel rilasciare il lock", e);
            PanjeaSwingUtil.checkAndThrowException(e);
            return false;
        }
    }

    @Override
    public void releaseAll() {
        LOGGER.debug("---> Enter releaseAll");
        try {
            lockingService.releaseAll();
        } catch (Exception e) {
            LOGGER.error("---> Errore nel rilasciare tutte le risorse lockate", e);
            PanjeaSwingUtil.checkAndThrowException(e);
        }
        LOGGER.debug("---> Exit releaseAll");
    }

    /**
     * @param lockingService
     *            The lockingService to set.
     */
    public void setLockingService(LockingServiceRemote lockingService) {
        this.lockingService = lockingService;
    }

}
