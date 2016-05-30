package it.eurotn.panjea.parametriricerca.domain;

import java.io.Serializable;
import java.util.Calendar;

public class DateStrategy implements PeriodoStrategyDateCalculator, Serializable {

	private static final long serialVersionUID = -1806961570178558493L;

	@Override
	public Periodo calcola(Periodo periodo) {
		Periodo result = new Periodo();
		if (periodo.getDataIniziale() == null && periodo.getDataFinale() == null) {
			result.setDataFinale(null);
			result.setDataIniziale(null);
			return result;
		}

		result.setDataIniziale(periodo.getDataIniziale());
		result.setDataFinale(periodo.getDataFinale());

		if (periodo.getDataIniziale() == null) {
			Calendar cal = Calendar.getInstance();
			cal.set(1950, 01, 01, 00, 00);
			result.setDataIniziale(cal.getTime());
		}

		if (periodo.getDataFinale() == null) {
			Calendar cal = Calendar.getInstance();
			cal.set(2070, 11, 31, 23, 59);
			result.setDataFinale(cal.getTime());
		}
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
		return true;
	}

}
