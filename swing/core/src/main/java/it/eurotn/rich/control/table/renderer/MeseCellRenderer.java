package it.eurotn.rich.control.table.renderer;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;

public class MeseCellRenderer extends ContextSensitiveCellRenderer {

    public static final EditorContext MESE_CONTEXT = new EditorContext("MESE_CONTEXT");

    private static final long serialVersionUID = -6526605820703910193L;

    private final Calendar cal = Calendar.getInstance();

    private final DateFormat defaultFormat = new SimpleDateFormat("MMMM");

    /**
     * Costruttore.
     * 
     */
    public MeseCellRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.RIGHT);
    }

    /**
     * 
     * @param paramInt
     *            giorno da settare
     * @return calendar con il mese impostato
     */
    protected Calendar getCalendarByMonth(int paramInt) {
        cal.set(2, paramInt);
        return cal;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        final JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                column);
        String valore = "";
        if (value instanceof Integer && ((Integer) value) > 0) {
            final Integer mese = (Integer) value;
            valore = this.defaultFormat.format(getCalendarByMonth(mese.intValue() - 1).getTime()).toUpperCase();
        }
        label.setIcon(null);
        label.setText(valore);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

}
