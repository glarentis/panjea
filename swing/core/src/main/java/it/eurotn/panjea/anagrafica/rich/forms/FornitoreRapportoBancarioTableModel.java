package it.eurotn.panjea.anagrafica.rich.forms;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class FornitoreRapportoBancarioTableModel extends DefaultBeanTableModel<SedeEntita> {

	private static final long serialVersionUID = 9218026942108741591L;

	/**
	 * Costruttore.
	 */
	public FornitoreRapportoBancarioTableModel() {
		super("fornitoreRapportoBancarioTableModel", new String[] { "entita", "sede" }, SedeEntita.class);
	}

}
