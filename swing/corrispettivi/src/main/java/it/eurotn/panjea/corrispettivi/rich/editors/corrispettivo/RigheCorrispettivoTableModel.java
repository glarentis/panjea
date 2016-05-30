package it.eurotn.panjea.corrispettivi.rich.editors.corrispettivo;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.NavigableTableModel;

import it.eurotn.panjea.corrispettivi.domain.RigaCorrispettivo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

/**
 * Table model delle righe corrispettivo con importo modificabile.
 *
 * @author Leonardo
 *
 */
public class RigheCorrispettivoTableModel extends DefaultBeanEditableTableModel<RigaCorrispettivo>
        implements NavigableTableModel {

    private static final long serialVersionUID = 3151344217947106554L;

    public static final String MODEL_ID = "righeCorrispettivoTableModel";

    private static ConverterContext numberPrezzoContext;
    private static EditorContext numberPrezzoEditorContext;

    static {
        numberPrezzoContext = new NumberWithDecimalConverterContext();
        numberPrezzoContext.setUserObject(new Integer(2));

        numberPrezzoEditorContext = new EditorContext("DecimalNumberEditorContext", 2);
    }

    /**
     * Costruttore.
     */
    public RigheCorrispettivoTableModel() {
        super(MODEL_ID, new String[] { "codiceIva.codice", "codiceIva.descrizioneInterna", "importo" },
                RigaCorrispettivo.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        if (column == 1) {
            return numberPrezzoContext;
        }
        return null;
    }

    @Override
    public EditorContext getEditorContextAt(int row, int column) {
        switch (column) {
        case 0:
            return new SearchContext("codice", "codiceIva");
        case 1:
            return new SearchContext("descrizioneInterna", "codiceIva");
        case 2:
            return numberPrezzoEditorContext;
        default:
            throw new IllegalArgumentException("Editor context non previsto");
        }
    }

    @Override
    public void setValueAt(Object arg0, int arg1, int arg2) {
        super.setValueAt(arg0, arg1, arg2);
        fireTableDataChanged();
    }
}
