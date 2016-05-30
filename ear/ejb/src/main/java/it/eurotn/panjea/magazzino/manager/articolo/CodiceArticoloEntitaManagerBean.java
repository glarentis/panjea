package it.eurotn.panjea.magazzino.manager.articolo;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.exception.CodiceArticoloEntitaAbitualeEsistenteException;
import it.eurotn.panjea.magazzino.exception.CodiceArticoloEntitaContoTerziEsistenteException;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.CodiceArticoloEntitaManager;
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
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.CodiceArticoloEntitaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CodiceArticoloEntitaManager")
public class CodiceArticoloEntitaManagerBean implements CodiceArticoloEntitaManager {

	private static Logger logger = Logger.getLogger(CodiceArticoloEntitaManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public void cancellaCodiceArticoloEntita(CodiceArticoloEntita codiceArticoloEntita) {
		logger.debug("--> Enter cancellaCodiceArticoloEntita");
		try {
			panjeaDAO.delete(codiceArticoloEntita);
		} catch (Exception e) {
			logger.info("--> errore nel cancellare il codiceArticoloEntita", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaCodiceArticoloEntita");
	}

	@Override
	public CodiceArticoloEntita caricaCodiceArticoloEntita(Integer idArticolo, Integer idEntita) {
		logger.debug("--> Enter caricaCodiceArticoloEntita");

		Query query = panjeaDAO.prepareNamedQuery("CodiceArticoloEntita.caricaByArticoloEEntita");
		query.setParameter("paramIdArticolo", idArticolo);
		query.setParameter("paramIdEntita", idEntita);

		CodiceArticoloEntita codiceArticoloEntita = null;
		try {
			codiceArticoloEntita = (CodiceArticoloEntita) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			// non rilancio l'errore perchè significa solo che non esiste un
			// codice articolo per l'entità scelta
			codiceArticoloEntita = null;
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento del codice articolo entità", e);
			throw new RuntimeException("errore durante il caricamento del codice articolo entità", e);
		}

		logger.debug("--> Exit caricaCodiceArticoloEntita");
		return codiceArticoloEntita;
	}

	/**
	 * Carica se esiste un codice articolo entità settato come entita principale per l'articolo.
	 * 
	 * @param idArticolo
	 *            articolo di riferimento
	 * @return {@link CodiceArticoloEntita}
	 */
	private CodiceArticoloEntita caricaCodiceEntitaPrincipale(Integer idArticolo) {
		logger.debug("--> Enter caricaCodiceEntitaPrincipale");

		Query query = panjeaDAO.prepareNamedQuery("CodiceArticoloEntita.caricaByArticoloEEntitaPrincipale");
		query.setParameter("paramIdArticolo", idArticolo);

		CodiceArticoloEntita codiceArticoloEntita;
		try {
			codiceArticoloEntita = (CodiceArticoloEntita) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			// non esiste ancora nessun codice articolo entità settato come
			// entita principale
			codiceArticoloEntita = null;
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento del codice articolo entità, entità principale.", e);
			throw new RuntimeException("errore durante il caricamento del codice articolo entità, entità principale.",
					e);
		}

		logger.debug("--> Exit caricaCodiceEntitaPrincipale");
		return codiceArticoloEntita;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CodiceArticoloEntita> caricaCodiciArticoloEntita(Entita entita) {
		logger.debug("--> Enter caricaCodiciArticoloEntita");
		Query query = panjeaDAO.prepareNamedQuery("CodiceArticoloEntita.caricaByEntita");
		query.setParameter("paramIdEntita", entita.getId());
		List<CodiceArticoloEntita> listResult = new ArrayList<CodiceArticoloEntita>();
		try {
			listResult = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento dei codici articolo entità", e);
			throw new RuntimeException("Errore durante il caricamento dei codici articolo entità", e);
		}
		logger.debug("--> Exit caricaCodiciArticoloEntita");
		return listResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CodiceArticoloEntita> caricaCodiciArticoloEntita(Integer idArticolo) {
		logger.debug("--> Enter caricaCodiciArticoloEntita " + idArticolo);
		Query query = panjeaDAO.prepareNamedQuery("CodiceArticoloEntita.caricaByArticolo");
		query.setParameter("paramIdArticolo", idArticolo);
		List<CodiceArticoloEntita> listResult = new ArrayList<CodiceArticoloEntita>();
		try {
			listResult = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> Errore durante il caricamento dei codici articolo entità", e);
			throw new RuntimeException("Errore durante il caricamento dei codici articolo entità", e);
		}
		logger.debug("--> Exit caricaCodiciArticoloEntita");
		return listResult;
	}

	/**
	 * Carica se esiste un codice articolo fornitore settato come consegna conto terzi per l'articolo.
	 * 
	 * @param idArticolo
	 *            articolo di riferimento
	 * @return {@link CodiceArticoloEntita}
	 */
	private CodiceArticoloEntita caricaConsegnaContoTerziByArticolo(Integer idArticolo) {
		logger.debug("--> Enter checkConsegnaContoTerziByArticolo");

		Query query = panjeaDAO.prepareNamedQuery("CodiceArticoloEntita.caricaConsegnaContoTerziByArticolo");
		query.setParameter("paramIdArticolo", idArticolo);

		CodiceArticoloEntita codiceArticoloEntita;
		try {
			codiceArticoloEntita = (CodiceArticoloEntita) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			// non esiste ancora nessun codice articolo entità settato come
			// consegna conto terzi
			codiceArticoloEntita = null;
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento del codice articolo entità conto terzi.", e);
			throw new RuntimeException("errore durante il caricamento del codice articolo entità conto terzi.", e);
		}

		logger.debug("--> Exit checkConsegnaContoTerziByArticolo");
		return codiceArticoloEntita;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EntitaLite> caricaEntitaAbituali(String tipoEntita) {
		logger.debug("--> Enter caricaEntitaAbituali");
		List<EntitaLite> entitaAbituali = new ArrayList<>();
		try {
			Query query = panjeaDAO
					.prepareQuery("select distinct ent from CodiceArticoloEntita cae inner join cae.entita ent inner join fetch ent.anagrafica anag join fetch anag.sedeAnagrafica sa where cae.entitaPrincipale=true and ent.class=:paramClassEntita");
			query.setParameter("paramClassEntita", tipoEntita);
			entitaAbituali = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento delle entità abituali", e);
			throw new RuntimeException("Errore durante il caricamento delle entità abituali", e);
		}
		logger.debug("--> Exit caricaEntitaAbituali");
		return entitaAbituali;
	}

	@Override
	public CodiceArticoloEntita salvaCodiceArticoloEntita(CodiceArticoloEntita codiceArticoloEntita)
			throws CodiceArticoloEntitaContoTerziEsistenteException, CodiceArticoloEntitaAbitualeEsistenteException {
		logger.debug("--> Enter salvaCodiceArticoloEntita");

		// se si sta cercando di salvare un codice articolo entita con consegna
		// conto terzi, verifico se lo posso salvare.
		if (codiceArticoloEntita.getConsegnaContoTerzi() && "F".equals(codiceArticoloEntita.getEntita().getTipo())) {
			CodiceArticoloEntita codiceArticoloContoTerzi = caricaConsegnaContoTerziByArticolo(codiceArticoloEntita
					.getArticolo().getId());

			if (codiceArticoloContoTerzi != null
					&& !codiceArticoloContoTerzi.getId().equals(codiceArticoloEntita.getId())) {
				throw new CodiceArticoloEntitaContoTerziEsistenteException(codiceArticoloContoTerzi);
			}
		}

		// controllo se esiste già un codice entità abituale
		if (codiceArticoloEntita.isEntitaPrincipale()) {
			CodiceArticoloEntita codiceArticoloEntitaPrincipale = caricaCodiceEntitaPrincipale(codiceArticoloEntita
					.getArticolo().getId());

			if (codiceArticoloEntitaPrincipale != null
					&& !codiceArticoloEntitaPrincipale.getId().equals(codiceArticoloEntita.getId())) {
				throw new CodiceArticoloEntitaAbitualeEsistenteException(codiceArticoloEntitaPrincipale);
			}
		}

		CodiceArticoloEntita codiceArticoloEntitaSalvato = null;
		try {
			codiceArticoloEntitaSalvato = panjeaDAO.save(codiceArticoloEntita);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del codiceArticoloEntita", e);
			throw new RuntimeException("Errore durante il salvataggio del codiceArticoloEntita", e);
		}
		logger.debug("--> Exit salvaCodiceArticoloEntita");
		return codiceArticoloEntitaSalvato;
	}

}
