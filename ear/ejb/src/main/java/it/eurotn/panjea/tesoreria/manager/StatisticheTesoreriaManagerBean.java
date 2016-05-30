package it.eurotn.panjea.tesoreria.manager;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rate.manager.interfaces.RateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.manager.interfaces.StatisticheTesoreriaManager;
import it.eurotn.panjea.tesoreria.manager.interfaces.TesoreriaSettingsManager;
import it.eurotn.panjea.tesoreria.util.SituazioneRata;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.StatisticheTesoreriaManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.StatisticheTesoreriaManager")
public class StatisticheTesoreriaManagerBean implements StatisticheTesoreriaManager {
	private static Logger logger = Logger.getLogger(StatisticheTesoreriaManagerBean.class);

	@EJB
	private PanjeaDAO panjeaDAO;

	@EJB
	private RateManager rateManager;

	@EJB
	private TesoreriaSettingsManager tesoreriaSettingsManager;

	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal calcolaImportoDocumentiAperti(EntitaLite entitaLite) {
		logger.debug("--> Enter calcolaImportoDocumentiAperti");
		if (tesoreriaSettingsManager.caricaSettings().getTipiDocumentoFido() == null
				|| tesoreriaSettingsManager.caricaSettings().getTipiDocumentoFido().size() == 0) {
			return BigDecimal.ZERO;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("select sum(distinct doc.importoInValutaAzienda) ");
		sb.append("from maga_area_magazzino am ");
		sb.append("inner join docu_documenti doc on doc.id=am.documento_id ");
		sb.append("inner join maga_righe_magazzino rm on rm.areaMagazzino_id=am.id ");
		sb.append("left join maga_righe_magazzino rmc on rmc.rigaMagazzinoCollegata_id=rm.id ");
		sb.append("where doc.entita_id=");
		sb.append(entitaLite.getId());
		sb.append(" and rm.TIPO_RIGA='A' ");
		sb.append("and doc.tipo_documento_id in (");
		Iterator<TipoDocumento> iteratorTd = tesoreriaSettingsManager.caricaSettings().getTipiDocumentoFido()
				.iterator();
		while (iteratorTd.hasNext()) {
			TipoDocumento obj = iteratorTd.next();
			sb.append(obj.getId());
			if (iteratorTd.hasNext()) {
				sb.append(",");
			}
		}
		sb.append(") and rmc.id is null ");
		sb.append("group by doc.entita_id ");
		SQLQuery query = panjeaDAO.prepareNativeQuery(sb.toString());
		List<BigDecimal> result = query.list();
		BigDecimal importo = BigDecimal.ZERO;
		if (result.size() == 1) {
			importo = (BigDecimal) query.list().get(0);
		}
		logger.debug("--> Exit calcolaImportoDocumentiAperti: " + importo);
		return importo;
	}

	@Override
	public BigDecimal calcolaImportoRateAperte(EntitaLite entita) {
		logger.debug("--> Enter calcolaImportoRateAperte");
		// Genero una query nativa per ottimizzare il più possibile.
		// devo fare una subquery perchè devo calcolare il totale rate(che non posso mettere in join con i
		// pagamenti),totale pagamenti e tot riemessi
		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("sum(rate.importoInValutaAzienda) - ");
		sb.append("(( ");
		sb.append("select ");
		sb.append("sum(ifnull((p.importoInValutaAzienda + p.importoInValutaAziendaForzato),0)) ");
		sb.append("from part_rate r ");
		sb.append("left join part_pagamenti p on p.rata_id=r.id ");
		sb.append("inner join part_area_partite ap on ap.id=r.areaRate_id ");
		sb.append("inner join docu_documenti doc on doc.id=ap.documento_id ");
		sb.append("inner join anag_entita entp on doc.entita_id=entp.id ");
		sb.append("where entp.id=:entitaId)- sum(if(rate.dataScadenza is null,rate.importoInValutaAzienda,0)-if (rate.rataRiemessa_id is not null,rate.importoInValutaAzienda,0))) ");
		sb.append("from part_rate rate ");
		sb.append("inner join part_area_partite ap on ap.id=rate.areaRate_id ");
		sb.append("inner join docu_documenti doc on doc.id=ap.documento_id ");
		sb.append("where doc.entita_id=:entitaId ");
		javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
		query.setParameter("entitaId", entita.getId());
		BigDecimal result = null;
		try {
			result = (BigDecimal) panjeaDAO.getSingleResult(query);
		} catch (Exception e) {
			logger.error("-->errore nel calcolare l'importo aperto per il cliente " + entita.getId(), e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit calcolaImportoRateAperte");
		return result;
	}

	@Override
	public List<SituazioneRata> caricaSituazioneRateDaUtilizzarePerAcconto(Integer idEntita, TipoPartita tipoPartita) {
		logger.debug("--> Enter caricaSituazioneRate");
		ParametriRicercaRate parametriRicercaRate = ParametriRicercaRate
				.creaParametriRicercaRateDaUtilizzarePerAcconto(idEntita, tipoPartita);
		List<SituazioneRata> rate = rateManager.ricercaRate(parametriRicercaRate);
		logger.debug("--> Exit caricaSituazioneRate");
		return rate;
	}

	@Override
	public List<SituazioneRata> caricaSituazioneRateStampa(TipoPartita tipoPartita, Date dataInizio, Date dataFine,
			Integer idEntita, Boolean stampaDettaglio, Integer idCategoriaEntita) {

		ParametriRicercaRate parametri = new ParametriRicercaRate();

		Periodo periodo = new Periodo();
		periodo.setTipoPeriodo(TipoPeriodo.DATE);
		periodo.setDataIniziale(dataInizio);
		periodo.setDataFinale(dataFine);
		parametri.setDataScadenza(periodo);

		if (idEntita != null) {
			EntitaLite entitaLite = new EntitaLite();
			entitaLite.setId(idEntita);
			parametri.setEntita(entitaLite);
		}

		parametri.setTipoPartita(tipoPartita);

		if (idCategoriaEntita != null) {
			CategoriaEntita categoriaEntita = new CategoriaEntita();
			categoriaEntita.setId(idCategoriaEntita);
			parametri.setCategoriaEntita(categoriaEntita);
		}

		Set<StatoRata> statiRata = new TreeSet<StatoRata>();
		statiRata.add(StatoRata.APERTA);
		statiRata.add(StatoRata.PAGAMENTO_PARZIALE);
		statiRata.add(StatoRata.IN_RIASSEGNAZIONE);
		parametri.setStatiRata(statiRata);

		// Escludo i pagamenti F24
		Set<TipoPagamento> tipiPagamentoDaEscludere = new TreeSet<TipoPagamento>();
		tipiPagamentoDaEscludere.add(TipoPagamento.F24);
		parametri.setTipiPagamentoEsclusi(tipiPagamentoDaEscludere);

		parametri.setImportoIniziale(null);

		parametri.setStampaDettaglio(stampaDettaglio);
		return rateManager.ricercaRate(parametri);
	}
}
