package it.eurotn.panjea.contabilita.rich.editors.tabelle.prestazioni;

import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

/**
 * @author fattazzo
 *
 */
public class PrestazioniTableModel extends DefaultBeanTableModel<Prestazione> {

    private static final long serialVersionUID = -7183844580017807353L;

    /**
     * Costruttore.
     */
    public PrestazioniTableModel() {
        super("prestazioniTableModel", new String[] { "codice", "descrizione" }, Prestazione.class);
    }

}
