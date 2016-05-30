package it.eurotn.panjea.fatturepa.rich.renderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.xml.datatype.XMLGregorianCalendar;

import com.jidesoft.converter.ObjectConverterManager;

public class XMLGregorianCalendarCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 6384874025291822125L;

    /**
     * Costruttore.
     */
    public XMLGregorianCalendarCellRenderer() {
        super();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null && value instanceof XMLGregorianCalendar) {
            label.setText(
                    ObjectConverterManager.toString(((XMLGregorianCalendar) value).toGregorianCalendar().getTime()));
        }

        return label;
    }
}
