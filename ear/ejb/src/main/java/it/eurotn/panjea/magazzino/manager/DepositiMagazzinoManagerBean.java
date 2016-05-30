package it.eurotn.panjea.magazzino.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.magazzino.domain.DepositoMagazzino;
import it.eurotn.panjea.magazzino.manager.interfaces.DepositiMagazzinoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

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
@Stateless(name = "Panjea.DepositiMagazzinoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DepositiMagazzinoManager")
public class DepositiMagazzinoManagerBean implements DepositiMagazzinoManager {

	private static Logger logger = Logger.getLogger(DepositiMagazzinoManagerBean.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void cancellaDepositoMagazzino(Integer id) {
		logger.debug("--> Enter cancellaDepositoMagazzino");

		DepositoMagazzino entity = new DepositoMagazzino();
		try {
			entity.setId(id);
			panjeaDAO.delete(entity);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione di  DepositoMagazzino" + entity.getId(), e);
			throw new RuntimeException("Errore durante la cancellazione di  DepositoMagazzino" + entity.getId(), e);
		}
		logger.debug("--> Exit cancellaDepositoMagazzino");
	}

	@Override
	public DepositoMagazzino caricaDepositoMagazzino(Integer id) {
		logger.debug("--> Enter caricaDepositoMagazzino");
		DepositoMagazzino entityResult = null;
		try {
			entityResult = panjeaDAO.load(DepositoMagazzino.class, id);
		} catch (ObjectNotFoundException e) {
			logger.error("--> DepositoMagazzino non trovato. Id: " + id, e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaDepositoMagazzino");
		return entityResult;
	}

	@Override
	public DepositoMagazzino caricaDepositoMagazzinoByDeposito(Deposito deposito) {
		logger.debug("--> Enter caricaDepositoMagazzinoByDeposito");
		DepositoMagazzino depositoMagazzino = null;
		try {
			Query query = panjeaDAO.prepareNamedQuery("DepositoMagazzino.caricaByDeposito");
			query.setParameter("paramIdDeposito", deposito.getId());

			depositoMagazzino = (DepositoMagazzino) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.error("--> DepositoMagazzino non trovato per il deposito con id : " + deposito.getId(), e);
			depositoMagazzino = new DepositoMagazzino();
			depositoMagazzino.setDeposito(deposito);
		} catch (DAOException e) {
			logger.error("--> Errore nel caricare il deposito magazzino per il deposito con id : " + deposito.getId(),
					e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaDepositoMagazzinoByDeposito");
		return depositoMagazzino;
	}

	@Override
	public DepositoMagazzino salvaDepositoMagazzino(DepositoMagazzino depositoMagazzino) {
		logger.debug("--> Enter salvaDepositoMagazzino");

		DepositoMagazzino depositoMagazzinoSalvato = null;
		try {
			depositoMagazzinoSalvato = panjeaDAO.save(depositoMagazzino);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del deposito magazzino.", e);
			throw new RuntimeException("Errore durante il salvataggio del deposito magazzino.", e);
		}

		logger.debug("--> Exit salvaDepositoMagazzino");
		return depositoMagazzinoSalvato;
	}
}
