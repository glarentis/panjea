package it.eurotn.panjea.parametriricerca.domain;

import java.util.Calendar;

public class MesePrecedenteStrategy implements PeriodoStrategyDateCalculator {
	private static final long serialVersionUID = -1088069415207995675L;

	@Override
	public Periodo calcola(Periodo periodo) {
		Periodo result = new Periodo();

		Calendar calendarIniziale = Calendar.getInstance();
		calendarIniziale.add(Calendar.MONTH, -1);
		calendarIniziale.set(Calendar.DAY_OF_MONTH, 1);
		calendarIniziale.set(Calendar.HOUR, 0);
		calendarIniziale.set(Calendar.MINUTE, 0);
		calendarIniziale.set(Calendar.SECOND, 0);
		calendarIniziale.set(Calendar.MILLISECOND, 0);

		Calendar calendarFinale = Calendar.getInstance();
		calendarFinale.add(Calendar.MONTH, -1);
		calendarFinale.set(Calendar.HOUR, 0);
		calendarFinale.set(Calendar.MINUTE, 0);
		calendarFinale.set(Calendar.SECOND, 0);
		calendarFinale.set(Calendar.MILLISECOND, 0);
		int giornoFinale = calendarFinale.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendarFinale.set(Calendar.DAY_OF_MONTH, giornoFinale);

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
