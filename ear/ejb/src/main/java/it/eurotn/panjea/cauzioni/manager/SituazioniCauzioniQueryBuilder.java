package it.eurotn.panjea.cauzioni.manager;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public final class SituazioniCauzioniQueryBuilder {

	/**
	 * Trasforma il set passato come parametro nella in SQL.
	 *
	 * @param params
	 *            parametri
	 * @return in creata
	 */
	private static String getSQLInFormat(Set<?> params) {

		String separator = "";

		StringBuilder sb = new StringBuilder();
		sb.append(" in (");
		for (Object object : params) {
			sb.append(separator);
			sb.append(object);
			separator = ",";
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * Restituisce l'sql creato per la movimentazione delle cauzioni.
	 *
	 * @param codiceAzienda
	 *            codice azienda
	 * @param idEntita
	 *            entita di riferimento
	 * @param idSedeEntita
	 *            sede entita di riferimento
	 * @param idArticolo
	 *            articolo di riferimento
	 * @return SQL generato
	 */
	public static String getSQLMovimentazioneCauzioniEntita(String codiceAzienda, Set<Integer> idEntita,
			Set<Integer> idSedeEntita, Set<Integer> idArticolo) {

		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("mov.areaMagazzino_id as idAreaMagazzino, ");
		sb.append("mov.dataRegistrazione as dataRegistrazione, ");
		sb.append("mov.dataDocumento as dataDocumento, ");
		sb.append("mov.tipoDocumentoId as idTipoDocumento, ");
		sb.append("concat(mov.tipoDocumentoCodice) as codiceTipoDocumento, ");
		sb.append("concat(mov.tipoDocumentoDescrizione) as descrizioneTipoDocumento, ");
		sb.append("mov.numeroDocumento as numeroDocumento, ");
		sb.append("sedi.sede_entita_id as idSedeEntita, ");
		sb.append("concat(sedi.codice_sede) as codiceSedeEntita, ");
		sb.append("concat(sedi.descrizione_sede) as descrizioneSedeEntita, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) > 0 then (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) else 0 end as dati, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) < 0 then abs(mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) else 0 end as resi, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) > 0 then (mov.importoCarico+mov.importoScarico+mov.importoCaricoAltro+mov.importoScaricoAltro) else 0 end as importoDati, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) < 0 then abs(mov.importoCarico+mov.importoScarico+mov.importoCaricoAltro+mov.importoScaricoAltro) else 0 end as importoResi, ");
		sb.append("artAnag.numeroDecimaliQta as numeroDecimaliQta ");
		sb.append("from dw_movimentimagazzino mov left join dw_sedientita sedi on mov.sedeEntita_id=sedi.sede_entita_id ");
		sb.append("						 inner join dw_articoli art on art.id=mov.articolo_id ");
		sb.append("						 inner join maga_categorie cat on cat.id = art.categoria_id ");
		sb.append("						 inner join maga_articoli artAnag on artAnag.id = art.id ");
		sb.append("where cat.cauzione = true ");
		sb.append("and mov.codiceAzienda='" + codiceAzienda + "' ");
		sb.append("and mov.tipoMovimento <> 0 ");
		if (idSedeEntita != null) {
			sb.append("and sedi.sede_entita_id ");
			sb.append(getSQLInFormat(idSedeEntita));
		}
		if (idEntita != null) {
			sb.append(" and sedi.entita_id ");
			sb.append(getSQLInFormat(idEntita));
		}
		if (idArticolo != null) {
			sb.append(" and art.id ");
			sb.append(getSQLInFormat(idArticolo));
		}
		sb.append(" order by mov.dataRegistrazione asc ");

		return sb.toString();
	}

	/**
	 * Restituisce l'SQL creato per il caricamento della situazione delle cauzioni.
	 *
	 * @param codiceAzienda
	 *            codice azienda
	 * @param entita
	 *            entità
	 * @param dataIniziale
	 *            data iniziale
	 * @param dataFinale
	 *            data finale
	 * @param articolo
	 *            articolo
	 * @return SQL generato
	 */
	public static String getSQLSituazioneCauzioni(String codiceAzienda, EntitaLite entita, Date dataIniziale,
			Date dataFinale, ArticoloLite articolo) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("situazioneCauzioni.idEntita as idEntita, ");
		sb.append("situazioneCauzioni.codiceEntita as codiceEntita, ");
		sb.append("situazioneCauzioni.descrizioneEntita as descrizioneEntita, ");
		sb.append("situazioneCauzioni.tipoEntita as tipoEntita, ");
		sb.append("situazioneCauzioni.idSedeEntita as idSedeEntita, ");
		sb.append("situazioneCauzioni.codiceSedeEntita as codiceSedeEntita, ");
		sb.append("situazioneCauzioni.descrizioneSedeEntita as descrizioneSedeEntita, ");
		sb.append("situazioneCauzioni.idCategoria as idCategoria, ");
		sb.append("situazioneCauzioni.codiceCategoria as codiceCategoria, ");
		sb.append("situazioneCauzioni.descrizioneCategoria as descrizioneCategoria, ");
		sb.append("situazioneCauzioni.idArticolo as idArticolo, ");
		sb.append("situazioneCauzioni.codiceArticolo as codiceArticolo, ");
		sb.append("situazioneCauzioni.descrizioneArticolo as descrizioneArticolo, ");
		sb.append("sum(situazioneCauzioni.dati) as dati, ");
		sb.append("sum(situazioneCauzioni.resi) as resi, ");
		sb.append("sum(situazioneCauzioni.dati-situazioneCauzioni.resi) as saldo, ");
		sb.append("sum(situazioneCauzioni.importoDati) as importoDati, ");
		sb.append("sum(situazioneCauzioni.importoResi) as importoResi, ");
		sb.append("sum(situazioneCauzioni.importoDati-situazioneCauzioni.importoResi) as saldoImporto ");
		sb.append("from( ");
		sb.append("select ");
		sb.append("sedi.entita_id as idEntita, ");
		sb.append("sedi.codice as codiceEntita, ");
		sb.append("concat(sedi.denominazione) as descrizioneEntita, ");
		sb.append("concat(sedi.TIPO_ANAGRAFICA) as tipoEntita, ");
		sb.append("sedi.sede_entita_id as idSedeEntita, ");
		sb.append("concat(sedi.codice_sede) as codiceSedeEntita, ");
		sb.append("concat(sedi.descrizione_sede) as descrizioneSedeEntita, ");
		sb.append("art.categoria_id as idCategoria, ");
		sb.append("art.codiceCategoria as codiceCategoria, ");
		sb.append("art.descrizioneCategoria as descrizioneCategoria, ");
		sb.append("art.id as idArticolo, ");
		sb.append("art.codice as codiceArticolo, ");
		sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) > 0 then (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) else 0 end as dati, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) < 0 then abs(mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) else 0 end as resi, ");
		sb.append("0 as saldo, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) > 0 then (mov.importoCarico+mov.importoScarico+mov.importoCaricoAltro+mov.importoScaricoAltro) else 0 end as importoDati, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) < 0 then abs(mov.importoCarico+mov.importoScarico+mov.importoCaricoAltro+mov.importoScaricoAltro) else 0 end as importoResi, ");
		sb.append("0 as saldoImporto ");
		sb.append("from dw_movimentimagazzino mov inner join dw_sedientita sedi on mov.sedeEntita_id=sedi.sede_entita_id ");
		sb.append("						 inner join dw_articoli art on art.id=mov.articolo_id ");
		sb.append("						 inner join maga_categorie cat on cat.id = art.categoria_id ");
		sb.append("where cat.cauzione = true ");
		sb.append(" and mov.codiceAzienda='" + codiceAzienda + "'");

		if (dataIniziale != null && dataFinale != null) {
			sb.append(" and mov.dataRegistrazione>='" + dateFormat.format(dataIniziale) + "'");
			sb.append(" and mov.dataRegistrazione<='" + dateFormat.format(dataFinale) + "'");
		}
		if (entita != null && entita.getId() != null) {
			sb.append(" and sedi.entita_id = " + entita.getId());
		}
		if (articolo != null && articolo.getId() != null) {
			sb.append(" and mov.articolo_id = " + articolo.getId());
		}
		sb.append(" and mov.tipoMovimento <> 0 ) as situazioneCauzioni ");
		sb.append(" group by situazioneCauzioni.idEntita, ");
		sb.append("	    situazioneCauzioni.codiceEntita, ");
		sb.append("	    situazioneCauzioni.descrizioneEntita, ");
		sb.append("	    situazioneCauzioni.tipoEntita, ");
		sb.append("	    situazioneCauzioni.idSedeEntita, ");
		sb.append("	    situazioneCauzioni.codiceSedeEntita, ");
		sb.append("	    situazioneCauzioni.descrizioneSedeEntita, ");
		sb.append("	    situazioneCauzioni.idCategoria, ");
		sb.append("	    situazioneCauzioni.codiceCategoria, ");
		sb.append("	    situazioneCauzioni.descrizioneCategoria ");

		return sb.toString();
	}

	/**
	 * Resitituisce l'SQL creato per il caricamento della situazione cauzioni fino al movimento di riferimento.
	 *
	 * @param areaMagazzino
	 *            area magazzino di riferimento
	 * @return SQL generato
	 */
	public static String getSQLSituazioneCauzioniAreaMagazzino(AreaMagazzino areaMagazzino) {

		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("unionCauzioni.idCategoria, ");
		sb.append("unionCauzioni.codiceCategoria, ");
		sb.append("unionCauzioni.descrizioneCategoria, ");
		sb.append("sum(unionCauzioni.dati) as dati, ");
		sb.append("sum(unionCauzioni.resi) as resi, ");
		sb.append("sum(unionCauzioni.saldo)as saldo, ");
		sb.append("sum(unionCauzioni.saldoEntita) as saldoEntita ");
		sb.append("from( ");
		sb.append("select ");
		sb.append("art.categoria_id as idCategoria, ");
		sb.append("art.codiceCategoria as codiceCategoria, ");
		sb.append("art.descrizioneCategoria as descrizioneCategoria, ");
		sb.append("0 as dati, ");
		sb.append("0 as resi, ");
		sb.append("0 as saldoEntita, ");
		sb.append("sum(mov.qtaMagazzinoCarico+qtaMagazzinoCaricoAltro)-sum(mov.qtaMagazzinoScarico+qtaMagazzinoScaricoAltro)*-1 as saldo ");
		sb.append("from dw_movimentimagazzino mov ");
		sb.append("	 inner join dw_articoli art on art.id=mov.articolo_id ");
		sb.append("	 inner join maga_categorie cat on cat.id = art.categoria_id ");
		sb.append("where cat.cauzione = true ");
		sb.append("and mov.codiceAzienda='" + areaMagazzino.getDocumento().getCodiceAzienda() + "' ");
		sb.append("and mov.tipoMovimento <> 0 ");
		sb.append("and mov.sedeEntita_id = " + areaMagazzino.getDocumento().getSedeEntita().getId());
		sb.append(" and mov.dataRegistrazione <= '"
				+ new SimpleDateFormat("yyyy-MM-dd").format(areaMagazzino.getDataRegistrazione()) + "' ");
		sb.append(" group by art.categoria_id, ");
		sb.append("	    art.codiceCategoria, ");
		sb.append("	    art.descrizioneCategoria ");
		sb.append("having saldo <> 0 ");
		sb.append("union all ");
		sb.append("select ");
		sb.append("art.categoria_id as idCategoria, ");
		sb.append("art.codiceCategoria as codiceCategoria, ");
		sb.append("art.descrizioneCategoria as descrizioneCategoria, ");
		sb.append("0 as dati, ");
		sb.append("0 as resi, ");
		sb.append("sum(mov.qtaMagazzinoCarico+qtaMagazzinoCaricoAltro)-sum(mov.qtaMagazzinoScarico+qtaMagazzinoScaricoAltro)*-1 as saldoEntita, ");
		sb.append("0 as saldo ");
		sb.append("from dw_movimentimagazzino mov left join dw_sedientita sedi on mov.sedeEntita_id=sedi.sede_entita_id ");
		sb.append("						 inner join dw_articoli art on art.id=mov.articolo_id ");
		sb.append("						 inner join maga_categorie cat on cat.id = art.categoria_id ");
		sb.append("where cat.cauzione = true ");
		sb.append("and mov.codiceAzienda='" + areaMagazzino.getDocumento().getCodiceAzienda() + "' ");
		sb.append("and mov.tipoMovimento <> 0 ");
		sb.append("and sedi.entita_id= " + areaMagazzino.getDocumento().getEntita().getId());
		sb.append(" and mov.dataRegistrazione <= '"
				+ new SimpleDateFormat("yyyy-MM-dd").format(areaMagazzino.getDataRegistrazione()) + "' ");
		sb.append(" group by art.categoria_id, ");
		sb.append("	    art.codiceCategoria, ");
		sb.append("	    art.descrizioneCategoria ");
		sb.append("having saldoEntita <> 0 ");
		sb.append("union all ");
		sb.append("select ");
		sb.append("art.categoria_id as idCategoria, ");
		sb.append("art.codiceCategoria as codiceCategoria, ");
		sb.append("art.descrizioneCategoria as descrizioneCategoria, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) > 0 then (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) else 0 end as dati, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) < 0 then abs(mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) else 0 end as resi, ");
		sb.append("0 as saldoEntita, ");
		sb.append("0 as saldo ");
		sb.append("from dw_movimentimagazzino mov ");
		sb.append("						 inner join dw_articoli art on art.id=mov.articolo_id ");
		sb.append("						 inner join maga_categorie cat on cat.id = art.categoria_id ");
		sb.append("where cat.cauzione = true ");
		sb.append("and mov.areaMagazzino_id=" + areaMagazzino.getId() + ") as unionCauzioni ");
		sb.append("group by unionCauzioni.idCategoria ");
		sb.append("order by unionCauzioni.codiceCategoria asc ");
		return sb.toString();
	}

	/**
	 * Restituisce l'SQL creato per il caricamento della situazione delle cauzioni dell'entità.
	 *
	 * @param codiceAzienda
	 *            codice azienda
	 * @param idEntita
	 *            id entità
	 * @param raggruppamentoSedi
	 *            indica e usare il raggruppamento per sede-articolo o solo per articolo
	 * @return SQL generato
	 */
	public static String getSQLSituazioneCauzioniEntita(String codiceAzienda, Integer idEntita,
			boolean raggruppamentoSedi) {

		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		sb.append("situazioneCauzioni.idEntita as idEntita, ");
		sb.append("situazioneCauzioni.codiceEntita as codiceEntita, ");
		sb.append("situazioneCauzioni.descrizioneEntita as descrizioneEntita, ");
		sb.append("situazioneCauzioni.tipoEntita as tipoEntita, ");
		if (raggruppamentoSedi) {
			sb.append("situazioneCauzioni.idSedeEntita as idSedeEntita, ");
			sb.append("situazioneCauzioni.codiceSedeEntita as codiceSedeEntita, ");
			sb.append("situazioneCauzioni.descrizioneSedeEntita as descrizioneSedeEntita, ");
		}
		sb.append("situazioneCauzioni.idArticolo as idArticolo, ");
		sb.append("situazioneCauzioni.codiceArticolo as codiceArticolo, ");
		sb.append("situazioneCauzioni.descrizioneArticolo as descrizioneArticolo, ");
		sb.append("sum(situazioneCauzioni.dati) as dati, ");
		sb.append("sum(situazioneCauzioni.resi) as resi, ");
		sb.append("sum(situazioneCauzioni.dati-situazioneCauzioni.resi) as saldo, ");
		sb.append("situazioneCauzioni.numeroDecimaliQta as numeroDecimaliQta, ");
		sb.append("sum(situazioneCauzioni.importoDati) as importoDati, ");
		sb.append("sum(situazioneCauzioni.importoResi) as importoResi, ");
		sb.append("sum(situazioneCauzioni.importoDati-situazioneCauzioni.importoResi) as saldoImporto ");
		sb.append("from ( ");
		sb.append("select ");
		sb.append("sedi.entita_id as idEntita, ");
		sb.append("sedi.codice as codiceEntita, ");
		sb.append("concat(sedi.denominazione) as descrizioneEntita, ");
		sb.append("concat(sedi.TIPO_ANAGRAFICA) as tipoEntita, ");
		if (raggruppamentoSedi) {
			sb.append("sedi.sede_entita_id as idSedeEntita, ");
			sb.append("concat(sedi.codice_sede) as codiceSedeEntita, ");
			sb.append("concat(sedi.descrizione_sede) as descrizioneSedeEntita, ");
		}
		sb.append("art.id as idArticolo, ");
		sb.append("art.codice as codiceArticolo, ");
		sb.append("art.descrizioneLinguaAziendale as descrizioneArticolo, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) > 0 then (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) else 0 end as dati, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) < 0 then abs(mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) else 0 end as resi, ");
		sb.append("anagArt.numeroDecimaliQta, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) > 0 then (mov.importoCarico+mov.importoScarico+mov.importoCaricoAltro+mov.importoScaricoAltro) else 0 end as importoDati, ");
		sb.append("case when (mov.qtaMagazzinoCarico+mov.qtaMagazzinoScarico+mov.qtaMagazzinoCaricoAltro+mov.qtaMagazzinoScaricoAltro) < 0 then abs(mov.importoCarico+mov.importoScarico+mov.importoCaricoAltro+mov.importoScaricoAltro) else 0 end as importoResi ");
		sb.append("from dw_movimentimagazzino mov ");
		sb.append("                        left join dw_sedientita sedi on mov.sedeEntita_id=sedi.sede_entita_id ");
		sb.append("						 inner join dw_articoli art on art.id=mov.articolo_id ");
		sb.append("						 inner join maga_categorie cat on cat.id = art.categoria_id ");
		sb.append("						 inner join maga_area_magazzino am on am.id=mov.areaMagazzino_id ");
		sb.append("						 inner join maga_articoli anagArt on anagArt.id = art.id ");
		sb.append("where cat.cauzione = true ");
		sb.append("and mov.codiceAzienda='" + codiceAzienda + "' ");
		sb.append("and sedi.entita_id = " + idEntita);
		sb.append(") as situazioneCauzioni ");
		sb.append("group by ");
		if (raggruppamentoSedi) {
			sb.append("       idSedeEntita, ");
			sb.append("	    codiceSedeEntita, ");
			sb.append("	    descrizioneSedeEntita, ");
		}
		sb.append("        idArticolo, ");
		sb.append("	    codiceArticolo, ");
		sb.append("	    descrizioneArticolo ");

		return sb.toString();
	}

	/**
	 * Costruttore.
	 *
	 */
	private SituazioniCauzioniQueryBuilder() {

	}
}
