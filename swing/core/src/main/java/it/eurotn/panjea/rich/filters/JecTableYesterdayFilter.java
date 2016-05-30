/**
 * 
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import com.jidesoft.filter.YesterdayFilter;

/**
 * @author leonardo
 */
public class JecTableYesterdayFilter extends YesterdayFilter implements PeriodoBuilder {

    private static final long serialVersionUID = 2069978148051688382L;

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.IERI);
        return periodo;
    }

}
