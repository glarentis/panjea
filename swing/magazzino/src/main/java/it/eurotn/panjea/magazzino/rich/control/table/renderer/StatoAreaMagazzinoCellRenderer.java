package it.eurotn.panjea.magazzino.rich.control.table.renderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.components.CompoundIcon;
import it.eurotn.panjea.rich.components.CompoundIcon.Axis;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

public class StatoAreaMagazzinoCellRenderer extends IconContextSensitiveCellRenderer {
    private static final long serialVersionUID = 8301042112478704859L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setText(null);

        if (DefaultBeanTableModel.class
                .isAssignableFrom(TableModelWrapperUtils.getActualTableModel(table.getModel()).getClass())) {
            @SuppressWarnings("unchecked")
            DefaultBeanTableModel<AreaMagazzinoRicerca> tableModel = (DefaultBeanTableModel<AreaMagazzinoRicerca>) TableModelWrapperUtils
                    .getActualTableModel(table.getModel());
            int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
            if (actualRow == -1) {
                return label;
            }

            AreaMagazzinoRicerca areaMagazzinoRicerca = tableModel.getObject(actualRow);
            if (areaMagazzinoRicerca.isChiuso()) {
                Icon iconChiusura = RcpSupport.getIcon("rigaChiusa.icon");
                if (label.getIcon() != null) {
                    label.setIcon(new CompoundIcon(Axis.X_AXIS, 5, label.getIcon(), iconChiusura));
                }
            }
        }
        return label;
    }
}
