package it.eurotn.panjea.contabilita.util.giornaleiva;

import java.util.Calendar;

public class DataGiornaleIvaTrimestraleCalculator extends DataPeriodicitaGiornaleIvaPrecedenteCalculator {

	@Override
	protected Calendar getDataPrecedente(Calendar calendar, boolean riepilogoAnnuale) {
		if (riepilogoAnnuale) {
			calendar.set(Calendar.MONTH, 9);
		} else {
			calendar.add(Calendar.MONTH, -3);
		}
		return calendar;
	}

}
