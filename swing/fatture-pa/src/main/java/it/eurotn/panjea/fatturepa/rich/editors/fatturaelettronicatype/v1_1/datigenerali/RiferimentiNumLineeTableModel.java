package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1_1.datigenerali;

import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

public class RiferimentiNumLineeTableModel extends DefaultBeanEditableTableModel<RiferimentoLineaPM> {

    private static final long serialVersionUID = 2706037394279730422L;

    /**
     * Costruttore.
     *
     */
    public RiferimentiNumLineeTableModel() {
        super("riferimentiNumLineeTableModel", new String[] { "value" }, RiferimentoLineaPM.class);
    }

}
