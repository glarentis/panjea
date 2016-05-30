package it.eurotn.panjea.contabilita.rich.editors.tabelle.strutturacontabile;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

/**
 * Table model delle righe corrispettivo con importo modificabile.
 *
 * @author Leonardo
 *
 */
public class StrutturaContabileTableModel extends DefaultBeanTableModel<StrutturaContabile> {

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
    public StrutturaContabileTableModel() {
        super(MODEL_ID, new String[] { "ordine", "formula", "descrizioneDare", "descrizioneAvere" },
                StrutturaContabile.class);
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
        if (column == 1) {
            return numberPrezzoEditorContext;
        }
        return null;
    }
}
