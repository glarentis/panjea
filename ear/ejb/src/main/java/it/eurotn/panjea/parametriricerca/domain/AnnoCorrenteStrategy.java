package it.eurotn.panjea.parametriricerca.domain;

import java.util.Calendar;

public class AnnoCorrenteStrategy implements PeriodoStrategyDateCalculator {

	private static final long serialVersionUID = -8337912428947444889L;

	@Override
	public Periodo calcola(Periodo periodo) {
		Periodo result = new Periodo();

		Calendar calendarIniziale = Calendar.getInstance();
		calendarIniziale.set(Calendar.DAY_OF_MONTH, 1);
		calendarIniziale.set(Calendar.MONTH, 0);
		calendarIniziale.set(Calendar.HOUR, 0);
		calendarIniziale.set(Calendar.MINUTE, 0);
		calendarIniziale.set(Calendar.SECOND, 0);
		calendarIniziale.set(Calendar.MILLISECOND, 0);

		Calendar calendarFinale = Calendar.getInstance();
		calendarFinale.set(Calendar.DAY_OF_MONTH, 31);
		calendarFinale.set(Calendar.MONTH, 11);
		calendarFinale.set(Calendar.HOUR, 0);
		calendarFinale.set(Calendar.MINUTE, 0);
		calendarFinale.set(Calendar.SECOND, 0);
		calendarFinale.set(Calendar.MILLISECOND, 0);

		result.setDataIniziale(calendarIniziale.getTime());
		result.setDataFinale(calendarFinale.getTime());

		return result;
	}

	@Override
	public boolean isAllowedDataInizialeNull() {
		return false;
	}

	@Override
	public boolean isAllowedNumGiorni() {
		return false;
	}

	@Override
	public boolean isAllowedSelectDate() {
		return false;
	}

}
