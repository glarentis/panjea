package it.eurotn.panjea.conai.manager.interfaces;

import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;

import java.util.HashMap;
import java.util.Map;

public class PlasticaDatiForm extends DatiForm {

	@Override
	protected Map<String, String> getControlByField() {
		HashMap<String, String> result = new HashMap<String, String>();
		result.put(DENOMINAZIONE, "Testo62");
		result.put(CODICE_SOCIO, "Testo63");
		result.put(ISCRITTO_PRODUTTORE, "Casella di controllo15");
		result.put(ISCRITTO_UTILIZZATORE, "Casella di controllo16");
		result.put(INDIRIZZO_FATTURAZIONE, "Testo64");
		result.put(PEC, "Testo65");
		result.put(CAP, "Testo66");
		result.put(CITTA, "Testo67");
		result.put(PROVINCIA, "Testo68");
		result.put(CF, "Testo69");
		result.put(PIVA, "Testo70");
		result.put(REFERENTE, "Testo71");
		result.put(TEL, "Testo72");
		result.put(FAX, "Testo73");
		result.put(EMAIL, "Testo74");
		result.put(PERIODO_ANNUALE, "Casella di controllo17");
		result.put(PERIODO_TRIMESTRE_1, "Casella di controllo34");
		result.put(PERIODO_TRIMESTRE_2, "Casella di controllo35");
		result.put(PERIODO_TRIMESTRE_3, "Casella di controllo36");
		result.put(PERIODO_TRIMESTRE_4, "Casella di controllo37");
		result.put(PERIODO_MESE_1, "Casella di controllo38");
		result.put(PERIODO_MESE_2, "Casella di controllo39");
		result.put(PERIODO_MESE_3, "Casella di controllo40");
		result.put(PERIODO_MESE_4, "Casella di controllo41");
		result.put(PERIODO_MESE_5, "Casella di controllo42");
		result.put(PERIODO_MESE_6, "Casella di controllo43");
		result.put(PERIODO_MESE_7, "Casella di controllo44");
		result.put(PERIODO_MESE_8, "Casella di controllo45");
		result.put(PERIODO_MESE_9, "Casella di controllo46");
		result.put(PERIODO_MESE_10, "Casella di controllo47");
		result.put(PERIODO_MESE_11, "Casella di controllo48");
		result.put(PERIODO_MESE_12, "Casella di controllo49");
		result.put(TOT_ESENTE, "Testo45");
		result.put(TOT_ASSOGETTATO, "Testo46");
		result.put(TOTALE, "Testo47");
		result.put(TOTALE_CONTRIBUTO, "Testo6");
		result.put(TOTALE_CONTRIBUTO_LETTERE, "Testo49");
		result.put(CONTRIBUTO, "Testo61");
		result.put(ANNO, "TestoAnno");
		result.put(DATA, "Testo8");
		return result;
	}

	@Override
	public ConaiMateriale getMateriale() {
		return ConaiMateriale.PLASTICA;
	}

}
