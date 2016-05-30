package it.eurotn.rich.control.table;

import javax.swing.table.TableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.DefaultGroupTableModel;
import com.jidesoft.grid.EditorContext;

public class JecGroupableTableModel extends DefaultGroupTableModel {

    private static final long serialVersionUID = -2276021141418770295L;

    /**
     * Costruttore.
     * 
     * @param tablemodel
     *            table model di base
     */
    public JecGroupableTableModel(final TableModel tablemodel) {
        super(tablemodel);
    }

    @Override
    public Class<?> getColumnClass(int paramInt) {
        if (!(getActualModel() instanceof ContextSensitiveTableModel)) {
            return super.getColumnClass(paramInt);
        } else {
            Class<?> result = null;
            try {
                result = super.getColumnClass(getActualColumnAt(paramInt));
            } catch (Exception e) {
                result = super.getColumnClass(paramInt);
            }
            return result;
        }
    }

    @Override
    public ConverterContext getConverterContextAt(int i, int j) {
        if ((getActualModel() instanceof ContextSensitiveTableModel)) {
            return ((ContextSensitiveTableModel) getActualModel()).getConverterContextAt(getActualRowAt(i),
                    getActualColumnAt(j));
        }
        return super.getConverterContextAt(i, j);
    }

    @Override
    public EditorContext getEditorContextAt(int i, int j) {
        if ((getActualModel() instanceof ContextSensitiveTableModel)) {
            return ((ContextSensitiveTableModel) getActualModel()).getEditorContextAt(getActualRowAt(i),
                    getActualColumnAt(j));
        }
        return super.getEditorContextAt(i, j);
    }
}
