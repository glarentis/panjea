package it.eurotn.panjea.conai.manager.interfaces;

import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;

import java.util.HashMap;
import java.util.Map;

public class AcciaioDatiForm extends DatiForm {

	@Override
	protected Map<String, String> getControlByField() {
		HashMap<String, String> result = new HashMap<String, String>();
		result.put(DENOMINAZIONE, "Testo36");
		result.put(CODICE_SOCIO, "Testo40");
		result.put(ISCRITTO_PRODUTTORE, "Casella di controllo1");
		result.put(ISCRITTO_UTILIZZATORE, "Casella di controllo2");
		result.put(INDIRIZZO_FATTURAZIONE, "Testo41");
		result.put(PEC, "Testo42");
		result.put(CAP, "Testo43");
		result.put(CITTA, "Testo44");
		result.put(PROVINCIA, "Testo45");
		result.put(CF, "Testo46");
		result.put(PIVA, "Testo47");
		result.put(REFERENTE, "Testo48");
		result.put(TEL, "Testo49");
		result.put(FAX, "Testo50");
		result.put(EMAIL, "Testo51");
		result.put(PERIODO_ANNUALE, "Casella di controllo3");
		result.put(PERIODO_TRIMESTRE_1, "Casella di controllo4");
		result.put(PERIODO_TRIMESTRE_2, "Casella di controllo5");
		result.put(PERIODO_TRIMESTRE_3, "Casella di controllo6");
		result.put(PERIODO_TRIMESTRE_4, "Casella di controllo7");
		result.put(PERIODO_MESE_1, "Casella di controllo8");
		result.put(PERIODO_MESE_2, "Casella di controllo9");
		result.put(PERIODO_MESE_3, "Casella di controllo10");
		result.put(PERIODO_MESE_4, "Casella di controllo11");
		result.put(PERIODO_MESE_5, "Casella di controllo12");
		result.put(PERIODO_MESE_6, "Casella di controllo13");
		result.put(PERIODO_MESE_7, "Casella di controllo14");
		result.put(PERIODO_MESE_8, "Casella di controllo15");
		result.put(PERIODO_MESE_9, "Casella di controllo16");
		result.put(PERIODO_MESE_10, "Casella di controllo17");
		result.put(PERIODO_MESE_11, "Casella di controllo18");
		result.put(PERIODO_MESE_12, "Casella di controllo19");
		result.put(TOT_ESENTE, "Testo10");
		result.put(TOT_ASSOGETTATO, "Testo56");
		result.put(TOTALE, "Testo57");
		result.put(TOTALE_CONTRIBUTO, "Testo13");
		result.put(TOTALE_CONTRIBUTO_LETTERE, "Testo11");

		result.put(CONTRIBUTO, "Testo12");
		result.put(ANNO, "TestoAnno");
		result.put(DATA, "Testo14");
		return result;
	}

	@Override
	public ConaiMateriale getMateriale() {
		return ConaiMateriale.ACCIAIO;
	}

}
