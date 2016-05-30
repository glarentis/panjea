package it.eurotn.panjea.magazzino.rich.editors.listinoprezzi;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.magazzino.manager.listinoprezzi.ListinoPrezziDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class ListinoPrezziTableModel extends DefaultBeanTableModel<ListinoPrezziDTO> {

    private static final long serialVersionUID = 4191968753484432018L;
    private final ConverterContext numberContext = new NumberWithDecimalConverterContext();

    /**
     * Costruttore.
     */
    public ListinoPrezziTableModel() {
        super("listinoPrezziTableModel", new String[] { "articolo", "articolo.codiceIva", "articolo.unitaMisura",
                "prezzo", "sconto", "prezzoNetto", "prezzoIvato" }, ListinoPrezziDTO.class);
        numberContext.setUserObject(2);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 3:
        case 5:
        case 6:
            return numberContext;
        default:
            return null;
        }
    }

}
