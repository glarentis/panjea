/**
 * 
 */
package it.eurotn.panjea.magazzino.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.manager.interfaces.ScontiManager;
import it.eurotn.panjea.magazzino.service.exception.ScontoNotValidException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * Session Bean che si occupa di gestire il CRUD della classe <code>Sconto</code>.
 * 
 * @author fattazzo
 */
@Stateless(name = "Panjea.ScontiManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.ScontiManager")
public class ScontiManagerBean implements ScontiManager {

	private static Logger logger = Logger.getLogger(ScontiManagerBean.class);

	@Resource
	private SessionContext sessionContext;

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void cancellaSconto(Sconto sconto) {
		logger.debug("--> Enter cancellaSconto");

		try {
			panjeaDAO.delete(sconto);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione dello sconto", e);
			throw new RuntimeException("Errore durante la cancellazione dello sconto", e);
		}

		logger.debug("--> Exit cancellaSconto");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sconto> caricaSconti() {
		logger.debug("--> Enter caricaSconti");

		Query query = panjeaDAO.prepareNamedQuery("Sconto.caricaAll");
		query.setParameter("paramCodiceAzienda", getAzienda());

		List<Sconto> list = new ArrayList<Sconto>();
		try {
			list = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento degli sconti per l'azienda " + getAzienda(), e);
			throw new RuntimeException("Errore durante il caricamento degli sconti per l'azienda " + getAzienda(), e);
		}

		logger.debug("--> Exit caricaSconti");
		return list;
	}

	@Override
	public Sconto caricaSconto(Sconto sconto) {
		logger.debug("--> Enter caricaSconto");

		Sconto scontoCaricato = null;
		try {
			scontoCaricato = panjeaDAO.load(Sconto.class, sconto.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> Errore durante il caricamento dello sconto " + sconto.getId(), e);
			throw new RuntimeException("Errore durante il caricamento dello sconto " + sconto.getId(), e);
		}

		logger.debug("--> Exit caricaSconto");
		return scontoCaricato;
	}

	/**
	 * @return codice azienda dell'utente loggato
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sconto> ricercaSconti(String codiceSconto) {
		logger.debug("--> Enter ricercaSconti");

		List<Sconto> listSconti = new ArrayList<Sconto>();
		if (codiceSconto == null || codiceSconto.isEmpty()) {
			logger.debug("--> Carico tutti gli sconti dell'azienda");
			listSconti = caricaSconti();
		} else {
			logger.debug("--> Carico tutti gli sconti che iniziano con " + codiceSconto);

			Map<String, Object> valueParametri = new TreeMap<String, Object>();
			StringBuffer queryHQL = new StringBuffer(" select s from Sconto s ");
			StringBuffer whereHQL = new StringBuffer(" where s.codiceAzienda = :paramCodiceAzienda ");
			valueParametri.put("paramCodiceAzienda", getAzienda());

			whereHQL.append(" and s.codice like :paramDefaultCodice ");
			valueParametri.put("paramDefaultCodice", codiceSconto + "%");

			Query query = panjeaDAO.getEntityManager().createQuery(queryHQL.toString() + whereHQL.toString());
			Set<String> set = valueParametri.keySet();
			for (String key : set) {
				query.setParameter(key, valueParametri.get(key));
			}
			try {
				listSconti = panjeaDAO.getResultList(query);
			} catch (DAOException e) {
				logger.error("--> Errore durante la ricerca degli sconti", e);
				throw new RuntimeException("Errore durante la ricerca degli sconti", e);
			}
		}

		logger.debug("--> Exit ricercaSconti");
		return listSconti;
	}

	@Override
	public Sconto salvaSconto(Sconto sconto) throws ScontoNotValidException {
		logger.debug("--> Enter salvaSconto");

		// controllo che gli sconti siano settati in maniera corretta.
		// Gli sconti precedenti devono essere sempre avvalorati.
		if (!sconto.isValid()) {
			throw new ScontoNotValidException("Lo sconto non è valido.");
		}

		// se sto salvando un nuovo sconto setto l'azienda
		if (sconto.isNew()) {
			sconto.setCodiceAzienda(getAzienda());
		}

		try {
			sconto = panjeaDAO.save(sconto);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio dello sconto.", e);
			throw new RuntimeException("Errore durante il salvataggio dello sconto.", e);
		}

		logger.debug("--> Exit salvaSconto");
		return sconto;
	}
}
