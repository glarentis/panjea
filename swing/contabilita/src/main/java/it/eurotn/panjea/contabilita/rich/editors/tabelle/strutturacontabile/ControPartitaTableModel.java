package it.eurotn.panjea.contabilita.rich.editors.tabelle.strutturacontabile;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

/**
 * Table model delle righe corrispettivo con importo modificabile.
 *
 * @author Leonardo
 *
 */
public class ControPartitaTableModel extends DefaultBeanTableModel<ControPartita> {

    private static final long serialVersionUID = 3151344217947106554L;

    public static final String MODEL_ID = "righeCorrispettivoTableModel";

    private static ConverterContext numberPrezzoContext;

    static {
        numberPrezzoContext = new NumberWithDecimalConverterContext();
        numberPrezzoContext.setUserObject(new Integer(2));
    }

    /**
     * Costruttore.
     */
    public ControPartitaTableModel() {
        super(MODEL_ID, new String[] { "ordine", "descrizione", "formula", "descrizioneDare", "descrizioneAvere" },
                ControPartita.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        if (column == 1) {
            return numberPrezzoContext;
        }
        return null;
    }
}
