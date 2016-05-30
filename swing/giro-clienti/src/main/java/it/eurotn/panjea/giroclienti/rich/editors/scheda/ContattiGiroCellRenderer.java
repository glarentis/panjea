package it.eurotn.panjea.giroclienti.rich.editors.scheda;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.giroclienti.domain.RigaGiroCliente;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

public class ContattiGiroCellRenderer extends IconContextSensitiveCellRenderer {

    private static final long serialVersionUID = -8493198559702429250L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        @SuppressWarnings("unchecked")
        final DefaultBeanTableModel<RigaGiroCliente> innerModel = (DefaultBeanTableModel<RigaGiroCliente>) TableModelWrapperUtils
                .getActualTableModel(table.getModel());
        final int roxInner = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);

        if (roxInner != -1) {
            final RigaGiroCliente rigaGiroCliente = innerModel.getElementAt(roxInner);

            label.setText(rigaGiroCliente.getDescrizioneContatti());
        }

        return label;
    }
}
