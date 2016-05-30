/**
 * 
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import com.jidesoft.filter.ThisMonthFilter;

/**
 * @author leonardo
 * 
 */
public class JecTableThisMonthFilter extends ThisMonthFilter implements PeriodoBuilder {

    private static final long serialVersionUID = 799764328668667412L;

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.MESE_CORRENTE);
        return periodo;
    }

}
