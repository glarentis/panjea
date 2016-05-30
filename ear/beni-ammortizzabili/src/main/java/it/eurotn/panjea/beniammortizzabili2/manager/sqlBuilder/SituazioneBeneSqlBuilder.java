package it.eurotn.panjea.beniammortizzabili2.manager.sqlBuilder;

public final class SituazioneBeneSqlBuilder {

	/**
	 * Select sql che carica la situazione di tutti i beni figli.
	 * 
	 * @return sql creato
	 */
	public static String getBeniFigliSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("select bene.id as id, ");
		sb.append("       bene.codice as codice, ");
		sb.append("       bene.descrizione as descrizione, ");
		sb.append("       bene.dataInizioAmmortamento as dataInizioAmmortamento, ");
		sb.append("       COALESCE(bene.importoSoggettoAdAmmortamentoSingolo,0) as importoSoggettoAdAmmortamento, ");
		sb.append("       0.0 as importoVariazioniBene, ");
		sb.append("       0.0 as importoVenditeBene, ");
		sb.append("       0.0 as importoAmmortamentoOrdinario, ");
		sb.append("       0.0 as importoAmmortamentoAnticipato, ");
		sb.append("       0.0 as importoVariazioniFondo, ");
		sb.append("       0.0 as importoVenditeFondo, ");
		sb.append("       bene.benePadre_id as benePadreId ");
		sb.append("from bamm_bene_ammortizzabile bene ");
		sb.append("where bene.codiceAzienda = :codiceAzienda and bene.benePadre_id is not null ");
		sb.append("order by bene.benePadre_id, bene.codice ");
		return sb.toString();
	}

	/**
	 * Select sql che carica la situazione di tutti i beni padri.
	 * 
	 * @return sql creato
	 */
	public static String getSituazioneBeniPadriSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("select bene.id as id, ");
		sb.append("       bene.codice as codice, ");
		sb.append("       bene.descrizione as descrizione, ");
		sb.append("       ubic.id as idUbicazione, ");
		sb.append("       ubic.codice as codiceUbicazione, ");
		sb.append("       ubic.descrizione as descrizioneUbicazione, ");
		sb.append("       specie.id as idSpecie, ");
		sb.append("       specie.codice as codiceSpecie, ");
		sb.append("       specie.descrizione as descrizioneSpecie, ");
		sb.append("       sottosp.id as idSottoSpecie, ");
		sb.append("       sottosp.codice as codiceSottoSpecie, ");
		sb.append("       sottosp.descrizione as descrizioneSottoSpecie, ");
		sb.append("       bene.dataInizioAmmortamento as dataInizioAmmortamento, ");
		sb.append("       (COALESCE(bene.importoSoggettoAdAmmortamentoSingolo,0) + (select COALESCE(SUM(beneFiglio.importoSoggettoAdAmmortamentoSingolo),0) from bamm_bene_ammortizzabile beneFiglio where beneFiglio.benePadre_id = bene.id)) as importoSoggettoAdAmmortamento, ");
		sb.append("       ((select COALESCE(sum(variazioniPadre.importoValutazioneBene),0) from bamm_valutazioni_bene variazioniPadre where variazioniPadre.bene_id = bene.id) + (select COALESCE(sum(variazioniFigli.importoValutazioneBene),0) from bamm_bene_ammortizzabile beneFiglio left join bamm_valutazioni_bene variazioniFigli on beneFiglio.id = variazioniFigli.bene_id where beneFiglio.benePadre_id = bene.id)) as importoVariazioniBene, ");
		sb.append("       ((select COALESCE(sum(venditePadre.importoStornoValoreBene),0) from bamm_vendite_bene venditePadre where venditePadre.bene_id = bene.id) + (select COALESCE(sum(venditeFigli.importoStornoValoreBene),0) from bamm_bene_ammortizzabile beneFiglio left join bamm_vendite_bene venditeFigli on beneFiglio.id = venditeFigli.bene_id where beneFiglio.benePadre_id = bene.id)) as importoVenditeBene, ");
		sb.append("       ((select coalesce(sum(quoteBene.impQuotaAmmortamentoOrdinario),0) from bamm_quote_ammortamento quoteBene where (quoteBene.beneAmmortizzabile_id = bene.id) and quoteBene.consolidata = true) + (select coalesce(sum(quoteFigliBene.impQuotaAmmortamentoOrdinario),0) from bamm_bene_ammortizzabile beneFiglio left join bamm_quote_ammortamento as quoteFigliBene on (beneFiglio.id = quoteFigliBene.beneAmmortizzabile_id) where (beneFiglio.benePadre_id = bene.id) and quoteFigliBene.consolidata = true)) AS importoAmmortamentoOrdinario, ");
		sb.append("       ((select coalesce(sum(quoteBene.impQuotaAmmortamentoAnticipato),0) from bamm_quote_ammortamento quoteBene where (quoteBene.beneAmmortizzabile_id = bene.id and quoteBene.consolidata = true)) + (select coalesce(sum(quoteFigliBene.impQuotaAmmortamentoAnticipato),0) from bamm_bene_ammortizzabile beneFiglio left join bamm_quote_ammortamento as quoteFigliBene on (beneFiglio.id = quoteFigliBene.beneAmmortizzabile_id) where (beneFiglio.benePadre_id = bene.id) and quoteFigliBene.consolidata = true)) AS importoAmmortamentoAnticipato, ");
		sb.append("       ((select COALESCE(sum(variazioniPadre.importoValutazioneFondo),0) from bamm_valutazioni_bene variazioniPadre where variazioniPadre.bene_id = bene.id) + (select COALESCE(sum(variazioniFigli.importoValutazioneFondo),0) from bamm_bene_ammortizzabile beneFiglio left join bamm_valutazioni_bene variazioniFigli on beneFiglio.id = variazioniFigli.bene_id where beneFiglio.benePadre_id = bene.id)) as importoVariazioniFondo, ");
		sb.append("       ((select COALESCE(sum(venditePadre.importoStornoFondoAmmortamento),0) from bamm_vendite_bene venditePadre where venditePadre.bene_id = bene.id) + (select COALESCE(sum(venditeFigli.importoStornoFondoAmmortamento),0) from bamm_bene_ammortizzabile beneFiglio left join bamm_vendite_bene venditeFigli on beneFiglio.id = venditeFigli.bene_id where beneFiglio.benePadre_id = bene.id)) as importoVenditeFondo ");
		sb.append("from bamm_bene_ammortizzabile bene left join anag_entita entita on (bene.fornitore_id = entita.id) ");
		sb.append("                                   left join bamm_ubicazioni ubic on (bene.ubicazione_id = ubic.id) ");
		sb.append("                                   inner join bamm_sottospecie sottosp on (bene.sottoSpecie_id = sottosp.id) ");
		sb.append("                                   inner join bamm_specie specie on (specie.id = sottosp.specie_id) ");
		sb.append("where bene.codiceAzienda = :codiceAzienda and ");
		sb.append("      bene.benePadre_id is null and ");
		sb.append("      (entita.id = :fornitore or (-1 = :fornitore)) ");
		sb.append("order by ubic.codice, specie.codice,sottosp.codice,bene.codice ");
		return sb.toString();
	}

	/**
	 * Costruttore.
	 * 
	 */
	private SituazioneBeneSqlBuilder() {
		super();
	}
}
