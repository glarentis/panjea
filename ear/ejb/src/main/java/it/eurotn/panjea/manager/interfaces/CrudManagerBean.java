package it.eurotn.panjea.manager.interfaces;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.IEntityCodiceAzienda;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

public abstract class CrudManagerBean<T extends EntityBase> implements CrudManager<T> {

    private static final Logger LOGGER = Logger.getLogger(CrudManagerBean.class);

    @Resource
    private SessionContext sessionContext;

    @EJB
    protected PanjeaDAO panjeaDAO;

    @Override
    public void cancella(Integer id) {
        LOGGER.debug("--> Enter cancella");

        T newInstance = null;
        try {
            newInstance = getManagedClass().newInstance();
            newInstance.setId(id);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante la cancellazione dell'oggetto " + getManagedClass().getSimpleName(), e);
            throw new GenericException(
                    "Errore durante la cancellazione dell'oggetto " + getManagedClass().getSimpleName(), e);
        }

        cancella(newInstance);

        LOGGER.debug("--> Exit cancella");
    }

    @Override
    public void cancella(T object) {
        LOGGER.debug("--> Enter cancella");

        try {
            panjeaDAO.delete(object);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante la cancellazione dell'oggetto " + object.getClass().getSimpleName(), e);
            throw new GenericException(
                    "Errore durante la cancellazione dell'oggetto " + object.getClass().getSimpleName(), e);
        }

        LOGGER.debug("--> Exit cancella");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> caricaAll() {
        LOGGER.debug("--> Enter caricaAll");

        List<T> result = new ArrayList<>();

        Query query = panjeaDAO.prepareQuery("from " + getManagedClass().getSimpleName());
        try {
            result = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento degli oggetti di tipo " + getManagedClass().getSimpleName(),
                    e);
            throw new GenericException(
                    "Errore durante il caricamento degli oggetti di tipo " + getManagedClass().getSimpleName(), e);
        }

        LOGGER.debug("--> Exit caricaAll");
        return result;
    }

    @Override
    public T caricaById(Integer id) {
        LOGGER.debug("--> Enter caricaById");

        T objectLoad = null;
        try {
            objectLoad = panjeaDAO.load(getManagedClass(), id);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il caricamento dell'oggetto " + getManagedClass().getSimpleName() + " id: "
                    + id, e);
            throw new GenericException(
                    "Errore durante il caricamento dell'oggetto " + getManagedClass().getSimpleName() + " id: " + id,
                    e);
        }

        LOGGER.debug("--> Exit caricaById");
        return objectLoad;
    }

    /**
     * @return codiceAzienda
     */
    protected String getCodiceAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    /**
     *
     * @return Entity gestita dal manager
     */
    protected abstract Class<T> getManagedClass();

    /**
     * @return userName loggato
     */
    protected String getUserName() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getUserName();
    }

    @Override
    public T salva(T object) {
        LOGGER.debug("--> Enter salva");

        if (object.isNew() && object instanceof IEntityCodiceAzienda) {
            ((IEntityCodiceAzienda) object).setCodiceAzienda(getCodiceAzienda());
        }

        T objectSave = null;
        try {
            objectSave = panjeaDAO.save(object);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio dell'oggetto " + object.getClass().getSimpleName(), e);
            throw new GenericException(
                    "Errore durante il salvataggio dell'oggetto " + object.getClass().getSimpleName(), e);
        }

        LOGGER.debug("--> Exit salva");
        return objectSave;
    }
}
