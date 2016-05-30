/**
 * 
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import com.jidesoft.filter.ThisYearFilter;

/**
 * @author leonardo
 * 
 */
public class JecTableThisYearFilter extends ThisYearFilter implements PeriodoBuilder {

    private static final long serialVersionUID = -5368453381029836804L;

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.ANNO_CORRENTE);
        return periodo;
    }

}
