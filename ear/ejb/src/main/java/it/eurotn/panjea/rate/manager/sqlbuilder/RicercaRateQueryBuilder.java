package it.eurotn.panjea.rate.manager.sqlbuilder;

import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.Query;

public final class RicercaRateQueryBuilder {

	/**
	 * @param parametri
	 *            parametri
	 * @param panjeaDAO
	 *            panjeaDAO
	 * @return query generata
	 */
	public static Query getQuery(ParametriRicercaRate parametri, PanjeaDAO panjeaDAO) {

		Map<String, Object> valueParametri = new TreeMap<String, Object>();

		StringBuilder sb = new StringBuilder();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		sb.append("select ");
		// dati per entitaDocumento
		sb.append("doc.entita_id as idEntita, ");
		sb.append("tipodoc.tipoEntita as tipoEntita, ");
		sb.append("entita.codice as codiceEntita, ");
		sb.append("coalesce(anag.denominazione,doc.codiceAzienda) as denominazione, ");
		sb.append("anag.id as idAnagraficaEntita, ");
		sb.append("sedeAnag.indirizzo as indirizzo, ");
		sb.append("nazione.codice as nazione, ");
		sb.append("lvl1.nome as livelloAmministrativo1, ");
		sb.append("lvl2.nome as livelloAmministrativo2, ");
		sb.append("lvl3.nome as livelloAmministrativo3, ");
		sb.append("lvl4.nome as livelloAmministrativo4, ");
		sb.append("localita.descrizione as localita, ");
		sb.append("cap.descrizione as cap, ");
		// dati per la sede entita
		sb.append("sedeEnt.id as idSedeEntita, ");
		sb.append("sedeEnt.codice as codiceSedeEntita, ");
		sb.append("sedeAnagEnt.descrizione as descrizioneSedeEntita, ");
		sb.append("localitaSedeAnagEnt.descrizione as localitaSedeEntita, ");
		sb.append("zonaGeog.id as idZonaGeografica, ");
		sb.append("zonaGeog.codice as codiceZonaGeografica, ");
		sb.append("zonaGeog.descrizione as descrizioneZonaGeografica, ");
		// dati per il documento
		sb.append("doc.id as idDocumento, ");
		sb.append("doc.importoInValuta as totaleDocumentoInValuta, ");
		sb.append("doc.importoInValutaAzienda as totaleDocumentoInValutaAzienda, ");
		sb.append("doc.codiceValuta as totaleDocumentoCodiceValuta, ");
		sb.append("doc.dataDocumento as dataDocumento, ");
		sb.append("doc.codice as numeroDocumento, ");
		// dati per il tipoDocumento
		sb.append("tipodoc.id as idTipoDocumento, ");
		sb.append("tipodoc.codice as codiceTipoDocumento, ");
		sb.append("tipodoc.descrizione as descrizioneTipoDocumento, ");
		// dati per la rata
		sb.append("rata.id as idRata, ");
		sb.append("rata.version as versioneRata, ");
		sb.append("tipoareapart.tipoPartita as tipoPartita, ");
		sb.append("rata.dataScadenza as dataScadenza, ");
		sb.append("rata.tipoPagamento as tipoPagamento, ");
		sb.append("rata.importoInValutaAzienda as importoInValutaAzienda, ");
		sb.append("rata.importoInValuta as importoInValuta, ");
		sb.append("rata.tassoDiCambio as importoTassoDiCambio, ");
		sb.append("rata.codiceValuta as codiceValuta, ");
		sb.append("rata.numeroRata as numeroRata, ");
		sb.append("rata.ritenutaAcconto as ritenutaAcconto, ");
		sb.append("rata.note as noteRata, ");
		sb.append("(select coalesce(SUM(r.importoInValuta),0) from part_rate r where r.rataRiemessa_id = rata.id) as importoInValutaRateCollegate, ");
		sb.append("(select coalesce(SUM(r.importoInValutaAzienda),0) from part_rate r where r.rataRiemessa_id = rata.id) as importoInValutaAziendaRateCollegate, ");
		// dati rata riemessa
		sb.append("rataRiemessa.id as idRataRiemessa, ");
		sb.append("rataRiemessa.version as versioneRataRiemessa, ");
		sb.append("rataRiemessa.numeroRata as numeroRataRiemessa, ");
		// dati area rata
		sb.append("areaPart.giorniLimite as giorniLimitiScontoFinanziario, ");
		sb.append("areaPart.percentualeSconto as percentualeScontoFinanziario, ");
		sb.append("areaPart.id as idAreaRate, ");
		// codice pagamento
		sb.append("codPag.id as idCodicePagamento, ");
		sb.append("codPag.codicePagamento as codiceCodicePagamento, ");
		sb.append("codPag.descrizione as descrizioneCodicePagamento, ");
		// dati areacontabile
		sb.append("areacont.codice as protocollo, ");
		sb.append("areacont.id as idAreaContabile, ");
		// area pagamento
		sb.append("areaPartPag.dataScadenzaAnticipoFatture as dataScadenzaAnticipoFatture, ");
		// importo pagato
		sb.append("coalesce(");
		sb.append("sum(pag.importoInValuta),0)+coalesce(sum(pag.importoInValutaForzato), 0");
		sb.append(") as totalePagatoValuta, ");
		sb.append("coalesce(");
		sb.append("sum(pag.importoInValutaAzienda),0)+coalesce(sum(pag.importoInValutaAziendaForzato), 0");
		sb.append(") as totalePagatoValutaAzienda, ");
		// data massima dei pagamenti
		sb.append("max(pag.dataPagamento) as maxDataPagamento, ");
		// numero di pagamenti
		sb.append("count(pag.id) as numPagamenti, ");
		// agente
		sb.append("(select agente.id from maga_righe_magazzino rigaMaga inner join anag_entita agente on rigaMaga.agente_id=agente.id inner join anag_anagrafica anagAgente on anagAgente.id = agente.anagrafica_id where rigaMaga.areaMagazzino_id = areaMaga.id limit 1) as idAgente, ");
		sb.append("(select agente.codice from maga_righe_magazzino rigaMaga inner join anag_entita agente on rigaMaga.agente_id=agente.id inner join anag_anagrafica anagAgente on anagAgente.id = agente.anagrafica_id where rigaMaga.areaMagazzino_id = areaMaga.id limit 1) as codiceAgente, ");
		sb.append("(select anagAgente.denominazione from maga_righe_magazzino rigaMaga inner join anag_entita agente on rigaMaga.agente_id=agente.id inner join anag_anagrafica anagAgente on anagAgente.id = agente.anagrafica_id where rigaMaga.areaMagazzino_id = areaMaga.id limit 1) as denominazioneAgente, ");
		sb.append("max(areacontPag.id) as idAreaContabilePagamenti ");
		sb.append("from part_rate rata ");
		sb.append("inner join part_area_partite areaPart on rata.areaRate_id=areaPart.id ");
		sb.append("inner join docu_documenti doc on areaPart.documento_id=doc.id ");
		sb.append("inner join docu_tipi_documento tipodoc on doc.tipo_documento_id=tipodoc.id ");
		sb.append("inner join part_tipi_area_partita tipoareapart on areaPart.tipoAreaPartita_id=tipoareapart.id ");
		sb.append("inner join part_codici_pagamento codPag on areaPart.codicePagamento_id=codPag.id ");
		sb.append("left join part_pagamenti pag on rata.id=pag.rata_id and pag.insoluto=0 ");
		sb.append("left join cont_area_contabile areacont on areacont.documento_id = areaPart.documento_id ");
		sb.append("left join anag_entita entita on doc.entita_id=entita.id ");
		sb.append("left join anag_sedi_entita sedeEnt on doc.sedeEntita_id=sedeEnt.id ");
		sb.append("left join anag_sedi_anagrafica sedeAnagEnt on sedeEnt.sede_anagrafica_id=sedeAnagEnt.id ");
		if (parametri.getCategoriaEntita() != null) {
			sb.append("inner join anag_sedi_entita_anag_categoria_entita entita_categorie on entita_categorie.anag_sedi_entita_id=sedeEnt.id ");
			sb.append("inner join anag_categoria_entita cat_entita on cat_entita.id=entita_categorie.categoriaEntita_id ");
		}
		sb.append("left join geog_localita localitaSedeAnagEnt on localitaSedeAnagEnt.id=sedeAnagEnt.localita_id ");
		sb.append("left join anag_zone_geografiche zonaGeog on zonaGeog.id=sedeEnt.zonaGeografica_id ");
		sb.append("left join anag_anagrafica anag on entita.anagrafica_id=anag.id ");
		sb.append("left join anag_sedi_anagrafica sedeAnag on sedeAnag.id=anag.sede_anagrafica_id ");
		sb.append("left join geog_nazioni nazione on nazione.id=sedeAnag.nazione_id ");
		sb.append("left join geog_livello_1 lvl1 on lvl1.id=sedeAnag.lvl1_id ");
		sb.append("left join geog_livello_2 lvl2 on lvl2.id=sedeAnag.lvl2_id ");
		sb.append("left join geog_livello_3 lvl3 on lvl3.id=sedeAnag.lvl3_id ");
		sb.append("left join geog_livello_4 lvl4 on lvl4.id=sedeAnag.lvl4_id ");
		sb.append("left join geog_localita localita on localita.id=sedeAnag.localita_id ");
		sb.append("left join geog_cap cap on cap.id=sedeAnag.cap_localita_id ");
		sb.append("left join anag_rapporti_bancari rappBancEntita on rata.rapportoBancarioEntita_id = rappBancEntita.id ");
		sb.append("left join maga_area_magazzino areaMaga on areaMaga.documento_id = doc.id ");
		sb.append("left join part_rate rataRiemessa on rataRiemessa.id=rata.rataRiemessa_id ");
		sb.append("left join part_area_partite areaPartPag on pag.areaChiusure_id = areaPartPag.id ");
		sb.append("left join docu_documenti docPag on areaPartPag.documento_id=docPag.id ");
		sb.append("left join cont_area_contabile areacontPag on areacontPag.documento_id = areaPartPag.documento_id ");
		sb.append("where ");

		if (parametri.getCompensazione() != null && parametri.getCompensazione()) {
			sb.append("anag.id = :paramAnagraficaId ");
			valueParametri.put("paramAnagraficaId", parametri.getAnagrafica().getId());
		} else {
			sb.append("tipoareapart.tipoPartita=:paramTipoAreaPartita ");
			valueParametri.put("paramTipoAreaPartita", parametri.getTipoPartita().ordinal());
		}

		if (parametri.getCategoriaEntita() != null) {
			sb.append(" and cat_entita.id=").append(parametri.getCategoriaEntita().getId()).append(" ");
		}

		if (parametri.getSoloEntita() != null && parametri.getSoloEntita().booleanValue()) {
			sb.append(" and (entita.id is not null) ");
		}

		if (parametri.getTipiPagamento() != null && !parametri.getTipiPagamento().isEmpty()) {
			if (parametri.getTipiPagamento().size() != 1) {
				String tipiPag = "";
				for (TipoPagamento tipoPagamento : parametri.getTipiPagamento()) {
					tipiPag = tipiPag + tipoPagamento.ordinal() + ",";
				}
				tipiPag = tipiPag.substring(0, tipiPag.length() - 1);
				sb.append(" and (rata.tipoPagamento in (" + tipiPag + ")) ");
			} else {
				sb.append(" and (rata.tipoPagamento=" + parametri.getTipiPagamento().iterator().next().ordinal() + ") ");
			}
		}

		if (parametri.getTipiPagamentoEsclusi() != null && !parametri.getTipiPagamentoEsclusi().isEmpty()) {
			if (parametri.getTipiPagamentoEsclusi().size() != 1) {
				String tipiPagEsclusi = "";
				for (TipoPagamento tipoPagamentoEscluso : parametri.getTipiPagamentoEsclusi()) {
					tipiPagEsclusi = tipiPagEsclusi + tipoPagamentoEscluso.ordinal() + ",";
				}
				tipiPagEsclusi = tipiPagEsclusi.substring(0, tipiPagEsclusi.length() - 1);
				sb.append(" and (rata.tipoPagamento not in (" + tipiPagEsclusi + ")) ");
			} else {
				sb.append(" and (rata.tipoPagamento<>"
						+ parametri.getTipiPagamentoEsclusi().iterator().next().ordinal() + ") ");
			}
		}

		// Se ho lo stato insoluto o riemesso da visualizzare e ho almeno una
		// data impostata devo filtrare alnche per la
		// dataScadenza a null
		String sqlRataRiemessaPresente = "and ";
		if (parametri.getStatiRata().contains(StatoRata.RIEMESSA)
				|| parametri.getStatiRata().contains(StatoRata.IN_RIASSEGNAZIONE)) {
			sqlRataRiemessaPresente = "and ( rata.dataScadenza is null  or ( ";
		}

		if (parametri.getDataScadenza().getDataIniziale() != null) {
			sb.append(sqlRataRiemessaPresente);
			sqlRataRiemessaPresente = " and ";
			sb.append("rata.dataScadenza>=:paramDataInizio ");
			valueParametri.put("paramDataInizio", dateFormat.format(parametri.getDataScadenza().getDataIniziale()));
		}

		if (parametri.getDataScadenza().getDataFinale() != null) {
			sb.append(sqlRataRiemessaPresente);
			sb.append("rata.dataScadenza<=:paramDataFine ");
			valueParametri.put("paramDataFine", dateFormat.format(parametri.getDataScadenza().getDataFinale()));
		}

		if (parametri.getDataScadenza().isPeriodoNull()) {
			// imposto cmq sempre una data di inizio per ottimizzare la query
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(Long.MIN_VALUE));
			sb.append(sqlRataRiemessaPresente);
			sb.append(" rata.dataScadenza>=:paramDataInizio ");
			valueParametri.put("paramDataInizio", dateFormat.format(calendar.get(Calendar.ERA)));
		}

		// Se ho inserito il filtro per la rataRiemessa chiudo la parentesi
		if (sb.toString().contains("rata.dataScadenza is null")) {
			sb.append(" )) ");
		}

		if (parametri.getEntita() != null && parametri.getEntita().getId() != null) {
			sb.append(" and entita.id=:paramIdEntita ");
			valueParametri.put("paramIdEntita", parametri.getEntita().getId());

			if (parametri.getSedeEntita() != null && parametri.getSedeEntita().getId() != null) {
				sb.append(" and sedeEnt.id=:paramIdSedeEntita ");
				valueParametri.put("paramIdSedeEntita", parametri.getSedeEntita().getId());
			}
		}

		if (parametri.getImportoIniziale() != null) {
			sb.append(" and rata.importoInValuta>= :paramImportoDa ");
			valueParametri.put("paramImportoDa", parametri.getImportoIniziale());
		}

		if (parametri.getImportoFinale() != null) {
			sb.append(" and rata.importoInValuta<= :paramImportoA ");
			valueParametri.put("paramImportoA", parametri.getImportoFinale());
		}

		if (parametri.getZonaGeografica() != null && parametri.getZonaGeografica().getId() != null) {
			sb.append(" and zonaGeog.id = :paramIdZonaGeografica");
			valueParametri.put("paramIdZonaGeografica", parametri.getZonaGeografica().getId());
		}

		if (parametri.getRapportoBancarioAzienda() != null && parametri.getRapportoBancarioAzienda().getId() != null) {
			sb.append(" and rata.rapportoBancarioAzienda_id= :paramRapportoBancarioAzienda");
			valueParametri.put("paramRapportoBancarioAzienda", parametri.getRapportoBancarioAzienda().getId());
		}

		if (parametri.getCodiceValuta() != null) {
			sb.append(" and rata.codiceValuta= :paramCodiceValuta");
			valueParametri.put("paramCodiceValuta", parametri.getCodiceValuta());
		}

		if (parametri.getBancaEntita() != null && parametri.getBancaEntita().getId() != null) {
			sb.append(" and rappBancEntita.banca_id = :paramRappBancEntita");
			valueParametri.put("paramRappBancEntita", parametri.getBancaEntita().getId());
		}

		if (parametri.getCodicePagamento() != null && parametri.getCodicePagamento().getId() != null) {
			sb.append(" and codPag.id = :paramCodicePagamento");
			valueParametri.put("paramCodicePagamento", parametri.getCodicePagamento().getId());
		}

		sb.append(" group by rata.id ");

		String having = " having ";
		if (parametri.getEscludiPagate() != null && parametri.getEscludiPagate().booleanValue()) {
			sb.append(" having coalesce(sum(pag.importoInValutaAzienda),0)+coalesce(sum(pag.importoInValutaAziendaForzato), 0)<>rata.importoInValutaAzienda ");
			having = " and ";
		}

		if (parametri.getAgente() != null && parametri.getAgente().getId() != null) {
			sb.append(having);
			sb.append(" idAgente = :paramIdAgente ");
			valueParametri.put("paramIdAgente", parametri.getAgente().getId());
		}

		if (parametri.getStampaDettaglio() != null) {
			if (parametri.getStampaDettaglio().booleanValue()) {
				sb.append(" order by entita.id,rata.dataScadenza ");
			} else {
				sb.append(" order by entita.id,doc.codiceOrder,rata.numeroRata ");
			}
		} else {
			sb.append(" order by null ");
		}

		javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
		Set<String> set = valueParametri.keySet();
		for (String key : set) {
			query.setParameter(key, valueParametri.get(key));
		}

		return query;
	}

	/**
	 * 
	 * @param tipoPartita
	 *            tipo partita
	 * @param dataIniziale
	 *            data iniziale
	 * @param dataFinale
	 *            data finale
	 * @param entita
	 *            entità
	 * @param stampaDettaglio
	 *            stampa del dettaglio
	 * @param panjeaDAO
	 *            panjeaDAO
	 * @return query creata
	 */
	public static Query getQuerySituazioneRateStampa(TipoPartita tipoPartita, Date dataIniziale, Date dataFinale,
			EntitaLite entita, boolean stampaDettaglio, PanjeaDAO panjeaDAO) {

		Map<String, Object> valueParametri = new TreeMap<String, Object>();

		StringBuilder sb = new StringBuilder();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		sb.append("select ");
		sb.append("doc.entita_id as idEntita, ");
		sb.append("entita.codice as codiceEntita, ");
		sb.append("anag.denominazione as denominazione, ");
		sb.append("sedeAnag.indirizzo as indirizzo, ");
		sb.append("localita.descrizione as localita, ");
		sb.append("doc.dataDocumento as dataDocumento, ");
		sb.append("doc.codice as numeroDocumento, ");
		sb.append("doc.importoInValutaAzienda as totaleDocumentoInValutaAzienda, ");
		sb.append("rata.id as idRata, ");
		sb.append("rata.version as versioneRata, ");
		sb.append("rata.dataScadenza as dataScadenza, ");
		sb.append("rata.tipoPagamento as tipoPagamento, ");
		sb.append("rata.importoInValutaAzienda as importoInValutaAzienda, ");
		sb.append("rata.numeroRata as numeroRata, ");
		// sb.append("(select coalesce(SUM(r.id),0) from part_rate r where r.rataRiemessa_id = rata.id) as numeroRateCollegate, ");
		sb.append("rataColl.numeroRata as numeroRataRiemessa, ");
		sb.append("areacont.codice as protocollo, ");
		sb.append("coalesce(sum(pag.importoInValutaAzienda),0)+coalesce(sum(pag.importoInValutaAziendaForzato), 0) as totalePagatoValutaAzienda, ");
		sb.append("max(pag.dataPagamento) as maxDataPagamento ");
		sb.append("from part_rate rata force index (dataScadenza) left outer join part_pagamenti pag on rata.id=pag.rata_id and pag.insoluto=0 ");
		sb.append("									  inner join part_area_partite areaPart on rata.areaRate_id=areaPart.id ");
		sb.append("									  left join cont_area_contabile areacont on areacont.documento_id = areaPart.documento_id ");
		sb.append("									  inner join docu_documenti doc on areaPart.documento_id=doc.id ");
		sb.append("									  inner join part_tipi_area_partita tipoareapart on areaPart.tipoAreaPartita_id=tipoareapart.id ");
		sb.append("									  left join anag_entita entita on doc.entita_id=entita.id ");
		sb.append("									  left join anag_anagrafica anag on entita.anagrafica_id=anag.id ");
		sb.append("									  left join anag_sedi_anagrafica sedeAnag on sedeAnag.id=anag.sede_anagrafica_id ");
		sb.append("									  left join geog_localita localita on localita.id=sedeAnag.localita_id ");
		sb.append("                                     left join part_rate rataColl on rataColl.id=rata.rataRiemessa_id ");
		sb.append("where tipoareapart.tipoPartita=:paramTipoAreaPartita ");
		valueParametri.put("paramTipoAreaPartita", tipoPartita.ordinal());

		sb.append("	 and rata.tipoPagamento <> 5 ");
		sb.append("	 and (rata.dataScadenza is null or (rata.dataScadenza>=:paramDataInizio and rata.dataScadenza<=:paramDataFine)) ");
		valueParametri.put("paramDataInizio", dateFormat.format(dataIniziale));
		valueParametri.put("paramDataFine", dateFormat.format(dataFinale));

		if (entita != null && entita.getId() != null) {
			sb.append(" and entita.id=:paramIdEntita ");
			valueParametri.put("paramIdEntita", entita.getId());
		}

		sb.append("	 group by rata.id ");
		sb.append(" having coalesce(sum(pag.importoInValutaAzienda),0)+coalesce(sum(pag.importoInValutaAziendaForzato), 0)<>rata.importoInValutaAzienda ");

		if (stampaDettaglio) {
			sb.append(" order by entita.id,rata.dataScadenza ");
		} else {
			sb.append(" order by entita.id,doc.codiceOrder,rata.numeroRata ");
		}

		javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sb.toString());
		Set<String> set = valueParametri.keySet();
		for (String key : set) {
			query.setParameter(key, valueParametri.get(key));
		}

		return query;
	}

	/**
	 * Costruttore.
	 * 
	 */
	private RicercaRateQueryBuilder() {
		super();
	}
}
