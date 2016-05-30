package it.eurotn.panjea.manutenzioni.rich.renderer;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

public class OperatoreContextSensitiveCellRender extends IconContextSensitiveCellRenderer {

    private static final long serialVersionUID = -3572555804124859162L;

    private final Map<String, Icon> rendererIcons = new HashMap<>();

    /**
     * @param operatore
     *            operatore
     * @return icona
     */
    private Icon getRendererIcon(Operatore operatore) {

        String operatoreClass = operatore.getClass().getName();

        if (rendererIcons.get(operatoreClass) == null) {
            rendererIcons.put(operatoreClass, getIconSource().getIcon(operatoreClass));
        }

        return rendererIcons.get(operatoreClass);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Icon icon = null;
        try {
            Operatore operatore = (Operatore) value;
            if (operatore != null && !operatore.isNew()) {
                icon = getRendererIcon(operatore);
            }
        } catch (Exception e) { // NOSONAR
            icon = null;
        }

        label.setIcon(icon);
        label.setHorizontalTextPosition(getTextPosition());

        return label;
    }
}
