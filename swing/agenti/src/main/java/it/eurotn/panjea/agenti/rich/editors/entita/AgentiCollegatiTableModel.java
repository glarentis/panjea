package it.eurotn.panjea.agenti.rich.editors.entita;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class AgentiCollegatiTableModel extends DefaultBeanTableModel<Agente> {
	private static final long serialVersionUID = 794443943330052929L;

	/**
	 * Costruttore.
	 */
	public AgentiCollegatiTableModel() {
		super("agentiCollegatiId", new String[] { "anagrafica.denominazione" }, Agente.class);
	}

}
