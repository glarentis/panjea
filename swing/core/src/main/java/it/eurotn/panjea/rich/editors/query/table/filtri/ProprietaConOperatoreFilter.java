package it.eurotn.panjea.rich.editors.query.table.filtri;

import com.jidesoft.grid.AbstractTableFilter;

import it.eurotn.querybuilder.domain.filter.OperatoreQuery;

public class ProprietaConOperatoreFilter extends AbstractTableFilter<OperatoreQuery> {

    private static final long serialVersionUID = -4420398722102497647L;

    @Override
    public boolean isValueFiltered(OperatoreQuery operatore) {
        return operatore == null;
    }

}
