package it.eurotn.rich.control.table;

import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.rules.constraint.Constraint;

import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.DefaultGroupTableModel;
import com.jidesoft.grid.GroupTable;
import com.jidesoft.grid.GroupTableHeader;
import com.jidesoft.grid.GroupTablePopupMenuCustomizer;
import com.jidesoft.grid.ISortableTableModel.SortItem;
import com.jidesoft.grid.IndexReferenceRow;
import com.jidesoft.grid.Row;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableHeaderPopupMenuInstaller;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.grid.TreeTableCellRenderer;
import com.jidesoft.pivot.FilterableAggregateTableModel;
import com.jidesoft.swing.PersistenceUtils;

import it.eurotn.rich.control.table.navigationloader.NavigatioLoaderContextSensitiveCellRenderer;

public class JecGroupTable<T> extends GroupTable implements ITable<T> {

    private class JecGroupTableHeader extends GroupTableHeader {
        private static final long serialVersionUID = -8798136143346972420L;

        /**
         * Costruttore.
         *
         * @param grouptable
         *            tabella alla quale verrà applicato l'header
         */
        public JecGroupTableHeader(final GroupTable grouptable) {
            super(grouptable);
            setGroupHeaderEnabled(true);
            setVerticalIndention(0);
            setShowFilterIcon(false);
            setAutoFilterEnabled(true);
            setLabelFont(new JLabel().getFont().deriveFont(Font.BOLD));
            setUseNativeHeaderRenderer(true);
            setAllowMultipleValues(true);
            setFont(getFont().deriveFont(Font.BOLD));
        }
    }

    private static final long serialVersionUID = -6099646912757273673L;

    private static Logger logger = Logger.getLogger(JecAggregateTable.class);

    private Constraint changeSelectionConstraint = null;

    private boolean editable = true;

    /**
     * Costruttore.
     *
     * @param tableModel
     *            table model
     */
    public JecGroupTable(final DefaultBeanTableModel<T> tableModel) {
        super(new JecGroupableTableModel(new FilterableAggregateTableModel(tableModel)));
        setOptimized(true);
        setShowTreeLines(false);
        // Giro gli shortcut originali / e * e li associo ai pulsanti - e +
        Object expandAll = getInputMap().get(KeyStroke.getKeyStroke(106, 0));
        getInputMap().put(KeyStroke.getKeyStroke(107, 0), expandAll);
        Object collapseAll = getInputMap().get(KeyStroke.getKeyStroke(111, 0));
        getInputMap().put(KeyStroke.getKeyStroke(109, 0), collapseAll);
        getInputMap().remove(KeyStroke.getKeyStroke(106, 0));
        getInputMap().remove(KeyStroke.getKeyStroke(111, 0));
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
    protected Action createDelegateAction(Action action, KeyStroke keystroke) {
        return super.createDelegateAction(action, keystroke);
    }

    @Override
    public int getActualColumn(int visualColumnIndex) {
        return TableModelWrapperUtils.getActualColumnAt(getModel(), convertColumnIndexToModel(visualColumnIndex));
    }

    @Override
    public int[] getActualRowsAt(int visualRowIndex, int visualColumnIndex) {

        DefaultGroupTableModel gtm = (DefaultGroupTableModel) TableModelWrapperUtils
                .getActualTableModel(this.getModel(), DefaultGroupTableModel.class);
        Row row = gtm.getRowAt(visualRowIndex);

        List<Integer> indexes = getActualRowsAt(row);

        int[] results = null;
        if (!indexes.isEmpty()) {
            results = new int[indexes.size()];
            for (int i = 0; i < indexes.size(); i++) {
                results[i] = indexes.get(i);
            }
        }
        return results;
    }

    /**
     * Restituisce tutti gli indici partendo dalla riga richiesta.
     *
     * @param row
     *            riga
     * @return indici
     */
    private List<Integer> getActualRowsAt(Row row) {
        List<Integer> indexes = new ArrayList<Integer>();

        if (row == null) {
            return indexes;
        }
        if (row instanceof DefaultGroupRow) {
            for (Object indexRow : ((DefaultGroupRow) row).getChildren()) {
                if (indexRow instanceof DefaultGroupRow) {
                    indexes.addAll(getActualRowsAt((Row) indexRow));
                } else {
                    indexes.add(((IndexReferenceRow) indexRow).getRowIndex());
                }
            }
        } else {
            indexes.add(((IndexReferenceRow) row).getRowIndex());
        }

        return indexes;
    }

    @Override
    public Rectangle getIconCellRect(int visualRowIndex, int visualColumnIndex) {
        Rectangle rectIcon = null;

        int columnIndexConvert = convertRowIndexToModel(convertColumnIndexToModel(visualColumnIndex));
        int rowIndexConvert = convertRowIndexToModel(convertRowIndexToModel(visualRowIndex));
        Object value = getModel().getValueAt(rowIndexConvert, columnIndexConvert);

        Rectangle rec = getEditorCellRect(visualRowIndex, visualColumnIndex);
        rectIcon = new Rectangle(rec.x, rec.y, 16, 16);

        if (value instanceof DefaultGroupRow) {
            // in una group table l'icona si trova dopo tutti i livelli e dopo l'icona di
            // espansione. Se inoltre sulla
            // riga è stato installato un NavigatioLoaderContextSensitiveCellRenderer considero
            // anche la larghezza della
            // sua icona.
            int iconGroupWidth = 0;
            TableCellRenderer cellRenderer = getCellRenderer(visualRowIndex, visualColumnIndex);
            if (cellRenderer != null && ((TreeTableCellRenderer) cellRenderer)
                    .getActualCellRenderer() instanceof NavigatioLoaderContextSensitiveCellRenderer) {
                int numberOfExtraIcons = ((NavigatioLoaderContextSensitiveCellRenderer) ((TreeTableCellRenderer) cellRenderer)
                        .getActualCellRenderer()).getNumberOfExtraIcons();
                iconGroupWidth = 16 * numberOfExtraIcons;
            }

            rectIcon = new Rectangle(rec.x + iconGroupWidth, rec.y, 16, 16);
        }

        return rectIcon;
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
            if (selectedIndex >= 0 && !tableModel.getObjects().isEmpty()) {
                if (selectedIndex >= tableModel.getObjects().size()) {
                    selectedObject = tableModel.getObject(0);
                } else {
                    selectedObject = tableModel.getObject(selectedIndex);
                }
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
        return new JecGroupTableHeader((GroupTable) table);
    }

    @Override
    public void installMenu(TableHeaderPopupMenuInstaller installer) {
        installer.addTableHeaderPopupMenuCustomizer(new GroupTablePopupMenuCustomizer());
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return super.isCellEditable(row, column) && editable;
    }

    @Override
    public void loadLayout(InputStream stream) {
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(stream, writer);

            String preference = writer.toString();

            String[] preferenceSplit = preference.split("@");
            String layoutTable = preferenceSplit[0];
            String[] groupColumns = preferenceSplit.length > 1 ? preferenceSplit[1].split(",") : new String[] {};
            String[] sortColumns = preferenceSplit.length > 2 ? preferenceSplit[2].split(",") : new String[] {};

            JecGroupableTableModel model = (JecGroupableTableModel) TableModelWrapperUtils
                    .getActualTableModel(getModel(), JecGroupableTableModel.class);
            model.clearGroupColumns();
            if (!(groupColumns.length == 1 && groupColumns[0].isEmpty())) {
                int[] groupColumn = new int[groupColumns.length];
                for (int i = 0; i < groupColumns.length; i++) {
                    String col = groupColumns[i];
                    if (!col.trim().isEmpty()) {
                        groupColumn[i] = Integer.parseInt(col.trim());
                    }
                }
                model.setGroupColumns(groupColumn);
                model.groupAndRefresh();
                model.collapseAll();
            }

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
        } catch (IOException e) {
            logger.error("-->errore nel caricare il layout per la tabella ", e);
        }
    }

    @Override
    public Component prepareEditor(TableCellEditor tablecelleditor, int i, int j) {
        return super.prepareEditor(tablecelleditor, i, j);
    }

    @Override
    public Component prepareRenderer(TableCellRenderer arg0, int arg1, int arg2) {
        return super.prepareRenderer(arg0, arg1, arg2);
    }

    @Override
    public void saveLayout(OutputStream stream) {
        JecGroupableTableModel model = (JecGroupableTableModel) TableModelWrapperUtils.getActualTableModel(getModel(),
                JecGroupableTableModel.class);

        SortableTableModel sortModel = (SortableTableModel) TableModelWrapperUtils.getActualTableModel(getModel(),
                SortableTableModel.class);

        int[] groupColumns = new int[model.getGroupColumnCount()];

        int[] sortColumns = new int[sortModel.getSortingColumns().size()];

        int index = 0;
        for (SortItem sortItem : sortModel.getSortingColumns()) {
            sortColumns[index++] = sortItem.getColumn();
        }

        for (int i = 0; i < model.getGroupColumnCount(); i++) {
            groupColumns[i] = model.getGroupColumnAt(i);
        }

        String preference = TableUtils.getTablePreferenceByName(this);
        preference += "@";
        preference += PersistenceUtils.intArrayToString(groupColumns);
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
        for (String columnNameI18 : columns) {

            Enumeration<TableColumn> cols = getColumnModel().getColumns();
            while (cols.hasMoreElements()) {
                TableColumn column = cols.nextElement();

                if (((String) column.getIdentifier()).contains(columnNameI18)) {
                    TableModel model = TableModelWrapperUtils.getActualTableModel(getModel(),
                            JecGroupableTableModel.class);
                    ((DefaultGroupTableModel) model)
                            .addGroupColumn(getColumnModel().getColumnIndex(column.getIdentifier()));
                    ((DefaultGroupTableModel) model).groupAndRefresh();
                    break;
                }
            }
        }

        if (getModel().getRowCount() < 2000) {
            TableUtils.autoResizeAllColumns(this);
        }
    }

    @Override
    public void setChangeSelectionConstraint(Constraint changeSelectionConstraint) {
        this.changeSelectionConstraint = changeSelectionConstraint;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
};
