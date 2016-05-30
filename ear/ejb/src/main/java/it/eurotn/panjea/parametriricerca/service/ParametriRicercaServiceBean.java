package it.eurotn.panjea.parametriricerca.service;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.ParametriRicercaDTO;
import it.eurotn.panjea.parametriricerca.service.interfaces.ParametriRicercaService;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.ParametriRicercaService")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@RemoteBinding(jndiBinding = "Panjea.ParametriRicercaService")
public class ParametriRicercaServiceBean implements ParametriRicercaService {

	private static Logger logger = Logger.getLogger(ParametriRicercaServiceBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext sessionContext;

	@Override
	public void cancellaParametro(Class<? extends AbstractParametriRicerca> classeParametro, String nome) {
		try {
			panjeaDAO.delete(caricaParametro(classeParametro, nome));
		} catch (DAOException e) {
			logger.error("-->errore nel cancellare il parametro di classe e nome :" + classeParametro + ":" + nome, e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ParametriRicercaDTO> caricaParametri(Class<? extends AbstractParametriRicerca> classeParametro) {
		Query query = panjeaDAO
				.prepareQuery("select new it.eurotn.panjea.parametriricerca.domain.ParametriRicercaDTO(p.nome,p.global) from "
						+ classeParametro.getName()
						+ "  p where p.class= "
						+ classeParametro.getName()
						+ " and (p.userInsert=:currentUser or p.global=true) order by nome");
		query.setParameter("currentUser", getPrincipal().getUserName());
		List<ParametriRicercaDTO> result = query.getResultList();
		return result;
	}

	@Override
	public AbstractParametriRicerca caricaParametro(Class<? extends AbstractParametriRicerca> classeParametro,
			String nome) {
		Query query = panjeaDAO.prepareQuery("select p from " + classeParametro.getName() + " p where p.class= "
				+ classeParametro.getName()
				+ " and p.nome=:nome and (p.userInsert=:currentUser or p.global=true) order by nome");
		query.setParameter("currentUser", getPrincipal().getUserName());
		query.setParameter("nome", nome);
		AbstractParametriRicerca result = (AbstractParametriRicerca) query.getSingleResult();
		return result;
	}

	/**
	 * 
	 * @return utente corrente
	 */
	private JecPrincipal getPrincipal() {
		return (JecPrincipal) sessionContext.getCallerPrincipal();
	}

	@Override
	public AbstractParametriRicerca salvaParametro(AbstractParametriRicerca parametro) {
		try {
			Query queryParametroPresente = panjeaDAO.prepareQuery("select p from " + parametro.getClass().getName()
					+ " p where p.nome=:nome");
			queryParametroPresente.setParameter("nome", parametro.getNome());
			try {
				AbstractParametriRicerca parametroPresente = (AbstractParametriRicerca) panjeaDAO
						.getSingleResult(queryParametroPresente);
				panjeaDAO.delete(parametroPresente);
			} catch (ObjectNotFoundException notFoundException) {
				if (logger.isDebugEnabled()) {
					logger.debug("--> la ricerca non esiste");
				}
			}
			parametro = panjeaDAO.save(parametro);
		} catch (DAOException e) {
			logger.error("-->errore nel salvare il parametro ", e);
			throw new RuntimeException(e);
		}
		return parametro;
	}

}
