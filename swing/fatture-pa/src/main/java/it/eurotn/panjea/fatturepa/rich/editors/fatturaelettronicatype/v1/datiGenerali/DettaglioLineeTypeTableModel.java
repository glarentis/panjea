package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.datiGenerali;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.gov.fatturapa.sdi.fatturapa.v1.DettaglioLineeType;

public class DettaglioLineeTypeTableModel extends DefaultBeanTableModel<DettaglioLineeType> {

    private static final long serialVersionUID = 2706037394279730422L;

    private static ConverterContext qtaContext;

    static {
        qtaContext = new NumberWithDecimalConverterContext();
        qtaContext.setUserObject(3);
    }

    /**
     * Costruttore.
     *
     */
    public DettaglioLineeTypeTableModel() {
        super("dettaglioLineeTypeTableModel", new String[] { "numeroLinea", "descrizione", "quantita", "unitaMisura",
                "prezzoUnitario", "prezzoTotale", "aliquotaIVA" }, DettaglioLineeType.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 2:
        case 4:
        case 5:
            return qtaContext;
        default:
            return super.getConverterContextAt(row, column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
