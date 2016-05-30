package it.eurotn.panjea.anagrafica.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Contatto;
import it.eurotn.panjea.anagrafica.domain.ContattoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Mansione;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.manager.interfaces.ContattiManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.ContattoOrphanException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * SessionBean Manager dei {@link Contatto} associati alle {@link SedeEntita}.
 * 
 * @author adriano
 * @version 1.0, 17/dic/07
 */
@Stateless(name = "Panjea.ContattiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ContattiManager")
public class ContattiManagerBean implements ContattiManager {

	private static Logger logger = Logger.getLogger(ContattiManagerBean.class);
	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void cancellaContattiPerEntita(Entita entita) {
		logger.debug("--> Enter cancellaContattiPerEntita");
		List<ContattoSedeEntita> list;
		try {
			list = caricaContattiSedeEntitaPerEntita(entita);
		} catch (AnagraficaServiceException e1) {
			logger.error("--> errore impossibile recuperare mansioni sede ", e1);
			throw new RuntimeException(e1);
		}
		for (ContattoSedeEntita contattoSedeEntita : list) {
			logger.debug("--> cancellazione di " + contattoSedeEntita.getId());
			try {
				cancellaContattoSedeEntita(contattoSedeEntita);
			} catch (Exception e) {
				logger.error("--> cancellaContattoSedeEntita ", e);
				throw new RuntimeException(e);
			}
		}
		logger.debug("--> Exit cancellaContattiPerEntita");
	}

	@Override
	public void cancellaContattiPerSedeEntita(SedeEntita sedeEntita) {
		logger.debug("--> Enter cancellaContattiPerSedeEntita");
		List<ContattoSedeEntita> list;
		try {
			list = caricaContattiSedeEntitaPerSedeEntita(sedeEntita);
		} catch (AnagraficaServiceException e1) {
			logger.error("--> errore, impopssibile recuperare Mansioni sede id SedeEntita  " + sedeEntita.getId(), e1);
			throw new RuntimeException(e1);
		}
		for (ContattoSedeEntita contattoSedeEntita : list) {
			try {
				logger.debug("--> cancellazione di " + contattoSedeEntita.getId());
				cancellaContattoSedeEntita(contattoSedeEntita);
			} catch (Exception e) {
				logger.error("--> cancellaContattoSedeEntita ", e);
				throw new RuntimeException(e);
			}
		}
		logger.debug("--> Exit cancellaContattiPerSedeEntita");

	}

	@Override
	public void cancellaContatto(Contatto contatto) {
		logger.debug("--> Enter cancellaContatto");
		try {
			panjeaDAO.delete(contatto);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaContatto");

	}

	@Override
	public void cancellaContattoSedeEntita(ContattoSedeEntita contattoSedeEntita) throws AnagraficaServiceException,
			ContattoOrphanException {
		logger.debug("--> Enter cancellaContattoSedeEntita");

		if (contattoSedeEntita.isNew()) {
			logger.error("--> id mansione null ");
			throw new AnagraficaServiceException("Impossibile cancellare la mansione sede, identificativo null ");
		}

		try {
			logger.debug("--> delete ContattoSedeEntita id " + contattoSedeEntita.getId());
			panjeaDAO.delete(contattoSedeEntita);
			logger.debug("--> verifica se il contatto � orfano ");
			if (isContattoOrphan(contattoSedeEntita.getContatto().getId())) {
				logger.debug("--> eliminazione contatto " + contattoSedeEntita.getContatto().getId());
				cancellaContatto(contattoSedeEntita.getContatto());
			}
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<Contatto> caricaContattiPerEntita(Entita entita) {
		logger.debug("--> Enter caricaContattiPerEntita");
		Map<String, Object> parametri = new TreeMap<String, Object>();
		List<Contatto> contatti = ricercaContatti(entita, parametri);
		logger.debug("--> Exit caricaContattiPerEntita");
		return contatti;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ContattoSedeEntita> caricaContattiSedeEntitaPerEntita(Entita entita) throws AnagraficaServiceException {
		logger.debug("--> Enter caricaContattiSedeEntitaPerEntita");
		Query query = panjeaDAO.prepareNamedQuery("ContattoSedeEntita.caricaPerEntita");
		query.setParameter("paramIdEntita", entita.getId());
		List<ContattoSedeEntita> contatti;
		try {
			contatti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile recuperare mansioni sede per entita ", e);
			throw new AnagraficaServiceException(e);
		}
		logger.debug("--> Exit caricaContattiSedeEntitaPerEntita");
		return contatti;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ContattoSedeEntita> caricaContattiSedeEntitaPerSedeEntita(SedeEntita sedeEntita)
			throws AnagraficaServiceException {
		logger.debug("--> Enter caricaContattiSedeEntitaPerSedeEntita");
		Query query = panjeaDAO.prepareNamedQuery("ContattoSedeEntita.caricaPerSedeEntita");
		query.setParameter("paramIdSedeEntita", sedeEntita.getId());
		List<ContattoSedeEntita> contatti;
		try {
			contatti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore, impossibile recuperare mansioni sede per entita ", e);
			throw new AnagraficaServiceException(e);
		}
		return contatti;
	}

	@Override
	public Contatto caricaContatto(Integer idContatto) throws AnagraficaServiceException {
		logger.debug("--> Enter caricaContatto");
		Contatto contatto;
		try {
			contatto = panjeaDAO.load(Contatto.class, idContatto);
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore, impossibile recuperare Contatto identificato da  " + idContatto, e);
			throw new AnagraficaServiceException(e);
		}
		return contatto;
	}

	@Override
	public ContattoSedeEntita caricaContattoSedeEntita(Integer idMansioneSede) {
		logger.debug("--> Enter caricaContattoSedeEntita");
		ContattoSedeEntita contattoSedeEntita = null;
		try {
			contattoSedeEntita = panjeaDAO.load(ContattoSedeEntita.class, idMansioneSede);
		} catch (Exception e) {
			logger.error("-->errore nel caricare il contattoSedeEntita", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaContattoSedeEntita");
		return contattoSedeEntita;
	}

	/**
	 * Verifica le associazioni di <code>Contatto</code> identificato da idContatto.
	 * 
	 * @param idContatto
	 *            id del contatto
	 * @return boolean true se il contatto isOrphan
	 * @throws DAOException
	 *             eccezione generica
	 */
	private boolean isContattoOrphan(Integer idContatto) throws DAOException {
		logger.debug("--> Enter isContattoOrphan");
		Map<String, Object> parametri = new TreeMap<String, Object>();
		parametri.put(Contatto.PROP_ID, idContatto);
		List<Contatto> list;
		list = ricercaContatti(parametri);
		logger.debug("--> Exit isContattoOrphan return: " + (list.size() == 0));
		return list.size() == 0;

	}

	/**
	 * Esegue la ricerca dei contatti attraverso i seguenti parametri contentuti nella <code>Map</code> parametri:
	 * 
	 * <code>Contatto.PROP_ID</code> <code>Contatto.PROP_EMAIL</code> <code>Contatto.PROP_INTERNO</code>
	 * <code>Contatto.PROP_COGNOME</code> <code>Contatto.PROP_NOME</code>
	 * <code>ContattoSedeEntita.PROP_MANSIONE.PROP_CODICE</code>
	 * <code>ContattoSedeEntita.PROP_MANSIONE.PROP_DESCRIZIONE</code>.
	 * 
	 * @param entita
	 *            entita di riferimento
	 * @param parametri
	 *            parametri di ricerca
	 * @return contatti trovati
	 */
	@SuppressWarnings("unchecked")
	private List<Contatto> ricercaContatti(Entita entita, Map<String, Object> parametri) {
		logger.debug("--> Enter RicercaContatti");

		Session hibernateSession = (Session) panjeaDAO.getEntityManager().getDelegate();
		Criteria criteria = hibernateSession.createCriteria(Contatto.class);

		Object value = parametri.get(Contatto.PROP_ID);
		if (value != null) {
			logger.debug("--> aggiunto parametro " + Contatto.PROP_ID + " value " + value);
			criteria.add(Restrictions.eq(Contatto.PROP_ID, value));
		}

		value = parametri.get(Contatto.PROP_EMAIL);
		if (value != null) {
			logger.debug("--> aggiunto parametro " + Contatto.PROP_EMAIL + " value " + value);
			criteria.add(Restrictions.like(Contatto.PROP_EMAIL, value));
		}
		value = parametri.get(Contatto.PROP_INTERNO);
		if (value != null) {
			logger.debug("--> aggiunto parametro " + Contatto.PROP_INTERNO + " value " + value);
			criteria.add(Restrictions.like(Contatto.PROP_INTERNO, value));
		}
		value = parametri.get(Contatto.PROP_COGNOME);
		if (value != null) {
			logger.debug("--> aggiunto parametro " + Contatto.PROP_COGNOME + " value " + value);
			criteria.add(Restrictions.like(Contatto.PROP_COGNOME, value));
		}
		value = parametri.get(Contatto.PROP_NOME);
		if (value != null) {
			logger.debug("--> aggiunto parametro " + Contatto.PROP_NOME + " value " + value);
			criteria.add(Restrictions.like(Contatto.PROP_NOME, value));
		}

		criteria = criteria.createCriteria("mansioni");

		value = parametri.get(ContattoSedeEntita.PROP_MANSIONE + "." + Mansione.PROP_DESCRIZIONE);
		if (value != null) {
			criteria.createAlias(ContattoSedeEntita.PROP_MANSIONE, "mansione");
			if (value != null) {
				logger.debug("--> aggiunto parametro " + ContattoSedeEntita.PROP_MANSIONE + " value " + value);
				criteria.add(Restrictions.like("mansione." + Mansione.PROP_DESCRIZIONE, value));
			}
		}

		logger.debug("--> Abilita filtro abilitato");
		criteria = criteria.createCriteria(ContattoSedeEntita.PROP_SEDE_ENTITA).createCriteria(SedeEntita.PROP_ENTITA)
				.add(Restrictions.eq(SedeEntita.PROP_ABILITATO, true));
		if (entita != null) {
			logger.debug("--> filtro entit� " + entita.getId());
			criteria.add(Restrictions.eq(Entita.PROP_ID, entita.getId()));
		}
		logger.debug("--> Exit RicercaContatti");
		return criteria.list();
	}

	@Override
	public List<Contatto> ricercaContatti(Map<String, Object> parametri) {
		logger.debug("--> Enter ricercaContatti");
		List<Contatto> contatti = ricercaContatti(null, parametri);
		logger.debug("--> Exit ricercaContatti");
		return contatti;
	}

	@Override
	public List<Contatto> ricercaContattiPerEntita(Entita entita, Map<String, Object> parametri) {
		logger.debug("--> Enter ricercaContattiPerEntita");
		List<Contatto> contattiSedeEntita = ricercaContatti(entita, parametri);
		logger.debug("--> Exit ricercaContattiPerEntita");
		return contattiSedeEntita;
	}

	@Override
	public Contatto salvaContatto(Contatto contatto) {
		logger.debug("--> Enter salvaContatto");
		Contatto contattoSave;
		try {
			contattoSave = panjeaDAO.save(contatto);
			logger.debug("--> Exit salvaContatto");
			return contattoSave;
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ContattoSedeEntita salvaContattoSedeEntita(ContattoSedeEntita contattoSedeEntita) {
		logger.debug("--> Enter salvaContattoSedeEntita");
		ContattoSedeEntita contattoSedeEntitaSave;
		// esegue prima il salvataggio di Contatto
		Contatto contattoSave = salvaContatto(contattoSedeEntita.getContatto());
		contattoSedeEntita.setContatto(contattoSave);
		try {
			logger.debug("--> salva ContattoSedeEntita " + contattoSedeEntita);
			contattoSedeEntitaSave = panjeaDAO.save(contattoSedeEntita);
			logger.debug("--> Exit salvaContatto");
			return contattoSedeEntitaSave;
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
	}
}
