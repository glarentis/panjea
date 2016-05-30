package it.eurotn.panjea.magazzino.manager.articolo;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.domain.ArticoloDeposito;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ArticoloDepositoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.ArticoloDepositoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ArticoloDepositoManager")
public class ArticoloDepositoManagerBean implements ArticoloDepositoManager {

	private static Logger logger = Logger.getLogger(ArticoloDepositoManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void cancellaArticoloDeposito(ArticoloDeposito articoloDeposito) {
		logger.debug("--> Enter cancellaArticoloDeposito");
		try {
			panjeaDAO.delete(articoloDeposito);
		} catch (Exception e) {
			logger.info("--> errore nel cancellare l' articoloDeposito", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaArticoloDeposito");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArticoloDeposito> caricaArticoliDeposito(Deposito deposito) {
		logger.debug("--> Enter caricaArticoliDeposito");
		Query query = panjeaDAO.prepareNamedQuery("ArticoloDeposito.caricaByDeposito");
		query.setParameter("paramIdDeposito", deposito.getId());
		List<ArticoloDeposito> listResult = new ArrayList<ArticoloDeposito>();
		try {
			listResult = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento degli articolo deposito", e);
			throw new RuntimeException("Errore durante il caricamento degli articolo deposito", e);
		}
		logger.debug("--> Exit caricaArticoliDeposito");
		return listResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArticoloDeposito> caricaArticoliDeposito(Integer idArticolo) {
		logger.debug("--> Enter caricaArticoliDeposito " + idArticolo);
		Query query = panjeaDAO.prepareNamedQuery("ArticoloDeposito.caricaByArticolo");
		query.setParameter("paramIdArticolo", idArticolo);
		List<ArticoloDeposito> listResult = new ArrayList<ArticoloDeposito>();
		try {
			listResult = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento degli articolo deposito", e);
			throw new RuntimeException("Errore durante il caricamento degli articolo deposito", e);
		}
		logger.debug("--> Exit caricaArticoliDeposito");
		return listResult;
	}

	@Override
	public ArticoloDeposito caricaArticoloDeposito(Integer idArticolo, Integer idDeposito) {
		logger.debug("--> Enter caricaArticoloDeposito");

		Query query = panjeaDAO.prepareNamedQuery("ArticoloDeposito.caricaByArticoloDeposito");
		query.setParameter("paramIdArticolo", idArticolo);
		query.setParameter("paramIdDeposito", idDeposito);

		ArticoloDeposito articoloDeposito = null;
		try {
			articoloDeposito = (ArticoloDeposito) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			// non rilancio l'errore perchè significa solo che non esiste un
			// codice articolo per l'entità scelta
			articoloDeposito = null;
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dell'articolo deposito", e);
			throw new RuntimeException("errore durante il caricamento dell'articolo deposito", e);
		}
		logger.debug("--> Exit caricaArticoloDeposito");
		return articoloDeposito;
	}

	@Override
	public ArticoloDeposito salvaArticoloDeposito(ArticoloDeposito articoloDeposito) {
		logger.debug("--> Enter salvaArticoloDeposito");

		ArticoloDeposito articoloDepositoSalvato = null;
		try {
			articoloDepositoSalvato = panjeaDAO.save(articoloDeposito);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio dell' ArticoloDeposito", e);
			throw new RuntimeException("Errore durante il salvataggio dell' ArticoloDeposito", e);
		}
		logger.debug("--> Exit salvaArticoloDeposito");
		return articoloDepositoSalvato;
	}

}
