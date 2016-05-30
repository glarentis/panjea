package it.eurotn.panjea.anagrafica.rich.tabelle.cambiovalute;

import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class ValuteTableModel extends DefaultBeanTableModel<CambioValuta> {
	private static final long serialVersionUID = 1L;
	private static ConverterContext context = new NumberWithDecimalConverterContext();

	/**
	 * Costruttore.
	 */
	public ValuteTableModel() {
		super("valuteTablePage", new String[] { "valuta.codiceValuta", "valuta.simbolo", "valuta.numeroDecimali",
				"data", "tasso" }, CambioValuta.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		if (column == 4) {
			context.setUserObject(getElementAt(row).getValuta().getNumeroDecimali());
			return context;
		}
		return super.getConverterContextAt(row, column);
	}

}
