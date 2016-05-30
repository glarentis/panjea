/**
 * 
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import com.jidesoft.filter.TodayFilter;

/**
 * @author leonardo
 */
public class JecTableTodayFilter extends TodayFilter implements PeriodoBuilder {

    private static final long serialVersionUID = -1955626357106556890L;

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.OGGI);
        return periodo;
    }

}
