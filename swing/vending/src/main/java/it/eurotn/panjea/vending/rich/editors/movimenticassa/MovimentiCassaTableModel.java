package it.eurotn.panjea.vending.rich.editors.movimenticassa;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class MovimentiCassaTableModel extends DefaultBeanTableModel<MovimentoCassa> {

    private static final long serialVersionUID = -9173890885980831555L;

    private static ConverterContext numberPrezzoContext = new NumberWithDecimalConverterContext(2);

    /**
     * Costruttore.
     */
    public MovimentiCassaTableModel() {
        super("movimentiCassaTableModel",
                new String[] { "cassa.tipologia", "cassa", "data", "totaleEntrate", "totaleUscite", "totale" },
                MovimentoCassa.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {

        if (column >= 3) {
            return numberPrezzoContext;
        }

        return super.getConverterContextAt(row, column);
    }

}
