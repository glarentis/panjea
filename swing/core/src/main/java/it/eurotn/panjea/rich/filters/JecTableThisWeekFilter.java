/**
 *
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.util.Calendar;

import com.jidesoft.filter.ThisWeekFilter;

/**
 * @author leonardo
 *
 */
public class JecTableThisWeekFilter extends ThisWeekFilter implements PeriodoBuilder {

    private static final long serialVersionUID = 799764328668667412L;

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.DATE);

        Calendar calendarIniziale = Calendar.getInstance();
        calendarIniziale.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendarIniziale.set(Calendar.HOUR, 0);
        calendarIniziale.set(Calendar.MINUTE, 0);
        calendarIniziale.set(Calendar.SECOND, 0);
        calendarIniziale.set(Calendar.MILLISECOND, 0);

        Calendar calendarFinale = Calendar.getInstance();
        calendarFinale.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendarFinale.set(Calendar.HOUR, 0);
        calendarFinale.set(Calendar.MINUTE, 0);
        calendarFinale.set(Calendar.SECOND, 0);
        calendarFinale.set(Calendar.MILLISECOND, 0);

        periodo.setDataIniziale(calendarIniziale.getTime());
        periodo.setDataFinale(calendarFinale.getTime());

        return periodo;
    }

}
