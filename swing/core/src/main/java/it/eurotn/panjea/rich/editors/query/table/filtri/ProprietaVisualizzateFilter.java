package it.eurotn.panjea.rich.editors.query.table.filtri;

import com.jidesoft.grid.AbstractTableFilter;

public class ProprietaVisualizzateFilter extends AbstractTableFilter<Boolean> {

    private static final long serialVersionUID = 1444838255728638711L;

    @Override
    public boolean isValueFiltered(Boolean isVisualizzata) {
        return !isVisualizzata;
    }

}
