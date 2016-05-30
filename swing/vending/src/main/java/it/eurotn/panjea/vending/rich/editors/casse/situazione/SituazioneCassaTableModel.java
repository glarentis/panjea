package it.eurotn.panjea.vending.rich.editors.casse.situazione;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.vending.domain.SituazioneCassa;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class SituazioneCassaTableModel extends DefaultBeanTableModel<SituazioneCassa> {

    private static final long serialVersionUID = -8913854939984857998L;

    private static ConverterContext numberPrezzoContext = new NumberWithDecimalConverterContext(2);

    /**
     * Costruttore.
     */
    public SituazioneCassaTableModel() {
        super("situazioneCassaTableModel",
                new String[] { "gettone", "gettone.valore", "quantitaIniziale", "quantitaEntrate", "quantitaUscite",
                        "quantitaFinale", "valoreIniziale", "valoreEntrate", "valoreUscite", "valoreFinale" },
                SituazioneCassa.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {

        if (column >= 0) {
            return numberPrezzoContext;
        }

        return super.getConverterContextAt(row, column);
    }
}
