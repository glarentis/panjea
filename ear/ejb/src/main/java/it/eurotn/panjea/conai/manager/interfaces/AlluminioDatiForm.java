package it.eurotn.panjea.conai.manager.interfaces;

import it.eurotn.panjea.conai.domain.ConaiArticolo.ConaiMateriale;

import java.util.HashMap;
import java.util.Map;

public class AlluminioDatiForm extends DatiForm {

	@Override
	protected Map<String, String> getControlByField() {
		HashMap<String, String> result = new HashMap<String, String>();
		result.put(DENOMINAZIONE, "Testo70");
		result.put(CODICE_SOCIO, "Testo71");
		result.put(ISCRITTO_PRODUTTORE, "Casella di controllo51");
		result.put(ISCRITTO_UTILIZZATORE, "Casella di controllo52");
		result.put(INDIRIZZO_FATTURAZIONE, "Testo72");
		result.put(PEC, "Testo73");
		result.put(CAP, "Testo74");
		result.put(CITTA, "Testo84");
		result.put(PROVINCIA, "Testo77");
		result.put(CF, "Testo78");
		result.put(PIVA, "Testo79");
		result.put(REFERENTE, "Testo80");
		result.put(TEL, "Testo81");
		result.put(FAX, "Testo82");
		result.put(EMAIL, "Testo83");
		result.put(PERIODO_ANNUALE, "Casella di controllo53");
		result.put(PERIODO_TRIMESTRE_1, "Casella di controllo54");
		result.put(PERIODO_TRIMESTRE_2, "Casella di controllo55");
		result.put(PERIODO_TRIMESTRE_3, "Casella di controllo56");
		result.put(PERIODO_TRIMESTRE_4, "Casella di controllo57");
		result.put(PERIODO_MESE_1, "Casella di controllo58");
		result.put(PERIODO_MESE_2, "Casella di controllo59");
		result.put(PERIODO_MESE_3, "Casella di controllo60");
		result.put(PERIODO_MESE_4, "Casella di controllo61");
		result.put(PERIODO_MESE_5, "Casella di controllo62");
		result.put(PERIODO_MESE_6, "Casella di controllo63");
		result.put(PERIODO_MESE_7, "Casella di controllo64");
		result.put(PERIODO_MESE_8, "Casella di controllo65");
		result.put(PERIODO_MESE_9, "Casella di controllo66");
		result.put(PERIODO_MESE_10, "Casella di controllo67");
		result.put(PERIODO_MESE_11, "Casella di controllo68");
		result.put(PERIODO_MESE_12, "Casella di controllo69");
		result.put(TOT_ESENTE, "Testo45");
		result.put(TOT_ASSOGETTATO, "Testo46");
		result.put(TOTALE, "Testo47");
		result.put(TOTALE_CONTRIBUTO, "Testo7");
		result.put(TOTALE_CONTRIBUTO_LETTERE, "Testo49");
		result.put(CONTRIBUTO, "Testo6");
		result.put(ANNO, "TestoAnno");
		result.put(DATA, "Testo8");
		return result;
	}

	@Override
	public ConaiMateriale getMateriale() {
		return ConaiMateriale.ALLUMINIO;
	}

}
