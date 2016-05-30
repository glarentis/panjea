package it.eurotn.panjea.parametriricerca.domain;

import java.util.Calendar;

public class UltimiGiorniStrategy implements PeriodoStrategyDateCalculator {

	private static final long serialVersionUID = 8424444396016875087L;

	@Override
	public Periodo calcola(Periodo periodo) {
		Periodo result = new Periodo();
		int ngiorni = periodo.getNumeroGiorni();
		ngiorni = ngiorni * -1;

		Calendar calendarIniziale = Calendar.getInstance();
		calendarIniziale.add(Calendar.DAY_OF_MONTH, ngiorni);
		calendarIniziale.set(Calendar.HOUR, 0);
		calendarIniziale.set(Calendar.MINUTE, 0);
		calendarIniziale.set(Calendar.SECOND, 0);
		calendarIniziale.set(Calendar.MILLISECOND, 0);

		Calendar calendarFinale = Calendar.getInstance();
		calendarFinale.set(Calendar.HOUR, 23);
		calendarFinale.set(Calendar.MINUTE, 59);
		calendarFinale.set(Calendar.SECOND, 59);
		calendarFinale.set(Calendar.MILLISECOND, 59);

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
		return true;
	}

	@Override
	public boolean isAllowedSelectDate() {
		return false;
	}

}
