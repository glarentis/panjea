/**
 * 
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.util.Calendar;
import java.util.Date;

import com.jidesoft.filter.LessThanFilter;

/**
 * @author leonardo
 */
public class JecTableLessThanFilter extends LessThanFilter implements PeriodoBuilder {

    private static final long serialVersionUID = -1463189000528140046L;

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date) getValue());
        cal.add(Calendar.DATE, -1);
        periodo.setTipoPeriodo(TipoPeriodo.DATE);
        periodo.setDataFinale(cal.getTime());
        return periodo;
    }

}
