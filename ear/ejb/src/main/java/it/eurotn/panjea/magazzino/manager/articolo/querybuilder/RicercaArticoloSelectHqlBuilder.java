package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.ricercaarticoli.ParametroRicercaAttributo;

public class RicercaArticoloSelectHqlBuilder implements RigaArticoloRicercaHqlFragment {

	/**
	 * Crea la parte di select relativa agli attributi facendo una concat dei codici e dei valori degli attributi
	 * presenti.
	 *
	 * @param parametriRicercaArticolo
	 *            parametriRicercaArticolo
	 * @return String
	 */
	private String createFieldsAttributi(ParametriRicercaArticolo parametriRicercaArticolo) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sbaa = new StringBuilder();
		StringBuilder sbta = new StringBuilder();
		try {
			for (ParametroRicercaAttributo parametroRicercaAttributo : parametriRicercaArticolo.getRicercaAttributi()) {
				String aliasAttr = new String("aa" + parametroRicercaAttributo.getNome());
				String aliasTipoAttr = new String("ta" + parametroRicercaAttributo.getNome());

				sbaa.append(aliasAttr).append(".valore").append(",'#',");
				sbta.append(aliasTipoAttr).append(".codice").append(",'#',");
			}
			// tolgo la virgola finale
			sbta.deleteCharAt(sbta.length() - 1);
			sbaa.deleteCharAt(sbaa.length() - 1);

			sb.append("CONCAT(");
			sb.append(sbta.toString());
			sb.append(") as codiciAttributi, ");
			sb.append("CONCAT(");
			sb.append(sbaa.toString());
			sb.append(") as valoriAttributi, ");
		} catch (Exception ex) {
			// se non riesco a fare il parser sulla stringa non metto le condizioni.
			return "";
		}
		return sb.toString();
	}

	@Override
	public String createHql(ParametriRicercaArticolo parametriRicercaArticolo) {
		StringBuilder sb = new StringBuilder();

		sb.append("select distinct ");
		sb.append("a.id as id, ");
		sb.append("a.version as version, ");
		sb.append("a.codice as codice, ");
		sb.append("a.codiceInterno as codiceInterno, ");
		sb.append("a.barCode as barCode, ");
		sb.append("a.descrizioneLinguaAziendale as descrizione, ");
		if (!parametriRicercaArticolo.isLinguaAzienda()) {
			sb.append("dl as descrizioneLinguaAziendale, ");
		}
		if (parametriRicercaArticolo.isRicercaCodiceArticoloEntitaPresente()) {
			sb.append("(case codEnt.entita.id WHEN :idEntita THEN codEnt.codice ELSE null END) as codiceEntita, ");
		}
		sb.append("a.abilitato as abilitato, ");
		if (parametriRicercaArticolo.isRicercaAttributiPresente()) {
			sb.append(createFieldsAttributi(parametriRicercaArticolo));
		}
		sb.append("a.distinta as distinta, ");
		sb.append("a.tipoLotto as tipoLotto, ");
		sb.append("um as unitaMisura, ");
		sb.append("a.numeroDecimaliQta as numeroDecimaliQta, ");
		sb.append("a.numeroDecimaliPrezzo as numeroDecimaliPrezzo, ");
		sb.append("codIva.percApplicazione as percApplicazioneCodiceIva, ");
		sb.append("a.provenienzaPrezzoArticolo as provenienzaPrezzoArticolo ");

		return sb.toString();
	}

}
