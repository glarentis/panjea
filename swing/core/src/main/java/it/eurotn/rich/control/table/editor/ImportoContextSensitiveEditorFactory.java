package it.eurotn.rich.control.table.editor;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.rich.components.ImportoTextField;

import java.math.BigDecimal;

import javax.swing.CellEditor;
import javax.swing.JTextField;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TextFieldCellEditor;

public class ImportoContextSensitiveEditorFactory extends AbstractCellEditorFactory {

    private class ImportolCellEditor extends TextFieldCellEditor {
        private static final long serialVersionUID = -6469527574524217974L;
        private ImportoTextField textField;
        private Importo importo;
        private int numeroDecimali;

        /**
         * Costruttore.
         * 
         * @param type
         *            tipo del dato nell'editor.
         */
        public ImportolCellEditor(final Class<?> type) {
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
            BigDecimal importoInValuta = textField.getValue();
            importo.setImportoInValuta(importoInValuta);
            importo.calcolaImportoValutaAzienda(numeroDecimali);
            return importo;
        }

        @Override
        public boolean isUseConverterContext() {
            return true;
        }

        @Override
        public void setCellEditorValue(Object obj) {
            importo = ((Importo) obj).clone();
            textField.setValue(importo.getImportoInValuta());
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
        return new ImportolCellEditor(getType());
    }

}
