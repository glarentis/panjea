package it.eurotn.panjea.tesoreria.rich.editors.ricercaareetesoreria;

import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class RisultatiRicercaAreeTesoreriaTableModel extends DefaultBeanTableModel<AreaTesoreria> {

	private static final long serialVersionUID = -158883790848779933L;

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 *            id del modello
	 */
	public RisultatiRicercaAreeTesoreriaTableModel(final String modelId) {
		super(modelId, new String[] { "documento.dataDocumento", "tipoAreaPartita.tipoDocumento", "documento.codice",
				"entitaDocumento", "documento.totale", "documento.totale.codiceValuta" }, AreaTesoreria.class);
	}

}
