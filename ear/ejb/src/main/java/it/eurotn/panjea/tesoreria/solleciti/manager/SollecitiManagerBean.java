package it.eurotn.panjea.tesoreria.solleciti.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.pagamenti.service.exception.PagamentiException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.solleciti.RigaSollecito;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.panjea.tesoreria.solleciti.TemplateSolleciti;
import it.eurotn.panjea.tesoreria.solleciti.manager.interfaces.SollecitiManager;
import it.eurotn.panjea.tesoreria.solleciti.manager.interfaces.TemplateSollecitiManager;
import it.eurotn.security.JecPrincipal;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.SollecitiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.SollecitiManager")
public class SollecitiManagerBean implements SollecitiManager {
	private static Logger logger = Logger.getLogger(SollecitiManagerBean.class.getName());

	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private TemplateSollecitiManager templateSollecitiManager;

	/**
	 * cancella i solleciti le cui rate sono state cancellate.
	 *
	 * @throws PagamentiException .
	 */
	@Override
	public void cancellaSollecitiOrphan() throws PagamentiException {
		logger.debug("--> Enter caricaSolleciti");
		Query query = panjeaDAO.prepareNamedQuery("Solleciti.deleteOrphan");
		try {
			panjeaDAO.executeQuery(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaSolleciti");
	}

	/**
	 * cancella un sollecito.
	 *
	 * @param sollecito
	 *            .
	 */
	@Override
	public void cancellaSollecito(Sollecito sollecito) {
		logger.debug("--> Enter cancellaSollecito");
		try {
			panjeaDAO.delete(sollecito);
		} catch (Exception e) {
			logger.error("--> Errore nel cancellare sollecito " + sollecito, e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaSollecito");
	}

	@Override
	public List<RigaSollecito> caricaRigheSollecito(Map<Object, Object> parametri) {
		Sollecito sollecito = null;
		Boolean sollecitoTest = new Boolean((String) parametri.get("sollecitoTest"));

		if (sollecitoTest != null && sollecitoTest) {
			Integer idTemplate = new Integer((String) parametri.get("idTemplateTest"));
			TemplateSolleciti templateSolleciti = templateSollecitiManager.caricaTemplateSollecito(idTemplate);
			sollecito = Sollecito.createTestSollecito(templateSolleciti);
		} else {
			try {
				String idString = (String) parametri.get("idSollecito");
				if (idString != null) {
					sollecito = caricaSollecito(Integer.parseInt(idString));
				}
			} catch (Exception e) {
				logger.error("--> Errore in caricaRigheSollecito", e);
				// throw new
				// RuntimeException("Errore durante il recupero del sollecitosolleciti",
				// e);
			}
		}
		return sollecito.getRigheSollecito();
	}

	/**
	 * carica la lista de template solleciti per la azienda corrente.
	 *
	 * @return lista de template.
	 * @throws PagamentiException .
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Sollecito> caricaSolleciti() throws PagamentiException {
		logger.debug("--> Enter caricaSolleciti");

		String codiceAzienda = ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
		Query query = panjeaDAO.prepareNamedQuery("Solleciti.caricaSolleciti");
		query.setParameter("paramCodiceAzienda", codiceAzienda);
		List<Sollecito> solleciti;
		try {
			solleciti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore impossibile recuperare la list di Solleciti ", e);
			throw new PagamentiException(e);
		}
		logger.debug("--> Exit caricaSolleciti");
		return solleciti;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sollecito> caricaSollecitiByCliente(Integer codice) throws PagamentiException {
		logger.debug("--> Enter caricaSollecitiByCliente");

		Query query = panjeaDAO.prepareNamedQuery("Solleciti.caricaSollecitiByCliente");
		query.setParameter("paramIdEntita", codice);
		List<Sollecito> solleciti;
		try {
			solleciti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore impossibile recuperare la list di Solleciti ", e);
			throw new PagamentiException(e);
		}
		logger.debug("--> Exit caricaSollecitiByCliente");
		return solleciti;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sollecito> caricaSollecitiByRata(Integer idRata) throws PagamentiException {
		logger.debug("--> Enter caricaSollecitiByRata");
		Query query = panjeaDAO.prepareNamedQuery("Solleciti.caricaSollecitiByRata");
		query.setParameter("paramIdRata", idRata);
		List<Sollecito> solleciti;
		try {
			solleciti = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore impossibile recuperare la list di Solleciti ", e);
			throw new PagamentiException(e);
		}
		logger.debug("--> Exit caricaSollecitiByRata");
		return solleciti;

	}

	/**
	 * carica un particollare sollecito.
	 *
	 * @param idSollecito
	 *            .
	 * @return .
	 * @throws PagamentiException .
	 */
	@Override
	public Sollecito caricaSollecito(int idSollecito) throws PagamentiException {
		try {
			return panjeaDAO.load(Sollecito.class, idSollecito);
		} catch (ObjectNotFoundException e) {
			logger.error("-->Sollecito non trovato. ID cercato=" + idSollecito);
			throw new PagamentiException(e);
		}
	}

	/**
	 * .
	 *
	 * @return .
	 */
	private JecPrincipal getPrincipal() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal;

	}

	/**
	 * salva un solleciti.
	 *
	 * @param sollecito
	 *            .
	 * @return .
	 */
	@Override
	public Sollecito salvaSollecito(Sollecito sollecito) {
		logger.debug("--> Enter salvaSollecito");
		Sollecito salvaSollecito = null;
		if (sollecito.isNew()) {
			sollecito.setCodiceAzienda(getPrincipal().getCodiceAzienda());
		}
		try {
			salvaSollecito = panjeaDAO.save(sollecito);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio del sollecito", e);
			throw new RuntimeException("Errore durante il salvataggio del sollecito", e);
		}
		return salvaSollecito;
	}

}
