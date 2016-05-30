package it.eurotn.panjea.bi.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.manager.interfaces.DashBoardManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.DashBoardManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DashBoardManager")
public class DashBoardManagerBean implements DashBoardManager {
	private static Logger logger = Logger.getLogger(DashBoardManagerBean.class);
	@EJB
	private PanjeaDAO panjeaDAO;
	@Resource
	private SessionContext sessionContext;

	@Override
	public void aggiornaDashBoardAnalisi(String nomeAnalisiOld, String categoriaAnalisiOld, String nomeAnalisiNew,
			String categoriaAnalisiNew) {
		logger.debug("--> Enter aggiornaDashBoardAnalisi");

		String hqlQuery = " update DashBoardAnalisi da set da.nomeAnalisi = :nomeAnalisiNew, da.categoriaAnalisi = :categoriaAnalisiNew where da.nomeAnalisi = :nomeAnalisiOld and da.categoriaAnalisi = :categoriaAnalisiOld";
		Query query = panjeaDAO.prepareQuery(hqlQuery);
		query.setParameter("nomeAnalisiOld", nomeAnalisiOld);
		query.setParameter("categoriaAnalisiOld", categoriaAnalisiOld);
		query.setParameter("nomeAnalisiNew", nomeAnalisiNew);
		query.setParameter("categoriaAnalisiNew", categoriaAnalisiNew);

		try {
			panjeaDAO.executeQuery(query);
		} catch (Exception e) {
			logger.error("--> errore durante l'aggiornamento delle dashboard contenenti l'analisi " + nomeAnalisiNew
					+ " - " + categoriaAnalisiNew, e);
			throw new RuntimeException("errore durante l'aggiornamento delle dashboard contenenti l'analisi "
					+ nomeAnalisiNew + " - " + categoriaAnalisiNew, e);
		}

		logger.debug("--> Exit aggiornaDashBoardAnalisi");
	}

	@Override
	public void cancellaDashBoard(String nomeDashBoard) {
		logger.debug("--> Enter cancellaDashBoard " + nomeDashBoard);

		try {
			DashBoard dashBoard = caricaDashBoard(nomeDashBoard);
			panjeaDAO.delete(dashBoard);
		} catch (DAOException e) {
			logger.error("-->errore nel cancellare la dashBoard " + nomeDashBoard + " per l'utente " + getUtente(), e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit cancellaDashBoard");
	}

	@Override
	public void cancellaDashBoardAnalisi(AnalisiBi analisiBi) {
		logger.debug("--> Enter cancellaDashBoardAnalisi");

		String hqlQuery = "delete DashBoardAnalisi da where da.nomeAnalisi = :nomeAnalisi and da.categoriaAnalisi = :categoriaAnalisi ";
		Query query = panjeaDAO.prepareQuery(hqlQuery);
		query.setParameter("nomeAnalisi", analisiBi.getNome());
		query.setParameter("categoriaAnalisi", analisiBi.getCategoria());

		try {
			panjeaDAO.executeQuery(query);
		} catch (Exception e) {
			logger.error(
					"--> errore durante la cancellazione delle dashboard contenenti l'analisi " + analisiBi.getNome()
							+ " - " + analisiBi.getCategoria(), e);
			throw new RuntimeException("errore durante la cancellazione delle dashboard contenenti l'analisi "
					+ analisiBi.getNome() + " - " + analisiBi.getCategoria(), e);
		}

		logger.debug("--> Exit cancellaDashBoardAnalisi");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DashBoard> caricaDashBoard(AnalisiBi analisiBi) {
		logger.debug("--> Enter caricaDashBoard");
		StringBuilder sb = new StringBuilder(220);
		sb.append("select distinct d.id,d.nome ");
		sb.append("from bi_dashboard_analisi da inner join bi_dashboard d on d.id = da.dashboard_id ");
		sb.append("					    inner join bi_analisi a on a.nome = da.nomeAnalisi = a.categoria = da.categoriaAnalisi ");
		sb.append("where a.id = ");
		sb.append(analisiBi.getId());

		String[] alias = new String[] { "id", "nome" };

		Query query = panjeaDAO.prepareSQLQuery(sb.toString(), DashBoard.class, Arrays.asList(alias));
		List<DashBoard> dashBoards;
		try {
			dashBoards = panjeaDAO.getResultList(query);
		} catch (Exception e1) {
			logger.error("--> errore durante il caricamento delle dashboard che contengono l'analisi", e1);
			throw new RuntimeException("errore durante il caricamento delle dashboard che contengono l'analisi", e1);
		}

		return dashBoards;
	}

	@Override
	public DashBoard caricaDashBoard(String nomeDashBoard) {
		logger.debug("--> Enter salvaDashBoard");
		DashBoard dashBoard = null;
		try {
			Query query = panjeaDAO.prepareNamedQuery("DashBoard.caricaByNomeEUtente");
			query.setParameter("nome", nomeDashBoard);
			query.setParameter("utente", getUtente());
			dashBoard = (DashBoard) query.getSingleResult();
		} catch (NoResultException nre) {
			dashBoard = new DashBoard();
			dashBoard.setNome(nomeDashBoard);
		}
		logger.debug("--> Exit salvaDashBoard");
		return dashBoard;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DashBoard> caricaListaDashBoard() {
		logger.debug("--> Enter caricaListaDashBoard");
		List<DashBoard> result = null;
		try {
			List<String> alias = new ArrayList<String>();
			alias.add("id");
			alias.add("nome");
			alias.add("categoria");
			Query query = panjeaDAO
					.prepareQuery(
							"select d.id as id ,d.nome as nome,d.categoria as categoria from DashBoard d where d.utente=:utente and privata=false",
							DashBoard.class, null);
			query.setParameter("utente", getUtente());
			result = panjeaDAO.getResultList(query);
		} catch (Exception nre) {
			logger.error("-->errore nel caricare la lista delle dashboard per l'utente " + getUtente(), nre);
		}
		logger.debug("--> Exit caricaListaDashBoard");
		return result;
	}

	/**
	 *
	 * @return codice utente loggato
	 */
	private String getUtente() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getUserName();
	}

	@Override
	public DashBoard salvaDashBoard(DashBoard dashBoard) {
		logger.debug("--> Enter salvaDashBoard");

		try {
			// Se ho una dashboard con lo stesso utente/nome la cancello
			Query query = panjeaDAO.prepareNamedQuery("DashBoard.caricaByNomeEUtente");
			query.setParameter("nome", dashBoard.getNome());
			query.setParameter("utente", getUtente());
			if (query.getResultList().size() > 0) {
				panjeaDAO.delete((EntityBase) query.getResultList().get(0));
			}
			dashBoard.setUtente(getUtente());
			dashBoard = panjeaDAO.save(dashBoard);
		} catch (DAOException e) {
			logger.error("-->errore nel salvare la dashboard " + dashBoard, e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit salvaDashBoard");
		return dashBoard;
	}

}
