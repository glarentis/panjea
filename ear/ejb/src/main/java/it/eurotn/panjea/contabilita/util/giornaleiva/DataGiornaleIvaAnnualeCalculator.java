package it.eurotn.panjea.contabilita.util.giornaleiva;

import java.util.Calendar;

public class DataGiornaleIvaAnnualeCalculator extends DataPeriodicitaGiornaleIvaPrecedenteCalculator {

	@Override
	protected Calendar getDataPrecedente(Calendar calendar, boolean riepilogoAnnuale) {
		calendar.add(Calendar.MONTH, 11);
		calendar.add(Calendar.YEAR, -1);
		return calendar;
	}

}
