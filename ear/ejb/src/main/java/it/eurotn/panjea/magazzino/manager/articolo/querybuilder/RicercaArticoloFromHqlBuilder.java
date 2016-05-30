package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.ricercaarticoli.ParametroRicercaAttributo;

public class RicercaArticoloFromHqlBuilder implements RigaArticoloRicercaHqlFragment {

	@Override
	public String createHql(ParametriRicercaArticolo parametriRicercaArticolo) {
		StringBuilder sb = new StringBuilder();

		sb.append("from Articolo a ");
		sb.append("inner join a.codiceIva codIva ");
		sb.append("inner join a.unitaMisura um ");
		if (parametriRicercaArticolo.isRicercaCodiceArticoloEntitaPresente()
				|| parametriRicercaArticolo.getIdEntita() != null) {
			sb.append("left join a.codiciEntita codEnt ");
		}
		if (!parametriRicercaArticolo.isLinguaAzienda()) {
			sb.append("left join fetch a.descrizioniLingua dl ");
		}
		if (parametriRicercaArticolo.isRicercaAttributiPresente()) {
			for (ParametroRicercaAttributo parametroRicercaAttributo : parametriRicercaArticolo.getRicercaAttributi()) {

				// devo aggiungere una join per ogni attributo che aggiungo per cercare ad es. un articolo che ha:
				// $tipo$=F e $nrpezzi$>100
				// verrà composto l'hql in questo modo:

				// left join a.attributiArticolo aatipo
				// left join aatipo.tipoAttributo tatipo
				// left join a.attributiArticolo aanrpezzi
				// left join aanrpezzi.tipoAttributo tanrpezzi

				// nella where (RicercaArticoloWhereAttributiHqlBuilder) dovrò ricomporre l'alias per eseguire
				// correttamente la query
				String aliasAttr = new String("aa" + parametroRicercaAttributo.getNome());
				String aliasTipoAttr = new String("ta" + parametroRicercaAttributo.getNome());

				sb.append("left join a.attributiArticolo ").append(aliasAttr).append(" ");
				sb.append("left join ").append(aliasAttr).append(".tipoAttributo ").append(aliasTipoAttr).append(" ");
			}
		}
		return sb.toString();
	}

}
