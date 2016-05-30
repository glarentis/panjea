package it.eurotn.panjea.rich.editors.email;

import it.eurotn.panjea.anagrafica.util.MailDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class MailTableModel extends DefaultBeanTableModel<MailDTO> {

    private static final long serialVersionUID = -6650712604808442232L;

    /**
     *
     * Costruttore.
     *
     */
    public MailTableModel() {
        super(MailTablePage.PAGE_ID, new String[] { "successo", "data", "emailDiSpedizione", "emailDestinazione",
                "entitaDestinazione", "oggetto" }, MailDTO.class);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
