package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico;

import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class AreaOrdinePMTableModel extends DefaultBeanTableModel<AreaOrdinePM> {

	private static final long serialVersionUID = -1833480143371072079L;

	/**
	 * Costruttore.
	 *
	 * @param modelId
	 *            id del modello
	 */
	public AreaOrdinePMTableModel(final String modelId) {
		super(modelId, new String[] { "entita", "numero", "sede", "tipoDocumento", "depositoOrigine",
				"depositoDestinazione" }, AreaOrdinePM.class);
	}

}
