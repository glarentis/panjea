package it.eurotn.panjea.contabilita.rich.editors.tabelle.contributiprevidenziali;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.contabilita.domain.ContributoINPS;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.rich.converter.I18NConverterContext;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

/**
 * @author fattazzo
 *
 */
public class ContributiPrevidenzialiTableModel extends DefaultBeanTableModel<ContributoPrevidenziale> {

    private static final long serialVersionUID = -2408684843324315754L;

    private static NumberWithDecimalConverterContext percentualeContext;
    private static I18NConverterContext i18nConverterContext;

    {
        percentualeContext = new NumberWithDecimalConverterContext();
        percentualeContext.setUserObject(new Integer(2));
        percentualeContext.setPostfisso("%");

        i18nConverterContext = new I18NConverterContext();
    }

    /**
     * Costruttore.
     */
    public ContributiPrevidenzialiTableModel() {
        super("ContributiPrevidenzialiTableModel", new String[] { "domainClassName", "codice", "percContributiva",
                "percCaricoLavoratore", "percCaricoAzienda" }, ContributoPrevidenziale.class, ContributoINPS.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 0:
            return i18nConverterContext;
        case 1:
            return null;
        default:
            return percentualeContext;
        }
    }

}
