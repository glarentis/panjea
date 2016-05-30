package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.datiGenerali;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiRiepilogoType;

public class DatiRiepilogoTypeTableModel extends DefaultBeanTableModel<DatiRiepilogoType> {

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
    public DatiRiepilogoTypeTableModel() {
        super("dettaglioLineeTypeTableModel", new String[] { "aliquotaIVA", "imponibileImporto", "imposta", "natura",
                "riferimentoNormativo", "esigibilitaIVA" }, DatiRiepilogoType.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 1:
        case 2:
            return qtaContext;
        default:
            return super.getConverterContextAt(row, column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
