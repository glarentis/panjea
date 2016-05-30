package it.eurotn.rich.control.table.options;

import javax.swing.JTable;

import com.jidesoft.pivot.AggregateTableUtils;

public class MyAggregateTableUtils extends AggregateTableUtils {

    /**
     * 
     * @param table
     *            table
     * @param i
     *            index
     * @return index
     */
    public static int getTableModelIndex(JTable table, int i) {
        return AggregateTableUtils.getOuterModelIndex(table, i);
    }
}
