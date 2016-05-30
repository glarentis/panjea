/**
 * 
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import com.jidesoft.filter.LastYearFilter;

/**
 * @author leonardo
 * 
 */
public class JecTableLastYearFilter extends LastYearFilter implements PeriodoBuilder {

    private static final long serialVersionUID = -9147498804153529282L;

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.ANNO_PRECEDENTE);
        return periodo;
    }

}
