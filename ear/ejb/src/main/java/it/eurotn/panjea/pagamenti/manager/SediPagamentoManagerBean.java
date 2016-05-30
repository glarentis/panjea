package it.eurotn.panjea.pagamenti.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.pagamenti.domain.SedePagamento;
import it.eurotn.panjea.pagamenti.manager.interfaces.SediPagamentoManager;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
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
@Stateless(name = "Panjea.SediPagamentoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SediPagamentoManager")
public class SediPagamentoManagerBean implements SediPagamentoManager {

	private static Logger logger = Logger.getLogger(SediPagamentoManagerBean.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext sessionContext;

	@Override
	public void cancellaSedePagamento(SedeEntita sedeEntita) {
		SedePagamento sedePagamento = caricaSedePagamentoBySedeEntita(sedeEntita, true);
		if (sedePagamento != null && sedePagamento.getId() != null) {
			cancellaSedePagamento(sedePagamento);
		}
	}

	@Override
	public void cancellaSedePagamento(SedePagamento sedePagamento) {
		logger.debug("--> Enter cancellaSedePagamento");
		try {
			panjeaDAO.delete(sedePagamento);
		} catch (VincoloException e) {
			logger.error("--> errore VincoloException in cancellaSedePagamento", e);
			throw new RuntimeException(e);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in cancellaSedePagamento", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaSedePagamento");
	}

	@Override
	public SedePagamento caricaSedePagamento(SedePagamento sedePagamento) throws PagamentiException {
		logger.debug("--> Enter caricaSedePagamento");
		SedePagamento sedePagamentoLoad = null;
		try {
			sedePagamentoLoad = panjeaDAO.load(SedePagamento.class, sedePagamento.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore ObjectNotFoundException in caricaSedePagamento", e);
			throw new PagamentiException(e);
		}
		return sedePagamentoLoad;
	}

	@Override
	public SedePagamento caricaSedePagamentoBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali) {
		logger.debug("--> Enter caricaSedePagamentoBySedeEntita");
		SedePagamento sedePagamentoLoad = null;
		try {
			sedeEntita = panjeaDAO.load(SedeEntita.class, sedeEntita.getId());
		} catch (Exception e1) {
			logger.error("-->errore nel caricare la sedeentita con id " + sedeEntita.getId(), e1);
			throw new RuntimeException(e1);
		}

		if (sedeEntita.isEreditaDatiCommerciali() && !ignoraEreditaDatiCommerciali) {
			EntitaLite entitaLite = new ClienteLite();
			entitaLite.setId(sedeEntita.getEntita().getId());
			sedePagamentoLoad = caricaSedePagamentoPrincipaleEntita(entitaLite);
		} else {
			Query query = panjeaDAO.prepareNamedQuery("SedePagamento.caricaBySedeEntita");
			query.setParameter("paramIdSedeEntita", sedeEntita.getId());
			try {
				sedePagamentoLoad = (SedePagamento) panjeaDAO.getSingleResult(query);
			} catch (ObjectNotFoundException e) {
				logger.debug("--> ObjectNotFoundException in caricaSedePagamentoBySedeEntita");
				sedePagamentoLoad = new SedePagamento();
				sedePagamentoLoad.setSedeEntita(sedeEntita);
			} catch (DAOException e) {
				logger.error("--> errore DAOException in caricaSedePagamentoBySedeEntita", e);
				throw new RuntimeException(e);
			}
		}
		logger.debug("--> Exit caricaSedePagamentoBySedeEntita");
		return sedePagamentoLoad;
	}

	@Override
	public SedePagamento caricaSedePagamentoPrincipaleEntita(EntitaLite entitaLite) {
		logger.debug("--> Enter caricaSedePagamentoBySedeEntita");
		SedePagamento sedePagamentoPrincipale = null;
		Query query = panjeaDAO.prepareNamedQuery("SedePagamento.caricaPrincipaleByEntita");
		query.setParameter("paramIdEntita", entitaLite.getId());
		try {
			sedePagamentoPrincipale = (SedePagamento) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> ObjectNotFoundException in caricaSedePagamentoBySedeEntita");
			sedePagamentoPrincipale = new SedePagamento();
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaSedePagamentoBySedeEntita", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaSedePagamentoBySedeEntita");
		return sedePagamentoPrincipale;
	}

	@Override
	public SedePagamento salvaSedePagamento(SedePagamento sedePagamento) {
		logger.debug("--> Enter salvaSedePagamento");
		SedePagamento sedePagamentoSave;
		try {
			sedePagamentoSave = panjeaDAO.save(sedePagamento);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaSedePagamento");
		return sedePagamentoSave;
	}

}
