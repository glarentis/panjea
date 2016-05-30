/**
 *
 */
package it.eurotn.panjea.anagrafica.manager.noteautomatiche;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.util.PanjeaEJBUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author fattazzo
 * 
 */
public final class NoteAutomaticheQueryBuilder {

	/**
	 * @return join per articoli.
	 */
	public static String getJoinForArticoli() {
		StringBuilder sb = new StringBuilder();
		sb.append("					left join anag_note_automatiche_documenti_maga_articoli notaArticolo on notaArticolo.anag_note_automatiche_documenti_id=na.id ");
		sb.append("					left join maga_righe_magazzino rm on rm.articolo_id=notaArticolo.articoli_id ");
		sb.append("					left join maga_area_magazzino am on am.id=rm.areaMagazzino_id ");
		sb.append("					left join ordi_righe_ordine ro on ro.articolo_id=notaArticolo.articoli_id ");
		sb.append("					left join ordi_area_ordine ao on ao.id=ro.areaOrdine_id ");

		return sb.toString();
	}

	/**
	 * @return default order by for layouts
	 */
	public static String getOrderByQuery() {
		return " order by se.id desc, ent.id desc, tipoDoc.descrizione desc, na.classeTipoDocumento desc ";
	}

	/**
	 * @return query per il caricamento dei layouts
	 */
	public static String getQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct na.id as id, ");
		sb.append("       na.version as version, ");
		sb.append("       tipoDoc.id as idTipoDocumento, ");
		sb.append("       tipoDoc.version as versionTipoDocumento, ");
		sb.append("       tipoDoc.codice as codiceTipoDocumento, ");
		sb.append("       tipoDoc.descrizione as descrizioneTipoDocumento, ");
		sb.append("       ent.TIPO_ANAGRAFICA as tipoEntita, ");
		sb.append("       ent.id as idEntita, ");
		sb.append("       ent.version as versionEntita, ");
		sb.append("       ent.codice as codiceEntita, ");
		sb.append("       anag.denominazione as denominazioneEntita, ");
		sb.append("       se.id as idSede, ");
		sb.append("       se.version as versionSede, ");
		sb.append("       sa.descrizione as descrizioneSede, ");
		sb.append("       sa.indirizzo as indirizzoSede, ");
		sb.append("       na.classeTipoDocumento as classeTipoDocumento, ");
		sb.append("       na.dataFine as dataFine, ");
		sb.append("       na.dataInizio as dataInizio, ");
		sb.append("       na.ripetiAnnualmente as ripetiAnnualmente, ");
		sb.append("       na.nota as nota ");
		sb.append("from anag_note_automatiche_documenti na left join docu_tipi_documento tipoDoc on na.tipoDocumento_id = tipoDoc.id ");
		sb.append("					left join anag_entita ent on na.entita_id = ent.id ");
		sb.append("					left join anag_anagrafica anag on ent.anagrafica_id = anag.id ");
		sb.append("					left join anag_sedi_entita se on se.id = na.sedeEntita_id ");
		sb.append("					left join anag_sedi_anagrafica sa on se.sede_anagrafica_id = sa.id ");

		return sb.toString();
	}

	/**
	 * @return scalar per la query dei layouts
	 */
	public static List<String> getQueryScalar() {
		return Arrays.asList("id", "version", "idTipoDocumento", "versionTipoDocumento", "classeTipoDocumento",
				"codiceTipoDocumento", "descrizioneTipoDocumento", "tipoEntita", "idEntita", "versionEntita",
				"codiceEntita", "denominazioneEntita", "idSede", "versionSede", "descrizioneSede", "indirizzoSede",
				"dataFine", "dataInizio", "ripetiAnnualmente");
	}

	/**
	 * Restituisce la where per gli articoli del documento.
	 * 
	 * @param documento
	 *            documento
	 * @return sql
	 */
	public static String getWhereForArticoli(Documento documento) {
		StringBuilder sb = new StringBuilder(100);
		// prendo solo le note a cui non sono associati articoli
		sb.append(" and (notaArticolo.articoli_id is null ");
		// o quelli con lo stesso articolo presente tra gli articoli del documento
		sb.append(" or ((notaArticolo.articoli_id=rm.articolo_id and am.documento_id=");
		sb.append(documento.getId());
		sb.append(") or (notaArticolo.articoli_id=ro.articolo_id and ao.documento_id=");
		sb.append(documento.getId());
		sb.append(")) ) ");

		return sb.toString();
	}

	/**
	 * Restituisce la where da utilizzare per caricare tutte le note valide per la data di riferimento.
	 * 
	 * @param date
	 *            data di riferimento
	 * @return sql
	 */
	public static String getWhereForDate(Date date) {

		date = PanjeaEJBUtil.getDateTimeToZero(date);
		String dateString = "'" + new SimpleDateFormat("yyyy-MM-dd").format(date) + "'";

		StringBuilder sb = new StringBuilder(100);
		// prendo tutte le note che non hanno date
		sb.append("((na.dataInizio is null and na.dataFine is null) or ");
		// prendo le note che hanno data inizio e basta
		sb.append("(" + dateString + " >= na.dataInizio and na.dataFine is null) or ");
		// prendo le note che hanno solo la data di fine
		sb.append("(" + dateString + " <= na.dataFine and na.dataInizio is null) or ");
		// prendo le note con l'intervallo date valido
		sb.append("(" + dateString + " BETWEEN na.dataInizio and na.dataFine) or ");
		// prendo le note che si ripetono annualmente con intervallo date valido
		sb.append("(na.ripetiAnnualmente = true and ");
		sb.append("CONCAT(YEAR(na.dataInizio),'-',MONTH(" + dateString + "),'-',DAYOFMONTH(" + dateString
				+ ")) between na.dataInizio and na.dataFine))");

		return sb.toString();
	}

	/**
	 * Restituisce la where da utilizzare per caricare tutte le note valide per il documento.
	 * 
	 * @param documento
	 *            documento di riferimento
	 * @return sql
	 */
	public static String getWhereForDocumento(Documento documento) {
		StringBuilder sb = new StringBuilder(100);

		Integer idSede = documento.getSedeEntita() != null ? documento.getSedeEntita().getId() : null;
		Integer idEntita = documento.getEntita() != null ? documento.getEntita().getId() : null;
		Integer idTipoDoc = documento.getTipoDocumento().getId();
		String classeTipoDocumento = documento.getTipoDocumento().getClasseTipoDocumento();

		if (idSede != null) {
			// prendo tutte quelle della sede, dell'entità e del tipo documento
			sb.append(" (na.sedeEntita_id = ");
			sb.append(idSede);
			sb.append(" or (na.sedeEntita_id is null and na.entita_id =");
			sb.append(idEntita);
			sb.append(") or (na.entita_id is null)) and ");
		} else if (idEntita != null) {
			// prendo solo quelli dell'entità e tipo documento
			sb.append(" (na.entita_id = ");
			sb.append(idEntita);
			sb.append(" and na.sedeEntita_id is null) or (na.entita_id is null) and ");
		} else {
			sb.append(" (na.entita_id is null) and ");
		}
		sb.append(" (na.tipoDocumento_id = ");
		sb.append(idTipoDoc);
		sb.append(" or na.tipoDocumento_id is null) ");

		sb.append(" and (na.classeTipoDocumento = '");
		sb.append(classeTipoDocumento);
		sb.append("' or na.classeTipoDocumento is null) ");

		return sb.toString();
	}

	/**
	 * Costruttore.
	 */
	private NoteAutomaticheQueryBuilder() {
		super();
	}
}
