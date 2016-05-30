package it.eurotn.panjea.contabilita.util.giornaleiva;

import java.util.Calendar;

public class DataGiornaleIvaMensileCalculator extends DataPeriodicitaGiornaleIvaPrecedenteCalculator {

	@Override
	protected Calendar getDataPrecedente(Calendar calendar, boolean riepilogoAnnuale) {
		if (riepilogoAnnuale) {
			calendar.set(Calendar.MONTH, 11);
		} else {
			calendar.add(Calendar.MONTH, -1);
		}
		return calendar;
	}

}
