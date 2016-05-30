package it.eurotn.rich.control.table.editor;

import it.eurotn.rich.components.ImportoTextField;

import java.math.BigDecimal;

import javax.swing.CellEditor;
import javax.swing.JTextField;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TextFieldCellEditor;

public class DoubleContextSensitiveEditorFactory extends AbstractCellEditorFactory {

    private class DoubleCellEditor extends TextFieldCellEditor {
        private static final long serialVersionUID = -6469527574524217974L;
        private ImportoTextField textField;
        private int numeroDecimali;

        /**
         * Costruttore.
         * 
         * @param type
         *            tipo del dato nell'editor.
         */
        public DoubleCellEditor(final Class<?> type) {
            super(type);
            numeroDecimali = 0;
        }

        @Override
        protected JTextField createTextField() {
            textField = new ImportoTextField();
            return textField.getTextField();
        }

        @Override
        public Object getCellEditorValue() {
            BigDecimal value = textField.getValue();
            if (value != null) {
                return value.doubleValue();
            } else {
                return null;
            }
        }

        @Override
        public boolean isUseConverterContext() {
            return true;
        }

        @Override
        public void setCellEditorValue(Object obj) {
            super.setCellEditorValue(obj);
        }

        @Override
        public void setConverterContext(ConverterContext context) {
            super.setConverterContext(context);
            if (context != null && context.getUserObject() != null) {
                numeroDecimali = (Integer) context.getUserObject();
            }
            textField.setNrOfDecimals(numeroDecimali);
        }

        @Override
        public void setEditorContext(EditorContext editorcontext) {
            super.setEditorContext(editorcontext);
            if (editorcontext != null && editorcontext.getUserObject() != null) {
                numeroDecimali = (Integer) editorcontext.getUserObject();
            }
            textField.setNrOfDecimals(numeroDecimali);
        }
    }

    @Override
    public CellEditor create() {
        return new DoubleCellEditor(getType());
    }
}
