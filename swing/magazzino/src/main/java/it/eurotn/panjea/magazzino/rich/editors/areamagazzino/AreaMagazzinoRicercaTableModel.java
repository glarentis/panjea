package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class AreaMagazzinoRicercaTableModel extends DefaultBeanTableModel<AreaMagazzinoRicerca> {

	private static final long serialVersionUID = -6971923108008422202L;
	private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

	{
		TOTALE_CONTEXT.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public AreaMagazzinoRicercaTableModel() {
		super(RisultatiRicercaAreaMagazzinoTablePage.PAGE_ID, new String[] { "codice", "dataDocumento",
				"documento.tipoDocumento", "totale", "entitaDocumento", "depositoOrigine", "depositoDestinazione",
				"dataRegistrazione", "stato", "note", "sedeEntita" }, AreaMagazzinoRicerca.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		switch (j) {
		case 3:
			return TOTALE_CONTEXT;
		default:
			return null;
		}
	}
}
