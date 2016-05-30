package it.eurotn.panjea.ordini.manager.documento;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.manager.documento.interfaces.TipiAreaOrdineManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.stampe.manager.interfaces.LayoutStampeManager;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 */
@Stateless(name = "Panjea.TipiAreaOrdineManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipiAreaOrdineManager")
public class TipiAreaOrdineManagerBean implements TipiAreaOrdineManager {
	private static Logger logger = Logger.getLogger(TipiAreaOrdineManagerBean.class);

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext context;

	@EJB
	private LayoutStampeManager layoutStampeManager;

	@Override
	public void cancellaTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine) {
		logger.debug("--> Enter cancellaTipoAreaOrdine con id " + tipoAreaOrdine.getId());
		try {
			layoutStampeManager.cancellaLayoutStampa(tipoAreaOrdine);

			panjeaDAO.delete(tipoAreaOrdine);
		} catch (Exception e) {
			logger.error("--> errore nella cancellazione ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaTipoAreaOrdine");

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoAreaOrdine> caricaTipiAreaOrdine(String fieldSearch, String valueSearch,
			boolean loadTipiDocumentoDisabilitati) {
		logger.debug("--> Enter caricaTipiAreaOrdine");

		StringBuilder sb = new StringBuilder();
		sb.append("select tao from TipoAreaOrdine tao ");
		sb.append("inner join tao.tipoDocumento td ");
		sb.append("where td.codiceAzienda = :paramCodiceAzienda ");
		if (!loadTipiDocumentoDisabilitati) {
			sb.append("and td.abilitato = true ");
		}
		if (valueSearch != null) {
			sb.append("and tao.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
		}
		if (fieldSearch != null) {
			sb.append(" order by ").append("tao.").append(fieldSearch);
		}

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setHint("org.hibernate.cacheable", "true");
		query.setHint("org.hibernate.cacheRegion", "tipoAreaOrdine");
		query.setParameter("paramCodiceAzienda", getAzienda());
		List<TipoAreaOrdine> tipiAreaOrdine;
		try {
			tipiAreaOrdine = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore impossibile recuperare la list di TipoAreaOrdine ", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipiAreaOrdine");
		return tipiAreaOrdine;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDocumento> caricaTipiDocumentiOrdine() {
		logger.debug("--> Enter caricaTipiDocumentiOrdine");
		Query query = panjeaDAO.prepareNamedQuery("TipoAreaOrdine.caricaTipiDocumentiMagazzino");
		query.setParameter("paramCodiceAzienda", getAzienda());
		List<TipoDocumento> tipiDocumento = null;
		try {
			tipiDocumento = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore in ricerca tipi area ordine", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaTipiDocumentiOrdine con n° documenti" + tipiDocumento.size());

		return tipiDocumento;

	}

	@Override
	public TipoAreaOrdine caricaTipoAreaOrdine(Integer id) {
		logger.debug("--> Enter caricaTipoAreaOrdine con id " + id);
		try {
			TipoAreaOrdine tipoAreaOrdine = panjeaDAO.load(TipoAreaOrdine.class, id);
			logger.debug("--> Exit caricaTipoAreaContabile");
			return tipoAreaOrdine;
		} catch (ObjectNotFoundException e) {
			logger.error("--> errore caricaTipoAreaOrdine", e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public TipoAreaOrdine caricaTipoAreaOrdinePerTipoDocumento(Integer idTipoDocumento) {
		Query query = panjeaDAO.prepareNamedQuery("TipoAreaOrdine.caricaByTipoDocumento");
		query.setParameter("paramId", idTipoDocumento);
		TipoAreaOrdine tipoAreaOrdineResult = null;
		try {
			tipoAreaOrdineResult = (TipoAreaOrdine) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.debug("--> TipoAreaOrdine non trovato");
			tipoAreaOrdineResult = new TipoAreaOrdine();
			logger.debug("--> Restituisco tipoAreaOrdine " + tipoAreaOrdineResult);
		} catch (DAOException e) {
			logger.error("--> errore DAOException ", e);
			throw new RuntimeException(
					"Impossibile restituire TipoAreaOrdine con tipoDocumento con id =" + idTipoDocumento, e);
		}
		return tipoAreaOrdineResult;
	}

	/**
	 *
	 * @return azienda loggata.
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();

	}

	@Override
	public TipoAreaOrdine salvaTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine) {
		logger.debug("--> Enter salvaTipoAreaOrdine");
		TipoAreaOrdine tipoAreaOrdineResult = null;
		try {
			tipoAreaOrdineResult = panjeaDAO.save(tipoAreaOrdine);
		} catch (Exception e) {
			logger.error("--> errore nel salvare il tipoAreaOrdine", e);
			throw new RuntimeException("errore nel salvare il tipoAreaOrdine", e);
		}
		logger.debug("--> Exit salvaTipoAreaOrdine");
		return tipoAreaOrdineResult;
	}
}
