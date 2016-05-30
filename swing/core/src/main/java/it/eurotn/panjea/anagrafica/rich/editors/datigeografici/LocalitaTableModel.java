package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class LocalitaTableModel extends DefaultBeanTableModel<Localita> {

	private static final long serialVersionUID = 9218026942108741591L;

	/**
	 * Costruttore.
	 */
	public LocalitaTableModel() {
		super("localitaTableModel", new String[] { "descrizione" }, Localita.class);
	}

}
