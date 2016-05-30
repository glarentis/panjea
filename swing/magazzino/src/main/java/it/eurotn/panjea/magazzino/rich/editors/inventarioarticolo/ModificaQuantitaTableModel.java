package it.eurotn.panjea.magazzino.rich.editors.inventarioarticolo;

import it.eurotn.panjea.magazzino.domain.RigaInventarioArticolo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class ModificaQuantitaTableModel extends DefaultBeanTableModel<RigaInventarioArticolo> {

	private static final long serialVersionUID = -3037587072942899699L;

	private static ConverterContext dateContext = new ConverterContext("dataContext", "dd/MM/yyyy HH:mm");
	private static ConverterContext numberContext;

	static {
		numberContext = new NumberWithDecimalConverterContext();
		numberContext.setUserObject(new Integer(6));
	}

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 * @param columnPropertyNames
	 * @param classe
	 */
	public ModificaQuantitaTableModel() {
		super("ModificaQuantitaTableModel", new String[] { "userInsert", "data", "quantita" },
				RigaInventarioArticolo.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 1:
			return dateContext;
		case 2:
			return numberContext;
		default:
			return null;
		}
	}

}
