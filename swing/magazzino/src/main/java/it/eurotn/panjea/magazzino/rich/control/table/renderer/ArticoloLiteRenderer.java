package it.eurotn.panjea.magazzino.rich.control.table.renderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.rich.factory.table.CustomArticoloRenderer;
import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

public class ArticoloLiteRenderer extends IconContextSensitiveCellRenderer {
    private static final long serialVersionUID = -5278686990215142270L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        if (!(value instanceof ArticoloLite)) {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Icon icon = null;
        ArticoloLite articolo = (ArticoloLite) value;
        if (!articolo.isDistinta()) {
            icon = RcpSupport.getIcon(Articolo.class.getName());
        } else {
            icon = RcpSupport.getIcon(CustomArticoloRenderer.KEY_ICON_ARTICOLO_DISTINTA);
        }

        label.setIcon(icon);
        label.setHorizontalTextPosition(getTextPosition());
        return label;
    }
}
