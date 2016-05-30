/**
 * 
 */
package it.eurotn.panjea.tesoreria.manager.sqlbuilder;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.tesoreria.domain.Effetto.StatoEffetto;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Costruisce la query per la ricerca effetti.
 * 
 * @author fattazzo
 * 
 */
public final class RicercaEffettiQueryBuilder {

	/**
	 * Restituisce l'SQL per la ricerca degli effetti.
	 * 
	 * @param codiceAzienda
	 *            codice azienda
	 * @param numeroDocumento
	 *            numero documento distinta bancaria da ricercare
	 * @param rapportoBancarioAzienda
	 *            rapporto bancaria
	 * @param daDataValuta
	 *            data iniziale della ricerca
	 * @param aDataValuta
	 *            data finale della ricerca
	 * @param entitaLite
	 *            entita
	 * @param statiEffetto
	 *            stati effetto da cercare
	 * @param daImporto
	 *            importo iniziale
	 * @param aImporto
	 *            importo finale
	 * @param escludiEffettiAccreditati
	 *            esclude gli effetti accreditati
	 * @return sql creato
	 */
	public static String getSQLRicercaEffetti(String codiceAzienda, CodiceDocumento numeroDocumento,
			RapportoBancarioAzienda rapportoBancarioAzienda, Date daDataValuta, Date aDataValuta,
			EntitaLite entitaLite, List<StatoEffetto> statiEffetto, Importo daImporto, Importo aImporto,
			boolean escludiEffettiAccreditati) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		StringBuilder sbSelect = new StringBuilder();
		sbSelect.append("select effetti.id as idEffetto, ");
		sbSelect.append("      effetti.version as versionEffetto, ");
		sbSelect.append("	  effetti.dataValuta as dataValuta, ");
		sbSelect.append("	  rappBancario.id as idRapportoBancario, ");
		sbSelect.append("      rappBancario.version as versionRapportoBancario, ");
		sbSelect.append("	  rappBancario.descrizione as descrizioneRapportoBancario, ");
		sbSelect.append("	  rappBancario.speseInsoluto as speseInsolutoRapportoBancario, ");
		sbSelect.append("	  tipoDoc.codice as codiceTipoDocumento, ");
		sbSelect.append("	  tipoDoc.descrizione as descrizioneTipoDocumento, ");
		sbSelect.append("      tipoDoc.tipoEntita as tipoEntitaTipoDocumento, ");
		sbSelect.append("	  doc.id as idDocumento, ");
		sbSelect.append("	  doc.codice as numeroDocumento, ");
		sbSelect.append("	  doc.dataDocumento as dataDocumento, ");
		sbSelect.append("	  tipoDocAreaDistinta.codice as codiceTipoDocumentoDistinta, ");
		sbSelect.append("	  tipoDocAreaDistinta.descrizione as descrizioneTipoDocumentoDistinta, ");
		sbSelect.append("      tipoDocAreaDistinta.tipoEntita as tipoEntitaTipoDocumentoDistinta, ");
		sbSelect.append("	  docAreaDistinta.id as idDocumentoDistinta, ");
		sbSelect.append("	  docAreaDistinta.codice as numeroDocumentoDistinta, ");
		sbSelect.append("	  docAreaDistinta.dataDocumento as dataDocumentoDistinta, ");
		sbSelect.append("	  areaDistinta.id as idAreaDistinta, ");
		sbSelect.append("	  areaDistinta.version as versionAreaDistinta, ");
		sbSelect.append("	  entitaRata.id as idEntitaRata, ");
		sbSelect.append("	  entitaRata.codice as codiceEntitaRata, ");
		sbSelect.append("	  anagRata.denominazione as denominazioneEntitaRata, ");
		sbSelect.append("      entitaRata.TIPO_ANAGRAFICA as tipoEntitaRata, ");
		sbSelect.append("	  sum(pag.importoInValutaAzienda) as importo, ");
		sbSelect.append("      pag.codiceValuta  as codiceValuta, ");
		sbSelect.append("	  pag.dataPagamento as dataPagamento, ");
		sbSelect.append("	  pag.insoluto as insoluto, ");
		sbSelect.append("      effetti.dataScadenza as dataScadenza, ");
		sbSelect.append("      areaEffetto.id as idAreaEffetto, ");
		sbSelect.append("      areaEffetto.version as versionAreaEffetto ");

		StringBuffer sb = new StringBuffer();
		sb.append("from part_effetti effetti inner join part_area_partite areaEffetto on (effetti.areaEffetti_id = areaEffetto.id) ");
		sb.append("					 inner join docu_documenti doc on (areaEffetto.documento_id = doc.id) ");
		sb.append("					 inner join docu_tipi_documento tipoDoc on (doc.tipo_documento_id = tipoDoc.id) ");
		sb.append("					 inner join anag_rapporti_bancari rappBancario on (doc.rapporto_bancario_azienda_id = rappBancario.id) ");
		sb.append("					 left join part_pagamenti pag on (effetti.id = pag.effetto_id) ");
		sb.append("					 left join part_rate rate on (pag.rata_id = rate.id) ");
		sb.append("					 left join part_area_partite areaRate on (rate.areaRate_id = areaRate.id) ");
		sb.append("					 left join docu_documenti docAreaRate on (areaRate.documento_id = docAreaRate.id) ");
		sb.append("					 left join anag_entita entitaRata on (docAreaRate.entita_id = entitaRata.id) ");
		sb.append("					 left join anag_anagrafica anagRata on (entitaRata.anagrafica_id = anagRata.id) ");
		sb.append("					 left join part_area_partite areaDistinta on ( areaEffetto.id = areaDistinta.areaEffettiCollegata_id) ");
		sb.append("					 left join docu_documenti docAreaDistinta on (areaDistinta.documento_id = docAreaDistinta.id) ");
		sb.append("					 left join docu_tipi_documento tipoDocAreaDistinta on (docAreaDistinta.tipo_documento_id = tipoDocAreaDistinta.id) ");
		sb.append(" where doc.codiceAzienda='" + codiceAzienda + "' ");

		// numero documento distinta bancaria da ricercare
		if (!StringUtils.isEmpty(numeroDocumento.getCodice())) {
			sb.append(" and docAreaDistinta.codiceOrder = " + numeroDocumento.getCodiceOrder());
		}

		// rapporto bancario della distinta bancaria
		boolean isBancaScelta = rapportoBancarioAzienda != null && rapportoBancarioAzienda.getId() != null
				&& rapportoBancarioAzienda.getId().intValue() != -1;
		if (isBancaScelta) {
			sb.append(" and rappBancario.id = " + rapportoBancarioAzienda.getId());
		}

		// da data valuta su effetto
		if (daDataValuta != null) {
			sb.append(" and  effetti.dataValuta >='" + dateFormat.format(daDataValuta) + "' ");
		}

		// a data valuta su effetto
		if (aDataValuta != null) {
			sb.append(" and  effetti.dataValuta <='" + dateFormat.format(aDataValuta) + "' ");
		}

		// Filtro entita
		if (entitaLite != null && entitaLite.getId() != null) {
			sb.append(" and entitaRata.id = " + entitaLite.getId());
		}

		// Filtro effetti accreditati
		if (escludiEffettiAccreditati) {
			sb.append(" and pag.dataPagamento is null ");
		}

		if (statiEffetto.size() > 0) {
			sb.append(" and (");

			String whereSatiEffetto = "";
			for (StatoEffetto statoEffetto : statiEffetto) {
				if (statoEffetto == StatoEffetto.PRESENTATO) {
					if (whereSatiEffetto.length() > 0) {
						whereSatiEffetto = whereSatiEffetto + " or ";
					}
					whereSatiEffetto = whereSatiEffetto + " (pag.dataPagamento is null and pag.insoluto = false) ";
				} else if (statoEffetto == StatoEffetto.CHIUSO) {
					if (whereSatiEffetto.length() > 0) {
						whereSatiEffetto = whereSatiEffetto + " or ";
					}
					whereSatiEffetto = whereSatiEffetto + " (pag.dataPagamento is not null and pag.insoluto = false) ";
				} else if (statoEffetto == StatoEffetto.INSOLUTO) {
					if (whereSatiEffetto.length() > 0) {
						whereSatiEffetto = whereSatiEffetto + " or ";
					}
					whereSatiEffetto = whereSatiEffetto + " (pag.insoluto = true) ";
				}
			}
			sb.append(whereSatiEffetto + ") ");
		}

		sb.append("group by effetti.id ");

		StringBuffer sbHaving = new StringBuffer();
		// Filtro da importo
		if (daImporto != null && daImporto.getImportoInValuta() != null
				&& daImporto.getImportoInValuta().compareTo(BigDecimal.ZERO) != 0) {
			sbHaving.append(" sum(pag.importoInValutaAzienda) >= " + daImporto.getImportoInValutaAzienda());
		}
		// Filtro a importo
		if (aImporto != null && aImporto.getImportoInValuta() != null
				&& aImporto.getImportoInValuta().compareTo(BigDecimal.ZERO) != 0) {
			if (sbHaving.length() > 0) {
				sbHaving.append(" and ");
			}
			sbHaving.append(" sum(pag.importoInValutaAzienda) <= " + aImporto.getImportoInValutaAzienda());
		}

		if (sbHaving.length() > 0) {
			sb.append(" having " + sbHaving.toString());
		}

		sb.append(" order by effetti.dataValuta ");
		return sbSelect.toString() + sb.toString();
	}

	/**
	 * Classe di utility, costruttore privato.
	 */
	private RicercaEffettiQueryBuilder() {

	}
}
