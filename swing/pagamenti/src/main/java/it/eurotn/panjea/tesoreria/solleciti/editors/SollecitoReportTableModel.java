package it.eurotn.panjea.tesoreria.solleciti.editors;

import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class SollecitoReportTableModel extends DefaultBeanTableModel<Sollecito> {
	private static final long serialVersionUID = -597458913344700341L;

	/**
	 * Costruttore.
	 */
	public SollecitoReportTableModel() {
		super(StampaSollecitiDialog.PAGE_ID, new String[] { "cliente", "email", "stampa", "telefono", "fax" },
				Sollecito.class);
	}

}
