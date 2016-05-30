package it.eurotn.panjea.conai.manager;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.conai.domain.ConaiArticolo;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;
import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiTipoImballo;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.conai.domain.ConaiParametriCreazione;
import it.eurotn.panjea.conai.domain.RigaConaiArticolo;
import it.eurotn.panjea.conai.domain.StatisticaTipoImballo;
import it.eurotn.panjea.conai.manager.interfaces.ConaiManager;
import it.eurotn.panjea.conai.manager.interfaces.PdfGenerator;
import it.eurotn.panjea.conai.util.AnalisiConaiDTO;
import it.eurotn.panjea.conai.util.parametriricerca.ParametriRicercaAnalisi;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.ConaiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ConaiManager")
public class ConaiManagerBean implements ConaiManager {

	private static Logger logger = Logger.getLogger(ConaiManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private PdfGenerator pdfGenerator;

	@Resource
	private SessionContext sessionContext;

	@Override
	public void cancellaArticoloConai(ConaiArticolo conaiArticolo) {
		logger.debug("--> Enter cancellaArticoloConai");
		try {
			panjeaDAO.delete(conaiArticolo);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione del'articolo conai " + conaiArticolo.getId(), e);
			throw new RuntimeException("Errore durante la cancellazione dell'articolo conai " + conaiArticolo.getId(),
					e);
		}
		logger.debug("--> Exit cancellaArticoloConai");
	}

	@Override
	public void cancellaComponenteConai(ConaiComponente conaiComponente) {
		logger.debug("--> Enter cancellaComponenteConai");
		try {
			panjeaDAO.delete(conaiComponente);
		} catch (Exception e) {
			logger.error("--> Errore durante la cancellazione del componente conai " + conaiComponente.getId(), e);
			throw new RuntimeException("Errore durante la cancellazione del componente conai "
					+ conaiComponente.getId(), e);
		}
		logger.debug("--> Exit cancellaComponenteConai");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AnalisiConaiDTO> caricaAnalisiConali(ParametriRicercaAnalisi parametri) {
		logger.debug("--> Enter caricaAnalisiConali");

		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("doc.id as idDocumento, ");
		sb.append("doc.codice as numeroDocumento, ");
		sb.append("doc.dataDocumento as dataDocumento, ");
		sb.append("tipoDoc.codice as codiceTipoDocumento, ");
		sb.append("tipoDoc.descrizione as descrizioneTipoDocumento, ");
		sb.append("tam.valoriFatturato as valoriFatturato, ");
		sb.append("ent.id as idEntita, ");
		sb.append("ent.codice as codiceEntita, ");
		sb.append("anag.denominazione as denominazioneEntita, ");
		sb.append("art.id as idArticolo, ");
		sb.append("art.codice as codiceArticolo, ");
		sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
		sb.append("crc.materiale as materiale, ");
		sb.append("crc.tipoImballo as tipoImballo, ");
		sb.append("rigaMaga.qta as qtaRigaArticolo, ");
		sb.append("crc.pesoUnitario as pesoUnitarioConai, ");
		sb.append("crc.pesoEsenzione as pesoEsenzioneConai, ");
		sb.append("crc.percentualeEsenzione as percentualeEsenzioneConai, ");
		sb.append("coalesce(rm.importoInValutaAziendaNetto,0) as prezzoNettoConai, ");
		sb.append("coalesce(rm.numeroDecimaliQta,6) as numeroDecimaliPesoConai, ");
		sb.append("coalesce(rm.numeroDecimaliPrezzo,6) as numeroDecimaliPrezzoConai ");
		sb.append("from maga_conai_righe_componente crc ");
		sb.append("left join maga_righe_magazzino rigaMaga on crc.rigaArticolo_id=rigaMaga.id ");
		sb.append("left join maga_area_magazzino am on rigaMaga.areaMagazzino_id=am.id ");
		sb.append("left join maga_tipi_area_magazzino tam on am.tipoAreaMagazzino_id=tam.id ");
		sb.append("left join docu_documenti doc on am.documento_id=doc.id ");
		sb.append("left join docu_tipi_documento tipoDoc on doc.tipo_documento_id=tipoDoc.id ");
		sb.append("left outer join anag_entita ent on doc.entita_id=ent.id ");
		sb.append("left outer join anag_anagrafica anag on ent.anagrafica_id=anag.id ");
		sb.append("left join maga_articoli art on rigaMaga.articolo_id=art.id ");
		sb.append("left join maga_righe_magazzino rm on rm.TIPO_RIGA='CN' and rm.areaMagazzino_id=am.id ");
		sb.append("left join maga_conai_articoli ca on rm.articolo_id=ca.articolo_id and crc.materiale=ca.materiale ");
		sb.append("where tam.gestioneConai=1 ");
		sb.append("and am.statoAreaMagazzino in (0,2) ");
		if (parametri.getPeriodo().getDataIniziale() != null) {
			sb.append("and am.dataRegistrazione>=:paramDataIniziale ");
		}
		if (parametri.getPeriodo().getDataFinale() != null) {
			sb.append("and am.dataRegistrazione<=:paramDataFinale ");
		}
		if (parametri.getCliente() != null && parametri.getCliente().getId() != null) {
			sb.append("          and doc.entita_id = :cliente ");
		}
		if (parametri.getTipiAreaMagazzino() != null) {
			if (parametri.getTipiAreaMagazzino().size() == 1) {
				TipoAreaMagazzino tipoAreaMagazzino = parametri.getTipiAreaMagazzino().iterator().next();
				sb.append(" and tipoDoc.id =" + tipoAreaMagazzino.getTipoDocumento().getId());
			} else if (parametri.getTipiAreaMagazzino().size() > 1) {
				sb.append(" and tipoDoc.id in (");
				for (TipoAreaMagazzino tipoAreaMagazzino : parametri.getTipiAreaMagazzino()) {
					sb.append(tipoAreaMagazzino.getTipoDocumento().getId() + ",");
				}
				// cancello l'ultima virgola che e' in piu' e chiudo la parentesi della IN
				sb.deleteCharAt(sb.length() - 1).append(") ");
			}
		}

		QueryImpl query = (QueryImpl) panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
		SQLQuery sqlQuery = ((SQLQuery) query.getHibernateQuery());
		sqlQuery.setResultTransformer(Transformers.aliasToBean((AnalisiConaiDTO.class)));

		if (parametri.getPeriodo().getDataIniziale() != null) {
			query.setParameter("paramDataIniziale", parametri.getPeriodo().getDataIniziale(), TemporalType.DATE);
		}

		if (parametri.getPeriodo().getDataFinale() != null) {
			query.setParameter("paramDataFinale", parametri.getPeriodo().getDataFinale(), TemporalType.DATE);
		}

		if (parametri.getCliente() != null && parametri.getCliente().getId() != null) {
			query.setParameter("cliente", parametri.getCliente().getId());
		}

		sqlQuery.addScalar("idDocumento");
		sqlQuery.addScalar("numeroDocumento");
		sqlQuery.addScalar("dataDocumento");
		sqlQuery.addScalar("codiceTipoDocumento");
		sqlQuery.addScalar("descrizioneTipoDocumento");
		sqlQuery.addScalar("valoriFatturato");
		sqlQuery.addScalar("idEntita");
		sqlQuery.addScalar("codiceEntita");
		sqlQuery.addScalar("denominazioneEntita");
		sqlQuery.addScalar("idArticolo");
		sqlQuery.addScalar("codiceArticolo");
		sqlQuery.addScalar("descrizioneArticolo");
		sqlQuery.addScalar("materiale");
		sqlQuery.addScalar("tipoImballo");
		sqlQuery.addScalar("qtaRigaArticolo");
		sqlQuery.addScalar("pesoUnitarioConai");
		sqlQuery.addScalar("pesoEsenzioneConai");
		sqlQuery.addScalar("percentualeEsenzioneConai");
		sqlQuery.addScalar("prezzoNettoConai");
		sqlQuery.addScalar("numeroDecimaliPesoConai", Hibernate.INTEGER);
		sqlQuery.addScalar("numeroDecimaliPrezzoConai", Hibernate.INTEGER);

		List<AnalisiConaiDTO> analisi = null;
		try {
			analisi = sqlQuery.list();
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dell'analisi CONAI", e);
			throw new RuntimeException("errore durante il caricamento dell'analisi CONAI", e);
		}

		logger.debug("--> Exit caricaAnalisiConali");
		return analisi;
	}

	@Override
	public List<ConaiArticolo> caricaArticoliConai() {
		logger.debug("--> Enter caricaArticoliConai");
		List<ConaiArticolo> articoliConai = new ArrayList<ConaiArticolo>();
		ConaiMateriale[] materiali = ConaiMateriale.values();
		for (ConaiMateriale materiale : materiali) {
			ConaiArticolo articoloConai = caricaArticoloConai(materiale);
			articoliConai.add(articoloConai);
		}

		logger.debug("--> Exit caricaArticoliConai");
		return articoliConai;
	}

	@Override
	public ConaiArticolo caricaArticoloConai(ConaiMateriale materiale) {
		Query query = panjeaDAO.prepareNamedQuery("ConaiArticolo.caricaByMateriale");
		query.setParameter("paramCodiceAzienda", getAzienda());
		query.setParameter("paramMateriale", materiale);

		ConaiArticolo articoloConai = null;
		try {
			articoloConai = (ConaiArticolo) panjeaDAO.getSingleResult(query);
		} catch (ObjectNotFoundException e) {
			logger.warn("--> Nessun articolo conai trovato per il materiale " + materiale.name()
					+ ", ne creo uno nuovo");
			articoloConai = new ConaiArticolo(materiale);
			articoloConai.setCodiceAzienda(getAzienda());
		} catch (Exception e) {
			logger.error("--> Errore nella ricerca di articolo conai per il materiale " + materiale.name());
			throw new RuntimeException("--> Errore nella ricerca di articolo conai per il materiale "
					+ materiale.name(), e);
		}
		return articoloConai;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConaiComponente> caricaComponentiConai(ArticoloLite articolo) {
		Query query = panjeaDAO.prepareNamedQuery("ConaiComponente.caricaByArticolo");
		query.setParameter("paramIdArticolo", articolo.getId());

		List<ConaiComponente> componentiConaiArticolo = null;
		try {
			componentiConaiArticolo = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> Errore nella ricerca dei componenti conai per l'articolo con id " + articolo.getId());
			throw new RuntimeException("--> Errore nella ricerca dei componenti conai per l'articolo con id "
					+ articolo.getId(), e);
		}
		return componentiConaiArticolo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RigaConaiArticolo> caricaRigheConai(AreaMagazzino areaMagazzino) {
		Query queryRigheConaiPresenti = panjeaDAO.prepareNamedQuery("RigaConaiArticolo.caricaByAreaMagazzino");
		queryRigheConaiPresenti.setParameter("areaMagazzino", areaMagazzino);
		List<RigaConaiArticolo> righeConai = null;
		try {
			righeConai = panjeaDAO.getResultList(queryRigheConaiPresenti);
		} catch (DAOException e) {
			logger.error("-->errore nel recuperare le righe per il conai.AreaMagazzino " + areaMagazzino, e);
			throw new RuntimeException(e);
		}
		return righeConai;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoAreaMagazzino> caricaTipiAreaMagazzinoConGestioneConai() {
		logger.debug("--> Enter caricaTipiAreaMagazzinoConGestioneConai");

		Query query = panjeaDAO.prepareNamedQuery("TipoAreaMagazzino.caricaByGestioneConaiAttiva");
		query.setParameter("paramCodiceAzienda", getAzienda());

		List<TipoAreaMagazzino> tipiArea = null;
		try {
			tipiArea = panjeaDAO.getResultList(query);
		} catch (Exception e) {
			logger.error("--> errore durante il caricamento dei TAM con gestione conai", e);
			throw new RuntimeException("errore durante il caricamento dei TAM con gestione conai", e);
		}

		logger.debug("--> Exit caricaTipiAreaMagazzinoConGestioneConai");
		return tipiArea;
	}

	/**
	 *
	 * @param dataIniziale
	 *            data iniziale
	 * @param dataFinale
	 *            data finale
	 * @param materiale
	 *            materiale per il qiale caricare le righe
	 * @return statistiche per i tipi imballi
	 */
	private List<StatisticaTipoImballo> caricaValoriComponentiByMateriale(ConaiMateriale materiale, Date dataIniziale,
			Date dataFinale) {
		StringBuilder sb = new StringBuilder();
		sb.append("select sum(rc.pesoUnitario * ra.qtaMagazzino),sum(rc.pesoEsenzione),rc.materiale,rc.tipoImballo ");
		sb.append("from RigaConaiComponente rc join rc.rigaArticolo ra ");
		sb.append("where rc.materiale=:materiale ");
		sb.append("and ra.areaMagazzino.tipoAreaMagazzino.valoriFatturato=true ");
		sb.append("and ra.areaMagazzino.statoAreaMagazzino in (0,2) ");
		sb.append("and ra.areaMagazzino.dataRegistrazione between :dataIniziale and :dataFinale ");
		sb.append("group by rc.tipoImballo ");
		Query query = panjeaDAO.prepareQuery(sb.toString());
		query.setParameter("dataIniziale", dataIniziale, TemporalType.DATE);
		query.setParameter("dataFinale", dataFinale, TemporalType.DATE);
		query.setParameter("materiale", materiale);
		@SuppressWarnings("unchecked")
		List<Object[]> queryResult = query.getResultList();
		ArrayList<StatisticaTipoImballo> result = new ArrayList<StatisticaTipoImballo>();
		for (Object[] risultato : queryResult) {
			StatisticaTipoImballo statistica = new StatisticaTipoImballo();
			statistica
			.setPesoTotale(risultato[0] == null ? BigDecimal.ZERO : BigDecimal.valueOf((Double) risultato[0]));
			statistica.setPesoPerEsenzione(risultato[1] == null ? BigDecimal.ZERO : (BigDecimal) risultato[1]);
			statistica.setMateriale((ConaiMateriale) risultato[2]);
			statistica.setTipoImballo((ConaiTipoImballo) risultato[3]);
			result.add(statistica);
		}
		return result;
	}

	@Override
	public byte[] generaModulo(ConaiParametriCreazione parametri) {
		ConaiMateriale materiale = parametri.getMateriale();
		ConaiArticolo conaiArticolo = caricaArticoloConai(materiale);
		BigDecimal prezzoMateriale = conaiArticolo.getPrezzo(parametri.getDataIniziale());
		Date dataIniziale = parametri.getDataIniziale();
		Date dataFinale = parametri.getDataFinale();
		parametri.setPrezzoMateriale(prezzoMateriale);

		List<StatisticaTipoImballo> statistiche = caricaValoriComponentiByMateriale(materiale, dataIniziale, dataFinale);
		return pdfGenerator.generaFile(parametri, statistiche);
	}

	/**
	 * @return codice azienda loggata
	 */
	private String getAzienda() {
		return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
	}

	@Override
	public ConaiArticolo salvaArticoloConai(ConaiArticolo conaiArticolo) {
		logger.debug("--> Enter salvaArticoloConai");
		ConaiArticolo articoloConai = null;
		try {
			conaiArticolo.aggiornaReferenzeConaiArticolo();
			articoloConai = panjeaDAO.save(conaiArticolo);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio di conai articolo", e);
			throw new RuntimeException("Errore durante il salvataggio di conai articolo", e);
		}
		logger.debug("--> Exit salvaArticoloConai");
		return articoloConai;
	}

	@Override
	public ConaiComponente salvaComponenteConai(ConaiComponente conaiComponente) {
		logger.debug("--> Enter salvaComponenteConai");
		ConaiComponente componente = null;
		try {
			componente = panjeaDAO.save(conaiComponente);
		} catch (Exception e) {
			logger.error("--> Errore durante il salvataggio di componente conai", e);
			throw new RuntimeException("Errore durante il salvataggio di componente conai", e);
		}
		logger.debug("--> Exit salvaComponenteConai");
		return componente;
	}

}
