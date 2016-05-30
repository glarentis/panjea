package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.ricercaarticoli.ParametroRicercaAttributo;

import java.util.List;

public class RicercaArticoloWhereAttributiHqlBuilder implements RigaArticoloRicercaHqlFragment {

	@Override
	public String createHql(ParametriRicercaArticolo parametriRicercaArticolo) {
		StringBuilder sb = new StringBuilder();

		List<ParametroRicercaAttributo> attributi = parametriRicercaArticolo.getRicercaAttributi();
		if (attributi != null && attributi.size() > 0) {
			String condition = "and (";

			// per ogni attributo devo utilizzare un alias diverso per poter cercare un articolo che possiede più
			// attributi, ad es:
			// $tipo$=F e $nrpezzi$>100

			// ricompongo l'alias con il nome dell'attributo (vedi RicercaArticoloFromHqlBuilder)
			for (ParametroRicercaAttributo attributo : attributi) {

				String aliasAttr = new String("aa" + attributo.getNome());
				String aliasTipoAttr = new String("ta" + attributo.getNome());

				String paramAttr = new String("param" + attributo.getNome());
				String paramValoreAttr = new String("paramValore" + attributo.getNome());

				sb.append(condition);
				sb.append(" (" + aliasTipoAttr + ".codice=:" + paramAttr + " ");
				if (attributo.getOperatore() != null) {
					sb.append(" and cast(" + aliasAttr + ".valore as " + attributo.getTipoDatoString() + ")");
					sb.append(attributo.getOperatore());
					sb.append(":" + paramValoreAttr + " ");
				}
				sb.append(") ");
				condition = " and ";
			}
			sb.append(") ");
		}
		return sb.toString();
	}

}
