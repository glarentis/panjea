package it.eurotn.panjea.centricosto.manager;

import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.centricosto.manager.interfaces.CentriCostoManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

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

@Stateless(name = "Panjea.CentriCostoManagerBean")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CentriCostoManagerBean")
public class CentriCostoManagerBean implements CentriCostoManager {

	private static Logger logger = Logger.getLogger(CentriCostoManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext context;

	@Override
	public void cancellaCentroCosto(CentroCosto centroCosto) {
		try {
			panjeaDAO.delete(centroCosto);
		} catch (Exception e) {
			logger.error("-->errore nel cancellare il centro di costo " + centroCosto, e);
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CentroCosto> caricaCentriCosto(String codice) {
		StringBuilder sb = new StringBuilder(
				"select distinct cc from CentroCosto cc left join fetch cc.sottoConti ss left join fetch ss.conto c left join fetch c.mastro m where cc.codiceAzienda=:codiceAzienda ");
		if (codice != null) {
			sb.append(" and cc.codice like ").append(PanjeaEJBUtil.addQuote(codice));
		}
		sb.append(" order by ");
		sb.append(" cc.codice, lpad(m.codice,3,'0'),lpad(c.codice,3,'0'),lpad(ss.codice,6,'0') ");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("codiceAzienda", getAzienda());
		return query.getResultList();
	}

	@Override
	public CentroCosto caricaCentroCosto(CentroCosto centroCosto) {
		logger.debug("--> Enter caricaCentroCosto");

		CentroCosto centroCostoCaricato = null;
		try {
			centroCostoCaricato = panjeaDAO.load(CentroCosto.class, centroCosto.getId());
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento del centro di costo.", e);
			throw new RuntimeException("errore durante il caricamento del centro di costo.", e);
		}

		logger.debug("--> Exit caricaCentroCosto");
		return centroCostoCaricato;
	}

	/**
	 * @return codice azienda loggata
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

	@Override
	public CentroCosto salvaCentroCosto(CentroCosto centroCosto) {
		try {
			if (centroCosto.getCodiceAzienda() == null) {
				centroCosto.setCodiceAzienda(getAzienda());
			}
			centroCosto = panjeaDAO.save(centroCosto);
		} catch (Exception e) {
			logger.error("-->errore nel salvare il centro di costo " + centroCosto, e);
			throw new RuntimeException(e);
		}
		return centroCosto;
	}

}
