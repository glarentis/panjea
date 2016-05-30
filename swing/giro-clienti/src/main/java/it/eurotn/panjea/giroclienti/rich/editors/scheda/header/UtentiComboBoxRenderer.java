package it.eurotn.panjea.giroclienti.rich.editors.scheda.header;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.panjea.sicurezza.domain.Utente;

public class UtentiComboBoxRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -8094102329699338708L;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value != null) {
            label.setText(ObjectConverterManager.toString(value));
            label.setIcon(RcpSupport.getIcon(Utente.class.getName()));
        }
        return label;
    }

}
