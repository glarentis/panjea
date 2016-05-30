package it.eurotn.panjea.vending.rich.editors.rilevazionievadts;

import it.eurotn.panjea.vending.domain.evadts.RilevazioniFasceEva;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

public class RilevazioniFasceEvaDtsTableModel extends DefaultBeanEditableTableModel<RilevazioniFasceEva> {
    private static final long serialVersionUID = 1L;

    /**
     * Costruttore.
     */
    public RilevazioniFasceEvaDtsTableModel() {
        super("rilevazioniFascieEvaDtsTableModel", new String[] { "la101", "la102", "la103", "la104" },
                RilevazioniFasceEva.class);
    }

}
