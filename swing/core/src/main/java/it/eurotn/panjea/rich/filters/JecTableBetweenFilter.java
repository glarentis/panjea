/**
 * 
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.util.Date;

import com.jidesoft.filter.BetweenFilter;

/**
 * @author leonardo
 */
public class JecTableBetweenFilter extends BetweenFilter implements PeriodoBuilder {

    private static final long serialVersionUID = 7684110318005066083L;

    public JecTableBetweenFilter(Object arg0, Object arg1) {
        super(arg0, arg1);
    }

    public JecTableBetweenFilter(String arg0, Object arg1, Object arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.DATE);
        periodo.setDataIniziale((Date) getValue1());
        periodo.setDataFinale((Date) getValue2());
        return periodo;
    }

}
