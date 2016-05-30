package it.eurotn.panjea.tesoreria.rich.editors.acconto;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.tesoreria.domain.AreaAcconto;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RisultatiRicercaAreaAccontoTableModel extends DefaultBeanTableModel<AreaAcconto> {

	private static final long serialVersionUID = 8135422122354911867L;

	private static final ConverterContext NUMBERPREZZOCONTEXT = new NumberWithDecimalConverterContext();

	/**
	 * Costruttore.
	 * 
	 * @param pageId
	 *            page id
	 */
	public RisultatiRicercaAreaAccontoTableModel(final String pageId) {
		super(pageId, new String[] { "documento.dataDocumento", "documento.codice", "documento.entitaDocumento",
				"documento.totale", "importoUtilizzato", "residuo", "documento.totale.codiceValuta", "automatico",
				"note" }, AreaAcconto.class);

		NUMBERPREZZOCONTEXT.setUserObject(new Integer(2));
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 4:
		case 5:
			return NUMBERPREZZOCONTEXT;
		default:
			return null;
		}
	}

}
