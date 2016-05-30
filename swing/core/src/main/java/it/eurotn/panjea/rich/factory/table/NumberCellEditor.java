/**
 * 
 */
package it.eurotn.panjea.rich.factory.table;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.JTextField;

import org.jdesktop.swingx.table.NumberEditorExt;

/**
 * Il NumberCellEditor permette di inserire qualsiasi dato di tipo numerico bloccando il commit del valore se il dato
 * non è corretto e durante la digitazione sostituisce tutti i caratteri '.' (punto) sia del tastierino numerico che non
 * con il carattere ',' (virgola).
 * 
 * @author fattazzo
 * 
 */
public class NumberCellEditor extends NumberEditorExt {

    private static final long serialVersionUID = -837980065603677164L;

    public NumberCellEditor() {
        super(new DecimalFormat("0.##"));
        final JTextField formattedTextField = (JTextField) getComponent();
        formattedTextField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (e.getKeyCode() == KeyEvent.VK_DECIMAL || e.getKeyCode() == KeyEvent.VK_PERIOD) {
                    formattedTextField.setText(formattedTextField.getText().replaceAll("\\.", "\\,"));
                    e.consume();
                }
            }

        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
}
