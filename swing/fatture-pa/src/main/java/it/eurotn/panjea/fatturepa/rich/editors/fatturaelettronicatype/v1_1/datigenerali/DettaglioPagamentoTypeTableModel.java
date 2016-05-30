package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1_1.datigenerali;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DettaglioPagamentoType;

public class DettaglioPagamentoTypeTableModel extends DefaultBeanTableModel<DettaglioPagamentoType> {

    private static final long serialVersionUID = 2706037394279730422L;

    private static ConverterContext qtaContext;

    static {
        qtaContext = new NumberWithDecimalConverterContext();
        qtaContext.setUserObject(2);
    }

    /**
     * Costruttore.
     *
     */
    public DettaglioPagamentoTypeTableModel() {
        super("DettaglioPagamentoTypeTableModel",
                new String[] { "modalitaPagamento", "dataRiferimentoTerminiPagamento", "giorniTerminiPagamento",
                        "dataScadenzaPagamento", "importoPagamento", "istitutoFinanziario", "IBAN", "ABI", "CAB",
                        "BIC" },
                DettaglioPagamentoType.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        if (column == 4) {
            return qtaContext;
        }
        return super.getConverterContextAt(row, column);
    }

}
