package it.eurotn.panjea.parametriricerca.domain;

import java.util.Calendar;

public class MeseCorrenteStrategy implements PeriodoStrategyDateCalculator {
	private static final long serialVersionUID = -5666702972262138837L;

	@Override
	public Periodo calcola(Periodo periodo) {
		Periodo result = new Periodo();

		Calendar calendarIniziale = Calendar.getInstance();
		int giornoIniziale = calendarIniziale.getMinimum(Calendar.DAY_OF_MONTH);
		calendarIniziale.set(Calendar.DAY_OF_MONTH, giornoIniziale);
		calendarIniziale.set(Calendar.HOUR, 0);
		calendarIniziale.set(Calendar.MINUTE, 0);
		calendarIniziale.set(Calendar.SECOND, 0);
		calendarIniziale.set(Calendar.MILLISECOND, 0);

		Calendar calendarFinale = Calendar.getInstance();
		int giornoFinale = calendarFinale.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendarFinale.set(Calendar.DAY_OF_MONTH, giornoFinale);
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
		return true;
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