package it.eurotn.panjea.rich.factory.table;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.JTable;

import com.jidesoft.grid.EditorContext;

public class CustomDoubleCellRenderer extends AbstractCustomTableCellRenderer {

    public static final EditorContext CONTEXT = new EditorContext("qta");

    private static final long serialVersionUID = 9217453065990280535L;

    @Override
    public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
        return null;
    }

    @Override
    public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String renderedValue = "";

        if (value != null && !value.toString().isEmpty()) {
            Double double1 = (Double) value;
            Format format = new DecimalFormat("###,###,###,##0.0000");
            renderedValue = format.format(double1);
        }

        setValue(renderedValue);

        return cell;
    }

}
