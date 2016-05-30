package it.eurotn.panjea.ordini.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.ordini.domain.SedeOrdine;
import it.eurotn.panjea.ordini.manager.interfaces.SediOrdineManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.SediOrdineManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SediOrdineManager")
public class SediOrdineManagerBean implements SediOrdineManager {

	private static Logger logger = Logger.getLogger(SediOrdineManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@EJB
	protected SediEntitaManager sediEntitaManager;

	@Override
	public SedeOrdine caricaSedeOrdineBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali) {
		logger.debug("--> Enter caricaSedeOrdineBySedeEntita");
		SedeOrdine sedeOrdineLoad = null;
		try {
			sedeEntita = panjeaDAO.load(SedeEntita.class, sedeEntita.getId());
		} catch (Exception e1) {
			logger.error("-->errore nel caricare la sedeentita con id " + sedeEntita.getId(), e1);
			throw new RuntimeException(e1);
		}

		if (sedeEntita.isEreditaDatiCommerciali() && !ignoraEreditaDatiCommerciali) {
			EntitaLite entitaLite = new ClienteLite();
			entitaLite.setId(sedeEntita.getEntita().getId());
			sedeOrdineLoad = caricaSedeOrdinePrincipaleEntita(entitaLite);
		} else {
			Query query = panjeaDAO.prepareNamedQuery("SedeOrdine.caricaBySedeEntita");
			query.setParameter("paramIdSedeEntita", sedeEntita.getId());
			try {
				sedeOrdineLoad = (SedeOrdine) panjeaDAO.getSingleResult(query);
			} catch (ObjectNotFoundException e) {
				logger.debug("--> ObjectNotFoundException in caricaSedeOrdineBySedeEntita");
				sedeOrdineLoad = new SedeOrdine();
				sedeOrdineLoad.setSedeEntita(sedeEntita);
			} catch (DAOException e) {
				logger.error("--> errore DAOException in caricaSedeOrdineBySedeEntita", e);
				throw new RuntimeException(e);
			}
		}
		logger.debug("--> Exit caricaSedeOrdineBySedeEntita");
		return sedeOrdineLoad;
	}
	
	@Override
	public void cancellaSedeOrdine(SedeEntita sedeEntita){
		SedeOrdine sedeMagazzino = caricaSedeOrdineBySedeEntita(sedeEntita, true);
		if (sedeMagazzino.getId() != null) {
			cancellaSedeOrdine(sedeMagazzino);
		}
	}
	
	@Override
	public void cancellaSedeOrdine(SedeOrdine sedeOrdine) {
		logger.debug("--> Enter cancellaSedeOrdine");
		try {
			panjeaDAO.delete(sedeOrdine);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione della sede ordine", e);
			throw new RuntimeException("Errore durante la cancellazione della sede ordine", e);
		}
		logger.debug("--> Exit cancellaSedeOrdine");
	}

	@Override
	public SedeOrdine caricaSedeOrdinePrincipaleEntita(EntitaLite entitaLite) {
		logger.debug("--> Enter caricaSedeOrdinePrincipaleEntita");
		SedeOrdine sedeOrdinePrincipale = null;
		Query query = panjeaDAO.prepareNamedQuery("SedeOrdine.caricaPrincipaleByEntita");
		query.setParameter("paramIdEntita", entitaLite.getId());
		try {
			sedeOrdinePrincipale = (SedeOrdine) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> ObjectNotFoundException in caricaSedeOrdinePrincipaleEntita");
			sedeOrdinePrincipale = new SedeOrdine();
			SedeEntita sedeEntita;
			try {
				sedeEntita = sediEntitaManager.caricaSedePrincipaleEntita(entitaLite.getId());
			} catch (AnagraficaServiceException e1) {
				logger.error(
						"-->errore durante il caricamento della sede principale dell'entità " + entitaLite.getId(), e1);
				throw new RuntimeException("errore durante il caricamento della sede principale dell'entità "
						+ entitaLite.getId(), e1);
			}
			sedeOrdinePrincipale.setSedeEntita(sedeEntita);
		} catch (DAOException e) {
			logger.error("--> errore DAOException in caricaSedeOrdinePrincipaleEntita", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaSedeOrdinePrincipaleEntita");
		return sedeOrdinePrincipale;
	}

	@Override
	public SedeOrdine salvaSedeOrdine(SedeOrdine sedeOrdine) {
		logger.debug("--> Enter salvaSedeOrdine");
		SedeOrdine sedeOrdineSave;
		try {
			sedeOrdineSave = panjeaDAO.save(sedeOrdine);
		} catch (Exception e) {
			logger.error("--> errore durante il salvataggio della sede ordine", e);
			throw new RuntimeException("errore durante il salvataggio della sede ordine", e);
		}
		logger.debug("--> Exit salvaSedeOrdine");
		return sedeOrdineSave;
	}

}
