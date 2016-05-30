package it.eurotn.rich.control.table.options;

import it.eurotn.rich.control.table.JideTableWidget;

import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.TableColumnChooser;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.pivot.AggregateTableUtils;
import com.jidesoft.pivot.PivotField;
import com.jidesoft.swing.CheckBoxList;

public class AggregateTableSelectionColumnsOptionPane extends AbstractTableSelectionColumnsOptionPane {

    private final class ListSelectionListenerImplementation implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {

            for (int i = 0; i < checkBoxList.getModel().getSize(); i++) {

                boolean indiceSelezionato = checkBoxList.getCheckBoxListSelectionModel().isSelectedIndex(i);

                if (AggregateTableUtils.isColumnVisible(table, i) && !indiceSelezionato) {
                    Object object = AggregateTableUtils.getColumnIdentifier(table, i);
                    int modelIdx = MyAggregateTableUtils.getTableModelIndex(table, i);
                    JTable tableTmp = AggregateTableUtils.getColumnTable(table, object);
                    AggregateTableUtils.hideColumn(tableTmp, object, modelIdx);
                } else if (!AggregateTableUtils.isColumnVisible(table, i) && indiceSelezionato) {
                    Object object = AggregateTableUtils.getColumnIdentifier(table, i);
                    int modelIdx = MyAggregateTableUtils.getTableModelIndex(table, i);
                    JTable tableTmp = AggregateTableUtils.getColumnTable(table, object);
                    AggregateTableUtils.showColumn(tableTmp, object, modelIdx);
                }
            }
        }
    }

    /**
     * Costruttore.
     * 
     * @param table
     *            tabella
     */
    public AggregateTableSelectionColumnsOptionPane(final JideTable table) {
        super(table);
        setListSelectionListener(new ListSelectionListenerImplementation());
    }

    @Override
    protected void onColumnsReset() {

        // tolgo il totale generale
        ((AggregateTable) table).getAggregateTableModel().getPivotDataModel().setShowGrandTotalForRow(false);

        // tolgo i subtotal
        PivotField[] fields = ((AggregateTable) table).getAggregateTableModel().getPivotDataModel().getFields();
        for (int i = 0; i < fields.length; i++) {
            PivotField pivotField = fields[i];
            pivotField.setSubtotalTypeForColumn(PivotField.SUBTOTAL_NONE);
            pivotField.setSubtotalTypeForRow(PivotField.SUBTOTAL_NONE);
        }

        int[] cols = ((AggregateTable) table).getAggregateTableModel().getAggregatedColumns();
        for (int i : cols) {
            ((AggregateTable) table).getAggregateTableModel().removeAggregatedColumn(i);
        }
        TableColumnChooser.resetColumnsToDefault(table);
        ((AggregateTable) table).setAggregatedColumns(
                (String[]) table.getClientProperty(JideTableWidget.DEFAULT_AGGREGATE_COLUMNS_PROPERTY));
        ((AggregateTable) table).aggregate();
    }

    @Override
    public void updateColumnsComponent(CheckBoxList checkBoxListComponent) {
        DefaultListModel listModel = (DefaultListModel) checkBoxListComponent.getModel();
        listModel.clear();

        for (int i = 0; i < ((AggregateTable) table).getAggregateTableModel().getColumnCount(); i++) {
            listModel.addElement(AggregateTableUtils.getColumnName(table, i));
        }

        checkBoxListComponent.clearCheckBoxListSelection();
        for (int i = 0; i < ((AggregateTable) table).getAggregateTableModel().getColumnCount(); i++) {
            if (AggregateTableUtils.isColumnVisible(table, i)) {
                checkBoxListComponent.addCheckBoxListSelectedIndex(i);
            }
        }
    }
}
