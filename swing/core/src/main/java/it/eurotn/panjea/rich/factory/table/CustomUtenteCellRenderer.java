package it.eurotn.panjea.rich.factory.table;

import it.eurotn.panjea.sicurezza.domain.Utente;

import javax.swing.SwingConstants;

public class CustomUtenteCellRenderer extends AbstractCustomTableCellRenderer {

    private static final long serialVersionUID = -6279343292063921287L;

    public CustomUtenteCellRenderer() {
        super(SwingConstants.LEFT);
    }

    @Override
    public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
        if (value != null) {
            return value.getClass().getName();
        } else {
            return null;
        }
    }

    @Override
    public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {
        if (value != null) {
            Utente utente = (Utente) value;
            return utente.getUserName() + " - " + utente.getDescrizione();
        } else {
            return null;
        }
    }
}
