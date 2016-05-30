package it.eurotn.panjea.conai.manager.interfaces;

import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;

import java.util.HashMap;
import java.util.Map;

public class LegnoDatiForm extends DatiForm {

	@Override
	protected Map<String, String> getControlByField() {
		HashMap<String, String> result = new HashMap<String, String>();
		result.put(DENOMINAZIONE, "Testo108");
		result.put(CODICE_SOCIO, "Testo109");
		result.put(ISCRITTO_PRODUTTORE, "Casella di controllo89");
		result.put(ISCRITTO_UTILIZZATORE, "Casella di controllo90");
		result.put(INDIRIZZO_FATTURAZIONE, "Testo120");
		result.put(PEC, "Testo121");
		result.put(CAP, "Testo110");
		result.put(CITTA, "Testo111");
		result.put(PROVINCIA, "Testo112");
		result.put(CF, "Testo113");
		result.put(PIVA, "Testo114");
		result.put(REFERENTE, "Testo115");
		result.put(TEL, "Testo116");
		result.put(FAX, "Testo117");
		result.put(EMAIL, "Testo118");
		result.put(PERIODO_ANNUALE, "Casella di controllo91");
		result.put(PERIODO_TRIMESTRE_1, "Casella di controllo92");
		result.put(PERIODO_TRIMESTRE_2, "Casella di controllo93");
		result.put(PERIODO_TRIMESTRE_3, "Casella di controllo94");
		result.put(PERIODO_TRIMESTRE_4, "Casella di controllo95");
		result.put(PERIODO_MESE_1, "Casella di controllo96");
		result.put(PERIODO_MESE_2, "Casella di controllo97");
		result.put(PERIODO_MESE_3, "Casella di controllo98");
		result.put(PERIODO_MESE_4, "Casella di controllo99");
		result.put(PERIODO_MESE_5, "Casella di controllo100");
		result.put(PERIODO_MESE_6, "Casella di controllo101");
		result.put(PERIODO_MESE_7, "Casella di controllo102");
		result.put(PERIODO_MESE_8, "Casella di controllo103");
		result.put(PERIODO_MESE_9, "Casella di controllo104");
		result.put(PERIODO_MESE_10, "Casella di controllo105");
		result.put(PERIODO_MESE_11, "Casella di controllo106");
		result.put(PERIODO_MESE_12, "Casella di controllo107");
		result.put(TOT_ESENTE, "Testo45");
		result.put(TOT_ASSOGETTATO, "Testo46");
		result.put(TOTALE, "Testo47");
		result.put(TOTALE_CONTRIBUTO, "Testo6");
		result.put(TOTALE_CONTRIBUTO_LETTERE, "Testo49");
		result.put(CONTRIBUTO, "Testo9");
		result.put(ANNO, "TestoAnno");
		result.put(DATA, "Testo8");
		return result;
	}

	@Override
	public ConaiMateriale getMateriale() {
		return ConaiMateriale.LEGNO;
	}

}
