package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni;

import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

public class RigheTableModel extends DefaultBeanEditableTableModel<RigaInstallazione> {
    private static final long serialVersionUID = 7157041928933025460L;

    /**
     * Costruttore.
     */
    public RigheTableModel() {
        super("righeInstallazioneTable",
                new String[] { "installazione.ubicazione", "installazione", "articolo", "causaleInstallazione" },
                RigaInstallazione.class);
    }

}
