/**
 * 
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.util.Date;

import com.jidesoft.filter.LessOrEqualFilter;

/**
 * @author leonardo
 * 
 */
public class JecTableLessOrEqualFilter extends LessOrEqualFilter implements PeriodoBuilder {

    private static final long serialVersionUID = 3261320589456307308L;

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.DATE);
        periodo.setDataFinale((Date) getValue());
        return periodo;
    }
}
