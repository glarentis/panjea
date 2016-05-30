package it.eurotn.panjea.cauzioni.manager;

import it.eurotn.panjea.cauzioni.manager.interfaces.CauzioniManager;
import it.eurotn.panjea.cauzioni.util.parametriricerca.MovimentazioneCauzioneDTO;
import it.eurotn.panjea.cauzioni.util.parametriricerca.ParametriRicercaSituazioneCauzioni;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniDTO;
import it.eurotn.panjea.cauzioni.util.parametriricerca.SituazioneCauzioniEntitaDTO;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(mappedName = "Panjea.CauzioniManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.CauzioniManager")
public class CauzioniManagerBean implements CauzioniManager {

	private static Logger logger = Logger.getLogger(CauzioniManagerBean.class);

	@Resource
	private SessionContext context;

	@EJB
	private PanjeaDAO panjeaDAO;

	@SuppressWarnings("unchecked")
	@Override
	public List<MovimentazioneCauzioneDTO> caricaMovimentazioneCauzioniArticolo(Set<Integer> idEntita,
			Set<Integer> idSedeEntita, Set<Integer> idArticolo) {
		logger.debug("--> Enter caricaMovimentazioneCauzioniArticolo");

		String sqlSituazioniQuery = SituazioniCauzioniQueryBuilder.getSQLMovimentazioneCauzioniEntita(getAzienda(),
				idEntita, idSedeEntita, idArticolo);

		org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
				.createNativeQuery(sqlSituazioniQuery);
		SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
		sqlQuery.setResultTransformer(Transformers.aliasToBean(MovimentazioneCauzioneDTO.class));

		List<MovimentazioneCauzioneDTO> movimentazione = new ArrayList<MovimentazioneCauzioneDTO>();

		try {
			sqlQuery.addScalar("idAreaMagazzino");
			sqlQuery.addScalar("dataRegistrazione");
			sqlQuery.addScalar("dataDocumento");
			sqlQuery.addScalar("idTipoDocumento");
			sqlQuery.addScalar("codiceTipoDocumento");
			sqlQuery.addScalar("descrizioneTipoDocumento");
			sqlQuery.addScalar("idSedeEntita");
			sqlQuery.addScalar("codiceSedeEntita");
			sqlQuery.addScalar("descrizioneSedeEntita");
			sqlQuery.addScalar("numeroDocumento", Hibernate.STRING);
			sqlQuery.addScalar("dati");
			sqlQuery.addScalar("resi");
			sqlQuery.addScalar("importoDati");
			sqlQuery.addScalar("importoResi");
			sqlQuery.addScalar("numeroDecimaliQta");
			movimentazione = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in caricaMovimentazioneCauzioniArticolo", e);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit caricaMovimentazioneCauzioniArticolo");
		return movimentazione;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SituazioneCauzioniDTO> caricaSituazioneCauzioni(ParametriRicercaSituazioneCauzioni parametri) {
		logger.debug("--> Enter caricaSituazioneCauzioni");

		String sqlSituazioniQuery = SituazioniCauzioniQueryBuilder.getSQLSituazioneCauzioni(getAzienda(),
				parametri.getEntita(), parametri.getPeriodo().getDataIniziale(),
				parametri.getPeriodo().getDataFinale(), parametri.getArticolo());

		org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
				.createNativeQuery(sqlSituazioniQuery);
		SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
		sqlQuery.setResultTransformer(Transformers.aliasToBean(SituazioneCauzioniDTO.class));

		List<SituazioneCauzioniDTO> situazioni = new ArrayList<SituazioneCauzioniDTO>();

		try {
			sqlQuery.addScalar("idEntita");
			sqlQuery.addScalar("codiceEntita");
			sqlQuery.addScalar("descrizioneEntita");
			sqlQuery.addScalar("tipoEntita");
			sqlQuery.addScalar("idSedeEntita");
			sqlQuery.addScalar("codiceSedeEntita");
			sqlQuery.addScalar("descrizioneSedeEntita");
			sqlQuery.addScalar("idCategoria");
			sqlQuery.addScalar("codiceCategoria");
			sqlQuery.addScalar("descrizioneCategoria");
			sqlQuery.addScalar("dati");
			sqlQuery.addScalar("resi");
			sqlQuery.addScalar("saldo");
			sqlQuery.addScalar("importoDati");
			sqlQuery.addScalar("importoResi");
			sqlQuery.addScalar("saldoImporto");
			sqlQuery.addScalar("idArticolo");
			sqlQuery.addScalar("codiceArticolo");
			sqlQuery.addScalar("descrizioneArticolo");
			situazioni = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in caricaSituazioneCauzioni", e);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit caricaSituazioneCauzioni");
		return situazioni;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SituazioneCauzioniDTO> caricaSituazioneCauzioniAreaMagazzino(AreaMagazzino areaMagazzino) {
		logger.debug("--> Enter caricaSituazioneCauzioniAreaMagazzino");

		List<SituazioneCauzioniDTO> situazioni = new ArrayList<SituazioneCauzioniDTO>();

		AreaMagazzino areaMagazzinoLoad;
		try {
			areaMagazzinoLoad = panjeaDAO.load(AreaMagazzino.class, areaMagazzino.getId());
		} catch (Exception e1) {
			logger.error("--> errore durante il caricamento dell'area magazzino.", e1);
			throw new RuntimeException("errore durante il caricamento dell'area magazzino.", e1);
		}

		if (areaMagazzinoLoad.getDocumento().getSedeEntita() != null
				&& areaMagazzinoLoad.getDocumento().getSedeEntita().getId() != null) {
			String sqlSituazioniQuery = SituazioniCauzioniQueryBuilder
					.getSQLSituazioneCauzioniAreaMagazzino(areaMagazzinoLoad);

			org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
					.createNativeQuery(sqlSituazioniQuery);
			SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
			sqlQuery.setResultTransformer(Transformers.aliasToBean(SituazioneCauzioniDTO.class));

			try {
				sqlQuery.addScalar("idCategoria");
				sqlQuery.addScalar("codiceCategoria");
				sqlQuery.addScalar("descrizioneCategoria");
				sqlQuery.addScalar("dati");
				sqlQuery.addScalar("resi");
				sqlQuery.addScalar("saldo");
				sqlQuery.addScalar("saldoEntita");
				situazioni = sqlQuery.list();
			} catch (Exception e) {
				logger.error("--> errore in caricaSituazioneCauzioni", e);
				throw new RuntimeException(e);
			}
		}

		logger.debug("--> Exit caricaSituazioneCauzioni");
		return situazioni;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SituazioneCauzioniEntitaDTO> caricaSituazioneCauzioniEntita(Integer idEntita, boolean raggruppamentoSedi) {
		logger.debug("--> Enter caricaSituazioneCauzioniEntita");

		String sqlSituazioniQuery = SituazioniCauzioniQueryBuilder.getSQLSituazioneCauzioniEntita(getAzienda(),
				idEntita, raggruppamentoSedi);

		org.hibernate.ejb.QueryImpl queryImpl = (org.hibernate.ejb.QueryImpl) panjeaDAO.getEntityManager()
				.createNativeQuery(sqlSituazioniQuery);
		SQLQuery sqlQuery = ((SQLQuery) queryImpl.getHibernateQuery());
		sqlQuery.setResultTransformer(Transformers.aliasToBean(SituazioneCauzioniEntitaDTO.class));

		List<SituazioneCauzioniEntitaDTO> situazioni = new ArrayList<SituazioneCauzioniEntitaDTO>();

		try {
			sqlQuery.addScalar("idEntita");
			sqlQuery.addScalar("codiceEntita");
			sqlQuery.addScalar("descrizioneEntita");
			sqlQuery.addScalar("tipoEntita");
			if (raggruppamentoSedi) {
				sqlQuery.addScalar("idSedeEntita");
				sqlQuery.addScalar("codiceSedeEntita");
				sqlQuery.addScalar("descrizioneSedeEntita");
			}
			sqlQuery.addScalar("idArticolo");
			sqlQuery.addScalar("codiceArticolo");
			sqlQuery.addScalar("descrizioneArticolo");
			sqlQuery.addScalar("dati");
			sqlQuery.addScalar("resi");
			sqlQuery.addScalar("saldo");
			sqlQuery.addScalar("importoDati");
			sqlQuery.addScalar("importoResi");
			sqlQuery.addScalar("saldoImporto");
			sqlQuery.addScalar("numeroDecimaliQta");
			situazioni = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore in caricaRigheArticoloMovimentazione", e);
			throw new RuntimeException(e);
		}

		logger.debug("--> Exit caricaSituazioneCauzioniEntita");
		return situazioni;
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
