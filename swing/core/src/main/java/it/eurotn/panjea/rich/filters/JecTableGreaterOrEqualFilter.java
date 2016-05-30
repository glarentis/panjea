/**
 * 
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.util.Date;

import com.jidesoft.filter.GreaterOrEqualFilter;

/**
 * @author leonardo
 */
public class JecTableGreaterOrEqualFilter extends GreaterOrEqualFilter implements PeriodoBuilder {

    private static final long serialVersionUID = 3364441342960128096L;

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.DATE);
        periodo.setDataIniziale((Date) getValue());
        return periodo;
    }

}
