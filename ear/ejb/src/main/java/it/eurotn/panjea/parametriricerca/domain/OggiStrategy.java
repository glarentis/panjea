package it.eurotn.panjea.parametriricerca.domain;

import java.util.Calendar;

public class OggiStrategy implements PeriodoStrategyDateCalculator {

	private static final long serialVersionUID = 6488681773723943902L;

	@Override
	public Periodo calcola(Periodo periodo) {
		Periodo result = new Periodo();

		Calendar calendar = Calendar.getInstance();
		result.setDataIniziale(calendar.getTime());
		result.setDataFinale(calendar.getTime());

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
