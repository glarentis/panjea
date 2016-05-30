package it.eurotn.panjea.vending.rich.editors.rilevazionievadts;

import it.eurotn.panjea.vending.domain.evadts.RilevazioniEvaDtsErrori;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

@SuppressWarnings("serial")
public class RilevazioniEvaDtsErroriTableModel extends DefaultBeanEditableTableModel<RilevazioniEvaDtsErrori> {

    /**
     * Costruttore
     */
    public RilevazioniEvaDtsErroriTableModel() {
        super("rilevazioniEvaDtsErrori", new String[] { "progressivo", "tipo", "codice", "elemento", "occorrenze" },
                RilevazioniEvaDtsErrori.class);
    }

}
