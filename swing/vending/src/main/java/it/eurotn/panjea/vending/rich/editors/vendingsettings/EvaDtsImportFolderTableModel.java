package it.eurotn.panjea.vending.rich.editors.vendingsettings;

import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

public class EvaDtsImportFolderTableModel extends DefaultBeanEditableTableModel<EvaDtsImportFolder> {

    private static final long serialVersionUID = 2706037394279730422L;

    /**
     * Costruttore.
     *
     */
    public EvaDtsImportFolderTableModel() {
        super("evaDtsImportFolderTableModel", new String[] { "folder", "fieldIDName", "gestioneValoreIDDoppio",
                "fieldIDContent", "calcolaCampoCA302" }, EvaDtsImportFolder.class);
    }

    @Override
    protected EvaDtsImportFolder createNewObject() {
        return new EvaDtsImportFolder();
    }

}
