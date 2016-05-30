package it.eurotn.rich.control.table;

import javax.swing.table.TableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.pivot.AggregateTableModel;
import com.jidesoft.pivot.PivotDataModel;

public class JecAggregateTableModel extends AggregateTableModel {

    private static final long serialVersionUID = 950419308421730847L;

    /**
     * Costruttore.
     * 
     * @param tablemodel
     *            table model
     */
    public JecAggregateTableModel(final TableModel tablemodel) {
        super(tablemodel);
    }

    @Override
    protected PivotDataModel createPivotDataModel(TableModel paramTableModel) {
        PivotDataModel pivotDataModel = super.createPivotDataModel(paramTableModel);
        return pivotDataModel;
    }

    @Override
    public int getActualRowAt(int paramInt) {
        int result = 0;
        try {
            result = super.getActualRowAt(paramInt);
        } catch (ArrayIndexOutOfBoundsException e) {
            result = -1;
        }
        return result;
    }

    @Override
    public synchronized Class<?> getColumnClass(int paramInt) {
        if (!(getActualModel() instanceof ContextSensitiveTableModel)) {
            return super.getColumnClass(paramInt);
        } else {
            Class<?> result = null;
            try {
                result = super.getColumnClass(getActualModelColumnIndex(paramInt));
            } catch (Exception e) {
                result = super.getColumnClass(paramInt);
            }
            return result;
        }
    }

    @Override
    public ConverterContext getConverterContextAt(int i, int j) {
        if (!(getActualModel() instanceof ContextSensitiveTableModel)) {
            return super.getConverterContextAt(i, j);
        } else {
            ConverterContext result = null;
            try {
                result = ((ContextSensitiveTableModel) getActualModel()).getConverterContextAt(getActualRowAt(i),
                        getActualModelColumnIndex(j));
            } catch (Exception e) {
                result = super.getConverterContextAt(i, j);
            }
            return result;
        }
    }

    @Override
    public EditorContext getEditorContextAt(int i, int j) {
        if (!(getActualModel() instanceof ContextSensitiveTableModel)) {
            return super.getEditorContextAt(i, j);
        } else {
            EditorContext result = null;
            try {
                result = ((ContextSensitiveTableModel) getActualModel()).getEditorContextAt(getActualRowAt(i),
                        getActualModelColumnIndex(j));
            } catch (Exception e) {
                result = super.getEditorContextAt(i, j);
            }
            return result;
        }
    }
}