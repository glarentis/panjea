package it.eurotn.panjea.rich.editors.query.table.filtri;

import org.apache.commons.lang3.StringUtils;

import com.jidesoft.grid.AbstractTableFilter;

public class ProprietaNomeFilter extends AbstractTableFilter<String> {

    private static final long serialVersionUID = 1444838255728638711L;

    private String filterValue = null;

    @Override
    public boolean isValueFiltered(String value) {
        return !StringUtils.containsIgnoreCase(value, filterValue);
    }

    /**
     * @param filterValue
     *            the filterValue to set
     */
    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

}
