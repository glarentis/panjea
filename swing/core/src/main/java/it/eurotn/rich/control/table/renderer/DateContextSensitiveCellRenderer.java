package it.eurotn.rich.control.table.renderer;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.jidesoft.grid.EditorContext;

public class DateContextSensitiveCellRenderer extends IconContextSensitiveCellRenderer {

    private static final long serialVersionUID = -6417505102994426573L;

    public static final EditorContext ORA_CONTEXT = new EditorContext("oraContext", "HH:mm");

    /**
     * Costruttore.
     */
    public DateContextSensitiveCellRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (ORA_CONTEXT == getEditorContext() && value != null) {
            label.setText("");
            if (value instanceof Date) {
                label.setText(new SimpleDateFormat((String) getEditorContext().getUserObject()).format(value));
            }
            label.setIcon(getIconSource().getIcon("time"));
        }

        return label;
    }

}
