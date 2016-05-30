/**
 * 
 */
package it.eurotn.panjea.magazzino.manager;

import it.eurotn.panjea.magazzino.manager.interfaces.ListinoStatisticheManager;
import it.eurotn.panjea.magazzino.manager.sqlbuilder.ConfrontoListinoQueryBuilder;
import it.eurotn.panjea.magazzino.util.ConfrontoListinoDTO;
import it.eurotn.panjea.magazzino.util.RigaConfrontoListinoDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaConfrontoListino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.util.SqlExecuter;
import it.eurotn.security.JecPrincipal;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * @author fattazzo
 * 
 */
@Stateless(name = "Panjea.ListinoStatisticheManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ListinoStatisticheManager")
public class ListinoStatisticheManagerBean implements ListinoStatisticheManager {

	private static Logger logger = Logger.getLogger(ListinoStatisticheManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@Resource
	private SessionContext sessionContext;

	@SuppressWarnings("unchecked")
	@Override
	public ConfrontoListinoDTO caricaConfrontoListino(ParametriRicercaConfrontoListino parametri) {

		ConfrontoListinoDTO confrontoListinoDTO = new ConfrontoListinoDTO();

		ConfrontoListinoQueryBuilder queryBuilder = new ConfrontoListinoQueryBuilder(parametri, getAzienda());
		Collection<String> sqlPrepareData = queryBuilder.getSqlPrepareData();
		Collection<String> sqlInsertData = queryBuilder.getSqlInsertData();
		String sqlResult = queryBuilder.getSQLResults();

		try {
			// creazione della tabella temporanea
			SqlExecuter executer = new SqlExecuter();
			for (String sql : sqlPrepareData) {
				executer.setSql(sql);
				((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
			}
			// inserimento dati
			for (String sql : sqlInsertData) {
				executer.setSql(sql);
				((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
			}

			List<RigaConfrontoListinoDTO> righeConfronto = null;
			SQLQuery query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createSQLQuery(sqlResult);
			query.setResultTransformer(Transformers.aliasToBean(RigaConfrontoListinoDTO.class));
			try {

				query.addScalar("idArticolo");
				query.addScalar("codiceArticolo");
				query.addScalar("descrizioneArticolo");
				query.addScalar("idCategoria");
				query.addScalar("codiceCategoria");
				query.addScalar("descrizioneCategoria");
				query.addScalar("prezzoBase");
				for (int i = 1; i < parametri.getConfronti().size() + 1; i++) {
					query.addScalar("descrizioneConfronto" + i);
					query.addScalar("prezzoConfronto" + i);
				}
				query.addScalar("numeroDecimaliPrezzo", Hibernate.INTEGER);
				query.addScalar("numeroConfronti", Hibernate.INTEGER);
				righeConfronto = query.list();
			} catch (Exception e) {
				logger.error("--> errore durante il caricamento delle righe confronto.", e);
				throw new RuntimeException("errore durante il caricamento delle righe confronto.", e);
			}
			confrontoListinoDTO.setRigheConfronto(righeConfronto);
		} finally {
			// drop della tabella temporanea
			Collection<String> sqlDrop = queryBuilder.getSQLDropTable();
			SqlExecuter executer = new SqlExecuter();
			for (String sql : sqlDrop) {
				executer.setSql(sql);
				((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
			}
		}

		return confrontoListinoDTO;
	}

	/**
	 * 
	 * @return codice azienda loggata
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

}
