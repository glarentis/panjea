package it.eurotn.panjea.fatturepa.rich.editors.ricerca.statoarea;

import javax.swing.SwingConstants;

import it.eurotn.panjea.fatturepa.domain.StatoFatturaPA;
import it.eurotn.panjea.rich.factory.table.CustomEnumCellRenderer;

public class StatoFatturaPACellRenderer extends CustomEnumCellRenderer {

    private static final long serialVersionUID = 4369232506289318023L;

    /**
     * Costruttore.
     */
    public StatoFatturaPACellRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.LEFT);
    }

    @Override
    public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {

        if (value != null && value instanceof StatoFatturaPA) {
            return value.getClass().getName() + "." + ((StatoFatturaPA) value).name();
        } else {
            return StatoFatturaPA.class.getName() + "._DI";
        }
    }
}
