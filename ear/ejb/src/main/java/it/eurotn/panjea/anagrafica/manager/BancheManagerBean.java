package it.eurotn.panjea.anagrafica.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.manager.interfaces.BancheManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.BancheManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.BancheManager")
public class BancheManagerBean implements BancheManager {

	private static Logger logger = Logger.getLogger(BancheManagerBean.class);
	private static final long serialVersionUID = -805463302057222970L;
	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void cancellaBanca(Banca banca) throws AnagraficaServiceException {
		logger.debug("--> Enter cancellaBanca");
		if (banca.isNew()) {
			logger.error("--> Impossibile cancellare la banca, id nullo.");
			throw new AnagraficaServiceException("Impossibile cancellare la banca, id nullo.");
		}
		try {
			panjeaDAO.delete(banca);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaBanca");
	}

	@Override
	public void cancellaFiliale(Filiale filiale) throws AnagraficaServiceException {
		logger.debug("--> Enter cancellaFiliale");
		if (filiale.isNew()) {
			logger.error("--> errore, impossibile cancellare filiale: id null ");
			throw new AnagraficaServiceException("Impossibile eliminare filiale: id null ");
		}
		try {
			panjeaDAO.delete(filiale);
		} catch (VincoloException e) {
			logger.error("--> Impossibile eliminare la filiale con id = " + filiale.getId(), e);
			throw new RuntimeException(e);
		} catch (DAOException e) {
			logger.error("--> Impossibile eliminare la filiale con id = " + filiale.getId(), e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public Banca caricaBanca(Integer idBanca) throws AnagraficaServiceException {
		logger.debug("--> Enter caricaBanca");
		Banca banca = null;
		try {
			banca = panjeaDAO.load(Banca.class, idBanca);
		} catch (ObjectNotFoundException e) {
			logger.error("--> Errore nel caricare la banca", e);
			throw new AnagraficaServiceException("Errore nel caricare la banca", e);
		}
		logger.debug("--> Exit caricaBanca");
		return banca;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Banca> caricaBanche(String fieldSearch, String valueSearch) throws AnagraficaServiceException {
		logger.debug("--> Enter caricaBanche");
		List<Banca> banche = null;
		StringBuilder sb = new StringBuilder("select b from Banca b ");
		if (valueSearch != null) {
			sb.append(" where b.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		sb.append(" order by b.");
		sb.append(fieldSearch);
		Query query = panjeaDAO.prepareQuery(sb.toString());
		try {
			banche = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore in getresultList caricaBanche", e);
			throw new AnagraficaServiceException(e.getMessage());
		}
		logger.debug("--> Exit caricaBanche");
		return banche;
	}

	@Override
	public Filiale caricaFiliale(Integer idFiliale) throws AnagraficaServiceException {
		logger.debug("--> Enter caricaFiliale");

		Filiale filiale = null;
		try {
			filiale = panjeaDAO.load(Filiale.class, idFiliale);
		} catch (ObjectNotFoundException e) {
			logger.error("--> Errore nel caricare la filiale", e);
			throw new AnagraficaServiceException("Errore nel caricare la filiale", e);
		}
		logger.debug("--> Exit caricaFiliale");
		return filiale;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Filiale> caricaFiliali() throws AnagraficaServiceException {
		logger.debug("--> Enter caricaFiliali");
		Query query = panjeaDAO.prepareNamedQuery("Filiale.caricaAll");
		List<Filiale> filiali;
		try {
			filiali = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile recuperare la Lista di filiali", e);
			throw new AnagraficaServiceException(e);
		}
		return filiali;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Filiale> caricaFiliali(Banca banca, String fieldSearch, String valueSearch)
			throws AnagraficaServiceException {
		logger.debug("--> Enter caricaFiliali");
		List<Filiale> filiali;
		Map<String, Object> parameters = new HashMap<String, Object>();

		StringBuilder sb = new StringBuilder("from Filiale f left join fetch f.cap inner join fetch f.banca  ");
		if ((banca != null) && (banca.getId() != null)) {
			sb.append("where f.banca = :paramBanca ");
			parameters.put("paramBanca", banca);
		}
		if (valueSearch != null) {
			sb.append(parameters.isEmpty() ? " where " : " and ");
			sb.append(" f.").append(fieldSearch).append(" like :paramValue");
			parameters.put("paramValue", valueSearch);
		}
		sb.append(" order by f.");
		sb.append(fieldSearch);
		Query query = panjeaDAO.prepareQuery(sb.toString());
		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		try {
			filiali = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile recuperare la Lista di filiali", e);
			throw new AnagraficaServiceException(e);
		}
		return filiali;
	}

	@Override
	public Banca salvaBanca(Banca banca) {
		logger.debug("--> Enter salvaBanca");
		Banca bancaSave = null;

		try {
			bancaSave = panjeaDAO.save(banca);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaBanca");
		return bancaSave;
	}

	@Override
	public Filiale salvaFiliale(Filiale filiale) {
		logger.debug("--> Enter salvaFiliale");

		Filiale filialeSave = null;
		try {
			filialeSave = panjeaDAO.save(filiale);
		} catch (DAOException e1) {
			throw new RuntimeException(e1);
		}

		logger.debug("--> Exit salvaFiliale");
		return filialeSave;
	}
}
