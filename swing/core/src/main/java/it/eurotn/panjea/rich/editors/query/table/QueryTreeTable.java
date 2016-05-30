package it.eurotn.panjea.rich.editors.query.table;

import java.awt.Dimension;

import javax.swing.table.TableCellEditor;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.FilterableTreeTableModel;
import com.jidesoft.grid.SortableTreeTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TreeTable;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.TableSearchable;

import it.eurotn.panjea.rich.bd.IQueryBuilderBD;
import it.eurotn.panjea.rich.editors.query.ProprietaEntityRow;
import it.eurotn.querybuilder.domain.EntitaQuerableMetaData;
import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.ProprietaEntityPersister;
import it.eurotn.querybuilder.domain.filter.OperatoreQuery;
import it.eurotn.rich.control.table.editor.EnumContextSensitiveEditorFactory;

public class QueryTreeTable extends TreeTable {

    private static final long serialVersionUID = -2851407099789349009L;

    private static EnumContextSensitiveEditorFactory enumContextSensitiveEditorFactory;

    static {
        enumContextSensitiveEditorFactory = new EnumContextSensitiveEditorFactory();
        enumContextSensitiveEditorFactory.setClass(OperatoreQuery.class);
        enumContextSensitiveEditorFactory.setInsertEmptyValue(true);
    }

    private QueryTreeTableModel tableModel;

    private IQueryBuilderBD queryBuilderBD = RcpSupport.getBean("queryBuilderBD");

    /**
     * Costruttore.
     */
    public QueryTreeTable() {
        super(new QueryFilterableTreeTableModel(new QueryTreeTableModel()));

        init();
    }

    /**
     * @return the filterableTreeTableModel
     */
    public QueryFilterableTreeTableModel getFilterableTreeTableModel() {
        return (QueryFilterableTreeTableModel) TableModelWrapperUtils.getActualTableModel(getModel(),
                FilterableTreeTableModel.class);
    }

    /**
     * @return the tableModel
     */
    public QueryTreeTableModel getQueryTableModel() {
        return tableModel;
    }

    @SuppressWarnings("rawtypes")
    private void init() {
        setExpandAllAllowed(true);
        setRowHeight(22);
        setShowTreeLines(true);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
        getColumnModel().getColumn(2).setCellEditor((TableCellEditor) enumContextSensitiveEditorFactory.create());
        ((SortableTreeTableModel) getModel()).setDefaultSortableOption(SortableTreeTableModel.SORTABLE_NONE);

        TableSearchable tableSearchable = SearchableUtils.installSearchable(this);
        tableSearchable.setRepeats(true);
        // TreeTableSearchable treeTableSearchable = new TreeTableSearchable(this) {
        //
        // @Override
        // protected String convertElementToString(Object paramObject) {
        // return ((ProprietaEntityRow) paramObject).getPropertyLabel();
        // }
        //
        // };
        // treeTableSearchable.setRecursive(true);
        // treeTableSearchable.setRepeats(true);
    }

    /**
     * Applica o rimuove i filtri della tree.
     */
    public void toggleTreePropertyFilter() {

        getFilterableTreeTableModel().setFiltersApplied(!getFilterableTreeTableModel().isFiltersApplied());
    }

    /**
     * Aggiorna il table model con le proprietà contenute nell' EntitaQuerableMetaData.
     *
     * @param clazz
     *            classe di cui caricare le proprietà
     * @param defaultFilterProperties
     *            proprietà di cui settare il filtro di default ( = )
     * @param defaultSelectProperties
     *            proprietà di cui settare il valore di select
     */
    @SuppressWarnings("rawtypes")
    public void updateModel(Class<?> clazz, String[] defaultFilterProperties, String[] defaultSelectProperties) {

        EntitaQuerableMetaData metaData = queryBuilderBD
                .caricaEntitaQuerableMetaData(new ProprietaEntityPersister(clazz));

        tableModel = new QueryTreeTableModel();
        for (ProprietaEntity proprieta : metaData.getProprieta()) {
            tableModel.addRow(new ProprietaEntityRow(proprieta, defaultFilterProperties, defaultSelectProperties));
        }
        setModel(new QueryFilterableTreeTableModel(tableModel));

        ((SortableTreeTableModel) getModel()).setDefaultSortableOption(SortableTreeTableModel.SORTABLE_NONE);
        getColumnModel().getColumn(2).setCellEditor((TableCellEditor) enumContextSensitiveEditorFactory.create());

        if (ArrayUtils.isNotEmpty(defaultFilterProperties) || ArrayUtils.isNotEmpty(defaultSelectProperties)) {
            expandAll();
            getFilterableTreeTableModel().setFiltersApplied(true);
        }
    }
}
