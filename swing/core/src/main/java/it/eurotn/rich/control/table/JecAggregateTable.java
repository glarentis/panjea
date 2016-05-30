package it.eurotn.rich.control.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.icon.EmptyIcon;
import org.springframework.rules.constraint.Constraint;

import com.jidesoft.grid.Expandable;
import com.jidesoft.grid.FilterableTableModel;
import com.jidesoft.grid.TableColumnChooser;
import com.jidesoft.grid.TableHeaderPopupMenuInstaller;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.pivot.AggregateTableModel;
import com.jidesoft.pivot.AggregateTablePersistenceUtils;
import com.jidesoft.pivot.CompoundKey;
import com.jidesoft.pivot.GrandTotalValues;
import com.jidesoft.pivot.HeaderTableCellRenderer;
import com.jidesoft.pivot.HeaderTableModel;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotDataModel;
import com.jidesoft.pivot.PivotResources;
import com.jidesoft.pivot.SummaryValue;
import com.jidesoft.pivot.SummaryValues;
import com.jidesoft.pivot.Values;

public class JecAggregateTable<T> extends AggregateTable implements ITable<T> {

    private static final long serialVersionUID = -6099646912757273673L;
    private static final Icon EMPTY_ICON = new EmptyIcon(32, 16);

    private static Logger logger = Logger.getLogger(JecAggregateTable.class);

    private Constraint changeSelectionConstraint = null;
    private boolean editable = true;

    /**
     * Costruttore.
     *
     * @param tableModel
     *            table model
     */
    public JecAggregateTable(final DefaultBeanTableModel<T> tableModel) {
        super(new JecAggregateTableModel(new FilterableTableModel(tableModel)));
        setAutoGrouping(true);
        setAutoAdjustGrouping(true);
        getAggregateTableModel().getPivotDataModel().setDisplayGrandTotalFirstForRow(true);
        getAggregateTableModel().getPivotDataModel().setShowSubtotalAsChild(false);
        ((PivotDataModel) getAggregateTableModel().getPivotDataModel()).setHideExpandIconOnSingleRow(false);
    }

    @Override
    public void changeSelection(int row, int arg1, boolean arg2, boolean arg3) {
        if (row == getSelectedRow()) {// Se cambio solamente cella non verifico nulla
            super.changeSelection(row, arg1, arg2, arg3);
            return;
        }
        if (changeSelectionConstraint == null || changeSelectionConstraint.test(getSelectedObject())) {
            super.changeSelection(row, arg1, arg2, arg3);
        }
    }

    @Override
    public boolean checkColumn(MouseEvent mouseevent, int actualColumn) {

        int visualRowIndex = this.rowAtPoint(mouseevent.getPoint());
        int visualColumnIndex = this.columnAtPoint(mouseevent.getPoint());
        boolean result = true;
        if (this.getAggregateTableModel().getPivotDataModel().getRowHeaderTableModel().isExpandable(visualRowIndex,
                visualColumnIndex)) {
            // se premo su una riga raggruppata devo controllare di non aver premuto sull'icona per
            // espandere/raggruppare.
            // se la x è <16 sto clikkando sul + per decomprimere/comprimere l'albero.
            Rectangle rectSelezione = null;
            Rectangle rec = this.getCellRect(visualRowIndex, visualColumnIndex, false);
            rectSelezione = new Rectangle(rec.x, rec.y, 16, 16);

            if (rectSelezione.contains(mouseevent.getPoint())) {
                // sto cliccando sull'icona
                result = false;
            }
        }
        result = result && actualColumn == getActualColumn(visualColumnIndex);
        return result;
    }

    @Override
    protected TableCellRenderer createCellRenderer() {
        return new HeaderTableCellRenderer() {
            private static final long serialVersionUID = -5798797270460757366L;

            @Override
            protected void customizeCellRenderer(JTable table, Expandable expandable, Component ret, int rowIndex,
                    int columnIndex) {
                super.customizeCellRenderer(table, expandable, ret, rowIndex, columnIndex);
                HeaderTableModel headerTableModel = ((AggregateTable) table).getHeaderTableModel();

                if (ret instanceof JLabel && headerTableModel != null) {
                    JLabel label = ((JLabel) ret);
                    String stringValue = label.getText();
                    int actualRowIndex = table instanceof AggregateTable ? TableModelWrapperUtils
                            .getActualRowAt(table.getModel(), rowIndex, AggregateTableModel.class) : rowIndex;
                    if (expandable instanceof SummaryValue
                            && !headerTableModel.isDataCell(actualRowIndex, columnIndex)) {
                        int summaryType = ((SummaryValue) expandable).getSummaryType();
                        String newStringValue = summaryType == -1
                                ? PivotResources.getResourceBundle(table.getLocale()).getString("Renderer.total")
                                : getAggregateTableModel().getPivotDataModel().getSummaryCalculator()
                                        .getSummaryName(table.getLocale(), summaryType);
                        label.setBackground(Color.red);
                        label.setIcon(EMPTY_ICON);
                        if (newStringValue != null && !newStringValue.equals(stringValue)) {
                            newStringValue = newStringValue.concat(" " + stringValue.replace(newStringValue, ""));
                            ((JLabel) ret).setText(newStringValue);
                        }
                    }
                }
            }
        };
    }

    @Override
    public int getActualColumn(int visualColumnIndex) {
        return TableModelWrapperUtils.getActualColumnAt(getModel(), convertColumnIndexToModel(visualColumnIndex));
    }

    @Override
    public int[] getActualRowsAt(int visualRowIndex, int visualColumnIndex) {
        int[] indiciRighe = new int[] {};
        int columnIndexConverted = TableModelWrapperUtils.getActualColumnAt(this.getModel(),
                this.convertColumnIndexToModel(visualColumnIndex));

        AggregateTableModel atm = (AggregateTableModel) TableModelWrapperUtils.getActualTableModel(this.getModel(),
                AggregateTableModel.class);

        visualRowIndex = TableModelWrapperUtils.getActualRowAt(this.getModel(), visualRowIndex,
                AggregateTableModel.class);
        visualColumnIndex = TableModelWrapperUtils.getActualColumnAt(this.getModel(), visualColumnIndex,
                AggregateTableModel.class);

        // se non ci sono colonne aggregate faccio solo una getActualRowAt dall'outer table model
        if (!atm.hasAggregateColumns()) {
            indiciRighe = new int[] { TableModelWrapperUtils.getActualRowAt(atm, visualRowIndex) };
        } else {
            IPivotDataModel pivotModel = atm.getPivotDataModel();
            if (pivotModel.getRowFields().length != 0) {
                // controllo di non essere su righe di totali
                if (pivotModel.getRowHeaderTableModel().getValuesAt(visualRowIndex) instanceof SummaryValues
                        || pivotModel.getRowHeaderTableModel()
                                .getValuesAt(visualRowIndex) instanceof GrandTotalValues) {
                    return indiciRighe;
                }

                // se la colonna scelta è aggregata devo comporre la compound key fino ad essa per avere tutte le righe
                // del gruppo
                List<Integer> righe = null;
                Values values = pivotModel.getRowHeaderTableModel().getValuesAt(visualRowIndex);
                if (values != null) {
                    List<Object> keys = new ArrayList<Object>();
                    if (atm.isColumnAggregated(columnIndexConverted)) {
                        for (int i = 0; i < atm.getAggregatedColumns().length; i++) {
                            if (i <= visualColumnIndex && values.getValueAt(i) != null) {
                                keys.add(values.getValueAt(i).getValue());
                            }
                        }
                    } else {
                        keys.add(values.getValueAt(values.getCount() - 1).getValue());
                    }
                    CompoundKey rowKey = CompoundKey.newInstance(keys.toArray());
                    righe = pivotModel.getDataAt(rowKey, CompoundKey.newInstance(new Object[0]));
                    // converto le righe del pivot model perchè potrebbe essere stato applicato un filtro.
                }
                if (righe == null) {
                    righe = new ArrayList<Integer>();
                }
                int[] rows = new int[righe.size()];
                int i = 0;
                for (Integer riga : righe) {
                    rows[i] = TableModelWrapperUtils.getActualRowAt(((PivotDataModel) pivotModel).getTableModel(),
                            riga);
                    i++;
                }
                indiciRighe = rows;
            } else {
                indiciRighe = new int[] { visualRowIndex };
            }
        }
        return indiciRighe;
    }

    /**
     *
     * @return colonne aggregate.
     */
    public int[] getAggregatedColumns() {
        return getAggregateTableModel().getAggregatedColumns();
    }

    @Override
    public Rectangle getIconCellRect(int visualRowIndex, int visualColumnIndex) {
        Rectangle rectIcon = null;

        Rectangle rect = getCellRect(visualRowIndex, visualColumnIndex, false);
        if (getAggregateTableModel().getPivotDataModel().getRowHeaderTableModel().isExpandable(visualRowIndex,
                visualColumnIndex)) {
            // se la colonna è raggruppata il rettangolo della cella viene calcolato tenendo conto dell'iconda di
            // espansione ( viene quindi shiftato di 16 )
            rectIcon = new Rectangle(rect.x + 16, rect.y, 16, 16);
        } else {
            rectIcon = new Rectangle(rect.x, rect.y, 16, 16);
        }

        return rectIcon;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getSelectedObject() {
        T selectedObject = null;
        // Se l'ultima selezionata non è nei selection index ho deselezionato la riga, quindi prendo il primo
        // selezionato
        int selectedIndex = getSelectionModel().getLeadSelectionIndex();
        if (getSelectionModel().isSelectedIndex(selectedIndex)) {
            int rowIndex = TableModelWrapperUtils.getActualRowAt(getModel(), selectedIndex);
            DefaultBeanTableModel<T> tableModel = (DefaultBeanTableModel<T>) TableModelWrapperUtils
                    .getActualTableModel(getAggregateTableModel());
            if (rowIndex >= 0) {
                selectedObject = tableModel.getObject(rowIndex);
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
                .getActualTableModel(getAggregateTableModel());
        if (tableModel.getRowCount() == 0) {
            return selectedObject;
        }

        int[] rowsIndex = TableModelWrapperUtils.getActualRowsAt(getModel(), getSelectedRows(), false);

        if (rowsIndex.length == 0 && getSelectedRows().length > 0) {
            Values values = getAggregateTableModel().getPivotDataModel().getRowHeaderTableModel()
                    .getValuesAt(getSelectedRow());
            List<Object> keys = new ArrayList<Object>();
            for (int i = 0; i < getAggregatedColumns().length; i++) {
                if (values.getValueAt(i) != null) {
                    keys.add(values.getValueAt(i).getValue());
                }
            }
            CompoundKey rowKey = CompoundKey.newInstance(keys.toArray());
            final List<Integer> righe = getAggregateTableModel().getPivotDataModel().getDataAt(rowKey,
                    CompoundKey.newInstance(new Object[0]));

            if (righe != null) {
                rowsIndex = new int[righe.size()];
                int i = 0;
                for (Integer riga : righe) {
                    rowsIndex[i] = getAggregateTableModel().getPivotDataModel().getDataSource().getActualRowIndex(riga);
                    i++;
                }
            }
        }

        for (Integer riga : rowsIndex) {
            selectedObject.add(tableModel.getObject(riga));
        }

        logger.debug("--> Exit getSelectedObjects");
        return selectedObject;
    }

    @Override
    public JTableHeader getTableHeader(JTable table) {
        return new JideAutoFilterTableHeader(table);
    }

    @Override
    public void installMenu(TableHeaderPopupMenuInstaller installer) {
        installer.addTableHeaderPopupMenuCustomizer(new JideAggregateTablePopupMenuCustomizer());
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return super.isCellEditable(row, column) && editable;
    }

    @Override
    public void loadLayout(InputStream stream) {
        try {
            AggregateTablePersistenceUtils.load(this, stream);
        } catch (Exception e) {
            // int[] aggregateColumns = getAggregatedColumns();
            // for (int i : aggregateColumns) {
            // getAggregateTableModel().removeAggregatedColumn(i);
            // }
            // aggregate();
            TableColumnChooser.resetColumnsToDefault(this);
            logger.error("-->errore nel ripristinare il layout della tabella.", e);
        }
    }

    @Override
    public Component prepareEditor(TableCellEditor editor, int row, int column) {
        Component result = super.prepareEditor(editor, row, column);
        if (result instanceof JTextComponent) {
            JTextComponent field = (JTextComponent) result;
            field.selectAll();
        }
        return result;
    }

    @Override
    public boolean processKeyBinding(KeyStroke keystroke, KeyEvent keyevent, int i, boolean flag) {
        // sovrascrivo il metodo per fa si che nelle tabelle editabili se il tasto CTRL è premuto non processo l'evento.
        // In questo modo ad esempio se la tabella è in una FormBackedDialogPageEditor un CTRL+S esegue il salvataggio
        // della pagina e non manda in edit la tabella.
        if (keyevent.isControlDown() && keyevent.getKeyCode() == KeyEvent.VK_S) {
            return false;
        }
        return super.processKeyBinding(keystroke, keyevent, i, flag);
    }

    @Override
    public void saveLayout(java.io.OutputStream stream) {
        try {
            AggregateTablePersistenceUtils.save(this, stream);
        } catch (Exception e) {
            logger.warn("-->errore nel salvare il layout", e);
        }
    }

    @Override
    public void selectRowObject(int innerIndexModel) {

        if (this.getAggregatedColumns().length != 0) {
            innerIndexModel = this.getAggregateTableModel().getRowAt(innerIndexModel);
        }

        innerIndexModel = TableModelWrapperUtils.getRowAt(this.getModel(), getAggregateTableModel(), innerIndexModel);

        if ((innerIndexModel > -1)) {
            getSelectionModel().setSelectionInterval(innerIndexModel, innerIndexModel);
        } else {
            getSelectionModel().clearSelection();
        }
    }

    @Override
    public void setAggregatedColumns(String[] columns) {
        super.setAggregatedColumns(columns);
        aggregate();
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

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        super.setValueAt(aValue, row, column);
        // setValue viene chiamato anche dopo l'editazione della cella.
        // In questo caso la table perde la selezione. riseleziono la riga che sto
        // modificando.
        changeSelection(row, column, false, false);
    }
};