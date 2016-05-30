package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo.StatoArticolo;

public class RicercaArticoloWhereHqlBuilder implements RigaArticoloRicercaHqlFragment {

	@Override
	public String createHql(ParametriRicercaArticolo parametriRicercaArticolo) {
		StringBuilder sb = new StringBuilder();

		sb.append("where a.codiceAzienda=:codiceAzienda ");
		if (parametriRicercaArticolo.getIdCategoria() != null) {
			sb.append("and a.categoria.id in (:idCategorie) ");
		}
		if (parametriRicercaArticolo.getIdCategoriaCommerciale() != null) {
			sb.append("and a.categoriaCommercialeArticolo=:idCategoriaCommerciale ");
		}
		if (parametriRicercaArticolo.isAssortimentoArticoli()) {
			sb.append("and codEnt.entita.id=:idEntita ");
		}
		if (parametriRicercaArticolo.isEscludiDistinte()) {
			sb.append("and a.distinta=false ");
		}

		if (parametriRicercaArticolo.isSoloDistinte()) {
			sb.append("and a.distinta=true ");
		}

		if (parametriRicercaArticolo.getStatoArticolo().equals(StatoArticolo.ABILITATO)) {
			sb.append("and a.abilitato=true ");
		}
		if (parametriRicercaArticolo.getStatoArticolo().equals(StatoArticolo.DISABILITATO)) {
			sb.append("and a.abilitato=false ");
		}

		RicercaArticoloWhereCodiceDescrizioneHqlBuilder whereCodiceDescrizioneHqlBuilder = new RicercaArticoloWhereCodiceDescrizioneHqlBuilder();
		sb.append(whereCodiceDescrizioneHqlBuilder.createHql(parametriRicercaArticolo));

		RicercaArticoloWhereAttributiHqlBuilder whereAttributiHqlBuilder = new RicercaArticoloWhereAttributiHqlBuilder();
		sb.append(whereAttributiHqlBuilder.createHql(parametriRicercaArticolo));

		return sb.toString();
	}

}
