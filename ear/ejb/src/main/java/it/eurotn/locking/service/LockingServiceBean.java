/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.eurotn.locking.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.Query;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.locking.DefaultLock;
import it.eurotn.locking.ILock;
import it.eurotn.locking.exception.LockFoundException;
import it.eurotn.locking.exception.LockNotFoundException;
import it.eurotn.locking.exception.LockStaleObjectException;
import it.eurotn.locking.repository.ILockRepository;
import it.eurotn.locking.repository.MapLockRepository;
import it.eurotn.locking.service.interfaces.LockingServiceRemote;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 *
 * @author adriano
 */
@Stateless(name = "Panjea.LockingService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.LockingService")
public class LockingServiceBean implements LockingServiceRemote {

	private static Logger logger = Logger.getLogger(LockingServiceBean.class);

	@Resource
	private SessionContext sessionContext;

	@EJB
	private PanjeaDAO panjeaDAO;
	private ILockRepository lockRepository;

	/**
	 * Costruttore.
	 */
	public LockingServiceBean() {
		lockRepository = getLockRepository();
	}

	/**
	 * Controllo se la versione dell'oggetto è l'ultima sul database. NB:Il
	 * metodo non � elegante e prevede che tutti i record abbiano un campo ID e
	 * VERSIONE.
	 *
	 * Estrae il sessionfactory dal JNDI
	 *
	 * @param newLock
	 *            lock
	 * @return true se l'ggetto che cerco di bloccare ha la stessa versione
	 *         dell'oggetto sul server.
	 * @throws NamingException
	 */
	private boolean checkVersion(DefaultLock newLock) {
		logger.debug("--> Enter checkVersion");
		try {
			Class<?> objectClass = Class.forName(newLock.getClassObj());
			Table annotation = objectClass.getAnnotation(Table.class);
			if (annotation != null) {
				return checkVersionEjb3(newLock, annotation);
			} else {
				logger.debug("--> oggetto non sottoposto a controllo di versione ");
				return true;
			}
		} catch (ClassNotFoundException e) {
			logger.error("--> Non trovo la classe " + newLock.getClassObj());
			throw new RuntimeException(e);
		}
	}

	/**
	 * verifica la versione dell'oggetto se questo è stato deployato per ejb3.
	 * <br/>
	 * Crea una nuova connessione direttamente dal dataSource
	 *
	 * @param newLock
	 *            lock da controllare
	 * @param table
	 *            annotation del bean.
	 * @return true se l'ggetto che cerco di bloccare ha la stessa versione
	 *         dell'oggetto sul server.
	 */
	private boolean checkVersionEjb3(DefaultLock newLock, Table table) {
		logger.debug("--> Enter checkVersionEjb3");
		StringBuilder sb = new StringBuilder("Select id from ");
		sb.append(table.name());
		sb.append(" WHERE id=");
		sb.append(newLock.getKeyObj());
		sb.append(" AND version=");
		sb.append(newLock.getVersion());

		try {
			Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
			List<?> result = query.getResultList();
			if (result.size() == 0) {
				logger.debug("--> Nessun record trovato per versione e id ");
				logger.debug("--> Exit checkVersionEjb3 con valore false");
				return false;
			}
		} catch (Exception exception) {
			logger.error("--> errore, impossibile eseguire la query", exception);
			throw new RuntimeException(exception);
		}

		logger.debug("--> Exit checkVersionEjb3 con valore true");
		return true;
	}

	/**
	 *
	 * @return principal loggato
	 */
	private JecPrincipal getJecPrincipal() {
		return (JecPrincipal) sessionContext.getCallerPrincipal();
	}

	@Override
	public Collection<ILock> getLockAll() {
		logger.debug("--> Restituisco la lista di tutti i lock");
		List<ILock> listLocks = new ArrayList<ILock>();
		Collection<ILock> locks = lockRepository.getLocks();
		ILock currentLock = null;
		for (Iterator<?> iter = locks.iterator(); iter.hasNext();) {
			currentLock = (ILock) iter.next();
			listLocks.add(currentLock);
		}
		return listLocks;
	}

	@Override
	public List<ILock> getLockByLoggedUser() {
		return getLockByUser(getJecPrincipal().getUserName());
	}

	@Override
	public List<ILock> getLockByUser(String userName) {
		List<ILock> listLocks = new ArrayList<ILock>();
		logger.debug("--> Recupero la lista dei lock");
		Collection<ILock> locks = lockRepository.getLocks();
		ILock currentLock = null;

		for (Iterator<ILock> iter = locks.iterator(); iter.hasNext();) {
			currentLock = iter.next();
			if (currentLock.getUserName().equals(userName)) {
				logger.debug("--> Aggiunto alla lista il lock " + currentLock);
				listLocks.add(currentLock);
			}
		}
		logger.debug("--> Restituisco la lista di lock dell'utente [" + userName + "]");
		return listLocks;

	}

	/**
	 * @return repository per i lock.
	 */
	protected ILockRepository getLockRepository() {
		return new MapLockRepository();
	}

	@Override
	public ILock lock(String classObj, Object keyObj, Integer versione)
			throws LockFoundException, LockStaleObjectException {
		return lock(getJecPrincipal().getUserName(), getJecPrincipal().getCodiceAzienda(), classObj, keyObj, versione);
	}

	@Override
	public ILock lock(String userName, String codiceAzienda, String classObj, Object keyObj, Integer versione)
			throws LockFoundException, LockStaleObjectException {
		logger.debug("--> Enter lock");

		DefaultLock newLock = new DefaultLock(classObj, keyObj, userName, codiceAzienda, versione);
		logger.debug("--> Creato il lock " + newLock);

		if (versione != null) {
			if (!checkVersion(newLock)) {
				throw new LockStaleObjectException();
			}
		}

		logger.debug("--> Cerco il lock nella Map");
		ILock searchLock = lockRepository.getLock(newLock);

		if (searchLock == null) {
			logger.debug("--> Lock non trovato. Posso bloccare il documento con il lock " + newLock);
			lockRepository.writeLock(newLock);

			return newLock;
		} else {
			if (searchLock.getUserName().equals(newLock.getUserName())) {
				logger.warn("--> L'utente ha richiesto un documento già bloccato da se stesso. Ritorno lo stesso lock"
						+ searchLock);
				return searchLock;
			} else {
				logger.debug("--> Lock già creato da un'altro utente, rilancio una LockFoundException");
				throw new LockFoundException(searchLock);
			}
		}

	}

	@Override
	public void release(ILock lock) throws LockNotFoundException, LockFoundException {

		ILock searchLock = lockRepository.getLock(lock);

		if (searchLock != null) {
			if (searchLock.getUserName().equals(lock.getUserName())) {
				logger.debug("--> Rimuovo il lock " + lock);
				lockRepository.removeLock(lock);
			} else {
				throw new LockFoundException(searchLock);
			}
		} else {
			logger.warn("--> Lock non trovato, rilancio una LockNotFoundException");
			throw new LockNotFoundException();
		}
	}

	@Override
	public void releaseAll() {
		logger.debug("--> Rimuovo tutti i lock dalla Map");
		lockRepository.clearLocks();
	}

	@Override
	public void releaseByUser(String userName) {
		logger.debug("--> Enter releaseByUser");
		Collection<ILock> listLock = lockRepository.getLocks();
		for (Iterator<ILock> iter = listLock.iterator(); iter.hasNext();) {
			ILock lock = iter.next();
			if (lock.getUserName().equals(userName)) {
				logger.debug("--> Rimuovo il lock " + lock);
				lockRepository.removeLock(lock);
			}
		}
		logger.debug("--> Exit releaseByUser");
	}

}
