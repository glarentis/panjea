package it.eurotn.panjea.vending.rich.editors.lettureselezionatrice;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class LettureSelezionatriceTableModel extends DefaultBeanTableModel<LetturaSelezionatrice> {

    private static final long serialVersionUID = 4137770707471885023L;

    private static ConverterContext numberPrezzoContext = new NumberWithDecimalConverterContext(2);

    /**
     * Costruttore.
     */
    public LettureSelezionatriceTableModel() {
        super("lettureSelezionatriceTableModel", new String[] { "statoLettura", "data", "rifornimento", "installazione",
                "distributore", "caricatore", "dataRifornimento", "importo", "reso" }, LetturaSelezionatrice.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {

        if (column >= 7) {
            return numberPrezzoContext;
        }

        return super.getConverterContextAt(row, column);
    }

}
