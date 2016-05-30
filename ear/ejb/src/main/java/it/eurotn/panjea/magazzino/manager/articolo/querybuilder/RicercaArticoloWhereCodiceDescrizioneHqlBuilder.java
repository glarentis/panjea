package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;

import org.apache.commons.lang3.StringUtils;

public class RicercaArticoloWhereCodiceDescrizioneHqlBuilder implements RigaArticoloRicercaHqlFragment {

	@Override
	public String createHql(ParametriRicercaArticolo parametriRicercaArticolo) {
		StringBuilder sb = new StringBuilder();

		boolean isOr = parametriRicercaArticolo.isRicercaCodiceOrDescrizione();
		boolean isLinguaAzienda = parametriRicercaArticolo.isLinguaAzienda();
		String codice = parametriRicercaArticolo.getCodice();
		String descrizione = parametriRicercaArticolo.getDescrizione();

		String conditionBetween = "and";
		String leftBracket = "";
		String rightBracket = "";
		if (isOr && codice != null && descrizione != null) {
			conditionBetween = "or";
			leftBracket = "(";
			rightBracket = ")";
		}
		if (codice != null) {
			sb.append("and ");
			sb.append(leftBracket);
			if (codice.contains("|")) {
				String[] codiciBetween = codice.replace("%", "").split("\\|");
				sb.append(" a.codice between '" + codiciBetween[0]);

				// al codice finale aggiungo l'ultimo carattere ascii ripetuto per poter comprendere nella ricerca tutti i codici articolo che cominciano con la stringa richiesta
				String codiceFinale = (codiciBetween.length > 1) ? codiciBetween[1] : codiciBetween[0];
				codiceFinale = codiceFinale + StringUtils.repeat(String.valueOf((char)126), 30);

				sb.append("' and '" + codiceFinale  + "'");
			} else {
				sb.append("a.codice like :codice ");
			}
		}
		if (descrizione != null) {
			sb.append(conditionBetween);
			sb.append(getHqlDescrizioneInLingua(isLinguaAzienda));
		}
		sb.append(rightBracket);

		if (parametriRicercaArticolo.isRicercaBarCodePresente()) {
			sb.append("and a.barCode like :barCode ");
		}

		if (parametriRicercaArticolo.isRicercaCodiceInternoPresente()) {
			sb.append("and a.codiceInterno like :codiceInterno ");
		}

		if (parametriRicercaArticolo.getCodiceEntita() != null) {
			sb.append("and codEnt.codice like :codiceEntita ");
		}

		return sb.toString();
	}

	/**
	 * Se in lingua aziendale, carico la descrizione standard, altrimenti devo caricare la lingua specifica.
	 *
	 * @param isLinguaAzienda
	 *            la lingua dell'utente è uguale alla lingua aziendale true, altrimenti false
	 * @return la parte di query che recupera la lingua in accordo con quella dell'utente
	 */
	private String getHqlDescrizioneInLingua(boolean isLinguaAzienda) {
		String descrizioneFragment = null;
		if (isLinguaAzienda) {
			descrizioneFragment = new String(" (a.descrizioneLinguaAziendale like :descrizione) ");
		} else {
			descrizioneFragment = new String(" (dl.codiceLingua=:lingua) and (dl.descrizione like :descrizione) ");
		}
		return descrizioneFragment;
	}

}
