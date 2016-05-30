package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1_1.datigenerali;

import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiSALType;

public class DatiSALTableModel extends DefaultBeanEditableTableModel<DatiSALType> {

    private static final long serialVersionUID = 2706037394279730422L;

    /**
     * Costruttore.
     *
     */
    public DatiSALTableModel() {
        super("datiSALTableModel", new String[] { "riferimentoFase" }, DatiSALType.class);
    }

}
