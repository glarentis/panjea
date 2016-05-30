package it.eurotn.panjea.vending.rich.editors.lettureselezionatrice;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.vending.domain.RigaLetturaSelezionatrice;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class RigheLetturaTableModel extends DefaultBeanTableModel<RigaLetturaSelezionatrice> {

    private static final long serialVersionUID = 2706037394279730422L;

    private static ConverterContext qtaContext;

    private static ConverterContext numberPrezzoContext = new NumberWithDecimalConverterContext(2);

    static {
        qtaContext = new NumberWithDecimalConverterContext(0);
    }

    /**
     * Costruttore.
     *
     */
    public RigheLetturaTableModel() {
        super("righeLetturaTableModel", new String[] { "gettone", "gettone.valore", "quantita", "importo" },
                RigaLetturaSelezionatrice.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 0:
            return qtaContext;
        case 2:
        case 3:
            return numberPrezzoContext;
        default:
            return super.getConverterContextAt(row, column);
        }
    }

}
