package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;

public class RicercaArticoloHqlBuilder implements RigaArticoloRicercaHqlFragment {

	@Override
	public String createHql(ParametriRicercaArticolo parametriRicercaArticolo) {
		StringBuilder sb = new StringBuilder();

		RicercaArticoloSelectHqlBuilder selectHqlBuilder = new RicercaArticoloSelectHqlBuilder();
		sb.append(selectHqlBuilder.createHql(parametriRicercaArticolo));

		RicercaArticoloFromHqlBuilder fromHqlBuilder = new RicercaArticoloFromHqlBuilder();
		sb.append(fromHqlBuilder.createHql(parametriRicercaArticolo));

		RicercaArticoloWhereHqlBuilder whereHqlBuilder = new RicercaArticoloWhereHqlBuilder();
		sb.append(whereHqlBuilder.createHql(parametriRicercaArticolo));

		RicercaArticoloOrderByHqlBuilder orderByHqlBuilder = new RicercaArticoloOrderByHqlBuilder();
		sb.append(orderByHqlBuilder.createHql(parametriRicercaArticolo));

		return sb.toString();
	}

}
