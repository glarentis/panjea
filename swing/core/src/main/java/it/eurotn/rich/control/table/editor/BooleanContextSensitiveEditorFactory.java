package it.eurotn.rich.control.table.editor;

import java.awt.Component;

import javax.swing.CellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellEditor;

public class BooleanContextSensitiveEditorFactory extends AbstractCellEditorFactory {

    private class BooleanCellEditor extends ContextSensitiveCellEditor {

        private static final long serialVersionUID = 1326026286430791000L;

        private Boolean booleanValue;

        /**
         * Costruttore.
         *
         * @param type
         *            tipo del dato nell'editor.
         */
        public BooleanCellEditor(final Class<?> type) {
            setAutoStopCellEditing(true);
            setType(type);
        }

        @Override
        public Object getCellEditorValue() {
            return booleanValue;
        }

        @Override
        public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean select, int i, int j) {

            booleanValue = (Boolean) obj;
            if (booleanValue == null) {
                booleanValue = false;
            }
            booleanValue = !booleanValue;

            JLabel label = new JLabel();
            label.setOpaque(true);
            label.setBackground(jtable.getSelectionBackground());
            label.setText("");
            label.setIcon(RcpSupport.getIcon(booleanValue.toString().toLowerCase()));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }

        @Override
        public boolean isAutoStopCellEditing() {
            return true;
        }

    }

    @Override
    public CellEditor create() {
        return new BooleanCellEditor(getType());
    }

}
