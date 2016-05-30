package it.eurotn.panjea.magazzino.rich.editors.confrontolistino;

import it.eurotn.panjea.magazzino.util.parametriricerca.TipoConfronto;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class TipoConfrontoTableModel extends DefaultBeanTableModel<TipoConfronto> {

	private static final long serialVersionUID = 9218026942108741591L;

	/**
	 * Costruttore.
	 */
	public TipoConfrontoTableModel() {
		super("tipoConfrontoTableModel", new String[] { "confronto", "listino" }, TipoConfronto.class);
	}

}
