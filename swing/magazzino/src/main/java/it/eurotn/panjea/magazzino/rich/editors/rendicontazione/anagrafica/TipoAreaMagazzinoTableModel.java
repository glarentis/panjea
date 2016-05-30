package it.eurotn.panjea.magazzino.rich.editors.rendicontazione.anagrafica;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class TipoAreaMagazzinoTableModel extends DefaultBeanTableModel<TipoAreaMagazzino> {
	private static final long serialVersionUID = 794443943330052929L;

	/**
	 * Costruttore.
	 */
	public TipoAreaMagazzinoTableModel() {
		super("tipoAreaMagazzinoTableModelId", new String[] { "tipoDocumento.descrizione" }, TipoAreaMagazzino.class);
	}

}
