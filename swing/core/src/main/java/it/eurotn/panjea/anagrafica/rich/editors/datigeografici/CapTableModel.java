package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class CapTableModel extends DefaultBeanTableModel<Cap> {

	private static final long serialVersionUID = -1319924849836492375L;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public CapTableModel() {
		super("capTableModel", new String[] { "descrizione" }, Cap.class);
	}

}
