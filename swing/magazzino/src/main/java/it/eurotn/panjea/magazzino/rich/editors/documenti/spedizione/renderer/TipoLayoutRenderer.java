package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.TipoLayout;

public class TipoLayoutRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 7917822691306897334L;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        TipoLayout tipoLayout = (TipoLayout) value;

        label.setText(RcpSupport.getMessage(TipoLayout.class.getName() + "." + tipoLayout.name()));
        return label;
    }
}
