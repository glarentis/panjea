package it.eurotn.panjea.magazzino.manager.articolo.querybuilder;

import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;

import org.apache.commons.lang3.StringUtils;

/**
 * Classe che si preoccupa di avvalorare le proprietà di ParametriRicercaArticolo in modo da impostare correttamente i
 * filtri per la ricerca articoli.
 * 
 * @author leonardo
 */
public final class ParametriRicercaArticoloCodicePopulator {

	/**
	 * Restituisce il barcode dalla stringa di partenza.
	 * 
	 * @param stringToParse
	 *            la stringa in cui cercare il barcode
	 * @return il barcode o null
	 */
	private static String parseBarCode(String stringToParse) {
		return parseDelimitedString(ParametriRicercaArticolo.DELIMITER_BARCODE, stringToParse);
	}

	/**
	 * Parse alla ricerca del codice che non sia barCode,codEntita o attributi.
	 * 
	 * @param stringToParse
	 *            la string in cui cercare il codice/descrizione articolo
	 * @param barCode
	 *            il barCode da escludere dalla ricerca
	 * @param codiceInterno
	 *            il codiceInterno da escludere dalla ricerca
	 * @param codEntita
	 *            il codEntita da escludere dalla ricerca
	 * @return la stringa che rappresenta il codice/descrizione dell'articolo
	 */
	private static String parseCodice(String stringToParse, String barCode, String codEntita, String codiceInterno) {
		// per identificare il codice, devo togliere tutto quello che non è
		// qualcosa di conosciuto
		String codice = new String(stringToParse);

		// tolgo barcode
		codice = codice.replaceAll(ParametriRicercaArticolo.DELIMITER_BARCODE, "");
		if (barCode != null) {
			codice = StringUtils.replace(codice, barCode, "");
		}

		// tolgo codiceInterno
		codice = codice.replaceAll(ParametriRicercaArticolo.DELIMITER_CODINTERNO, "");
		if (codiceInterno != null) {
			codice = StringUtils.replace(codice, codiceInterno, "");
		}

		// tolgo codice entita
		codice = codice.replaceAll(ParametriRicercaArticolo.DELIMITER_CODENT, "");
		if (codEntita != null) {
			codice = StringUtils.replace(codice, codEntita, "");
		}

		// tolgo attributi
		int[] beginEndAttributi = ParametriRicercaArticoloAttributiPopulator.getBeginEndAttributiIndexes(codice);
		if (beginEndAttributi[0] != -1 && beginEndAttributi[1] != -1) {
			String attrToRemove = codice.substring(beginEndAttributi[0], beginEndAttributi[1]);
			codice = StringUtils.replace(codice, attrToRemove, "");
		}
		codice = codice.trim();

		// quello che rimane è codice/descrizione
		return codice.isEmpty() ? null : codice;
	}

	/**
	 * Restituisce il codiceEntita dalla stringa di partenza.
	 * 
	 * @param stringToParse
	 *            la stringa in cui cercare il codiceEntita
	 * @return il codiceEntita o null
	 */
	private static String parseCodiceEntita(String stringToParse) {
		return parseDelimitedString(ParametriRicercaArticolo.DELIMITER_CODENT, stringToParse);
	}

	/**
	 * Restituisce il codiceInterno dalla stringa di partenza.
	 * 
	 * @param stringToParse
	 *            la stringa in cui cercare il codiceInterno
	 * @return il codiceInterno o null
	 */
	private static String parseCodiceInterno(String stringToParse) {
		return parseDelimitedString(ParametriRicercaArticolo.DELIMITER_CODINTERNO, stringToParse);
	}

	/**
	 * Esegue il parse della string alla ricerca della parte rappresentante la proprietà associata al delimitatore
	 * scelto.
	 * 
	 * @param delimiter
	 *            il delimitatore da trovare
	 * @param stringToFilter
	 *            la stringa di cui eseguire il parse
	 * @return la stringa di cui è stato eseguito il parse alla quale è stata rimossa la parte trovata
	 */
	private static String parseDelimitedString(String delimiter, String stringToFilter) {
		String valueToSet = null;

		// try {
		// // il pattern è @(.*?)@?
		// // quindi trova una string tipo @1a2b3c@ ma anche @1a2b3c con
		// l'ultimo delimitatore opzionale
		// Pattern pattern = Pattern.compile(delimiter + "(.*?)" + delimiter +
		// "?");
		// Matcher matcher = pattern.matcher(stringToFilter);
		// if (matcher.find()) {
		// valueToSet = matcher.group(1);
		// }
		// } catch (Exception e) {
		// // non faccio nulla e il value to set rimane null
		// }

		// in una stringa #001# trova: 001 con # come delimiter
		if (stringToFilter.indexOf(delimiter) != -1) {
			// primo delimitatore
			int sxCodePos = stringToFilter.indexOf(delimiter);
			// ultimo delimitatore
			int dxCodePos = stringToFilter.lastIndexOf(delimiter);

			// se l'ultimo delimitatore è ad una posizione diversa vuol dire che
			// è il secondo delimitatore, altrimenti
			// prendo la lunghezza della stringa come seconda posizione (sto
			// ancora scrivendo)
			dxCodePos = dxCodePos != sxCodePos ? dxCodePos + 1 : stringToFilter.trim().length();

			// substring sul barcode
			String code = stringToFilter.substring(sxCodePos, dxCodePos);

			// tolgo il delimitatore per isolare il solo codice
			valueToSet = code.replaceAll(delimiter, "");
			valueToSet = valueToSet.trim();
		}
		return valueToSet;
	}

	/**
	 * Popola le proprietà di ParametriRicercaArticolo prendendo informazioni dall'esterno (ad esempio il codice azienda
	 * o le categorie figlie) e trattando proprietà già impostate dei parametri (ad esempio la proprietà filtro in cui
	 * ci possono essere informazioni riguardanti attributi, barcode, ecc).
	 * 
	 * @param parametriRicercaArticolo
	 *            i parametri di cui trattare i dati
	 * @return i parametri con tutte le proprietà utili alla ricerca valorizzate
	 */
	public static ParametriRicercaArticolo populate(ParametriRicercaArticolo parametriRicercaArticolo) {
		if (parametriRicercaArticolo.getFiltro() != null) {

			String filterToParse = new String(parametriRicercaArticolo.getFiltro());

			// barcode o null se non presente #
			String barCode = parseBarCode(filterToParse);
			parametriRicercaArticolo.setBarCode(barCode);

			// barcode o null se non presente £
			String codiceInterno = parseCodiceInterno(filterToParse);
			parametriRicercaArticolo.setCodiceInterno(codiceInterno);

			// codentita o null se non presente @
			String codEntita = parseCodiceEntita(filterToParse);
			parametriRicercaArticolo.setCodiceEntita(codEntita);

			// codice/descrizione è quello che non riconosco come
			// barcode,codiceentita o attributi
			String codice = parseCodice(filterToParse, barCode, codEntita, codiceInterno);
			parametriRicercaArticolo.setCodice(codice);
			parametriRicercaArticolo.setDescrizione(codice);
		}

		// per tutte le proprietà, se presenti, devo aggiungere la percentuale
		// in modo da cercare in 'like'
		if (parametriRicercaArticolo.getBarCode() != null) {
			parametriRicercaArticolo.setBarCode(parametriRicercaArticolo.getBarCode().replaceAll("\\*", "%") + "%");
		}
		if (parametriRicercaArticolo.getCodiceInterno() != null) {
			parametriRicercaArticolo.setCodiceInterno(parametriRicercaArticolo.getCodiceInterno()
					.replaceAll("\\*", "%") + "%");
		}
		if (parametriRicercaArticolo.getCodiceEntita() != null) {
			parametriRicercaArticolo.setCodiceEntita(parametriRicercaArticolo.getCodiceEntita().replaceAll("\\*", "%")
					+ "%");
		}
		if (parametriRicercaArticolo.getCodice() != null) {
			parametriRicercaArticolo.setCodice(parametriRicercaArticolo.getCodice().replaceAll("\\*", "%") + "%");
		}
		if (parametriRicercaArticolo.getDescrizione() != null) {
			parametriRicercaArticolo.setDescrizione(parametriRicercaArticolo.getDescrizione().replaceAll("\\*", "%")
					+ "%");
		}

		return parametriRicercaArticolo;
	}

	/**
	 * Costruttore.
	 */
	private ParametriRicercaArticoloCodicePopulator() {
		super();
	}

}
