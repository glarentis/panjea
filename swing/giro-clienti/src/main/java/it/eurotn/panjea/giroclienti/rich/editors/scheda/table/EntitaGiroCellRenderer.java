package it.eurotn.panjea.giroclienti.rich.editors.scheda.table;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

public class EntitaGiroCellRenderer extends IconContextSensitiveCellRenderer {

    private static final long serialVersionUID = 7145586743533897109L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null && value instanceof SedeEntita) {
            label.setText("<html><b>" + ObjectConverterManager.toString(((SedeEntita) value).getEntita()) + "</b><br>"
                    + label.getText() + "</html>");
            label.setIcon(getIconSource().getIcon(Cliente.class.getName()));
        }

        return label;
    }
}
