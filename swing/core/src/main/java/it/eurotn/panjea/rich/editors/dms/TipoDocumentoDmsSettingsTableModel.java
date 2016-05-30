package it.eurotn.panjea.rich.editors.dms;

import com.jidesoft.grid.EditorContext;

import it.eurotn.panjea.dms.domain.TipoDocumentoDmsSettings;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

public class TipoDocumentoDmsSettingsTableModel extends DefaultBeanEditableTableModel<TipoDocumentoDmsSettings> {

    private static final long serialVersionUID = 2706037394279730422L;

    /**
     * Costruttore.
     *
     */
    public TipoDocumentoDmsSettingsTableModel() {
        super("tipiDocumentoDmsSettingsTableModel", new String[] { "tipoDocumento", "folderPattern" },
                TipoDocumentoDmsSettings.class);
    }

    @Override
    public EditorContext getEditorContextAt(int row, int col) {

        if (col == 0) {
            return new SearchContext("codice");
        }

        return super.getEditorContextAt(row, col);
    }

}
