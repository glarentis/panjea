package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception;

import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class EntitaSenzaTipoDocumentoTableModel extends DefaultBeanTableModel<EntitaEvasione> {

	private static final long serialVersionUID = -1833480143371072079L;

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 *            id del modello
	 */
	public EntitaSenzaTipoDocumentoTableModel(final String modelId) {
		super(modelId, new String[] { "selezionata", "entita", "tipoAreaEvasione.tipoDocumento" }, EntitaEvasione.class);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 0;
	}

}
