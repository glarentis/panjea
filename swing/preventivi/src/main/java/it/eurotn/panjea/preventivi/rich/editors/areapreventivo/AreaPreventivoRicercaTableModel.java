package it.eurotn.panjea.preventivi.rich.editors.areapreventivo;

import it.eurotn.panjea.preventivi.util.AreaPreventivoRicerca;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class AreaPreventivoRicercaTableModel extends DefaultBeanTableModel<AreaPreventivoRicerca> {
	private static final long serialVersionUID = -1L;
	private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

	static {
		TOTALE_CONTEXT.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public AreaPreventivoRicercaTableModel() {
		super(RisultatiRicercaAreaPreventivoTablePage.PAGE_ID, new String[] { "codice", "documento.dataDocumento",
				"documento.tipoDocumento", "documento.totale.importoInValutaAzienda", "entitaDocumento", "sedeEntita",
				"dataRegistrazione", "note", "statoAreaPreventivo" }, AreaPreventivoRicerca.class);
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
