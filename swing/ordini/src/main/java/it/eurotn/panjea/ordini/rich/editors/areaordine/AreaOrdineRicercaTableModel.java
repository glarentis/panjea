package it.eurotn.panjea.ordini.rich.editors.areaordine;

import it.eurotn.panjea.ordini.util.AreaOrdineRicerca;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class AreaOrdineRicercaTableModel extends DefaultBeanTableModel<AreaOrdineRicerca> {
	private static final long serialVersionUID = -6971923108008422202L;
	private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

	{
		TOTALE_CONTEXT.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public AreaOrdineRicercaTableModel() {
		super(RisultatiRicercaAreaOrdineTablePage.PAGE_ID, new String[] { "documento.codice",
				"documento.dataDocumento", "documento.tipoDocumento", "documento.totale.importoInValutaAzienda",
				"entitaDocumento", "sedeEntita", "depositoOrigine", "dataRegistrazione", "evaso", "statoAreaOrdine",
				"note", "numeroOrdineCliente" }, AreaOrdineRicerca.class);
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
