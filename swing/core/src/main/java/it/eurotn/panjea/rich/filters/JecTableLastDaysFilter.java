/**
 * 
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.util.Calendar;
import java.util.Date;

import com.jidesoft.filter.AbstractFilter;

/**
 * @author leonardo
 */
public class JecTableLastDaysFilter extends AbstractFilter implements PeriodoBuilder {

    private Object value;

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.DATE);
        Calendar calToday = Calendar.getInstance();
        calToday.set(Calendar.HOUR, 0);
        calToday.set(Calendar.MINUTE, 0);
        calToday.set(Calendar.SECOND, 0);
        calToday.set(Calendar.MILLISECOND, 0);

        Calendar calDaysBefore = Calendar.getInstance();
        calDaysBefore.setTimeInMillis(calToday.getTimeInMillis());
        calDaysBefore.add(Calendar.DATE, -(Integer) getValue());
        periodo.setDataIniziale(calDaysBefore.getTime());
        periodo.setDataFinale(calToday.getTime());
        return periodo;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    @Override
    public boolean isValueFiltered(Object arg0) {
        Calendar calToday = Calendar.getInstance();
        calToday.set(Calendar.HOUR, 0);
        calToday.set(Calendar.MINUTE, 0);
        calToday.set(Calendar.SECOND, 0);
        calToday.set(Calendar.MILLISECOND, 0);

        Calendar calDaysBefore = Calendar.getInstance();
        calDaysBefore.setTimeInMillis(calToday.getTimeInMillis());
        calDaysBefore.add(Calendar.DATE, -(Integer) getValue());

        Calendar calValue = Calendar.getInstance();
        calValue.setTime((Date) arg0);

        if (calValue.before(calToday) && calValue.after(calDaysBefore)) {
            return true;
        }
        return false;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

}
