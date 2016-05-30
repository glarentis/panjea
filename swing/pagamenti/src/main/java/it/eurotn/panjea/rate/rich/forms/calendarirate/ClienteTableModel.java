package it.eurotn.panjea.rate.rich.forms.calendarirate;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class ClienteTableModel extends DefaultBeanTableModel<ClienteLite> {

	private static final long serialVersionUID = -1319924849836492375L;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public ClienteTableModel() {
		super("ClienteTableModel", new String[] { "codice", "anagrafica.denominazione" }, ClienteLite.class);
	}

}
