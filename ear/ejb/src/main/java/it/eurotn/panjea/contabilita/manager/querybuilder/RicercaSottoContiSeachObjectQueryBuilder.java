/**
 *
 */
package it.eurotn.panjea.contabilita.manager.querybuilder;

import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti.ETipoRicercaSottoConto;

import org.apache.commons.lang3.StringUtils;

/**
 * @author fattazzo
 * 
 */
public class RicercaSottoContiSeachObjectQueryBuilder {

	/**
	 * Crea l'sql per la from.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return sql
	 */
	private String createFrom(ParametriRicercaSottoConti parametri) {
		StringBuilder sb = new StringBuilder();

		sb.append(" from cont_sottoconti sott inner join cont_conti cont on sott.conto_id = cont.id ");
		sb.append(" inner join cont_mastri mastri on mastri.id = cont.mastro_id ");

		return sb.toString();
	}

	/**
	 * Crea l'sql per la order by.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return sql
	 */
	private String createOrderBy(ParametriRicercaSottoConti parametri) {
		StringBuilder sb = new StringBuilder(" order by sott.descrizione");

		if (parametri.getTipoRicercaSottoConto() == ETipoRicercaSottoConto.CODICE) {
			sb = new StringBuilder(" order by mastri.codice, cont.codice,sott.codice");
		}

		return sb.toString();
	}

	/**
	 * Crea la query di ricerca per i sottoconti.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @param codiceAzienda
	 *            codice azienda
	 * @return query creata
	 */
	public String createQuery(ParametriRicercaSottoConti parametri, String codiceAzienda) {
		StringBuilder sb = new StringBuilder();

		sb.append(createSelect(parametri));

		sb.append(createFrom(parametri));

		sb.append(createWhere(parametri, codiceAzienda));

		sb.append(createOrderBy(parametri));

		return sb.toString();
	}

	/**
	 * Crea l'sql per la select.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @return sql
	 */
	private String createSelect(ParametriRicercaSottoConti parametri) {
		StringBuilder sb = new StringBuilder();
		sb.append("select sott.id as id, ");
		sb.append("concat_ws('.',mastri.codice,cont.codice,sott.codice) as sottoContoCodice, ");
		sb.append("sott.descrizione as descrizione, ");
		sb.append("sott.version as version ");

		return sb.toString();
	}

	/**
	 * Crea l'sql per la where.
	 * 
	 * @param parametri
	 *            parametri di ricerca
	 * @param codiceAzienda
	 *            codice azienda
	 * @return sql
	 */
	private String createWhere(ParametriRicercaSottoConti parametri, String codiceAzienda) {
		StringBuilder sb = new StringBuilder();

		sb.append(" where sott.codiceAzienda = '" + codiceAzienda + "'");
		sb.append(" and sott.codice <> '000000' ");

		String valoreDaRicercare = StringUtils.replace(parametri.getValoreDaRicercare(), "'", "''");
		if (valoreDaRicercare != null) {

			switch (parametri.getTipoRicercaSottoConto()) {
			case CODICE:
				String codiceMastro = "";
				String codiceConto = "";
				String codiceSottoConto2 = "";
				String[] strings = valoreDaRicercare.split("\\.");

				for (int i = 0; i < strings.length; i++) {
					switch (i) {
					case 0:
						codiceMastro = strings[i].trim();
						break;
					case 1:
						codiceConto = strings[i].trim();
						break;
					case 2:
						codiceSottoConto2 = strings[i].trim();
						break;
					default:
						break;
					}
				}

				if ((codiceMastro != null) && (!codiceMastro.trim().isEmpty())) {
					sb.append(" and mastri.codice like '%" + codiceMastro.trim() + "%' ");
					if ((codiceConto != null) && (!codiceConto.trim().isEmpty())) {
						sb.append(" and cont.codice like '%" + codiceConto.trim() + "%' ");
						if ((codiceSottoConto2 != null) && (!codiceSottoConto2.trim().isEmpty())) {
							sb.append(" and sott.codice like '%" + codiceSottoConto2.trim() + "%' ");
						}
					}
				}
				break;
			case DESCRIZIONE:
				if (!valoreDaRicercare.isEmpty()) {
					sb.append(" and ( sott.descrizione like '" + valoreDaRicercare.replaceAll("\\*", "%") + "%' ) ");
				}
				break;
			default:
				throw new RuntimeException("Impossibile definire il tipo di ricerca del sottoconto ");
			}
		}

		if (parametri.getSottotipoConto() != null) {
			switch (parametri.getSottotipoConto()) {
			case CLIENTE:
			case FORNITORE:
				sb.append(" and cont.sottotipoConto = " + parametri.getSottotipoConto().ordinal() + " ");
				break;
			default:
				break;
			}
		}

		return sb.toString();
	}

}
