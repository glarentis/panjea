/**
 *
 */
package it.eurotn.panjea.magazzino.manager.contratti.stampa.sqlbuilder;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaStampaContratti;
import it.eurotn.util.PanjeaEJBUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * @author fattazzo
 *
 */
public final class RigheContrattoStampaQueryBuilder {

	private static String createWhere(boolean escludiContrattiNonAttivi, EntitaLite entita, ArticoloLite articolo,
			String codiceAzienda) {
		StringBuilder sb = new StringBuilder(400);

		// faccio prima la where sull'articolo perchè se è filtrato devo aggiungere la join
		if (articolo != null && !articolo.isNew()) {
			// join aggiunta perchè devo aggiungere anche tutte le righe contratto delle categorie commerciali
			// dell'articolo filtrato anche se non ho una riga contratto associata all'articolo
			sb.append("left join maga_articoli artCat on artCat.id = "
					+ articolo.getId()
					+ " and (artCat.categoriaCommercialeArticolo_id = rc.categoriaCommercialeArticolo_id or artCat.categoriaCommercialeArticolo2_id = rc.categoriaCommercialeArticolo_id) ");
			sb.append("	 where ((rc.categoriaCommercialeArticolo_id is null and rc.articolo_id is null) or ");
			sb.append("	  rc.articolo_id = ");
			sb.append(articolo.getId());
			sb.append(" or ");
			sb.append("	  (cc1Art.id = ccArtRigaContr.id or cc2Art.id = ccArtRigaContr.id) or artCat.id is not null) ");
		} else {
			sb.append("where 1=1 ");
		}

		sb.append(" and c.codiceAzienda = " + PanjeaEJBUtil.addQuote(codiceAzienda));

		if (escludiContrattiNonAttivi) {
			Date oggi = PanjeaEJBUtil.getDateTimeToZero(Calendar.getInstance().getTime());
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			sb.append(" and c.dataInizio <= ");
			sb.append(PanjeaEJBUtil.addQuote(dateFormat.format(oggi)));
			sb.append("  and c.dataFine >=");
			sb.append(PanjeaEJBUtil.addQuote(dateFormat.format(oggi)));
		}
		if (entita != null && !entita.isNew()) {
			sb.append(" and ent.id =  ");
			sb.append(entita.getId());
		}

		return sb.toString();
	}

	/**
	 * Restituisce l'sql per il caricamento delle righe contratto per i contratti legati alla categoria della sede che
	 * eredita dati commerciali
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @param codiceAzienda
	 *            codice azienda
	 * @return array che contiene come primo valore la stringa SQL della query e come secondo valore gli alias
	 */
	public static Object[] getCateogorieSediEreditateSqlQuery(ParametriRicercaStampaContratti parametri,
			String codiceAzienda) {
		StringBuilder sb = new StringBuilder();
		sb.append("select rc.id, ");
		sb.append("  3 as pesoEntita, ");
		sb.append("	  c.id as contrattoId, ");
		sb.append("	  c.codice as contrattoCodice, ");
		sb.append("	  c.descrizione as contrattoDescrizione, ");
		sb.append("	  c.dataInizio as contrattoDataInizio, ");
		sb.append("	  c.dataFine as contrattoDataFine, ");
		sb.append("  valuta.simbolo as simboloValuta, ");
		sb.append("	  cm.id as categoriaSedeId, ");
		sb.append("	  cm.descrizione as categoriaSedeDescrizione, ");
		sb.append("	  ses.id as sedeId, ");
		sb.append("	  ses.codice as sedeCodice, ");
		sb.append("	  sas.descrizione as sedeDescrizione, ");
		sb.append("	  ccArtRigaContr.id as categoriaCommercialeContrattoId, ");
		sb.append("	  ccArtRigaContr.codice as categoriaCommercialeContrattoCodice, ");
		sb.append("case when ccArtRigaContr.id is null and art.id is null then 0 ");
		sb.append("	when ccArtRigaContr.id is null and art.id is not null then 2 ");
		sb.append("	when ccArtRigaContr.id is not null and art.id is null then 1 end as pesoArticolo, ");
		sb.append("	  art.id as articoloId, ");
		sb.append("	  art.codice as articoloCodice, ");
		sb.append("	  art.descrizioneLinguaAziendale as articoloDescrizione, ");
		sb.append("	  cc1Art.id as categoriaCommercialeArticoloId, ");
		sb.append("	  cc1Art.codice as categoriaCommercialeArticoloCodice, ");
		sb.append("	  cc2Art.id as categoriaCommercialeArticolo2Id, ");
		sb.append("	  cc2Art.codice as categoriaCommercialeArticolo2Codice, ");
		sb.append("	  rc.strategiaPrezzoAbilitata as strategiaPrezzoAbilitata, ");
		sb.append("	  rc.azionePrezzo as azionePrezzo, ");
		sb.append("	  rc.quantitaSogliaPrezzo as quantitaSogliaPrezzo, ");
		sb.append("	  rc.tipoValorePrezzo as tipoValorePrezzoNum, ");
		sb.append("	  rc.valorePrezzo as valorePrezzo, ");
		sb.append("  rc.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("	  rc.strategiaScontoAbilitata as strategiaScontoAbilitata, ");
		sb.append("	  rc.azioneSconto as azioneSconto, ");
		sb.append("	  rc.quantitaSogliaSconto as quantitaSogliaSconto, ");
		sb.append("	  sconto.sconto1 as sconto1, ");
		sb.append("	  sconto.sconto2 as sconto2, ");
		sb.append("	  sconto.sconto3 as sconto3, ");
		sb.append("	  sconto.sconto4 as sconto4 ");
		sb.append("from maga_righe_contratto rc inner join maga_contratti c on rc.contratto_id = c.id ");
		sb.append("						inner join anag_valute_azienda valuta on c.codiceValuta = valuta.codiceValuta ");
		sb.append("					    inner join maga_contratti_maga_categorie_sedi_magazzino ccsedi on c.id = ccsedi.maga_contratti_id ");
		sb.append("					    inner join maga_categorie_sedi_magazzino cm on cm.id = ccsedi.categorieSediMagazzino_id ");
		sb.append("					    inner join maga_sedi_magazzino smp on smp.categoriaSedeMagazzino_id = cm.id ");
		sb.append("					    inner join anag_sedi_entita sep on sep.id = smp.sedeEntita_id ");
		sb.append("					    inner join anag_sedi_anagrafica sap on sep.sede_anagrafica_id = sap.id ");
		sb.append("					    inner join anag_tipo_sede_entita tsp on tsp.id = sep.tipoSede_id and tsp.sede_principale = true ");
		sb.append("					    inner join anag_sedi_entita ses on ses.entita_id = sep.entita_id and ses.ereditaDatiCommerciali = true ");
		sb.append("					    inner join maga_sedi_magazzino sms on sms.sedeEntita_id = ses.id ");
		sb.append("					    inner join anag_sedi_anagrafica sas on sas.id = ses.sede_anagrafica_id ");
		sb.append("					    inner join anag_entita ent on ent.id = sep.entita_id ");
		sb.append("					    left join maga_categoria_commerciale_articolo ccArtRigaContr on ccArtRigaContr.id = rc.categoriaCommercialeArticolo_id ");
		sb.append("					    left join maga_articoli art on rc.articolo_id = art.id ");
		sb.append("					    left join maga_categoria_commerciale_articolo cc1Art on cc1Art.id = art.categoriaCommercialeArticolo_id ");
		sb.append("					    left join maga_categoria_commerciale_articolo cc2Art on cc2Art.id = art.categoriaCommercialeArticolo2_id ");
		sb.append("					    left join maga_sconti sconto on sconto.id = rc.sconto_id ");
		sb.append(createWhere(parametri.isEscludiContrattiNonAttivi(), parametri.getEntita(), parametri.getArticolo(),
				codiceAzienda));
		sb.append(" order by c.codice,ses.codice,ccArtRigaContr.codice,art.codice");

		String[] alias = new String[] { "pesoEntita", "contrattoId", "contrattoCodice", "contrattoDescrizione",
				"contrattoDataInizio", "contrattoDataFine", "simboloValuta", "categoriaSedeId",
				"categoriaSedeDescrizione", "sedeId", "sedeCodice", "sedeDescrizione",
				"categoriaCommercialeContrattoId", "categoriaCommercialeContrattoCodice", "pesoArticolo", "articoloId",
				"articoloCodice", "articoloDescrizione", "categoriaCommercialeArticoloId",
				"categoriaCommercialeArticoloCodice", "categoriaCommercialeArticolo2Id",
				"categoriaCommercialeArticolo2Codice", "strategiaPrezzoAbilitata", "azionePrezzo",
				"quantitaSogliaPrezzo", "tipoValorePrezzoNum", "valorePrezzo", "numeroDecimaliPrezzo",
				"strategiaScontoAbilitata", "azioneSconto", "quantitaSogliaSconto", "sconto1", "sconto2", "sconto3",
				"sconto4" };

		Object[] result = new Object[] { sb.toString(), Arrays.asList(alias) };
		return result;
	}

	/**
	 * Restituisce l'sql per il caricamento delle righe contratto per i contratti legati alla categoria della sede che
	 * non eredita dati commerciali
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @param codiceAzienda
	 *            codice azienda
	 * @return array che contiene come primo valore la stringa SQL della query e come secondo valore gli alias
	 */
	public static Object[] getCateogorieSediNonEreditateSqlQuery(ParametriRicercaStampaContratti parametri,
			String codiceAzienda) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct rc.id, ");
		sb.append("  2 as pesoEntita, ");
		sb.append("	  c.id as contrattoId, ");
		sb.append("	  c.codice as contrattoCodice, ");
		sb.append("	  c.descrizione as contrattoDescrizione, ");
		sb.append("	  c.dataInizio as contrattoDataInizio, ");
		sb.append("	  c.dataFine as contrattoDataFine, ");
		sb.append("  valuta.simbolo as simboloValuta, ");
		sb.append("	  cm.id as categoriaSedeId, ");
		sb.append("	  cm.descrizione as categoriaSedeDescrizione, ");
		sb.append("	  se.id as sedeId, ");
		sb.append("	  se.codice as sedeCodice, ");
		sb.append("	  sa.descrizione as sedeDescrizione, ");
		sb.append("	  cm.id as categoriaSedeEntitaId, ");
		sb.append("	  cm.descrizione as categoriaSedeEntitaDescrizione, ");
		sb.append("	  ccArtRigaContr.id as categoriaCommercialeContrattoId, ");
		sb.append("	  ccArtRigaContr.codice as categoriaCommercialeContrattoCodice, ");
		sb.append("case when ccArtRigaContr.id is null and art.id is null then 0 ");
		sb.append("	when ccArtRigaContr.id is null and art.id is not null then 2 ");
		sb.append("	when ccArtRigaContr.id is not null and art.id is null then 1 end as pesoArticolo, ");
		sb.append("	  art.id as articoloId, ");
		sb.append("	  art.codice as articoloCodice, ");
		sb.append("	  art.descrizioneLinguaAziendale as articoloDescrizione, ");
		sb.append("	  cc1Art.id as categoriaCommercialeArticoloId, ");
		sb.append("	  cc1Art.codice as categoriaCommercialeArticoloCodice, ");
		sb.append("	  cc2Art.id as categoriaCommercialeArticolo2Id, ");
		sb.append("	  cc2Art.codice as categoriaCommercialeArticolo2Codice, ");
		sb.append("	  rc.strategiaPrezzoAbilitata as strategiaPrezzoAbilitata, ");
		sb.append("	  rc.azionePrezzo as azionePrezzo, ");
		sb.append("	  rc.quantitaSogliaPrezzo as quantitaSogliaPrezzo, ");
		sb.append("	  rc.tipoValorePrezzo as tipoValorePrezzoNum, ");
		sb.append("	  rc.valorePrezzo as valorePrezzo, ");
		sb.append("  rc.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("	  rc.strategiaScontoAbilitata as strategiaScontoAbilitata, ");
		sb.append("	  rc.azioneSconto as azioneSconto, ");
		sb.append("	  rc.quantitaSogliaSconto as quantitaSogliaSconto, ");
		sb.append("	  sconto.sconto1 as sconto1, ");
		sb.append("	  sconto.sconto2 as sconto2, ");
		sb.append("	  sconto.sconto3 as sconto3, ");
		sb.append("	  sconto.sconto4 as sconto4 ");
		sb.append("from maga_righe_contratto rc inner join maga_contratti c on rc.contratto_id = c.id ");
		sb.append("						inner join anag_valute_azienda valuta on c.codiceValuta = valuta.codiceValuta ");
		sb.append("					    inner join maga_contratti_maga_categorie_sedi_magazzino ccsedi on c.id = ccsedi.maga_contratti_id ");
		sb.append("					    inner join maga_categorie_sedi_magazzino cm on cm.id = ccsedi.categorieSediMagazzino_id ");
		sb.append("					    inner join maga_sedi_magazzino sm on sm.categoriaSedeMagazzino_id = cm.id ");
		sb.append("					    inner join anag_sedi_entita se on se.id = sm.sedeEntita_id and se.ereditaDatiCommerciali = false ");
		sb.append("					    inner join anag_sedi_anagrafica sa on se.sede_anagrafica_id = sa.id ");
		sb.append("					    inner join anag_entita ent on ent.id = se.entita_id ");
		sb.append("					    left join maga_categoria_commerciale_articolo ccArtRigaContr on ccArtRigaContr.id = rc.categoriaCommercialeArticolo_id ");
		sb.append("					    left join maga_articoli art on rc.articolo_id = art.id ");
		sb.append("					    left join maga_categoria_commerciale_articolo cc1Art on cc1Art.id = art.categoriaCommercialeArticolo_id ");
		sb.append("					    left join maga_categoria_commerciale_articolo cc2Art on cc2Art.id = art.categoriaCommercialeArticolo2_id ");
		sb.append("					    left join maga_sconti sconto on sconto.id = rc.sconto_id ");
		sb.append(createWhere(parametri.isEscludiContrattiNonAttivi(), parametri.getEntita(), parametri.getArticolo(),
				codiceAzienda));
		sb.append(" order by c.codice,se.codice,ccArtRigaContr.codice,art.codice");

		String[] alias = new String[] { "pesoEntita", "contrattoId", "contrattoCodice", "contrattoDescrizione",
				"contrattoDataInizio", "contrattoDataFine", "simboloValuta", "categoriaSedeId",
				"categoriaSedeDescrizione", "sedeId", "sedeCodice", "sedeDescrizione", "categoriaSedeEntitaId",
				"categoriaSedeEntitaDescrizione", "categoriaCommercialeContrattoId",
				"categoriaCommercialeContrattoCodice", "pesoArticolo", "articoloId", "articoloCodice",
				"articoloDescrizione", "categoriaCommercialeArticoloId", "categoriaCommercialeArticoloCodice",
				"categoriaCommercialeArticolo2Id", "categoriaCommercialeArticolo2Codice", "strategiaPrezzoAbilitata",
				"azionePrezzo", "quantitaSogliaPrezzo", "tipoValorePrezzoNum", "valorePrezzo", "numeroDecimaliPrezzo",
				"strategiaScontoAbilitata", "azioneSconto", "quantitaSogliaSconto", "sconto1", "sconto2", "sconto3",
				"sconto4" };

		Object[] result = new Object[] { sb.toString(), Arrays.asList(alias) };
		return result;
	}

	/**
	 * Restituisce l'sql per il caricamento delle righe contratto per i contratti legati alla categoria della sede non
	 * filtrando l'entità
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @param codiceAzienda
	 *            codice azienda
	 * @return array che contiene come primo valore la stringa SQL della query e come secondo valore gli alias
	 */
	public static Object[] getCateogorieSediSqlQuery(ParametriRicercaStampaContratti parametri, String codiceAzienda) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct rc.id, ");
		sb.append("  1 as pesoEntita, ");
		sb.append("	  c.id as contrattoId, ");
		sb.append("	  c.codice as contrattoCodice, ");
		sb.append("	  c.descrizione as contrattoDescrizione, ");
		sb.append("	  c.dataInizio as contrattoDataInizio, ");
		sb.append("	  c.dataFine as contrattoDataFine, ");
		sb.append("  valuta.simbolo as simboloValuta, ");
		sb.append("	  cm.id as categoriaSedeId, ");
		sb.append("	  cm.descrizione  as categoriaSedeDescrizione, ");
		sb.append("	  ccArtRigaContr.id as categoriaCommercialeContrattoId, ");
		sb.append("	  ccArtRigaContr.codice as categoriaCommercialeContrattoCodice, ");
		sb.append("case when ccArtRigaContr.id is null and art.id is null then 0 ");
		sb.append("	when ccArtRigaContr.id is null and art.id is not null then 2 ");
		sb.append("	when ccArtRigaContr.id is not null and art.id is null then 1 end as pesoArticolo, ");
		sb.append("	  art.id as articoloId, ");
		sb.append("	  art.codice as articoloCodice, ");
		sb.append("	  art.descrizioneLinguaAziendale as articoloDescrizione, ");
		sb.append("	  cc1Art.id as categoriaCommercialeArticoloId, ");
		sb.append("	  cc1Art.codice as categoriaCommercialeArticoloCodice, ");
		sb.append("	  cc2Art.id as categoriaCommercialeArticolo2Id, ");
		sb.append("	  cc2Art.codice as categoriaCommercialeArticolo2Codice, ");
		sb.append("	  rc.strategiaPrezzoAbilitata as strategiaPrezzoAbilitata, ");
		sb.append("	  rc.azionePrezzo as azionePrezzo, ");
		sb.append("	  rc.quantitaSogliaPrezzo as quantitaSogliaPrezzo, ");
		sb.append("	  rc.tipoValorePrezzo as tipoValorePrezzoNum, ");
		sb.append("	  rc.valorePrezzo as valorePrezzo, ");
		sb.append("  rc.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("	  rc.strategiaScontoAbilitata as strategiaScontoAbilitata, ");
		sb.append("	  rc.azioneSconto as azioneSconto, ");
		sb.append("	  rc.quantitaSogliaSconto as quantitaSogliaSconto, ");
		sb.append("	  sconto.sconto1 as sconto1, ");
		sb.append("	  sconto.sconto2 as sconto2, ");
		sb.append("	  sconto.sconto3 as sconto3, ");
		sb.append("	  sconto.sconto4 as sconto4 ");
		sb.append("from maga_righe_contratto rc inner join maga_contratti c on rc.contratto_id = c.id ");
		sb.append("						inner join anag_valute_azienda valuta on c.codiceValuta = valuta.codiceValuta ");
		sb.append("					    inner join maga_contratti_maga_categorie_sedi_magazzino ccsedi on c.id = ccsedi.maga_contratti_id ");
		sb.append("					    inner join maga_categorie_sedi_magazzino cm on cm.id = ccsedi.categorieSediMagazzino_id ");
		sb.append("					    left join maga_categoria_commerciale_articolo ccArtRigaContr on ccArtRigaContr.id = rc.categoriaCommercialeArticolo_id ");
		sb.append("					    left join maga_articoli art on rc.articolo_id = art.id ");
		sb.append("					    left join maga_categoria_commerciale_articolo cc1Art on cc1Art.id = art.categoriaCommercialeArticolo_id ");
		sb.append("					    left join maga_categoria_commerciale_articolo cc2Art on cc2Art.id = art.categoriaCommercialeArticolo2_id ");
		sb.append("					    left join maga_sconti sconto on sconto.id = rc.sconto_id ");
		sb.append(createWhere(parametri.isEscludiContrattiNonAttivi(), parametri.getEntita(), parametri.getArticolo(),
				codiceAzienda));
		sb.append(" order by c.codice,categoriaSedeDescrizione,ccArtRigaContr.codice,art.codice");

		String[] alias = new String[] { "pesoEntita", "contrattoId", "contrattoCodice", "contrattoDescrizione",
				"contrattoDataInizio", "contrattoDataFine", "simboloValuta", "categoriaSedeDescrizione",
				"categoriaCommercialeContrattoId", "categoriaCommercialeContrattoCodice", "pesoArticolo", "articoloId",
				"articoloCodice", "articoloDescrizione", "categoriaCommercialeArticoloId",
				"categoriaCommercialeArticoloCodice", "categoriaCommercialeArticolo2Id",
				"categoriaCommercialeArticolo2Codice", "strategiaPrezzoAbilitata", "azionePrezzo",
				"quantitaSogliaPrezzo", "tipoValorePrezzoNum", "valorePrezzo", "numeroDecimaliPrezzo",
				"strategiaScontoAbilitata", "azioneSconto", "quantitaSogliaSconto", "sconto1", "sconto2", "sconto3",
				"sconto4" };

		Object[] result = new Object[] { sb.toString(), Arrays.asList(alias) };
		return result;
	}

	/**
	 * Restituisce l'sql per il caricamento delle righe contratto per i contratti legati alle entità.
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @param codiceAzienda
	 *            codice azienda
	 * @return array che contiene come primo valore la stringa SQL della query e come secondo valore gli alias
	 */
	public static Object[] getEntitaSqlQuery(ParametriRicercaStampaContratti parametri, String codiceAzienda) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct rc.id, ");
		sb.append("  5 as pesoEntita, ");
		sb.append("	  c.id as contrattoId, ");
		sb.append("	  c.codice as contrattoCodice, ");
		sb.append("	  c.descrizione as contrattoDescrizione, ");
		sb.append("	  c.dataInizio as contrattoDataInizio, ");
		sb.append("	  c.dataFine as contrattoDataFine, ");
		sb.append("  valuta.simbolo as simboloValuta, ");
		sb.append("	  ent.TIPO_ANAGRAFICA as classEntita, ");
		sb.append("	  ent.id as entitaId, ");
		sb.append("	  ent.codice as entitaCodice, ");
		sb.append("	  anag.denominazione as entitaDescrizione, ");
		sb.append("	  ccArtRigaContr.id as categoriaCommercialeContrattoId, ");
		sb.append("	  ccArtRigaContr.codice as categoriaCommercialeContrattoCodice, ");
		sb.append("case when ccArtRigaContr.id is null and art.id is null then 0 ");
		sb.append("	when ccArtRigaContr.id is null and art.id is not null then 2 ");
		sb.append("	when ccArtRigaContr.id is not null and art.id is null then 1 end as pesoArticolo, ");
		sb.append("	  art.id as articoloId, ");
		sb.append("	  art.codice as articoloCodice, ");
		sb.append("	  art.descrizioneLinguaAziendale as articoloDescrizione, ");
		sb.append("	  cc1Art.id as categoriaCommercialeArticoloId, ");
		sb.append("	  cc1Art.codice as categoriaCommercialeArticoloCodice, ");
		sb.append("	  cc2Art.id as categoriaCommercialeArticolo2Id, ");
		sb.append("	  cc2Art.codice as categoriaCommercialeArticolo2Codice, ");
		sb.append("	  rc.strategiaPrezzoAbilitata as strategiaPrezzoAbilitata, ");
		sb.append("	  rc.azionePrezzo as azionePrezzo, ");
		sb.append("	  rc.quantitaSogliaPrezzo as quantitaSogliaPrezzo, ");
		sb.append("	  rc.tipoValorePrezzo as tipoValorePrezzoNum, ");
		sb.append("	  rc.valorePrezzo as valorePrezzo, ");
		sb.append("  rc.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("	  rc.strategiaScontoAbilitata as strategiaScontoAbilitata, ");
		sb.append("	  rc.azioneSconto as azioneSconto, ");
		sb.append("	  rc.quantitaSogliaSconto as quantitaSogliaSconto, ");
		sb.append("	  sconto.sconto1 as sconto1, ");
		sb.append("	  sconto.sconto2 as sconto2, ");
		sb.append("	  sconto.sconto3 as sconto3, ");
		sb.append("	  sconto.sconto4 as sconto4 ");
		sb.append("from maga_righe_contratto rc inner join maga_contratti c on rc.contratto_id = c.id ");
		sb.append("						inner join anag_valute_azienda valuta on c.codiceValuta = valuta.codiceValuta ");
		sb.append("					    inner join maga_contratti_anag_entita cent on c.id = cent.maga_contratti_id ");
		sb.append("					    inner join anag_entita ent on ent.id = cent.entita_id ");
		sb.append("					    inner join anag_anagrafica anag on anag.id = ent.anagrafica_id ");
		sb.append("					    left join maga_categoria_commerciale_articolo ccArtRigaContr on ccArtRigaContr.id = rc.categoriaCommercialeArticolo_id ");
		sb.append("					    left join maga_articoli art on rc.articolo_id = art.id ");
		sb.append("					    left join maga_categoria_commerciale_articolo cc1Art on cc1Art.id = art.categoriaCommercialeArticolo_id ");
		sb.append("					    left join maga_categoria_commerciale_articolo cc2Art on cc2Art.id = art.categoriaCommercialeArticolo2_id ");
		sb.append("					    left join maga_sconti sconto on sconto.id = rc.sconto_id ");
		sb.append(createWhere(parametri.isEscludiContrattiNonAttivi(), parametri.getEntita(), parametri.getArticolo(),
				codiceAzienda));
		sb.append(" order by c.codice,ent.codice,ccArtRigaContr.codice,art.codice");

		String[] alias = new String[] { "pesoEntita", "contrattoId", "contrattoCodice", "contrattoDescrizione",
				"contrattoDataInizio", "contrattoDataFine", "simboloValuta", "classEntita", "entitaId", "entitaCodice",
				"entitaDescrizione", "categoriaCommercialeContrattoId", "categoriaCommercialeContrattoCodice",
				"pesoArticolo", "articoloId", "articoloCodice", "articoloDescrizione",
				"categoriaCommercialeArticoloId", "categoriaCommercialeArticoloCodice",
				"categoriaCommercialeArticolo2Id", "categoriaCommercialeArticolo2Codice", "strategiaPrezzoAbilitata",
				"azionePrezzo", "quantitaSogliaPrezzo", "tipoValorePrezzoNum", "valorePrezzo", "numeroDecimaliPrezzo",
				"strategiaScontoAbilitata", "azioneSconto", "quantitaSogliaSconto", "sconto1", "sconto2", "sconto3",
				"sconto4" };

		Object[] result = new Object[] { sb.toString(), Arrays.asList(alias) };
		return result;
	}

	/**
	 * Restituisce l'sql per il caricamento delle righe contratto per i contratti legati alla sede.
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @param codiceAzienda
	 *            codice azienda
	 * @return array che contiene come primo valore la stringa SQL della query e come secondo valore gli alias
	 */
	public static Object[] getSediSqlQuery(ParametriRicercaStampaContratti parametri, String codiceAzienda) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct rc.id, ");
		sb.append("  4 as pesoEntita, ");
		sb.append("	  c.id as contrattoId, ");
		sb.append("	  c.codice as contrattoCodice, ");
		sb.append("	  c.descrizione as contrattoDescrizione, ");
		sb.append("	  c.dataInizio as contrattoDataInizio, ");
		sb.append("	  c.dataFine as contrattoDataFine, ");
		sb.append("  valuta.simbolo as simboloValuta, ");
		sb.append("	  ent.TIPO_ANAGRAFICA as classEntita, ");
		sb.append("	  ent.id as entitaId, ");
		sb.append("	  ent.codice as entitaCodice, ");
		sb.append("	  anag.denominazione as entitaDescrizione, ");
		sb.append("	  se.id as sedeId, ");
		sb.append("	  se.codice as sedeCodice, ");
		sb.append("	  sa.descrizione as sedeDescrizione, ");
		sb.append("	  cm.id as categoriaSedeEntitaId, ");
		sb.append("	  cm.descrizione as categoriaSedeEntitaDescrizione, ");
		sb.append("	  ccArtRigaContr.id as categoriaCommercialeContrattoId, ");
		sb.append("	  ccArtRigaContr.codice as categoriaCommercialeContrattoCodice, ");
		sb.append("case when ccArtRigaContr.id is null and art.id is null then 0 ");
		sb.append("	when ccArtRigaContr.id is null and art.id is not null then 2 ");
		sb.append("	when ccArtRigaContr.id is not null and art.id is null then 1 end as pesoArticolo, ");
		sb.append("	  art.id as articoloId, ");
		sb.append("	  art.codice as articoloCodice, ");
		sb.append("	  art.descrizioneLinguaAziendale as articoloDescrizione, ");
		sb.append("	  cc1Art.id as categoriaCommercialeArticoloId, ");
		sb.append("	  cc1Art.codice as categoriaCommercialeArticoloCodice, ");
		sb.append("	  cc2Art.id as categoriaCommercialeArticolo2Id, ");
		sb.append("	  cc2Art.codice as categoriaCommercialeArticolo2Codice, ");
		sb.append("	  rc.strategiaPrezzoAbilitata as strategiaPrezzoAbilitata, ");
		sb.append("	  rc.azionePrezzo as azionePrezzo, ");
		sb.append("	  rc.quantitaSogliaPrezzo as quantitaSogliaPrezzo, ");
		sb.append("	  rc.tipoValorePrezzo as tipoValorePrezzoNum, ");
		sb.append("	  rc.valorePrezzo as valorePrezzo, ");
		sb.append("  rc.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("	  rc.strategiaScontoAbilitata as strategiaScontoAbilitata, ");
		sb.append("	  rc.azioneSconto as azioneSconto, ");
		sb.append("	  rc.quantitaSogliaSconto as quantitaSogliaSconto, ");
		sb.append("	  sconto.sconto1 as sconto1, ");
		sb.append("	  sconto.sconto2 as sconto2, ");
		sb.append("	  sconto.sconto3 as sconto3, ");
		sb.append("	  sconto.sconto4 as sconto4 ");
		sb.append("from maga_righe_contratto rc inner join maga_contratti c on rc.contratto_id = c.id ");
		sb.append("						inner join anag_valute_azienda valuta on c.codiceValuta = valuta.codiceValuta ");
		sb.append("					    inner join maga_contratti_maga_sedi_magazzino csm on c.id = csm.maga_contratti_id ");
		sb.append("					    inner join maga_sedi_magazzino sm on sm.id = csm.sediMagazzino_id ");
		sb.append("					    left join maga_categorie_sedi_magazzino cm on cm.id = sm.categoriaSedeMagazzino_id ");
		sb.append("					    inner join anag_sedi_entita se on se.id = sm.sedeEntita_id ");
		sb.append("					    inner join anag_sedi_anagrafica sa on sa.id = se.sede_anagrafica_id ");
		sb.append("					    inner join anag_entita ent on ent.id = se.entita_id ");
		sb.append("					    inner join anag_anagrafica anag on anag.id = ent.anagrafica_id ");
		sb.append("					    left join maga_categoria_commerciale_articolo ccArtRigaContr on ccArtRigaContr.id = rc.categoriaCommercialeArticolo_id ");
		sb.append("					    left join maga_articoli art on rc.articolo_id = art.id ");
		sb.append("					    left join maga_categoria_commerciale_articolo cc1Art on cc1Art.id = art.categoriaCommercialeArticolo_id ");
		sb.append("					    left join maga_categoria_commerciale_articolo cc2Art on cc2Art.id = art.categoriaCommercialeArticolo2_id ");
		sb.append("					    left join maga_sconti sconto on sconto.id = rc.sconto_id ");
		sb.append(createWhere(parametri.isEscludiContrattiNonAttivi(), parametri.getEntita(), parametri.getArticolo(),
				codiceAzienda));
		sb.append(" order by c.codice,cm.descrizione,  ent.codice, sa.descrizione, ccArtRigaContr.codice, art.codice ");

		String[] alias = new String[] { "pesoEntita", "contrattoId", "contrattoCodice", "contrattoDescrizione",
				"contrattoDataInizio", "contrattoDataFine", "simboloValuta", "classEntita", "entitaId", "entitaCodice",
				"entitaDescrizione", "sedeId", "sedeCodice", "sedeDescrizione", "categoriaSedeEntitaId",
				"categoriaSedeEntitaDescrizione", "categoriaCommercialeContrattoId",
				"categoriaCommercialeContrattoCodice", "pesoArticolo", "articoloId", "articoloCodice",
				"articoloDescrizione", "categoriaCommercialeArticoloId", "categoriaCommercialeArticoloCodice",
				"categoriaCommercialeArticolo2Id", "categoriaCommercialeArticolo2Codice", "strategiaPrezzoAbilitata",
				"azionePrezzo", "quantitaSogliaPrezzo", "tipoValorePrezzoNum", "valorePrezzo", "numeroDecimaliPrezzo",
				"strategiaScontoAbilitata", "azioneSconto", "quantitaSogliaSconto", "sconto1", "sconto2", "sconto3",
		"sconto4" };

		Object[] result = new Object[] { sb.toString(), Arrays.asList(alias) };
		return result;
	}

	/**
	 * Restituisce l'sql per il caricamento delle righe contratto per i contratti legati a tutte le categorie
	 *
	 * @param parametri
	 *            parametri di ricerca
	 * @param codiceAzienda
	 *            codice azienda
	 * @return array che contiene come primo valore la stringa SQL della query e come secondo valore gli alias
	 */
	public static Object[] getTutteCateogorieQuery(ParametriRicercaStampaContratti parametri, String codiceAzienda) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct rc.id, ");
		sb.append("  0 as pesoEntita, ");
		sb.append("	  c.id as contrattoId, ");
		sb.append("	  c.codice as contrattoCodice, ");
		sb.append("	  c.descrizione as contrattoDescrizione, ");
		sb.append("	  c.dataInizio as contrattoDataInizio, ");
		sb.append("	  c.dataFine as contrattoDataFine, ");
		sb.append("  valuta.simbolo as simboloValuta, ");
		sb.append("  'Tutte le categorie' as categoriaSedeDescrizione, ");
		sb.append("	  ccArtRigaContr.id as categoriaCommercialeContrattoId, ");
		sb.append("	  ccArtRigaContr.codice as categoriaCommercialeContrattoCodice, ");
		sb.append("case when ccArtRigaContr.id is null and art.id is null then 0 ");
		sb.append("	when ccArtRigaContr.id is null and art.id is not null then 2 ");
		sb.append("	when ccArtRigaContr.id is not null and art.id is null then 1 end as pesoArticolo, ");
		sb.append("	  art.id as articoloId, ");
		sb.append("	  art.codice as articoloCodice, ");
		sb.append("	  art.descrizioneLinguaAziendale as articoloDescrizione, ");
		sb.append("	  cc1Art.id as categoriaCommercialeArticoloId, ");
		sb.append("	  cc1Art.codice as categoriaCommercialeArticoloCodice, ");
		sb.append("	  cc2Art.id as categoriaCommercialeArticolo2Id, ");
		sb.append("	  cc2Art.codice as categoriaCommercialeArticolo2Codice, ");
		sb.append("	  rc.strategiaPrezzoAbilitata as strategiaPrezzoAbilitata, ");
		sb.append("	  rc.azionePrezzo as azionePrezzo, ");
		sb.append("	  rc.quantitaSogliaPrezzo as quantitaSogliaPrezzo, ");
		sb.append("	  rc.tipoValorePrezzo as tipoValorePrezzoNum, ");
		sb.append("	  rc.valorePrezzo as valorePrezzo, ");
		sb.append("  rc.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("	  rc.strategiaScontoAbilitata as strategiaScontoAbilitata, ");
		sb.append("	  rc.azioneSconto as azioneSconto, ");
		sb.append("	  rc.quantitaSogliaSconto as quantitaSogliaSconto, ");
		sb.append("	  sconto.sconto1 as sconto1, ");
		sb.append("	  sconto.sconto2 as sconto2, ");
		sb.append("	  sconto.sconto3 as sconto3, ");
		sb.append("	  sconto.sconto4 as sconto4 ");
		sb.append("from maga_righe_contratto rc inner join maga_contratti c on rc.contratto_id = c.id and c.tutteCategorieSedeMagazzino = true ");
		sb.append("						inner join anag_valute_azienda valuta on c.codiceValuta = valuta.codiceValuta ");
		sb.append("					    left join maga_categoria_commerciale_articolo ccArtRigaContr on ccArtRigaContr.id = rc.categoriaCommercialeArticolo_id ");
		sb.append("					    left join maga_articoli art on rc.articolo_id = art.id ");
		sb.append("					    left join maga_categoria_commerciale_articolo cc1Art on cc1Art.id = art.categoriaCommercialeArticolo_id ");
		sb.append("					    left join maga_categoria_commerciale_articolo cc2Art on cc2Art.id = art.categoriaCommercialeArticolo2_id ");
		sb.append("					    left join maga_sconti sconto on sconto.id = rc.sconto_id ");
		// passo l'entità a null perchè non ha senso filtrare per entità i contratti legati a tutte le categorie
		sb.append(createWhere(parametri.isEscludiContrattiNonAttivi(), null, parametri.getArticolo(), codiceAzienda));
		sb.append(" order by c.codice, ccArtRigaContr.codice, art.codice ");

		String[] alias = new String[] { "pesoEntita", "contrattoId", "contrattoCodice", "contrattoDescrizione",
				"contrattoDataInizio", "contrattoDataFine", "simboloValuta", "categoriaSedeDescrizione",
				"categoriaCommercialeContrattoId", "categoriaCommercialeContrattoCodice", "pesoArticolo", "articoloId",
				"articoloCodice", "articoloDescrizione", "categoriaCommercialeArticoloId",
				"categoriaCommercialeArticoloCodice", "categoriaCommercialeArticolo2Id",
				"categoriaCommercialeArticolo2Codice", "strategiaPrezzoAbilitata", "azionePrezzo",
				"quantitaSogliaPrezzo", "tipoValorePrezzoNum", "valorePrezzo", "numeroDecimaliPrezzo",
				"strategiaScontoAbilitata", "azioneSconto", "quantitaSogliaSconto", "sconto1", "sconto2", "sconto3",
		"sconto4" };

		Object[] result = new Object[] { sb.toString(), Arrays.asList(alias) };
		return result;
	}

	/**
	 * Costruttore.
	 */
	private RigheContrattoStampaQueryBuilder() {
		super();
	}

}
