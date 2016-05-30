package it.eurotn.rich.control.table.editor;

import it.eurotn.rich.components.ImportoTextField;

import java.awt.Component;

import javax.swing.CellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TextFieldCellEditor;

public class BigDecimalContextSensitiveEditorFactory extends AbstractCellEditorFactory {

    private class BigDecimalCellEditor extends TextFieldCellEditor {
        private static final long serialVersionUID = -6469527574524217974L;
        private ImportoTextField textField;

        /**
         * Costruttore.
         * 
         * @param type
         *            tipo del dato nell'editor.
         */
        public BigDecimalCellEditor(final Class<?> type) {
            super(type);
            setAutoStopCellEditing(true);
        }

        @Override
        protected JTextField createTextField() {
            textField = new ImportoTextField();
            return textField.getTextField();
        }

        @Override
        public Object getCellEditorValue() {
            Object obj = super.getCellEditorValue();
            if (getTextField().getText() == null
                    || (getTextField().getText() != null && getTextField().getText().isEmpty())) {
                return null;
            }
            return obj;
        }

        @Override
        public Component getTableCellEditorComponent(JTable paramJTable, Object paramObject, boolean paramBoolean,
                int paramInt1, int paramInt2) {
            return super.getTableCellEditorComponent(paramJTable, paramObject, paramBoolean, paramInt1, paramInt2);
        }

        @Override
        public boolean isUseConverterContext() {
            return true;
        }

        @Override
        public void setEditorContext(EditorContext editorcontext) {
            super.setEditorContext(editorcontext);
            int numeroDecimali = 0;
            if (editorcontext != null && editorcontext.getUserObject() != null) {
                numeroDecimali = (Integer) editorcontext.getUserObject();
            }
            textField.setNrOfDecimals(numeroDecimali);
        }
    }

    @Override
    public CellEditor create() {
        return new BigDecimalCellEditor(getType());
    }

}
