/**
 *
 */
package it.eurotn.panjea.contabilita.manager.ritenutaacconto;

import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.contabilita.util.ParametriSituazioneRitenuteAcconto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @author fattazzo
 *
 *         Crea l'sql per il caricamento della situazione delle ritenute di acconto. Creo 3 sql separati e uso una union
 *         per avere 3 sql più semplice (?) da getire piuttosto di uno da inifinite righe.
 */
public final class SituazioneRateRitenuteQueryBuilder {

	/**
	 * @return alias per le colonne
	 */
	public static String[] getAlias() {
		return new String[] { "idAreaContabile", "protocollo", "protocolloOrder", "dataRegistrazione", "tributo",
				"percentualeAliquota", "idTipoDocumento", "codiceTipoDocumento", "descrizioneTipoDocumento",
				"idDocumento", "totaleDocumento", "numeroDocumento", "numeroDocumentoOrder", "idFornitore",
				"codiceFornitore", "denominazioneFornitore", "idRata", "residuo", "totalePagato", "dataPagamento",
				"totaleRitenute", "totaleRitenuteAperte", "totaleRitenutePagate", "ultimaDataPagamentoRitenuta" };
	}

	/**
	 * Sql per il caricamento delle ritenute pagate.
	 *
	 * @return sql
	 */
	private static String getRateRitenuteChiuseSQL() {
		StringBuilder sb = new StringBuilder(500);
		sb.append("select areacont.id as idAc, ");
		sb.append("	  rata.id as idrata, ");
		sb.append("	  0 as residuo, ");
		sb.append("	  0 as totalePagato, ");
		sb.append("	  null as dataPagamento, ");
		sb.append("	  0 as totaleRitenute, ");
		sb.append("	  0 as totaleRitenuteAperte, ");
		sb.append("	  coalesce(sum(rataRit.importoInValutaAzienda),0) as totaleRitenutePagate, ");
		sb.append("	  max(pagRit.dataPagamento) as ultimaDataPagamentoRitenuta ");
		sb.append("from part_rate rata inner join part_area_partite areaPart on rata.areaRate_id=areaPart.id ");
		sb.append("				inner join part_pagamenti pag on rata.id=pag.rata_id ");
		sb.append("				inner join cont_area_contabile areacont on areacont.documento_id = areaPart.documento_id ");
		sb.append("				inner join cont_tipi_area_contabile tac on tac.id = areacont.tipoAreaContabile_id ");
		sb.append("				inner join part_rate rataRit on rataRit.pagamentoRiferimento_id = pag.id ");
		sb.append("				inner join  part_pagamenti pagRit on pagRit.rata_id = rataRit.id ");
		sb.append("where tac.ritenutaAcconto = true and rataRit.ritenutaAcconto = true and areacont.statoAreaContabile < 2 ");
		sb.append("group by areacont.id,rata.id ");

		return sb.toString();
	}

	/**
	 * Sql per il caricamento delle ritenute da pagare ( rata del fornitore pagata e pagamento della ritenuta non
	 * presente ).
	 *
	 * @return sql
	 */
	private static String getRateRitenuteDaPagareSQL() {
		StringBuilder sb = new StringBuilder(500);
		sb.append("select areacont.id as idAc, ");
		sb.append("	  rata.id as idrata, ");
		sb.append("	  0 as residuo, ");
		sb.append("	  0 as totalePagato, ");
		sb.append("	  null as dataPagamento, ");
		sb.append("	  0 as totaleRitenute, ");
		sb.append("	  coalesce(sum(rataRit.importoInValutaAzienda),0) as totaleRitenuteAperte, ");
		sb.append("	  0 as totaleRitenutePagate, ");
		sb.append("	  null as ultimaDataPagamentoRitenuta ");
		sb.append("from part_rate rata inner join part_area_partite areaPart on rata.areaRate_id=areaPart.id ");
		sb.append("				inner join part_pagamenti pag on rata.id=pag.rata_id ");
		sb.append("				inner join cont_area_contabile areacont on areacont.documento_id = areaPart.documento_id ");
		sb.append("				inner join cont_tipi_area_contabile tac on tac.id = areacont.tipoAreaContabile_id ");
		sb.append("				inner join part_rate rataRit on rataRit.pagamentoRiferimento_id = pag.id ");
		sb.append("				left join  part_pagamenti pagRit on pagRit.rata_id = rataRit.id ");
		sb.append("where tac.ritenutaAcconto = true and rataRit.ritenutaAcconto = true and pagRit.id is null and areacont.statoAreaContabile < 2 ");
		sb.append("group by areacont.id,rata.id ");

		return sb.toString();
	}

	/**
	 * Sql per il caricamento delle rate ( residuo e totale pagato ) e del totale delle rate di ritenuta.
	 *
	 * @return sql
	 */
	private static String getRateSQL() {
		StringBuilder sb = new StringBuilder(500);
		sb.append("select areacont.id as idAc, ");
		sb.append("	  rata.id as idRata, ");
		sb.append("	  rata.importoInValutaAzienda - (select coalesce(sum(pagAll.importoInValutaAzienda),0)+coalesce(sum(pagAll.importoInValutaAziendaForzato),0) ");
		sb.append("	  						   from part_pagamenti pagAll where rata.id = pagAll.rata_id and pagAll.insoluto=0) as residuo, ");
		sb.append("	  coalesce(pag.importoInValutaAzienda,0)+coalesce(pag.importoInValutaAziendaForzato,0) as totalePagato, ");
		sb.append("	  pag.dataPagamento as dataPagamento, ");
		sb.append("	  (select coalesce(sum(rateRit.importoInValutaAzienda),0) from part_rate rateRit where rateRit.ritenutaAcconto = true and rateRit.areaRate_id = areaPart.id) as totaleRitenute, ");
		sb.append("	  0 as totaleRitenuteAperte, ");
		sb.append("	  0 as totaleRitenutePagate, ");
		sb.append("	  null as ultimaDataPagamentoRitenuta ");
		sb.append("from part_rate rata inner join part_area_partite areaPart on rata.areaRate_id=areaPart.id ");
		sb.append("				left join part_pagamenti pag on rata.id=pag.rata_id and pag.insoluto=0 ");
		sb.append("				inner join cont_area_contabile areacont on areacont.documento_id = areaPart.documento_id ");
		sb.append("				inner join cont_tipi_area_contabile tac on tac.id = areacont.tipoAreaContabile_id ");
		sb.append("where tac.ritenutaAcconto = true and rata.ritenutaAcconto = false and areacont.statoAreaContabile < 2 ");

		return sb.toString();
	}

	/**
	 * Sql per il caricamento della situazione delle ritenute d'acconto.
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @return sql creato
	 */
	public static String getSQL(ParametriSituazioneRitenuteAcconto parametri) {
		StringBuilder sb = new StringBuilder(2000);

		sb.append("select idAc as idAreaContabile, ");
		sb.append("	  	   areacont.codice as protocollo, ");
		sb.append("	  	   areacont.codiceOrder as protocolloOrder, ");
		sb.append("	  	   areacont.dataRegistrazione as dataRegistrazione, ");
		sb.append("		   areacont.tributo as tributo, ");
		sb.append("         areacont.percentualeAliquota as percentualeAliquota, ");
		sb.append("	  	   tipodoc.id as idTipoDocumento, ");
		sb.append("	  	   tipodoc.codice as codiceTipoDocumento, ");
		sb.append("	  	   tipodoc.descrizione as descrizioneTipoDocumento, ");
		sb.append("		   doc.id as idDocumento, ");
		sb.append("	  	   doc.importoInValutaAzienda as totaleDocumento, ");
		sb.append("	  	   doc.codice as numeroDocumento, ");
		sb.append("	  	   doc.codiceOrder as numeroDocumentoOrder, ");
		sb.append("		   ent.id as idFornitore, ");
		sb.append("		   ent.codice as codiceFornitore, ");
		sb.append("		   anag.denominazione as denominazioneFornitore, ");
		sb.append("	  	   idRata as idRata, ");
		sb.append("	  	   sum(residuo) as residuo, ");
		sb.append("	  	   sum(totalePagato) as totalePagato, ");
		sb.append("	  	   max(dataPagamento) as dataPagamento, ");
		sb.append("	  	   max(totaleRitenute) as totaleRitenute, ");
		sb.append("	  	   sum(totaleRitenuteAperte) as totaleRitenuteAperte, ");
		sb.append("	  	   sum(totaleRitenutePagate) as totaleRitenutePagate, ");
		sb.append("	  	   max(ultimaDataPagamentoRitenuta) as ultimaDataPagamentoRitenuta ");
		sb.append("from (");
		sb.append(getRateSQL());
		sb.append(" union ");
		sb.append(getRateRitenuteDaPagareSQL());
		sb.append(" union ");
		sb.append(getRateRitenuteChiuseSQL());
		sb.append(") as situazione inner join cont_area_contabile areacont on areacont.id = situazione.idAc  ");
		sb.append("                      inner join docu_documenti doc on areacont.documento_id=doc.id ");
		sb.append("                      inner join docu_tipi_documento tipodoc on doc.tipo_documento_id=tipodoc.id ");
		sb.append("                      inner join anag_entita ent on ent.id = doc.entita_id ");
		sb.append("                      inner join anag_anagrafica anag on anag.id = ent.anagrafica_id ");
		sb.append("where (doc.entita_id = :paramEntita or :paramTutteEntita = 1) and ");
		sb.append("          areacont.dataRegistrazione >= :paramDataIniziale and areacont.dataRegistrazione <= :paramDataFinale");
		sb.append("group by idAc,idRata ");

		// replace dei parametri
		String sql = sb.toString();

		FornitoreLite fornitore = parametri.getFornitore();
		sql = sql.replaceAll(":paramEntita", fornitore == null ? "0" : fornitore.getId().toString());
		sql = sql.replaceAll(":paramTutteEntita", fornitore == null ? "1" : "0");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		sql = sql.replaceAll(":paramDataIniziale", "'" + dateFormat.format(parametri.getPeriodo().getDataIniziale())
				+ "'");
		sql = sql.replaceAll(":paramDataFinale", "'" + dateFormat.format(parametri.getPeriodo().getDataFinale()) + "'");

		return sql;
	}

	private SituazioneRateRitenuteQueryBuilder() {

	}

}
