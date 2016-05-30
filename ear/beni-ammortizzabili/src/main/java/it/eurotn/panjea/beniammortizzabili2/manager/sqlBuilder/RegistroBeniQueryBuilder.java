package it.eurotn.panjea.beniammortizzabili2.manager.sqlBuilder;

public final class RegistroBeniQueryBuilder {

	/**
	 * Istruzione per caricare i beni figli.
	 *
	 * @param idBenePadre
	 *            id del bene padre
	 * @param anno
	 *            anno
	 * @return SQL
	 */
	public static String getBeniFigli(Integer idBenePadre, Integer anno) {
		StringBuffer sb = new StringBuffer();
		sb.append("select bene.id, ");
		sb.append("	  bene.codice, ");
		sb.append("	  bene.descrizione, ");
		sb.append("	  bene.importoSoggettoAdAmmortamentoSingolo ");
		sb.append("from bamm_bene_ammortizzabile bene ");
		sb.append("where bene.benePadre_id = " + idBenePadre + " and ");
		sb.append("bene.annoAcquisto <= " + anno + " ");
		sb.append("order by bene.codice ");
		return sb.toString();
	}

	/**
	 * Istruzione per caricare tutti i beni.
	 *
	 * @param annoRiferimento
	 *            anno di riferimento
	 * @param idGruppo
	 *            id gruppo
	 * @param idSpecie
	 *            id specie
	 * @param idSottoSpecie
	 *            id sottospecie
	 * @return SQL
	 */
	public static String getRegistroBeni(Integer annoRiferimento, Integer idGruppo, Integer idSpecie,
			Integer idSottoSpecie) {

		StringBuffer sb = new StringBuffer();
		sb.append("select distinct bene.id as idBene, ");
		sb.append("			 bene.codice as codiceBene, ");
		sb.append("			 bene.descrizione as descrizioneBene, ");
		sb.append("			 bene.annoAcquisto as annoAcquistoBene, ");
		sb.append("			 coalesce(bene.importoSoggettoAdAmmortamentoSingolo,0) as importoSoggettoAdAmmortamentoSingolo, ");
		sb.append("			 gruppo.id as idGruppo, ");
		sb.append("			 gruppo.codice as codiceGruppo, ");
		sb.append("			 gruppo.descrizione as descrizioneGruppo, ");
		sb.append("			 specie.id as idSpecie, ");
		sb.append("			 specie.codice as codiceSpecie, ");
		sb.append("			 specie.descrizione as descrizioneSpecie, ");
		sb.append("			 sottosp.id as idSottoSpecie, ");
		sb.append("			 sottosp.codice as codiceSottoSpecie, ");
		sb.append("			 sottosp.descrizione as descrizioneSottoSpecie, ");
		sb.append("				(select (coalesce(sum(coalesce(valutFondo.importoValutazioneBene,0)),0)) as valoreFondo ");
		sb.append("		from bamm_bene_ammortizzabile beneFondo left join bamm_valutazioni_bene valutFondo on (valutFondo.bene_id = beneFondo.id and valutFondo.anno <= "
				+ annoRiferimento + ") ");
		sb.append("		where beneFondo.id = bene.id ");
		sb.append("		group by beneFondo.id) as rivalutazioni, ");
		sb.append("		(select (coalesce(sum(coalesce(venditeFondo.importoStornoValoreBene,0)),0)) as valoreFondo ");
		sb.append("		from bamm_bene_ammortizzabile beneFondo left join bamm_vendite_bene venditeFondo on (venditeFondo.bene_id = beneFondo.id and YEAR(venditeFondo.dataVendita) <= "
				+ annoRiferimento + ") ");
		sb.append("		where beneFondo.id = bene.id ");
		sb.append("		group by beneFondo.id) as vendite, ");
		sb.append("		(select (coalesce(sum(coalesce(venditeFondo.plusMinusValore,0)),0)) as valoreFondo ");
		sb.append("		from bamm_bene_ammortizzabile beneFondo left join bamm_vendite_bene venditeFondo on (venditeFondo.bene_id = beneFondo.id and YEAR(venditeFondo.dataVendita) <= "
				+ annoRiferimento + ") ");
		sb.append("		where beneFondo.id = bene.id ");
		sb.append("		group by beneFondo.id) as minusPlusValenze, ");
		sb.append("			 coalesce(if(quota.percPrimoAnnoApplicata,ROUND(quota.percQuotaAmmortamentoOrdinario/2,2),quota.percQuotaAmmortamentoOrdinario),0) as percOrdinario, ");
		sb.append("			 coalesce(if(quota.percPrimoAnnoApplicata,ROUND(quota.percQuotaAmmortamentoAnticipato/2,2),quota.percQuotaAmmortamentoAnticipato),0) as percAnticipato, ");
		sb.append("			 coalesce(quota.impQuotaAmmortamentoOrdinario,0) as impOrdinario, ");
		sb.append("			 coalesce(quota.impQuotaAmmortamentoAnticipato,0) as impAnticipato, ");
		sb.append("			 coalesce(quota.percPrimoAnnoApplicata,false) as percPrimoAnnoApplicata, ");
		sb.append("			 (select (coalesce(sum(coalesce(quotaFondo.impQuotaAmmortamentoOrdinario,0)),0) + ");
		sb.append("			  coalesce(sum(coalesce(quotaFondo.impQuotaAmmortamentoAnticipato,0)),0))as valoreFondo ");
		sb.append("			  from bamm_bene_ammortizzabile beneFondo left join bamm_quote_ammortamento quotaFondo on (quotaFondo.beneAmmortizzabile_id = beneFondo.id and quotaFondo.consolidata = true and quotaFondo.annoSolareAmmortamento < "
				+ annoRiferimento + ") ");
		sb.append("			  where beneFondo.id = bene.id ");
		sb.append("			  group by beneFondo.id) as valoreQuoteFondo, ");
		sb.append("				(select (coalesce(sum(coalesce(valutFondo.importoValutazioneFondo,0)),0)) as valoreFondo ");
		sb.append("		from bamm_bene_ammortizzabile beneFondo left join bamm_valutazioni_bene valutFondo on (valutFondo.bene_id = beneFondo.id and valutFondo.anno <= "
				+ annoRiferimento + ") ");
		sb.append("		where beneFondo.id = bene.id ");
		sb.append("		group by beneFondo.id) as valoreValutazioniFondo, ");
		sb.append("		(select (coalesce(sum(coalesce(venditeFondo.importoStornoFondoAmmortamento,0)),0)) as valoreFondo ");
		sb.append("		from bamm_bene_ammortizzabile beneFondo left join bamm_vendite_bene venditeFondo on (venditeFondo.bene_id = beneFondo.id and YEAR(venditeFondo.dataVendita) <= "
				+ annoRiferimento + ") ");
		sb.append("		where beneFondo.id = bene.id ");
		sb.append("		group by beneFondo.id) as valoreVenditeFondo, ");

		// sb.append("			 coalesce(sum(coalesce(beneFiglio.importoSoggettoAdAmmortamentoSingolo,0)),0) as importoSoggettoAdAmmortamentoFigli, ");

		sb.append("			 (select (coalesce(sum(coalesce(beneFiglio.importoSoggettoAdAmmortamentoSingolo,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFiglio ");
		sb.append("			  where beneFiglio.benePadre_id = bene.id and beneFiglio.annoAcquisto <= " + annoRiferimento
				+ ")  as importoSoggettoAdAmmortamentoFigli, ");

		sb.append("			 (select (coalesce(sum(coalesce(valutFigli.importoValutazioneBene,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFiglio left join bamm_valutazioni_bene valutFigli on (valutFigli.bene_id = beneFiglio.id and valutFigli.anno <= "
				+ annoRiferimento + ") ");
		sb.append("			  where beneFiglio.benePadre_id = bene.id) as rivalutazioniFigli, ");
		sb.append("			 (select (coalesce(sum(coalesce(venditeFiglio.importoStornoValoreBene,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFiglio left join bamm_vendite_bene venditeFiglio on (venditeFiglio.bene_id = beneFiglio.id and YEAR(venditeFiglio.dataVendita) <= "
				+ annoRiferimento + ") ");
		sb.append("			  where beneFiglio.benePadre_id = bene.id) as venditeFigli, ");
		sb.append("			 (select (coalesce(sum(coalesce(valutFigli.importoValutazioneFondo,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFiglio left join bamm_valutazioni_bene valutFigli on (valutFigli.bene_id = beneFiglio.id and valutFigli.anno < "
				+ annoRiferimento + ") ");
		sb.append("			  where beneFiglio.benePadre_id = bene.id) as rivalutazioniFondoFigli, ");
		sb.append("			 (select (coalesce(sum(coalesce(venditeFiglio.importoStornoFondoAmmortamento,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFiglio left join bamm_vendite_bene venditeFiglio on (venditeFiglio.bene_id = beneFiglio.id and YEAR(venditeFiglio.dataVendita) < "
				+ annoRiferimento + ") ");
		sb.append("			  where beneFiglio.benePadre_id = bene.id) as venditeFondoFigli, ");
		sb.append("			 (select (coalesce(sum(coalesce(venditeFiglio.plusMinusValore,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFiglio left join bamm_vendite_bene venditeFiglio on (venditeFiglio.bene_id = beneFiglio.id and YEAR(venditeFiglio.dataVendita) <= "
				+ annoRiferimento + ") ");
		sb.append("			  where beneFiglio.benePadre_id = bene.id) as minusPlusValenzeFigli ");
		sb.append("from bamm_bene_ammortizzabile bene left join bamm_valutazioni_bene valut on (valut.bene_id = bene.id and valut.anno <= "
				+ annoRiferimento + ") ");
		sb.append("						     left join bamm_vendite_bene vendite on (vendite.bene_id = bene.id and YEAR(vendite.dataVendita) <= "
				+ annoRiferimento + ") ");
		sb.append("							left join bamm_quote_ammortamento quota on (quota.beneAmmortizzabile_id = bene.id and quota.consolidata = true and quota.annoSolareAmmortamento = "
				+ annoRiferimento + ") ");
		sb.append("							inner join bamm_sottospecie sottosp on ( sottosp.id = bene.sottoSpecie_id) ");
		sb.append("							inner join bamm_specie specie on ( specie.id = sottosp.specie_id) ");
		sb.append("							inner join bamm_gruppo gruppo on ( gruppo.id = specie.gruppo_id) ");
		sb.append("							left join bamm_bene_ammortizzabile beneFiglio on (beneFiglio.benePadre_id = bene.id and beneFiglio.annoAcquisto <= "
				+ annoRiferimento + ") ");
		sb.append("		where bene.benePadre_id is null ");
		sb.append(" and bene.stampaSuRegistriBeni = true ");
		sb.append(" and bene.annoAcquisto <= " + annoRiferimento);

		if (idGruppo != -1) {
			sb.append(" and gruppo.id = " + idGruppo);
		}
		if (idSpecie != -1) {
			sb.append(" and specie.id =" + idSpecie);
		}
		if (idSottoSpecie != -1) {
			sb.append(" and sottosp.id = " + idSottoSpecie);
		}
		sb.append(" group by bene.id ");
		sb.append(" order by gruppo.id,specie.id,sottosp.id,bene.codice ");
		return sb.toString();
	}

	/**
	 * Costruttore.
	 */
	private RegistroBeniQueryBuilder() {
		super();
	}
}
