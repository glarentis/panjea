package it.eurotn.panjea.conai.manager.interfaces;

import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;

import java.util.Map;

public abstract class DatiForm {

	private Map<String, String> nomeControlli;
	public static final String DENOMINAZIONE = "denominazione";
	public static final String CODICE_SOCIO = "codiceSocio";
	public static final String ISCRITTO_PRODUTTORE = "iscritto_produttore";
	public static final String ISCRITTO_UTILIZZATORE = "iscritto_utilizzatore";
	public static final String INDIRIZZO_FATTURAZIONE = "indirizzo_fatturazione";
	public static final String PEC = "Pec";
	public static final String CAP = "Cap";
	public static final String CITTA = "Citta";
	public static final String PROVINCIA = "Provincia";
	public static final String CF = "codiceFiscale";
	public static final String PIVA = "partitaIva";
	public static final String REFERENTE = "Referente";
	public static final String TEL = "telefono";
	public static final String FAX = "Fax";
	public static final String EMAIL = "Email";
	public static final String PERIODO_ANNUALE = "annuale";
	public static final String PERIODO_TRIMESTRE_1 = "T1";
	public static final String PERIODO_TRIMESTRE_2 = "T2";
	public static final String PERIODO_TRIMESTRE_3 = "T3";
	public static final String PERIODO_TRIMESTRE_4 = "T4";
	public static final String PERIODO_MESE_1 = "M1";
	public static final String PERIODO_MESE_2 = "M2";
	public static final String PERIODO_MESE_3 = "M3";
	public static final String PERIODO_MESE_4 = "M4";
	public static final String PERIODO_MESE_5 = "M5";
	public static final String PERIODO_MESE_6 = "M6";
	public static final String PERIODO_MESE_7 = "M7";
	public static final String PERIODO_MESE_8 = "M8";
	public static final String PERIODO_MESE_9 = "M9";
	public static final String PERIODO_MESE_10 = "M10";
	public static final String PERIODO_MESE_11 = "M11";
	public static final String PERIODO_MESE_12 = "M12";
	public static final String TOT_ESENTE = "TOTALE_ESENTE";
	public static final String TOT_ASSOGETTATO = "TOTALE_ASSOGETTATO";
	public static final String TOTALE = "TOTALE";
	public static final String TOTALE_CONTRIBUTO = "TOTALE_CONTRIBUTO";
	public static final String TOTALE_CONTRIBUTO_LETTERE = "TOTALE_CONTRIBUTO_LETTERE";
	public static final String CONTRIBUTO = "CONTRIBUTO";
	public static final String ANNO = "ANNO";
	public static final String DATA = "DATA";

	/**
	 *
	 * Costruttore.
	 *
	 */
	public DatiForm() {
		nomeControlli = getControlByField();
	}

	/**
	 *
	 * @return mappa contenente il nome del campo ed il nome del controllo
	 */
	protected abstract Map<String, String> getControlByField();

	/**
	 *
	 * @param fieldName
	 *            nome del campo per il quale estrarre il nome del controllo
	 * @return nome del controllo associato nel pdf
	 */
	public String getControlName(String fieldName) {
		return nomeControlli.get(fieldName);
	}

	/**
	 *
	 * @return materiale associato ai dati.
	 */
	public abstract ConaiMateriale getMateriale();
}
