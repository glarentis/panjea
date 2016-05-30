package it.eurotn.panjea.beniammortizzabili2.manager.sqlBuilder;

public final class VenditeAnnualiQueryBuilder {

	/**
	 * Carica le vendite dei beni padre.
	 *
	 * @param annoRiferimento
	 *            anno di riferimento
	 * @param idSpecie
	 *            id specie
	 * @param idSottoSpecie
	 *            id sottospecie
	 * @return SQL
	 */
	public static String getVenditeBeniPadre(Integer annoRiferimento, Integer idSpecie, Integer idSottoSpecie) {

		StringBuilder sb = new StringBuilder();
		sb.append("select distinct bene.id as idBene, ");
		sb.append("			 bene.codice as codiceBene, ");
		sb.append("			 bene.descrizione as descrizioneBene, ");
		sb.append("			 bene.annoAcquisto as annoAcquisto, ");
		sb.append("			 coalesce(bene.importoSoggettoAdAmmortamentoSingolo,0) as importoSoggettoAdAmmortamentoSingolo, ");
		sb.append("			 specie.id as idSpecie, ");
		sb.append("			 specie.codice as codiceSpecie, ");
		sb.append("			 specie.descrizione as descrizioneSpecie, ");
		sb.append("			 sottosp.id as idSottoSpecie, ");
		sb.append("			 sottosp.codice as codiceSottoSpecie, ");
		sb.append("			 sottosp.descrizione as descrizioneSottoSpecie, ");
		sb.append("			 tipoElim.id as tipoElimid, ");
		sb.append("			 tipoElim.descrizione as tipologiaEliminazione, ");
		sb.append("			 (select (coalesce(sum(coalesce(valutFondo.importoValutazioneBene,0)),0)) as valoreFondo ");
		sb.append("			  from bamm_bene_ammortizzabile beneFondo left join bamm_valutazioni_bene valutFondo on (valutFondo.bene_id = beneFondo.id) ");
		sb.append("			  where beneFondo.id = bene.id ");
		sb.append("			  group by beneFondo.id) as rivalutazioni, ");
		sb.append("			 (select (coalesce(sum(coalesce(venditeBene.valoreVendita,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneVend left join bamm_vendite_bene venditeBene on (venditeBene.bene_id = beneVend.id) ");
		sb.append("			  where beneVend.id = bene.id and YEAR(venditeBene.dataVendita) = " + annoRiferimento
				+ ") as venditeAnnualiBene, ");
		sb.append(" (select count(venditeBene.valoreVendita) as valore from bamm_bene_ammortizzabile beneVend left join bamm_vendite_bene venditeBene on (venditeBene.bene_id = beneVend.id) where beneVend.id = bene.id and YEAR(venditeBene.dataVendita) = "
				+ annoRiferimento + ") as numVenditeAnnualiBene, ");
		sb.append("			 (select (coalesce(sum(coalesce(venditeBenePlus.plusMinusValore,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile benePlus left join bamm_vendite_bene venditeBenePlus on (venditeBenePlus.bene_id = benePlus.id) ");
		sb.append("			  where benePlus.id = bene.id and YEAR(venditeBenePlus.dataVendita) = " + annoRiferimento
				+ ") as plusMinusValoreAnnualiBene, ");
		sb.append("			 (select (coalesce(sum(coalesce(venditeFondo.importoStornoValoreBene,0)),0)) as valoreFondo ");
		sb.append("			  from bamm_bene_ammortizzabile beneFondo left join bamm_vendite_bene venditeFondo on (venditeFondo.bene_id = beneFondo.id) ");
		sb.append("			  where beneFondo.id = bene.id ");
		sb.append("			  group by beneFondo.id) as vendite, ");
		sb.append("			 (select (coalesce(sum(coalesce(quotaFondo.impQuotaAmmortamentoOrdinario,0)),0) + ");
		sb.append("					coalesce(sum(coalesce(quotaFondo.impQuotaAmmortamentoAnticipato,0)),0))as valoreFondo ");
		sb.append("			  from bamm_bene_ammortizzabile beneFondo left join bamm_quote_ammortamento quotaFondo on (quotaFondo.beneAmmortizzabile_id = beneFondo.id and quotaFondo.consolidata = true) ");
		sb.append("			  where beneFondo.id = bene.id ");
		sb.append("			  group by beneFondo.id) as valoreQuoteFondo, ");
		sb.append("			 (select (coalesce(sum(coalesce(valutFondo.importoValutazioneFondo,0)),0)) as valoreFondo ");
		sb.append("			  from bamm_bene_ammortizzabile beneFondo left join bamm_valutazioni_bene valutFondo on (valutFondo.bene_id = beneFondo.id) ");
		sb.append("			  where beneFondo.id = bene.id ");
		sb.append("			  group by beneFondo.id) as valoreValutazioniFondo, ");
		sb.append("			 (select (coalesce(sum(coalesce(venditeFondo.importoStornoFondoAmmortamento,0)),0)) as valoreFondo ");
		sb.append("			  from bamm_bene_ammortizzabile beneFondo left join bamm_vendite_bene venditeFondo on (venditeFondo.bene_id = beneFondo.id) ");
		sb.append("			  where beneFondo.id = bene.id and YEAR(venditeFondo.dataVendita) < " + annoRiferimento);
		sb.append("			  ) as valoreVenditeFondo, ");
		sb.append("			 (select (coalesce(sum(coalesce(beneFiglio.importoSoggettoAdAmmortamentoSingolo,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFiglio ");
		sb.append("			  where beneFiglio.benePadre_id = bene.id)  as importoSoggettoAdAmmortamentoFigli, ");
		sb.append("			 (select (coalesce(sum(coalesce(valutFigli.importoValutazioneBene,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFiglio left join bamm_valutazioni_bene valutFigli on (valutFigli.bene_id = beneFiglio.id) ");
		sb.append("			  where beneFiglio.benePadre_id = bene.id) as rivalutazioniFigli, ");
		sb.append("			 (select (coalesce(sum(coalesce(venditeFiglio.importoStornoValoreBene,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFiglio left join bamm_vendite_bene venditeFiglio on (venditeFiglio.bene_id = beneFiglio.id) ");
		sb.append("			  where beneFiglio.benePadre_id = bene.id) as venditeFigli, ");
		sb.append("			 (select (coalesce(sum(coalesce(valutFigli.importoValutazioneFondo,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFiglio left join bamm_valutazioni_bene valutFigli on (valutFigli.bene_id = beneFiglio.id) ");
		sb.append("			  where beneFiglio.benePadre_id = bene.id) as rivalutazioniFondoFigli, ");
		sb.append("			 (select (coalesce(sum(coalesce(venditeFiglio.importoStornoFondoAmmortamento,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFiglio left join bamm_vendite_bene venditeFiglio on (venditeFiglio.bene_id = beneFiglio.id) ");
		sb.append("			  where beneFiglio.benePadre_id = bene.id and YEAR(venditeFiglio.dataVendita) < "
				+ annoRiferimento + ")  as venditeFondoFigli, ");
		sb.append("			 (select (coalesce(sum(coalesce(venditeAnnoFigli.valoreVendita,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFiglio left join bamm_vendite_bene venditeAnnoFigli on (venditeAnnoFigli.bene_id = beneFiglio.id) ");
		sb.append("			  where beneFiglio.benePadre_id = bene.id and YEAR(venditeAnnoFigli.dataVendita) = "
				+ annoRiferimento + ") as venditeAnnualiFigli , ");
		sb.append(" (select sum(venditeAnnoFigli.valoreVendita) as valore from bamm_bene_ammortizzabile beneFiglio left join bamm_vendite_bene venditeAnnoFigli on (venditeAnnoFigli.bene_id = beneFiglio.id) where beneFiglio.benePadre_id = bene.id and YEAR(venditeAnnoFigli.dataVendita) = "
				+ annoRiferimento + ") as numVenditeAnnualiFigli, ");
		sb.append("			 (select (coalesce(sum(coalesce(venditeBeneFigliPlus.plusMinusValore,0)),0)) as valore ");
		sb.append("			  from bamm_bene_ammortizzabile beneFigliPlus left join bamm_vendite_bene venditeBeneFigliPlus on (venditeBeneFigliPlus.bene_id = beneFigliPlus.id) ");
		sb.append("			  where beneFigliPlus.benePadre_id = bene.id and YEAR(venditeBeneFigliPlus.dataVendita) = "
				+ annoRiferimento + ") as plusMinusValoreAnnualiFigli ");
		sb.append("from bamm_bene_ammortizzabile bene left join bamm_valutazioni_bene valut on (valut.bene_id = bene.id) ");
		sb.append("						     left join bamm_vendite_bene vendite on (vendite.bene_id = bene.id)  and year(vendite.dataVendita) =  "
				+ annoRiferimento);
		sb.append("							inner join bamm_sottospecie sottosp on ( sottosp.id = bene.sottoSpecie_id) ");
		sb.append("							inner join bamm_specie specie on ( specie.id = sottosp.specie_id) ");
		sb.append("							left join bamm_bene_ammortizzabile beneFiglio on (beneFiglio.benePadre_id = bene.id) ");
		sb.append("							left join bamm_tipologie_eliminazione tipoElim on vendite.tipologiaEliminazione_id = tipoElim.id ");
		sb.append("where bene.benePadre_id is null ");

		if (idSpecie != -1) {
			sb.append(" and specie.id =" + idSpecie);
		}
		if (idSottoSpecie != -1) {
			sb.append(" and sottosp.id = " + idSottoSpecie);
		}
		sb.append(" group by bene.id ");
		sb.append(" having (numVenditeAnnualiBene > 0 or numVenditeAnnualiFigli > 0) ");
		// sb.append(" having (venditeAnnualiFigli > 0 or venditeAnnualiBene > 0)  or (tipoElimid is not null) ");
		sb.append(" order by specie.id,sottosp.id,bene.codice ");
		return sb.toString();
	}

	/**
	 * Costruttore.
	 *
	 */
	private VenditeAnnualiQueryBuilder() {
		super();
	}

}
