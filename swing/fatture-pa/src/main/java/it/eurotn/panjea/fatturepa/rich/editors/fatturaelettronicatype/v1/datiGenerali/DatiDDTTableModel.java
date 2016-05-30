package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.datiGenerali;

import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiDDTType;

public class DatiDDTTableModel extends DefaultBeanEditableTableModel<DatiDDTType> {

    private static final long serialVersionUID = 2706037394279730422L;

    /**
     * Costruttore.
     *
     */
    public DatiDDTTableModel() {
        super("datiDDTTableModel", new String[] { "numeroDDT", "dataDDT" }, DatiDDTType.class);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

}
