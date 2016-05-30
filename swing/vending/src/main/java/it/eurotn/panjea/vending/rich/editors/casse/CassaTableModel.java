package it.eurotn.panjea.vending.rich.editors.casse;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.vending.domain.Cassa;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class CassaTableModel extends DefaultBeanTableModel<Cassa> {

    private static final long serialVersionUID = -9173890885980831555L;

    private static ConverterContext numberPrezzoContext = new NumberWithDecimalConverterContext(2);

    /**
     * Costruttore.
     */
    public CassaTableModel() {
        super("cassaTableModel", new String[] { "tipologia", "codice", "descrizione", "totaleIniziale", "totaleEntrate",
                "totaleUscite", "totale" }, Cassa.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {

        if (column >= 3) {
            return numberPrezzoContext;
        }

        return super.getConverterContextAt(row, column);
    }

}
