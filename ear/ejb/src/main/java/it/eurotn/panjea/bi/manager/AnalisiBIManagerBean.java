package it.eurotn.panjea.bi.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBIResult;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.sql.AnalisiBiFillerExecuter;
import it.eurotn.panjea.bi.domain.analisi.sql.CastProvider;
import it.eurotn.panjea.bi.domain.analisi.sql.detail.AnalisiBiSqlDetailGenerator;
import it.eurotn.panjea.bi.domain.analisi.sql.detail.RigaDettaglioAnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.manager.interfaces.AnalisiBIManager;
import it.eurotn.panjea.bi.manager.interfaces.DashBoardManager;
import it.eurotn.panjea.magazzino.exception.AnalisiNonPresenteException;
import it.eurotn.panjea.magazzino.exception.AnalisiPresenteInDashBoardException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.AnalisiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AnalisiManager")
public class AnalisiBIManagerBean implements AnalisiBIManager {
	private static Logger logger = Logger.getLogger(AnalisiBIManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private DashBoardManager dashBoardManager;

	@Override
	public void cancellaAnalisi(AnalisiBi analisiBi, boolean removeFromDashboard)
			throws AnalisiPresenteInDashBoardException, AnalisiNonPresenteException {
		logger.debug("--> Enter cancellaAnalisi");

		if (removeFromDashboard) {
			try {
				analisiBi = panjeaDAO.load(AnalisiBi.class, analisiBi.getId());
				dashBoardManager.cancellaDashBoardAnalisi(analisiBi);
			} catch (ObjectNotFoundException e) {
				throw new AnalisiNonPresenteException("");
			}
		} else {
			// controllo se ci sono dashboard che contengono l'analisi
			List<DashBoard> dashBoards = dashBoardManager.caricaDashBoard(analisiBi);
			if (dashBoards != null && !dashBoards.isEmpty()) {
				throw new AnalisiPresenteInDashBoardException(dashBoards, analisiBi.getId());
			}
		}

		try {
			panjeaDAO.delete(analisiBi);
		} catch (Exception e) {
			logger.error("--> errore nel cancellare l'analisi " + analisiBi.getId(), e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaAnalisi");
	}

	@Override
	public AnalisiBi caricaAnalisi(String nomeAnalisi, String categoriaAnalisi) throws AnalisiNonPresenteException {
		logger.debug("--> Enter caricaAnalisi");
		AnalisiBi result;
		try {
			Query query = panjeaDAO.prepareNamedQuery("AnalisiBi.caricaByNomeCategoria");
			query.setParameter("nomeAnalisi", nomeAnalisi);
			query.setParameter("categoriaAnalisi", categoriaAnalisi);
			result = (AnalisiBi) panjeaDAO.getSingleResult(query);
			logger.debug("--> analisi caricata " + result);
		} catch (ObjectNotFoundException e) {
			throw new AnalisiNonPresenteException(nomeAnalisi);
		} catch (DAOException e) {
			logger.error("--> errore nel caricare l'analisisi  " + nomeAnalisi, e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaAnalisi");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnalisiBi> caricaListaAnalisi() {
		logger.debug("--> Enter caricaListaAnalisi");
		Query query = panjeaDAO
				.prepareQuery("select a.id as id,a.nome as nome,a.categoria as categoria,a.descrizione as descrizione from AnalisiBi a");
		List<AnalisiBi> result = null;
		try {
			((org.hibernate.ejb.HibernateQuery) query).getHibernateQuery().setResultTransformer(
					Transformers.aliasToBean(AnalisiBi.class));
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore nel caricare la lista delle analisi", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit caricaListaAnalisi");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Object> caricaValoriPerColonna(Colonna colonna) {
		StringBuilder sb = new StringBuilder("select ");
		// Se il tipo di colonna è Character devo eseguire un cast per mysql
		CastProvider castProvider = new CastProvider();
		sb = castProvider.castString(sb, colonna);
		sb.append(" from ");
		sb.append(colonna.getTabella().getSqlTable(null));
		if (!colonna.getTabella().getWhere().isEmpty()) {
			sb.append(" where ");
			sb.append(colonna.getTabella().getWhereSql());
		}
		sb.append(" group by ");
		sb.append(colonna.getNome());
		sb.append(" order by ");
		sb.append(colonna.getNome());

		Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
		List<Object> result = new ArrayList<Object>();
		try {
			result = panjeaDAO.getResultList(query);
		} catch (DAOException e) {
			logger.error("--> errore nel caricare la lista articoli", e);
			throw new RuntimeException(e);
		}
		return new HashSet<Object>(result);
	}

	@Override
	public void copiaAnalisi(AnalisiBi analisi) {
		logger.debug("--> Enter copiaAnalisi");
		try {
			analisi = caricaAnalisi(analisi.getNome(), analisi.getCategoria());
		} catch (AnalisiNonPresenteException e) {
			throw new RuntimeException(e);
		}
		AnalisiBi nuovaAnalisi = analisi.copia();
		salvaAnalisi(nuovaAnalisi);
		logger.debug("--> Exit copiaAnalisi");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaDettaglioAnalisiBi> drillThrough(AnalisiBi analisi, Map<Colonna, Object[]> detailFilter,
			Colonna colonnaMisura, int page, int sizeOfPage) {

		AnalisiBiSqlDetailGenerator sqlDetailGenerator = new AnalisiBiSqlDetailGenerator(analisi, detailFilter,
				colonnaMisura);

		System.err.println(sqlDetailGenerator.buildSql());

		org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
				.createNativeQuery(sqlDetailGenerator.buildSql());

		SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
		/** calcolo index dei inicio e fine dei dati della pagina richesta **/
		sqlQuery.setFirstResult((page - 1) * sizeOfPage).setMaxResults(sizeOfPage);

		sqlQuery.setResultTransformer(Transformers.aliasToBean(RigaDettaglioAnalisiBi.class));
		List<RigaDettaglioAnalisiBi> righeDettaglio = null;
		try {
			sqlQuery.addScalar("idArticolo");
			sqlQuery.addScalar("codiceArticolo");
			sqlQuery.addScalar("descrizioneArticolo");
			sqlQuery.addScalar("idCategoria");
			sqlQuery.addScalar("codiceCategoria");
			sqlQuery.addScalar("descrizioneCategoria");
			sqlQuery.addScalar("idDeposito");
			sqlQuery.addScalar("codiceDeposito");
			sqlQuery.addScalar("descrizioneDeposito");
			sqlQuery.addScalar("idDocumento");
			sqlQuery.addScalar("dataRegistrazione");
			sqlQuery.addScalar("dataDocumento");
			sqlQuery.addScalar("numeroDocumento", Hibernate.STRING);
			sqlQuery.addScalar("numeroDocumentoOrder", Hibernate.STRING);
			sqlQuery.addScalar("idTipoDocumento");
			sqlQuery.addScalar("codiceTipoDocumento");
			sqlQuery.addScalar("descrizioneTipoDocumento");
			sqlQuery.addScalar("idEntita");
			sqlQuery.addScalar("codiceEntita");
			sqlQuery.addScalar("descrizioneEntita");
			sqlQuery.addScalar("tipoEntita");
			sqlQuery.addScalar("numeroDecimaliPrezzo");
			sqlQuery.addScalar("numeroDecimaliQuantita");
			sqlQuery.addScalar("prezzoUnitario");
			sqlQuery.addScalar("prezzoNetto");
			sqlQuery.addScalar("PrezzoTotale");
			sqlQuery.addScalar("variazione1");
			sqlQuery.addScalar("variazione2");
			sqlQuery.addScalar("variazione3");
			sqlQuery.addScalar("variazione4");
			sqlQuery.addScalar("qta");
			sqlQuery.addScalar("qtaMagazzino", Hibernate.DOUBLE);
			sqlQuery.addScalar("noteRiga");
			sqlQuery.addScalar("descrizioneRiga");
			sqlQuery.addScalar("idSedeEntita");
			sqlQuery.addScalar("codiceSedeEntita");
			sqlQuery.addScalar("descrizioneSedeEntita");
			righeDettaglio = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in caricaRigheArticoloMovimentazione", e);
			throw new RuntimeException(e);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("--> Exit caricaMovimentazione " + righeDettaglio.size());
		}
		return righeDettaglio;
	}

	@Override
	public AnalisiBIResult eseguiAnalisi(AnalisiBi analisiBi) {
		AnalisiBiFillerExecuter analisiExecuter = new AnalisiBiFillerExecuter(analisiBi);
		((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(analisiExecuter);
		return analisiExecuter.getResult();
	}

	@Override
	public AnalisiBi salvaAnalisi(AnalisiBi analisiBi) {
		logger.debug("--> Enter salvaAnalisi");
		try {
			// se l'analisi non è nuova carico quella esistente perchè potrebbe cambiare il nome e/o la categoria. In
			// questo caso devo cambiarlo anche a tutte le dashboard a cui fa riferimento.
			AnalisiBi analisiBiOld = null;
			if (!analisiBi.isNew()) {
				analisiBiOld = panjeaDAO.load(AnalisiBi.class, analisiBi.getId());
				((Session) panjeaDAO.getEntityManager().getDelegate()).evict(analisiBiOld);
			}

			analisiBi = panjeaDAO.save(analisiBi);

			if (analisiBiOld != null) {
				dashBoardManager.aggiornaDashBoardAnalisi(analisiBiOld.getNome(), analisiBiOld.getCategoria(),
						analisiBi.getNome(), analisiBi.getCategoria());
			}
		} catch (Exception e) {
			logger.error("--> errore nel salvare l'analisi " + analisiBi.getNome(), e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaAnalisi");
		return analisiBi;
	}

}
