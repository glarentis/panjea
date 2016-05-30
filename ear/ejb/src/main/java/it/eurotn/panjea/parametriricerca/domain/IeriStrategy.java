/**
 * 
 */
package it.eurotn.panjea.parametriricerca.domain;

import java.util.Calendar;

/**
 * @author leonardo
 */
public class IeriStrategy implements PeriodoStrategyDateCalculator {

	private static final long serialVersionUID = 4554815407435919872L;

	@Override
	public Periodo calcola(Periodo periodo) {
		Periodo result = new Periodo();

		Calendar calendarIniziale = Calendar.getInstance();
		calendarIniziale.set(Calendar.HOUR, 0);
		calendarIniziale.set(Calendar.MINUTE, 0);
		calendarIniziale.set(Calendar.MILLISECOND, 0);
		calendarIniziale.set(Calendar.SECOND, 0);
		calendarIniziale.add(Calendar.DATE, -1);

		Calendar calendarFinale = Calendar.getInstance();

		calendarFinale.set(Calendar.HOUR, 23);
		calendarFinale.set(Calendar.MINUTE, 59);
		calendarFinale.set(Calendar.SECOND, 59);
		calendarFinale.add(Calendar.DATE, -1);

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
