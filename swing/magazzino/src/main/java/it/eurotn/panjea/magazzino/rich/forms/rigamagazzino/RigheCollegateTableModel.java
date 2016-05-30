package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RigheCollegateTableModel extends DefaultBeanTableModel<RigaDestinazione> {

	private static final long serialVersionUID = 711422498164686904L;

	private static ConverterContext context = new NumberWithDecimalConverterContext();

	/**
	 * Costruttore.
	 * 
	 */
	public RigheCollegateTableModel() {
		super("righeCollegateTableModelId", new String[] { "documento.codice", "documento.dataDocumento",
				"documento.tipoDocumento", "articolo", "quantita" }, RigaDestinazione.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 4:
			RigaDestinazione rigaDestinazione = getElementAt(row);
			context.setUserObject(rigaDestinazione.getArticolo().getNumeroDecimaliQta());
			return context;
		default:
			return super.getConverterContextAt(row, column);
		}
	}

}
