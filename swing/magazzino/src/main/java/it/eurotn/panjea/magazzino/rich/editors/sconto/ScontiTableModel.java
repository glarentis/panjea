package it.eurotn.panjea.magazzino.rich.editors.sconto;

import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

@SuppressWarnings("serial")
public class ScontiTableModel extends DefaultBeanTableModel<Sconto> {
	private static ConverterContext decimalContext;

	static {
		decimalContext = new NumberWithDecimalConverterContext();
		decimalContext.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public ScontiTableModel() {
		super("scontiTableModel", new String[] { "codice", "descrizione", "sconto1", "sconto2", "sconto3", "sconto4" },
				Sconto.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			return decimalContext;
		default:
			return super.getConverterContextAt(row, column);
		}
	}
}
