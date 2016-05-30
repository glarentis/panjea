package it.eurotn.panjea.contabilita.rich.editors.tabelle.causaliritenutaacconto;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

/**
 * @author fattazzo
 *
 */
public class CausaliRitenutaAccontoTableModel extends DefaultBeanTableModel<CausaleRitenutaAcconto> {

    private static final long serialVersionUID = -7183844580017807353L;

    private static NumberWithDecimalConverterContext percentualeContext;

    {
        percentualeContext = new NumberWithDecimalConverterContext();
        percentualeContext.setUserObject(new Integer(2));
        percentualeContext.setPostfisso("%");
    }

    /**
     * Costruttore.
     */
    public CausaliRitenutaAccontoTableModel() {
        super("causaliRitenutaAccontoTableModel",
                new String[] { "codice", "descrizione", "percentualeAliquota", "percentualeImponibile" },
                CausaleRitenutaAcconto.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 2:
        case 3:
            return percentualeContext;
        default:
            return null;
        }
    }

}
