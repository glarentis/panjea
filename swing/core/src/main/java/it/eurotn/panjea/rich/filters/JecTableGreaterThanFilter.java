/**
 * 
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.util.Calendar;
import java.util.Date;

import com.jidesoft.filter.GreaterThanFilter;

/**
 * @author leonardo
 * 
 */
public class JecTableGreaterThanFilter extends GreaterThanFilter implements PeriodoBuilder {

    private static final long serialVersionUID = 96115502905280790L;

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) getValue());
        cal.add(Calendar.DATE, 1);
        periodo.setTipoPeriodo(TipoPeriodo.DATE);
        periodo.setDataIniziale(cal.getTime());
        return periodo;
    }

}
