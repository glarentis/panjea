package it.eurotn.panjea.anagrafica.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.AbstractLayout;
import it.eurotn.panjea.anagrafica.domain.ChartLayout;
import it.eurotn.panjea.anagrafica.domain.DockedLayout;
import it.eurotn.panjea.anagrafica.domain.TableLayout;
import it.eurotn.panjea.anagrafica.manager.interfaces.LayoutManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
@Stateless(name = "Panjea.LayoutManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LayoutManager")
public class LayoutManagerBean implements LayoutManager {

	private static Logger logger = Logger.getLogger(LayoutManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext sessionContext;

	@Override
	public void cancella(AbstractLayout layout) {
		logger.debug("--> Enter cancella");

		try {
			AbstractLayout layoutMerge = panjeaDAO.getEntityManager().getReference(layout.getClass(), layout.getId());
			panjeaDAO.getEntityManager().remove(layoutMerge);
			panjeaDAO.getEntityManager().flush();
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione del layout " + layout.getChiave(), e);
			throw new RuntimeException("Errore durante la cancellazione del layout " + layout.getChiave(), e);
		}
		logger.debug("--> Exit cancella");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChartLayout> caricaChartLayout(String key) {
		logger.debug("--> Enter caricaChartLayout");

		Query query = panjeaDAO.prepareNamedQuery("ChartLayout.caricaChartByKey");
		query.setParameter("paramKey", key);
		query.setParameter("paramUser", getJecPrincipal().getUserName());

		List<ChartLayout> layouts = new ArrayList<ChartLayout>();
		layouts = query.getResultList();

		logger.debug("--> Exit caricaChartLayout");
		return layouts;
	}

	@Override
	public DockedLayout caricaDefaultDockedLayout(String key) {
		logger.debug("--> Enter caricaDefaultDockedLayout");

		DockedLayout layout = null;

		Query query = panjeaDAO.prepareNamedQuery("DockedLayout.caricaByDefaultKey");
		query.setParameter("paramKey", key);
		try {
			layout = (DockedLayout) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			// non trovato il layout dell'editor di default
			if (logger.isDebugEnabled()) {
				logger.debug("--> Non esiste il layout di default per il docked " + key);
			}
		} catch (Exception e) {
			logger.error("-->errore durante il caricamento del layout " + key, e);
			throw new RuntimeException("errore durante il caricamento del layout " + key, e);
		}
		logger.debug("--> Exit caricaDefaultDockedLayout");
		return layout;
	}

	@Override
	public DockedLayout caricaDockedLayout(String key) {
		logger.debug("--> Enter caricaDockedLayout");

		DockedLayout layout = null;

		// cerco il layout per l'utente
		Query query = panjeaDAO.prepareNamedQuery("DockedLayout.caricaByUtenteEKey");
		query.setParameter("paramKey", key);
		query.setParameter("paramUser", getJecPrincipal().getUserName());
		try {
			layout = (DockedLayout) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			// non trovato il layout dell'editor per l'utente
			if (logger.isDebugEnabled()) {
				logger.debug("--> Layout dell'editor per l'utente non trovato");
			}
		} catch (Exception e) {
			logger.error("-->errore durante il caricamento del layout " + key, e);
			throw new RuntimeException("errore durante il caricamento del layout " + key, e);
		}

		// se non è stato trovato, carico quello di default
		if (layout == null) {
			layout = caricaDefaultDockedLayout(key);
		}
		logger.debug("--> Exit caricaDockedLayout");
		return layout;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TableLayout> caricaTableLayout(String key) {
		logger.debug("--> Enter carica");

		Query query = panjeaDAO.prepareNamedQuery("TableLayout.caricaTableByKey");
		query.setParameter("paramKey", key);
		query.setParameter("paramUser", getJecPrincipal().getUserName());

		List<TableLayout> layouts = new ArrayList<TableLayout>();
		layouts = query.getResultList();

		logger.debug("--> Exit salva");
		return layouts;
	}

	/**
	 * @return utente loggato
	 */
	private JecPrincipal getJecPrincipal() {
		return (JecPrincipal) sessionContext.getCallerPrincipal();
	}

	/**
	 * Rimuove il valore ultimoCaricato dai layoutdella chiave di riferimento.
	 * 
	 * @param chiaveLayout
	 *            chiave
	 */
	private void removeUltimoCaricato(String chiaveLayout) {
		StringBuilder sb = new StringBuilder();
		sb.append("update preference_layout ");
		sb.append("set ultimoCaricato = replace(ultimoCaricato,'");
		sb.append(getJecPrincipal().getUserName());
		sb.append(",','') ");
		sb.append("where TIPO_LAYOUT = 'T' and chiave = '");
		sb.append(chiaveLayout);
		sb.append("'");

		Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
		try {
			panjeaDAO.executeQuery(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public AbstractLayout salva(AbstractLayout layout) {
		logger.debug("--> Enter salva");

		// rimuovo il valore ultimoCaricato dai layoutdella chiave di
		// riferimento
		removeUltimoCaricato(layout.getChiave());

		String ultimoCaricato = "";

		// Se il layout che sto salvando è nuovo verifico se esiste un layout
		// con la stessa chiave
		// In tal caso lo cancello.
		if (layout.getId() == null) {
			Map<String, Object> valueParam = new HashMap<String, Object>();
			StringBuffer sb = new StringBuffer();
			sb.append("delete ");
			sb.append(layout.getClass().getName());
			sb.append(" a where a.chiave= :paramChiave ");
			valueParam.put("paramChiave", layout.getChiave());

			if (layout.getName() != null) {
				sb.append(" and a.name= :paramNome ");
				valueParam.put("paramNome", layout.getName());
			} else {
				sb.append(" and a.name is null");
			}

			if (layout.isGlobal()) {
				sb.append(" and a.global = true ");
			} else {
				sb.append(" and a.global = false and a.utente= :paramUtente ");
				valueParam.put("paramUtente", getJecPrincipal().getUserName());
			}

			Query query = panjeaDAO.prepareQuery(sb.toString());

			for (Entry<String, Object> entry : valueParam.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
			try {
				panjeaDAO.executeQuery(query);
			} catch (DAOException e) {
				throw new RuntimeException(e);
			}
		} else if (layout.isGlobal()) {
			// se è gobale devo ricaricare il layout per potrebbe averlo salvato
			// qualcuno e quindi modificato la proprietà ultimoCambiato
			TableLayout layoutOld = panjeaDAO.loadLazy(TableLayout.class, layout.getId());
			ultimoCaricato = layoutOld.getUltimoCaricato() != null ? layoutOld.getUltimoCaricato() : "";
		}

		ultimoCaricato = ultimoCaricato + getJecPrincipal().getUserName() + ",";

		AbstractLayout layoutSalvato = null;
		try {
			if (layout.getUtente() == null) {
				layout.setUtente(getJecPrincipal().getUserName());
			}
			layout.setUltimoCaricato(ultimoCaricato);
			layoutSalvato = panjeaDAO.getEntityManager().merge(layout);
			panjeaDAO.getEntityManager().flush();
		} catch (Exception e) {
			logger.error("-->errore durante il salvataggio del layout", e);
			throw new RuntimeException("errore durante il salvataggio del layout", e);
		}

		logger.debug("--> Exit salva");
		return layoutSalvato;
	}

}
