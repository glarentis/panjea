package it.eurotn.panjea.preventivi.manager;

import it.eurotn.panjea.preventivi.manager.interfaces.PreventiviMovimentazioneManager;
import it.eurotn.panjea.preventivi.manager.sqlbuilder.MovimentazioneQueryBuilder;
import it.eurotn.panjea.preventivi.util.RigaMovimentazione;
import it.eurotn.panjea.preventivi.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 */
@Stateless(name = "Panjea.PreventiviMovimentazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.PreventiviMovimentazioneManager")
public class PreventiviMovimentazioneManagerBean implements PreventiviMovimentazioneManager {

	private static Logger logger = Logger.getLogger(PreventiviMovimentazioneManagerBean.class);

	@Resource
	protected SessionContext context;

	@EJB
	protected PanjeaDAO panjeaDAO;

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
			int page, int sizeOfPage) {

		String sqlStringQuery = MovimentazioneQueryBuilder.getSqlMovimentazione(parametriRicercaMovimentazione,
				getAzienda());

		org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
				.createNativeQuery(sqlStringQuery);
		SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
		sqlQuery.setFirstResult((page - 1) * sizeOfPage).setMaxResults(sizeOfPage);
		sqlQuery.setResultTransformer(Transformers.aliasToBean(RigaMovimentazione.class));
		List<RigaMovimentazione> righeMovimentazione = null;
		try {
			MovimentazioneQueryBuilder.addQueryScalar(sqlQuery);
			righeMovimentazione = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in caricaRigheArticoloMovimentazione", e);
			throw new RuntimeException(e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit caricaMovimentazione " + righeMovimentazione.size());
		}
		return righeMovimentazione;
	}

	/**
	 * 
	 * @return codice Azienda loggata
	 */
	private String getAzienda() {
		JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
		return jecPrincipal.getCodiceAzienda();
	}

}
