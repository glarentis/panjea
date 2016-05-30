package it.eurotn.panjea.magazzino.manager.articolo;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.RaggruppamentoArticoliManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.RaggruppamentoArticoliManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RaggruppamentoArticoliManager")
public class RaggruppamentoArticoliManagerBean implements RaggruppamentoArticoliManager {
	private static Logger logger = Logger.getLogger(RaggruppamentoArticoliManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void cancellaRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli) {
		logger.debug("--> Enter cancellaRaggruppamento");
		try {
			panjeaDAO.delete(raggruppamentoArticoli);
		} catch (DAOException e) {
			logger.error("-->errore nel cancellare il raggruppamento " + raggruppamentoArticoli, e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaRaggruppamento");
	}

	@Override
	public void cancellaRigaRaggruppamentoArticoli(RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli) {
		logger.debug("--> Enter cancellaRigaRaggruppamentoArticoli");
		try {
			panjeaDAO.delete(rigaRaggruppamentoArticoli);
		} catch (DAOException e) {
			logger.error("-->errore nel cancellare la riga raggruppamento articoli " + rigaRaggruppamentoArticoli, e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaRigaRaggruppamentoArticoli");

	}

	@Override
	public List<RaggruppamentoArticoli> caricaRaggruppamenti() {
		logger.debug("--> Enter caricaRaggruppamenti");
		Query query = panjeaDAO.prepareNamedQuery("RaggruppamentoArticoli.caricaAll");
		@SuppressWarnings("unchecked")
		List<RaggruppamentoArticoli> result = query.getResultList();
		logger.debug("--> Exit caricaRaggruppamenti");
		return result;
	}

	@Override
	public RaggruppamentoArticoli caricaRaggruppamentoArticoli(RaggruppamentoArticoli raggruppamentoArticoli) {
		logger.debug("--> Enter caricaRaggruppamentoArticoli");

		RaggruppamentoArticoli raggruppamentoArticoliLoad = null;
		try {
			raggruppamentoArticoliLoad = panjeaDAO.load(RaggruppamentoArticoli.class, raggruppamentoArticoli.getId());
			raggruppamentoArticoliLoad.getRigheRaggruppamentoArticoli().size();
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento del raggruppamento articoli.", e);
			throw new RuntimeException("Errore durante il caricamento del raggruppamento articoli.", e);
		}

		logger.debug("--> Exit caricaRaggruppamentoArticoli");
		return raggruppamentoArticoliLoad;
	}

	@Override
	public Set<RigaRaggruppamentoArticoli> caricaRigheRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli) {
		logger.debug("--> Enter caricaRigheRaggruppamento");
		try {
			raggruppamentoArticoli = panjeaDAO.load(RaggruppamentoArticoli.class, raggruppamentoArticoli.getId());
		} catch (ObjectNotFoundException e) {
			logger.error("-->errore oggetto non trovato con id " + raggruppamentoArticoli.getId(), e);
			throw new RuntimeException(e);
		}
		raggruppamentoArticoli.getRigheRaggruppamentoArticoli().size();
		logger.debug("--> Exit caricaRigheRaggruppamento");
		return raggruppamentoArticoli.getRigheRaggruppamentoArticoli();
	}

	@Override
	public List<RigaRaggruppamentoArticoli> caricaRigheRaggruppamentoArticoliByArticolo(int idArticolo) {
		throw new UnsupportedOperationException("Da implementare");
	}

	@Override
	public RaggruppamentoArticoli salvaRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli) {
		try {
			raggruppamentoArticoli = panjeaDAO.save(raggruppamentoArticoli);
		} catch (DAOException e) {
			logger.error("-->errore nel salvare il raggruppamento articoli " + raggruppamentoArticoli, e);
			throw new RuntimeException(e);
		}
		return raggruppamentoArticoli;
	}

	@Override
	public RigaRaggruppamentoArticoli salvaRigaRaggruppamentoArticoli(
			RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli) {
		RigaRaggruppamentoArticoli result;
		try {
			result = panjeaDAO.save(rigaRaggruppamentoArticoli);
			result.getRaggruppamento().getId();
		} catch (DAOException e) {
			logger.error("-->errore nel salvare la rigaRaggruppamentoArticoli " + rigaRaggruppamentoArticoli, e);
			throw new RuntimeException(e);
		}
		return result;
	}

}
