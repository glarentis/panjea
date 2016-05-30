package it.eurotn.panjea.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.proxy.HibernateProxy;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import com.mysql.jdbc.MysqlDataTruncation;

import it.eurotn.dao.exception.ConstraintExceptionFactory;
import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.DataTooLongException;
import it.eurotn.dao.exception.LockAcquisitionException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.StaleObjectStateException;
import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.util.AliasToBeanNestedResultTransformer;
import it.eurotn.security.JecPrincipal;

/**
 *
 *
 * @author adriano
 * @version 1.0, 02/ott/07
 *
 */
@Stateless(name = "Panjea.PanjeaDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PanjeaDAO")
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PanjeaDAOBean implements PanjeaDAO {

    private static Logger logger = Logger.getLogger(PanjeaDAOBean.class);

    @Resource
    private SessionContext context;

    /**
     * Controlla se l'eccezione deve essere rilanciata come un'eccezione specifica (e wrappata in una classe di
     * eccezione di panjea)<br/>
     * oppure se può essere rilanciata come una {@link DAOException}.
     *
     * @param e
     *            eccezione
     * @throws DAOException
     *             eccezione DAO con l'eccezione wrappata
     */
    @SuppressWarnings("unchecked")
    private void checkAndThrowException(Throwable e) throws DAOException {
        DAOException ex = null;
        if (e instanceof NoResultException) {
            ex = new ObjectNotFoundException(e);
        } else if (e instanceof org.hibernate.StaleObjectStateException) {
            ex = new StaleObjectStateException(e);
        } else if (e instanceof OptimisticLockException) {
            ex = new StaleObjectStateException(e);
        } else if (e instanceof org.hibernate.exception.LockAcquisitionException) {
            logger.error("LockAcquisitionException");
            ex = new LockAcquisitionException();
        } else if (e instanceof EntityExistsException && e.getCause() instanceof ConstraintViolationException) {
            Session session = (Session) getEntityManager().getDelegate();
            ex = ConstraintExceptionFactory.getException(e, session.getSessionFactory().getAllClassMetadata());
        } else if (e.getCause() != null && e.getCause().getCause() != null
                && e.getCause().getCause() instanceof MysqlDataTruncation) {
            ex = new DataTooLongException(e.getCause().getCause().getMessage());
        } else {
            ex = new DAOException(e);
        }
        throw ex;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(EntityBase entity) throws DAOException {
        logger.debug("--> Enter delete");
        try {
            Object entityMerge = null;
            if (entity instanceof HibernateProxy) {
                HibernateProxy proxy = (HibernateProxy) entity;
                entityMerge = getEntityManager().getReference(proxy.getHibernateLazyInitializer().getPersistentClass(),
                        entity.getId());
            } else {
                entityMerge = getEntityManager().getReference(entity.getClass(), entity.getId());
            }
            getEntityManager().remove(entityMerge);
            getEntityManager().flush();
        } catch (PersistenceException e) {
            logger.error("--> errore nella cancellazione dell'entity  ", e);
            checkAndThrowException(e);
        }
        logger.debug("--> Exit delete");
    }

    @Override
    public <T> void evict(T entity) {
        ((Session) getEntityManager().getDelegate()).evict(entity);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public int executeQuery(Query query) throws DAOException {
        int result = -1;
        try {
            result = query.executeUpdate();
        } catch (Exception ex) {
            checkAndThrowException(ex);
        }
        return result;
    }

    /**
     * @return ritorna l'utente loggato.
     */
    @Override
    public JecPrincipal getCurrentPrincipal() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal;
    }

    @Override
    public EntityManager getEntityManager() {
        String codiceAzienda = getCurrentPrincipal().getCodiceAzienda().toLowerCase();
        EntityManager em = (EntityManager) context.lookup("java:/" + codiceAzienda + "EntityManager");
        return em;
    }

    @Override
    public String getJndiCurrentDataSource() {
        return "java:/" + getCurrentPrincipal().getCodiceAzienda().toLowerCase() + "DS";
    }

    @Override
    public List<?> getResultList(Query query) throws DAOException {
        logger.debug("--> Enter getResultList");
        List<?> resultQuery = Collections.emptyList();
        try {
            resultQuery = query.getResultList();
        } catch (NoResultException e) {
            return resultQuery;
        } catch (PersistenceException e) {
            logger.error("--> errore nell'eseguire la query  " + query.toString() + ", stackTrace:" + e.getMessage());
            checkAndThrowException(e);
        }
        logger.debug("--> Exit getResultList size " + resultQuery.size());
        return resultQuery;
    }

    @Override
    public Object getSingleResult(Query query) throws DAOException {
        logger.debug("--> Enter getSingleResult");
        Object result = null;
        try {
            result = query.getSingleResult();
        } catch (PersistenceException e) {
            checkAndThrowException(e);
        }
        logger.debug("--> Exit getSingleResult");
        return result;
    }

    @Override
    public <T> T load(Class<T> entityClass, Integer primaryKey) throws ObjectNotFoundException {
        logger.debug("--> Enter load " + primaryKey);
        T entity = getEntityManager().find(entityClass, primaryKey);
        if (entity == null) {
            logger.error("--> errore: nessuna istanza esistente per la classe " + entityClass.getName()
                    + " identificata da " + primaryKey);
            throw new ObjectNotFoundException();
        }
        logger.debug("--> Exit load");
        return entity;
    }

    @Override
    public <T> T loadLazy(Class<T> clazz, Integer primaryKey) {
        logger.debug("--> Enter loadLazy");
        T entity = getEntityManager().getReference(clazz, primaryKey);
        logger.debug("--> Exit loadLazy");
        return entity;
    }

    @Override
    public Query prepareNamedQuery(String name) {
        logger.debug("--> Enter prepareNamedQuery");
        Query query = getEntityManager().createNamedQuery(name);
        logger.debug("--> Exit prepareNamedQuery");
        return query;
    }

    @Override
    public Query prepareNamedQueryWithoutFlush(String name) {
        logger.debug("--> Enter prepareNamedQueryWithoutFlush");
        Query query = getEntityManager().createNamedQuery(name);
        query.setFlushMode(FlushModeType.COMMIT);
        logger.debug("--> Exit prepareNamedQueryWithoutFlush");
        return query;
    }

    @Override
    public SQLQuery prepareNativeQuery(String sqlString) {
        return ((Session) getEntityManager().getDelegate()).createSQLQuery(sqlString);
    }

    @Override
    public SQLQuery prepareNativeQuery(String sqlString, Class<?> transformer) {
        SQLQuery query = ((Session) getEntityManager().getDelegate()).createSQLQuery(sqlString);
        query.setResultTransformer(new AliasToBeanNestedResultTransformer(transformer));
        return query;
    }

    @Override
    public Query prepareQuery(String qlString) {
        logger.debug("--> Enter prepareQuery");
        Query query = getEntityManager().createQuery(qlString);
        logger.debug("--> Exit prepareQuery");
        return query;
    }

    @Override
    public Query prepareQuery(String qlString, Class<?> classTransformer, List<String> alias) {
        Query query = prepareQuery(qlString);
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(new AliasToBeanNestedResultTransformer(classTransformer));
        if (alias != null) {
            for (String valoreAlias : alias) {
                ((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar(valoreAlias);
            }
        }
        return query;
    }

    @Override
    public Query prepareQueryWithoutFlush(String qlString) {
        logger.debug("--> Enter prepareQueryWithoutFlush");
        Query query = getEntityManager().createQuery(qlString);
        query.setFlushMode(FlushModeType.COMMIT);
        logger.debug("--> Exit prepareQueryWithoutFlush");
        return query;
    }

    @Override
    public Query prepareSQLQuery(String sqlString, Class<?> classTransformer, List<String> alias) {
        Query query = getEntityManager().createNativeQuery(sqlString);
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(new AliasToBeanNestedResultTransformer(classTransformer));
        if (alias != null) {
            for (String valoreAlias : alias) {
                ((SQLQuery) ((QueryImpl) query).getHibernateQuery()).addScalar(valoreAlias);
            }
        }
        return query;
    }

    @Override
    public <T> T save(T entity) throws DAOException {
        logger.debug("--> Enter save");
        T entityMerge = null;
        try {
            entityMerge = getEntityManager().merge(entity);
            getEntityManager().flush();
        } catch (PersistenceException e) {
            logger.error("--> errore nel salvare l'entità  " + entity, e);
            checkAndThrowException(e);
        }
        logger.debug("--> Exit save");
        return entityMerge;
    }

    @Override
    public <T> T saveWithoutFlush(T entity) throws DAOException {
        logger.debug("--> Enter saveWithoutFlush");
        T entityMerge = getEntityManager().merge(entity);
        logger.debug("--> Exit saveWithoutFlush");
        return entityMerge;
    }

}
