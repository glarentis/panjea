package it.eurotn.rich.control.table.renderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.util.RcpSupport;

public class BooleanContextSensitiveCellRenderer extends IconContextSensitiveCellRenderer {

    private static final long serialVersionUID = 7899860849203005381L;

    private Icon falseIcon = null;
    private Icon trueIcon = null;

    /**
     * Costruttore.
     **/
    public BooleanContextSensitiveCellRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.CENTER);
        falseIcon = RcpSupport.getIcon(Boolean.FALSE.toString().toLowerCase());
        trueIcon = RcpSupport.getIcon(Boolean.TRUE.toString().toLowerCase());
    }

    /**
     * @param value
     *            valore
     *
     * @return icona
     */
    public Icon getIcon(Boolean value) {
        if (value) {
            return trueIcon;
        } else {
            return falseIcon;
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        Boolean booleanValue = null;
        try {
            booleanValue = (Boolean) value;
        } catch (Exception e) {
            System.out.println("DEBUG:BooleanContextSensitiveCellRenderer->getTableCellRendererComponent:" + value);
        }

        JLabel label = (JLabel) super.getTableCellRendererComponent(table, booleanValue, isSelected, hasFocus, row,
                column);
        label.setText("");
        if (booleanValue != null) {
            label.setIcon(getIcon(booleanValue));
        }

        return label;
    }
}
