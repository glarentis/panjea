package it.eurotn.panjea.rich.editors.query.table;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.lang3.StringUtils;

import com.jidesoft.filter.Filter;
import com.jidesoft.grid.QuickFilterField;

import it.eurotn.panjea.rich.editors.query.table.filtri.ProprietaNomeFilter;

public class QueryTreeTableComponent extends JPanel {

    private static final long serialVersionUID = 6257444011700301396L;

    private QueryTreeTable treeTable;

    private QuickFilterField nomefilterFiled;

    /**
     * Costruttore.
     */
    public QueryTreeTableComponent() {
        super(new BorderLayout());

        initControl();
    }

    /**
     * @return the treeTable
     */
    public QueryTreeTable getTreeTable() {
        return treeTable;
    }

    private void initControl() {

        treeTable = new QueryTreeTable();
        add(new JScrollPane(treeTable), BorderLayout.CENTER);

        nomefilterFiled = new QuickFilterField() {

            private static final long serialVersionUID = 4151845532379624861L;

            @Override
            public void applyFilter(String paramString) {
                if (StringUtils.isBlank(paramString)) {
                    treeTable.getFilterableTreeTableModel().removeAllFilters(0);
                    treeTable.getFilterableTreeTableModel().setFiltersApplied(false);
                } else {
                    treeTable.getFilterableTreeTableModel().setFiltersApplied(false);
                    treeTable.expandAll();
                    if (treeTable.getFilterableTreeTableModel().getFilters(0).length == 0) {
                        treeTable.getFilterableTreeTableModel().addFilter(0, getFilter());
                    }
                    ((ProprietaNomeFilter) getFilter()).setFilterValue(paramString);
                    treeTable.getFilterableTreeTableModel().setFiltersApplied(true);
                }
            }

            @Override
            protected Filter<String> createFilter() {
                return new ProprietaNomeFilter();
            }
        };
        add(nomefilterFiled, BorderLayout.NORTH);
    }
}
