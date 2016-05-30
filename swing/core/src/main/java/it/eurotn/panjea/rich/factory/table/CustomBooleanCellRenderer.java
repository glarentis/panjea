package it.eurotn.panjea.rich.factory.table;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class CustomBooleanCellRenderer extends JCheckBox implements TableCellRenderer {

    private static final long serialVersionUID = 4145273532803256827L;

    private final CustomTableCellRenderer renderer = new CustomTableCellRenderer();

    public CustomBooleanCellRenderer() {
        setFont(getFont().deriveFont(Font.PLAIN));
        setBorder(new EmptyBorder(0, 3, 0, 3));
        setHorizontalAlignment(CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        // Se il valore non � un booleano ritorno un rendered standard
        if (!(value instanceof Boolean)) {
            return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

        this.setSelected((Boolean) value);
        return this;
    }
}
