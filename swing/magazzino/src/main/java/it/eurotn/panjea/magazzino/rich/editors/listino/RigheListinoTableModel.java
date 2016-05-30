package it.eurotn.panjea.magazzino.rich.editors.listino;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class RigheListinoTableModel extends DefaultBeanTableModel<RigaListinoDTO> {

    private static final long serialVersionUID = 1L;

    private static ConverterContext context = new NumberWithDecimalConverterContext();

    /**
     * Costruttore.
     */
    public RigheListinoTableModel() {
        super(RigheListinoTablePage.PAGE_ID, new String[] { "categoria", "codiceArticolo", "descrizioneArticolo",
                "barCodeArticolo", "prezzo", "codiceUnitaMisura", "codiceCodiceIva" }, RigaListinoDTO.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 4:
            if (row == -1) {
                context.setUserObject(6);
            } else {
                RigaListinoDTO rigaListino = getElementAt(row);
                context.setUserObject(rigaListino.getNumeroDecimaliPrezzo());
            }
            return context;
        default:
            return super.getConverterContextAt(row, column);
        }
    }
}
