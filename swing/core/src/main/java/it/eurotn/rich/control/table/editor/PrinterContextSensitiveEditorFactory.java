package it.eurotn.rich.control.table.editor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.CellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.springframework.richclient.list.ComboBoxListModel;

import com.jidesoft.grid.ContextSensitiveCellEditor;
import com.jidesoft.grid.EditorContext;

public class PrinterContextSensitiveEditorFactory extends AbstractCellEditorFactory {

    public class PrinterComboBoxCellEditor extends ContextSensitiveCellEditor implements TableCellEditor {

        private static final long serialVersionUID = 4851449918454791796L;

        private JComboBox printerComboBox;

        /**
         * Creates a DirectionCellEditor.
         */
        public PrinterComboBoxCellEditor() {
            ComboBoxListModel model = new ComboBoxListModel(getPrintersName());
            printerComboBox = new JComboBox(model);
            printerComboBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    PrinterComboBoxCellEditor.this.stopCellEditing();
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return printerComboBox.getSelectedItem();
        }

        /**
         * Carica i nomi delle stampanti di sistema.
         * 
         * @return nomi delle stampanti caricati
         */
        private List<String> getPrintersName() {

            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

            List<String> printers = new ArrayList<String>();
            printers.add("");
            for (PrintService printer : printServices) {
                printers.add(printer.getName());
            }

            return printers;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            printerComboBox.setSelectedItem(value);
            return printerComboBox;
        }
    }

    public static final EditorContext CONTEXT = new EditorContext("printerComboBoxCellEditor");

    @Override
    public CellEditor create() {
        return new PrinterComboBoxCellEditor();
    }

}
