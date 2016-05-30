package it.eurotn.rich.control.table;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.springframework.rules.constraint.Constraint;

import com.jidesoft.grid.AutoFilterTableHeader;
import com.jidesoft.grid.HierarchicalTable;
import com.jidesoft.grid.ISortableTableModel.SortItem;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableHeaderPopupMenuInstaller;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.swing.PersistenceUtils;

public class JecHierarchicalTable<T> extends HierarchicalTable implements ITable<T> {

    private static final long serialVersionUID = 8156597428546025667L;

    private static Logger logger = Logger.getLogger(JecHierarchicalTable.class);

    private Constraint changeSelectionConstraint = null;

    private boolean editable = true;

    /**
     * Costruttore.
     *
     * @param tableModel
     *            table model
     */
    public JecHierarchicalTable(final TableModel tableModel) {
        super(tableModel);
        setSingleExpansion(true);
    }

    @Override
    public void changeSelection(int arg0, int arg1, boolean arg2, boolean arg3) {
        if (changeSelectionConstraint == null || changeSelectionConstraint.test(getSelectedObject())) {
            super.changeSelection(arg0, arg1, arg2, arg3);
        }
    }

    @Override
    public boolean checkColumn(MouseEvent mouseevent, int actualColumn) {
        throw new UnsupportedOperationException("Da implementare");
    }

    @Override
    public int getActualColumn(int visualColumnIndex) {
        return TableModelWrapperUtils.getActualColumnAt(getModel(), visualColumnIndex);
    }

    @Override
    public int[] getActualRowsAt(int visualRowIndex, int visualColumnIndex) {
        int idx = this.convertRowIndexToModel(this.convertRowIndexToModel(visualRowIndex));
        return new int[] { idx };
    }

    @Override
    public Rectangle getIconCellRect(int visualRowIndex, int visualColumnIndex) {
        Rectangle rec = getEditorCellRect(visualRowIndex, visualColumnIndex);
        return new Rectangle(rec.x, rec.y, 16, 16);
    }

    @Override
    public T getSelectedObject() {
        T selectedObject = null;
        int selectedIndex = getSelectionModel().getLeadSelectionIndex();
        if (getSelectionModel().isSelectedIndex(selectedIndex)) {
            @SuppressWarnings("unchecked")
            DefaultBeanTableModel<T> tableModel = (DefaultBeanTableModel<T>) TableModelWrapperUtils
                    .getActualTableModel(getModel());
            selectedIndex = TableModelWrapperUtils.getActualRowAt(getModel(), selectedIndex);
            if (selectedIndex >= 0) {
                selectedObject = tableModel.getObject(selectedIndex);
            }
        } else {
            List<T> selectedObjects = getSelectedObjects();
            if (selectedObjects.size() > 0) {
                selectedObject = selectedObjects.get(0);
            }
        }
        return selectedObject;
    }

    @Override
    public List<T> getSelectedObjects() {
        logger.debug("--> Enter getSelectedObjects");
        List<T> selectedObject = new ArrayList<T>();

        @SuppressWarnings("unchecked")
        DefaultBeanTableModel<T> tableModel = (DefaultBeanTableModel<T>) TableModelWrapperUtils
                .getActualTableModel(getModel());

        if (tableModel.getRowCount() == 0) {
            return selectedObject;
        }

        int[] rowsIndex = TableModelWrapperUtils.getActualRowsAt(getModel(), getSelectedRows(), false);
        for (Integer riga : rowsIndex) {
            selectedObject.add(tableModel.getObject(riga));
        }

        logger.debug("--> Exit getSelectedObjects");
        return selectedObject;
    }

    @Override
    public JTableHeader getTableHeader(JTable table) {
        AutoFilterTableHeader header = new AutoFilterTableHeader(table);
        header.setAllowMultipleValues(true);
        header.setShowFilterIcon(false);
        header.setAutoFilterEnabled(true);
        header.setUseNativeHeaderRenderer(true);
        header.setAllowMultipleValues(true);
        header.setFont(getFont().deriveFont(Font.BOLD));
        return header;
    }

    @Override
    public void installMenu(TableHeaderPopupMenuInstaller installer) {

    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return super.isCellEditable(row, column) && editable;
    }

    @Override
    public void loadLayout(InputStream stream) {
        byte[] contentData = new byte[1000];
        try {
            stream.read(contentData);

            String preference = new String(contentData);

            String layoutTable = preference.split("@")[0];
            String[] sortColumns = preference.split("@")[1].split(",");

            if (!(sortColumns.length == 1 && sortColumns[0].isEmpty())) {
                SortableTableModel sortModel = (SortableTableModel) TableModelWrapperUtils
                        .getActualTableModel(getModel(), SortableTableModel.class);
                sortModel.reset();
                for (String sortColumn : sortColumns) {
                    String col = sortColumn;
                    if (!col.trim().isEmpty()) {
                        sortModel.sortColumn(Integer.parseInt(col.trim()));
                    }
                }
            }
            TableUtils.setTablePreferenceByName(this, layoutTable);
        } catch (Exception e) {
            logger.error("-->errore nel caricare il layout per la tabella ", e);
        }
    }

    @Override
    public void saveLayout(OutputStream stream) {
        SortableTableModel sortModel = (SortableTableModel) TableModelWrapperUtils.getActualTableModel(getModel(),
                SortableTableModel.class);

        int[] sortColumns = new int[sortModel.getSortingColumns().size()];

        int index = 0;
        for (SortItem sortItem : sortModel.getSortingColumns()) {
            sortColumns[index++] = sortItem.getColumn();
        }

        String preference = TableUtils.getTablePreferenceByName(this);
        preference += "@";
        preference += PersistenceUtils.intArrayToString(sortColumns);
        try {
            stream.write(preference.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectRowObject(int innerIndexModel) {
        innerIndexModel = TableModelWrapperUtils.getRowAt(this.getModel(), innerIndexModel);

        if ((innerIndexModel > -1)) {
            getSelectionModel().setSelectionInterval(innerIndexModel, innerIndexModel);
        } else {
            getSelectionModel().clearSelection();
        }
    }

    @Override
    public void setAggregatedColumns(String[] columns) {
        // non faccio niente sulla hierarchical table
    }

    @Override
    public void setChangeSelectionConstraint(Constraint changeSelectionConstraint) {
        this.changeSelectionConstraint = changeSelectionConstraint;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

}
