package it.eurotn.panjea.service.interfaces;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.entity.EntityBase;
import it.eurotn.security.JecPrincipal;

import java.security.Principal;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SQLQuery;

@Local
public interface PanjeaDAO {

	/**
	 * Cancellazione di <code>entity</code>.
	 *
	 * @param entity
	 *            entity da cancellare
	 * @throws DAOException
	 *             eccezione generica
	 */
	void delete(EntityBase entity) throws DAOException;

	/**
	 *
	 * @param entity
	 *            entità da rimuovere dalla session
	 */
	<T> void evict(T entity);

	/**
	 *
	 * @param query
	 *            quert da eseguire
	 * @return n. record interessati
	 * @throws DAOException
	 *             eccezione generica
	 */
	int executeQuery(Query query) throws DAOException;

	/**
	 * Recupera dal contesto (es. {@link SessionContext} il {@link Principal} corrente Se viene utilizzato
	 * JecDatabaseLoginModule viene restituito
	 *
	 * @return principal
	 */
	JecPrincipal getCurrentPrincipal();

	/**
	 *
	 * @return entity manager utilizzato dal DAO
	 *
	 */
	EntityManager getEntityManager();

	/**
	 *
	 * @return nome jndi del datasource corrente (dipendente dal jecPrincipal loggato)
	 */
	String getJndiCurrentDataSource();

	/**
	 * Esecuzione di {@link Query} e restituzione di {@link List} come risultato, in caso di assenza di risultato viene
	 * restituita una {@link List} vuota. il parametro {@link Query} deve essere gi� preparata e valorizzato dai
	 * parametri
	 *
	 * @param query
	 *            query da eseguire
	 * @return risultati della query
	 * @throws DAOException
	 *             eccezione generica
	 */
	@SuppressWarnings("rawtypes")
	List getResultList(Query query) throws DAOException;

	/**
	 * Esecuzione di {@link Query} e restituzione del singolo risultato, in caso di assenza di risultato viene
	 * restituito <code>null</code>. il parametro {@link Query} deve essere gi� preparata e valorizzato dai parametri
	 *
	 * @param query
	 *            query da eseguire
	 * @return oggetto caricato
	 * @throws DAOException
	 *             eccezione generica
	 */
	Object getSingleResult(Query query) throws DAOException;

	/**
	 * Caricamento dal lato di persistenza dell'istanza di <code>clazz</code> identificata da <code>primaryKey</code>.
	 *
	 * @param entityClass
	 *            classe dell'oggetto da caricare
	 * @param primaryKey
	 *            chiave dell'entity
	 * @return entity caricata
	 * @param <T>
	 *            tipo della classe
	 * @throws ObjectNotFoundException
	 *             rilanciata se un'entity con chiave primaryKey non è stata trovata.
	 */
	<T> T load(Class<T> entityClass, Integer primaryKey) throws ObjectNotFoundException;

	/**
	 * Caricamento lazy dal lato di persistenza dell'istanza di <code>clazz</code> idetificata da
	 * <code>primaryKey</code>.
	 *
	 * @param entityClass
	 *            classe dell'oggetto da caricare
	 * @param primaryKey
	 *            chiave dell'entity
	 * @param <T>
	 *            tipo della classe
	 * @return entity caricata
	 */
	<T> T loadLazy(Class<T> entityClass, Integer primaryKey);

	/**
	 * Preparazione di <code>Query</code> mappata da <code>name</code>.
	 *
	 * @param name
	 *            nome della query
	 * @return query con nome name
	 */
	Query prepareNamedQuery(String name);

	/**
	 * Preparazione di <code>Query</code> mappata da <code>name</code>.<br/>
	 * All'esecuzione della query non esegue il flush della sessione.
	 *
	 * @param name
	 *            nome della query
	 * @return query con nome name
	 */
	Query prepareNamedQueryWithoutFlush(String name);

	/**
	 * Prepara una query nativa con la possibilità di un transformer.
	 *
	 * @param sqlString
	 *            stringa sql nativo
	 * @return query
	 */
	SQLQuery prepareNativeQuery(String sqlString);

	/**
	 * Prepara una query nativa con la possibilità di un transformer.
	 *
	 * @param sqlString
	 *            stringa sql nativo
	 * @param transformer
	 *            bean da restituire con il risultato
	 * @return query con il transformer applicato
	 */
	SQLQuery prepareNativeQuery(String sqlString, Class<?> transformer);

	/**
	 * Preparazione di <code>Query</code> hql.
	 *
	 * @param qlString
	 *            stringa hql
	 * @return query preparata in hql
	 */
	Query prepareQuery(String qlString);

	/**
	 * Preparazione di <code>Query</code> hql.
	 *
	 * @param qlString
	 *            stringa hql
	 * @param classTransformer
	 *            classe da creare come risultato
	 * @param alias
	 *            alias da associare alla query,NULL se non utilizzato
	 * @return query preparata in hql con un bean transformer associato
	 */
	Query prepareQuery(String qlString, Class<?> classTransformer, List<String> alias);

	/**
	 * Preparazione di <code>Query</code> ed impostazione della sua esecuzione senza il flush della sessione.
	 *
	 * @param qlString
	 *            stringa hql
	 * @return query preparata in hql
	 */
	Query prepareQueryWithoutFlush(String qlString);

	/**
	 * Preparazione di <code>Query</code> hql.
	 *
	 * @param sqlString
	 *            stringa hql
	 * @param classTransformer
	 *            classe da creare come risultato
	 * @param alias
	 *            alias da associare alla query,NULL se non utilizzato
	 * @return query preparata in hql con un bean transformer associato
	 */
	Query prepareSQLQuery(String sqlString, Class<?> classTransformer, List<String> alias);

	/**
	 * Salvataggio di <code>entity</code>..
	 *
	 * @param entity
	 *            entità da salvare
	 * @param <T>
	 *            tipo dell'entità
	 * @return entità salvata
	 * @throws DAOException
	 *             eccezione generica
	 */
	<T> T save(T entity) throws DAOException;

	/**
	 * Salvataggio di <code>entity</code> senza eseguire il flush della sessione.
	 *
	 * @param entity
	 *            entità da salvare
	 * @param <T>
	 *            tipo dell'entità
	 * @return entità salvata
	 * @throws DAOException
	 *             eccezione generica
	 */
	<T> T saveWithoutFlush(T entity) throws DAOException;
}