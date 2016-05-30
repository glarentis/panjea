/**
 * 
 */
package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import java.util.Date;

import com.jidesoft.filter.EqualFilter;

/**
 * @author leonardo
 */
public class JecTableEqualFilter extends EqualFilter implements PeriodoBuilder {

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.DATE);
        periodo.setDataIniziale((Date) getValue());
        periodo.setDataFinale((Date) getValue());
        return periodo;
    }

}
