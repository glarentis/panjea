package it.eurotn.panjea.vending.rich.editors.gettoni;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.vending.domain.Gettone;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class GettoniTableModel extends DefaultBeanTableModel<Gettone> {

    private static final long serialVersionUID = -5492065511529573242L;

    private static ConverterContext numberPrezzoContext = new NumberWithDecimalConverterContext(2);

    /**
     * Costruttore.
     */
    public GettoniTableModel() {
        super("gettoniTableModel", new String[] { "valore", "codice", "descrizione" }, Gettone.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {

        if (column == 0) {
            return numberPrezzoContext;
        }

        return null;
    }
}
