package it.eurotn.rich.control.table.options;

import javax.swing.DefaultListModel;

import com.jidesoft.grid.DefaultGroupTableModel;
import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.TableColumnChooser;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.swing.CheckBoxList;

public class TableSelectionColumnsOptionPane extends AbstractTableSelectionColumnsOptionPane {

    /**
     * Costruttore.
     * 
     * @param table
     *            tabella
     */
    public TableSelectionColumnsOptionPane(final JideTable table) {
        super(table);
    }

    @Override
    protected void onColumnsReset() {
        DefaultGroupTableModel groupableTableModel = (DefaultGroupTableModel) TableModelWrapperUtils
                .getActualTableModel(table.getModel(), DefaultGroupTableModel.class);
        if (groupableTableModel != null) {
            groupableTableModel.clearGroupColumns();
            groupableTableModel.groupAndRefresh();
        }
        TableColumnChooser.resetColumnsToDefault(table);
    }

    /**
     * Aggiorna il componente di selezione delle colonne in base alla tabella di riferimento.
     * 
     * @param checkBoxListComponent
     *            componente di selezione
     */
    @Override
    public void updateColumnsComponent(CheckBoxList checkBoxListComponent) {

        DefaultListModel listModel = (DefaultListModel) checkBoxListComponent.getModel();
        listModel.clear();

        for (int i = 0; i < table.getModel().getColumnCount(); i++) {
            listModel.addElement(table.getModel().getColumnName(i));
        }

        checkBoxListComponent.clearCheckBoxListSelection();
        for (int i = 0; i < table.getModel().getColumnCount(); i++) {
            if (TableColumnChooser.isVisibleColumn(table, i)) {
                checkBoxListComponent.addCheckBoxListSelectedIndex(i);
            }
        }

    }
}
