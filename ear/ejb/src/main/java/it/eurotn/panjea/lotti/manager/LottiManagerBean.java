package it.eurotn.panjea.lotti.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.lotti.domain.LottoInterno;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.manager.filter.CodiceLottoFilterExecuter;
import it.eurotn.panjea.lotti.manager.interfaces.LottiManager;
import it.eurotn.panjea.lotti.manager.querybuilder.MovimentazioneLottoQueryBuilder;
import it.eurotn.panjea.lotti.manager.querybuilder.SituazioneLottiQueryBuilder;
import it.eurotn.panjea.lotti.util.MovimentazioneLotto;
import it.eurotn.panjea.lotti.util.StatisticaLotto;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

@Stateless(name = "Panjea.LottiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.LottiManager")
public class LottiManagerBean implements LottiManager {

	public enum RimanenzaLotto {
		TUTTE, POSITIVA, NEGATIVA, ZERO
	}

	private static Logger logger = Logger.getLogger(LottiManagerBean.class);

	@EJB
	protected PanjeaDAO panjeaDAO;

	@Resource
	protected SessionContext sessionContext;

	@Override
	public void cancellaLottiNonUtilizzati() {
		logger.debug("--> Enter cancellaLottiNonUtilizzati");

		// cancello tutti i lotti interni che non sono mai stati utilizzati
		Query query = panjeaDAO
				.prepareQuery("delete Lotto lot where lot.righeLotti is empty and lot.class = LottoInterno");
		query.executeUpdate();

		// cancello tutti i lotti fornitore che non sono mai stati utilizzati (
		// non sono sono stati assegnati a nessuna riga articolo e non hanno
		// lotti interni collegati )
		StringBuilder sb = new StringBuilder();
		sb.append("delete ");
		sb.append("from maga_lotti ");
		sb.append(
				"where id not in ( select lotto_id from maga_righe_lotti righe where righe.lotto_id = maga_lotti.id) and ");
		sb.append(
				"	 id not in (select lotto_id from ( select * from maga_lotti lottiInterni where lottiInterni.TIPO_LOTTO = 'I') as lottiInt where lottiInt.lotto_id = maga_lotti.id) and ");
		sb.append("    maga_lotti.TIPO_LOTTO = 'F' ");

		query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
		try {
			panjeaDAO.executeQuery(query);
		} catch (DAOException e) {
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit cancellaLottiNonUtilizzati");
	}

	@Override
	public List<ArticoloLite> caricaArticoliByCodiceLotto(Lotto lotto) {
		return caricaArticoliByCodiceLotto(lotto, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ArticoloLite> caricaArticoliByCodiceLotto(Lotto lotto, boolean filtraDataScadenza) {
		logger.debug("--> Enter caricaArticoliByCodiceLotto");

		StringBuilder sb = new StringBuilder();
		sb.append(
				"select distinct lott.articolo.id as id,lott.articolo.codice as codice,lott.articolo.descrizioneLinguaAziendale as descrizione,lott.articolo.tipoLotto as tipoLotto ");
		sb.append(
				"from Lotto lott  where lott.codice =:codiceLotto and lott.codiceAzienda = :codiceAzienda and lott.class = Lotto");
		if (filtraDataScadenza) {
			sb.append(" and lott.dataScadenza = :paramDataScadenza ");
		}

		Query query = panjeaDAO.prepareQuery(sb.toString(), ArticoloLite.class, null);
		query.setParameter("codiceAzienda", getAzienda());
		query.setParameter("codiceLotto", lotto.getCodice());
		if (filtraDataScadenza) {
			query.setParameter("paramDataScadenza", lotto.getDataScadenza());
		}

		List<ArticoloLite> articoli = new ArrayList<ArticoloLite>();
		try {
			articoli = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento degli articoli per il codice lotto: " + lotto.getCodice(),
					e);
			throw new RuntimeException(
					"errore durante il caricamento degli articoli per il codice lotto: " + lotto.getCodice(), e);
		}

		logger.debug("--> Exit caricaArticoliByCodiceLotto");
		return articoli;
	}

	@Override
	public List<Lotto> caricaLotti(ArticoloLite articolo, DepositoLite deposito, RimanenzaLotto rimanenzaLotto,
			Date dataScadenza, String codiceLotto, Date dataInizioRicercaScadenza) {
		logger.debug("--> Enter caricaLotti");

		List<Lotto> lotti = new ArrayList<Lotto>();

		List<StatisticaLotto> statisticheLotti = caricaSituazioneLotti(articolo, deposito, null, null, rimanenzaLotto,
				dataScadenza, codiceLotto, dataInizioRicercaScadenza);

		for (StatisticaLotto statisticaLotto : statisticheLotti) {
			Lotto lotto = statisticaLotto.getLotto();
			lotti.add(lotto);
		}

		logger.debug("--> Exit caricaLotti");
		return lotti;
	}

	@Override
	public List<Lotto> caricaLotti(ArticoloLite articolo, DepositoLite deposito, TipoMovimento tipoMovimento,
			boolean storno, String codice, Date dataScadenza, Date dataInizioRicercaScadenza,
			boolean cercaSoloLottiAperti) {

		RimanenzaLotto rimanenzaLotto = RimanenzaLotto.TUTTE;
		if (tipoMovimento != null) {
			switch (tipoMovimento) {
			case TRASFERIMENTO:
				rimanenzaLotto = RimanenzaLotto.POSITIVA;
				break;
			case SCARICO:
				if (!storno) {
					rimanenzaLotto = RimanenzaLotto.POSITIVA;
				}
				break;
			case CARICO:
				if (storno) {
					rimanenzaLotto = RimanenzaLotto.POSITIVA;
				}
				break;
			default:
				rimanenzaLotto = RimanenzaLotto.TUTTE;
				break;
			}
		}
		// cercaSoloLottiAperti comanda sul tipoMovimento
		rimanenzaLotto = cercaSoloLottiAperti ? RimanenzaLotto.POSITIVA : rimanenzaLotto;

		List<Lotto> lotti = caricaLotti(articolo, deposito, rimanenzaLotto, dataScadenza, codice,
				dataInizioRicercaScadenza);

		return lotti;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Lotto> caricaLotti(ArticoloLite articolo, TipoLotto tipoLotto, String codice) {

		List<Lotto> lotti = new ArrayList<Lotto>();
		Query query = null;
		switch (tipoLotto) {
		case LOTTO:
			query = panjeaDAO.prepareNamedQuery("Lotto.caricaLottiByArticolo");
			break;
		case LOTTO_INTERNO:
			query = panjeaDAO.prepareNamedQuery("LottoInterno.caricaLottiByArticolo");
			break;
		default:
			return lotti;
		}

		query.setParameter("codiceAzienda", getAzienda());
		query.setParameter("idArticolo", articolo.getId());

		try {
			lotti = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore durante il caricamento dei lotti", e);
			throw new RuntimeException("Errore durante il caricamento dei lotti", e);
		}

		CodiceLottoFilterExecuter filterExecuter = new CodiceLottoFilterExecuter();
		lotti = filterExecuter.filter(lotti, codice);

		return lotti;
	}

	@Override
	public Lotto caricaLotto(Lotto lotto) {
		logger.debug("--> Enter caricaLotto");

		Lotto lottoCaricato;
		try {
			lottoCaricato = panjeaDAO.load(Lotto.class, lotto.getId());
			lottoCaricato.getArticolo().getId();
		} catch (Exception e) {
			logger.error("-->errore durante il caricamento del lotto.", e);
			throw new RuntimeException("errore durante il caricamento del lotto.", e);
		}

		logger.debug("--> Exit caricaLotto");
		return lottoCaricato;
	}

	@Override
	public Lotto caricaLotto(String codice, Date dataScadenza, ArticoloLite articolo) {
		logger.debug("--> Enter caricaLotto");

		StringBuilder sb = new StringBuilder();
		sb.append("select lott from Lotto lott join fetch lott.articolo ");
		sb.append("where lott.articolo.id = :idArticolo and ");
		sb.append("lott.codiceAzienda = :codiceAzienda and ");
		sb.append("lott.class = Lotto and ");
		sb.append("lott.codice = :codiceLotto and ");
		sb.append("lott.dataScadenza = :dataScadenzaLotto ");

		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("idArticolo", articolo.getId());
		query.setParameter("codiceAzienda", getAzienda());
		query.setParameter("codiceLotto", codice);
		query.setParameter("dataScadenzaLotto", dataScadenza);

		Lotto lottoResult = null;
		try {
			lottoResult = (Lotto) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			// non esiste il lotto con questi parametri, restituisco null.
			lottoResult = null;
			if (logger.isDebugEnabled()) {
				logger.debug("--> Nessun lotto trovato con i parametri impostati, ritorno null.");
			}
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento del lotto", e);
			throw new RuntimeException("errore durante il caricamento del lotto", e);
		}

		logger.debug("--> Exit caricaLotto");
		return lottoResult;
	}

	@Override
	public List<MovimentazioneLotto> caricaMovimentazioneLotti(Set<Integer> idLotti) {

		List<MovimentazioneLotto> listResult = new ArrayList<MovimentazioneLotto>();

		for (Integer id : idLotti) {
			Lotto lottoLoad;
			try {
				lottoLoad = panjeaDAO.load(Lotto.class, id);
			} catch (ObjectNotFoundException e) {
				logger.error("--> errore durante il caricamento del lotto.", e);
				throw new RuntimeException("errore durante il caricamento del lotto.", e);
			}

			if (lottoLoad instanceof LottoInterno) {
				listResult.addAll(caricaMovimentazioneLotto(TipoLotto.LOTTO_INTERNO, lottoLoad));
			} else {
				listResult.addAll(caricaMovimentazioneLotto(TipoLotto.LOTTO, lottoLoad));
			}
		}

		return listResult;
	}

	@Override
	public List<MovimentazioneLotto> caricaMovimentazioneLotto(Lotto lotto) {
		return caricaMovimentazioneLotto(TipoLotto.LOTTO, lotto);
	}

	/**
	 * Carica la movimentazione del lotto.
	 *
	 * @param tipoLotto
	 *            tipoLotto
	 * @param lotto
	 *            lotto di riferimento
	 * @return movimentazione del lotto
	 */
	@SuppressWarnings("unchecked")
	private List<MovimentazioneLotto> caricaMovimentazioneLotto(TipoLotto tipoLotto, Lotto lotto) {
		logger.debug("--> Enter caricaMovimentazioneLotto");

		// carico i movimenti di carico, scarico, inventario e trasferimento
		// scarico
		Query query1 = panjeaDAO.prepareQuery(MovimentazioneLottoQueryBuilder.getMovimentiQuery1(tipoLotto));
		((org.hibernate.ejb.HibernateQuery) query1).getHibernateQuery()
				.setResultTransformer(Transformers.aliasToBean(MovimentazioneLotto.class));

		// carico i movimenti di trasferimento carico
		Query query2 = panjeaDAO.prepareQuery(MovimentazioneLottoQueryBuilder.getMovimentiQuery2(tipoLotto));
		((org.hibernate.ejb.HibernateQuery) query2).getHibernateQuery()
				.setResultTransformer(Transformers.aliasToBean(MovimentazioneLotto.class));

		query1.setParameter(MovimentazioneLottoQueryBuilder.PARAM_LOTTO, lotto);
		query2.setParameter(MovimentazioneLottoQueryBuilder.PARAM_LOTTO, lotto);

		List<MovimentazioneLotto> listResult1;
		List<MovimentazioneLotto> listResult2;
		try {
			listResult1 = panjeaDAO.getResultList(query1);
			listResult2 = panjeaDAO.getResultList(query2);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento della movimentazione del lotto " + lotto.getCodice(), e);
			throw new RuntimeException(
					"errore durante il caricamento della movimentazione del lotto " + lotto.getCodice(), e);
		}

		List<MovimentazioneLotto> listResult = new ArrayList<MovimentazioneLotto>();
		listResult.addAll(listResult1);
		listResult.addAll(listResult2);

		logger.debug("--> Exit caricaMovimentazioneLotto");
		return listResult;
	}

	@Override
	public List<MovimentazioneLotto> caricaMovimentazioneLottoInterno(LottoInterno lotto) {

		LottoInterno lottoInterno;
		try {
			lottoInterno = panjeaDAO.load(LottoInterno.class, lotto.getId());
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento del lotto interno " + lotto.getId(), e);
			throw new RuntimeException("errore durante il caricamento del lotto interno " + lotto.getId(), e);
		}

		return caricaMovimentazioneLotto(TipoLotto.LOTTO_INTERNO, lottoInterno.getLotto());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaLotto> caricaRigheLotto(AreaMagazzino areaMagazzino) {
		logger.debug("--> Enter caricaRigheLotto");

		StringBuilder hql = new StringBuilder();
		hql.append("select rl from RigaLotto rl ");
		hql.append("inner join fetch rl.rigaArticolo ra ");
		hql.append("inner join fetch ra.areaMagazzino am ");
		hql.append("inner join rl.lotto l ");
		hql.append("where am.id=:paramIdAreaMagazzino");

		Query query = panjeaDAO.prepareQuery(hql.toString());
		query.setParameter("paramIdAreaMagazzino", areaMagazzino.getId());

		List<RigaLotto> righeLotto = query.getResultList();
		logger.debug("--> Exit caricaRigheLotto");
		return righeLotto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaLotto> caricaRigheLotto(RigaArticolo rigaArticolo) {
		logger.debug("--> Enter caricaRigheLotto");
		StringBuilder hql = new StringBuilder();
		hql.append("select rl from RigaLotto rl ");
		hql.append("inner join fetch rl.rigaArticolo ra ");
		hql.append("inner join rl.lotto l ");
		hql.append("where ra.id=:paramIdRigaArticolo");

		Query query = panjeaDAO.prepareQuery(hql.toString());
		query.setParameter("paramIdRigaArticolo", rigaArticolo.getId());

		List<RigaLotto> righeLotto = query.getResultList();
		logger.debug("--> Exit caricaRigheLotto");
		return righeLotto;
	}

	@Override
	public List<StatisticaLotto> caricaSituazioneLotti(ArticoloLite articoloLite) {
		List<StatisticaLotto> statistiche = new ArrayList<StatisticaLotto>();

		if (articoloLite == null || articoloLite.getTipoLotto() == TipoLotto.NESSUNO) {
			return statistiche;
		}

		return caricaSituazioneLotti(articoloLite, null, null, null, RimanenzaLotto.TUTTE, null, null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StatisticaLotto> caricaSituazioneLotti(ArticoloLite articoloLite, DepositoLite deposito, Date data,
			List<CategoriaLite> categorieArticolo, RimanenzaLotto rimanenzaLotto, Date dataScadenza, String codiceLotto,
			Date dataInizioRicercaScadenza) {
		logger.debug("--> Enter caricaSituazioneLotti");

		List<StatisticaLotto> statistiche = new ArrayList<StatisticaLotto>();

		if (articoloLite != null && articoloLite.getTipoLotto() == TipoLotto.NESSUNO) {
			return statistiche;
		}

		boolean filtraArticolo = articoloLite != null && articoloLite.getId() != null;
		boolean filtraCategorie = categorieArticolo != null && !categorieArticolo.isEmpty();
		boolean filtraDeposito = deposito != null && !deposito.isNew();

		Query query = panjeaDAO.prepareSQLQuery(
				SituazioneLottiQueryBuilder.getSituazioneLottiArticolo(filtraDeposito, filtraArticolo, filtraCategorie,
						data, rimanenzaLotto, dataScadenza, codiceLotto, dataInizioRicercaScadenza),
				StatisticaLotto.class, SituazioneLottiQueryBuilder.getAlias());
		if (filtraCategorie) {
			List<Integer> idCategorieList = new ArrayList<Integer>();
			for (CategoriaLite categoriaLite : categorieArticolo) {
				idCategorieList.add(categoriaLite.getId());
			}
			query.setParameter("categorie", idCategorieList);
		}
		if (filtraArticolo) {
			query.setParameter("articolo", articoloLite.getId());
		}
		if (data != null) {
			query.setParameter("paramData", data);
		}
		if (filtraDeposito) {
			query.setParameter("deposito", deposito.getId());
		}
		if (dataScadenza != null) {
			query.setParameter("paramDataScadenza", new SimpleDateFormat("yyyy-MM-dd").format(dataScadenza));
		}
		if (!StringUtils.isBlank(codiceLotto)) {
			query.setParameter("paramCodiceLotto", codiceLotto);
		}
		if (dataInizioRicercaScadenza != null) {
			query.setParameter("paramDataInizioDataScadenza",
					new SimpleDateFormat("yyyy-MM-dd").format(dataInizioRicercaScadenza));
		}
		List<StatisticaLotto> results;
		try {
			results = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dei lotti.", e);
			throw new RuntimeException("errore durante il caricamento dei lotti.", e);
		}
		statistiche.addAll(results);

		logger.debug("--> Exit caricaSituazioneLotti");
		return statistiche;
	}

	/**
	 *
	 * @return codice azienda loggata
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

	@Override
	public Lotto salvaLotto(Lotto lotto) {
		logger.debug("--> Enter salvaLotto");

		lotto.setCodiceAzienda(getAzienda());

		Lotto lottoSalvato = null;
		try {
			lottoSalvato = panjeaDAO.save(lotto);
			lottoSalvato.getArticolo().getId();
		} catch (Exception e) {
			logger.error("-->errore durante il salvataggio del lotto", e);
			throw new RuntimeException("errore durante il salvataggio del lotto", e);
		}

		logger.debug("--> Exit salvaLotto");
		return lottoSalvato;
	}

}
