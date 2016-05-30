package it.eurotn.panjea.rich.filters;

import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import com.jidesoft.filter.LastMonthFilter;

public class JecTableLastMonthFilter extends LastMonthFilter implements PeriodoBuilder {

    private static final long serialVersionUID = -7951787489537647081L;

    @Override
    public Periodo getPeriodo() {
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.MESE_PRECEDENTE);
        return periodo;
    }

}
