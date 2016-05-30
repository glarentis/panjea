package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import it.eurotn.panjea.contabilita.domain.RigaCentroCosto;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RigheCentroCostoTableModel extends DefaultBeanTableModel<RigaCentroCosto> {

	private static final long serialVersionUID = 7223754450132263896L;
	private static ConverterContext context;

	static {
		context = new NumberWithDecimalConverterContext();
		context.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public RigheCentroCostoTableModel() {
		super("righeCentrocosto", new String[] { "centroCosto", "importo", "nota" }, RigaCentroCosto.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		if (column == 1) {
			return context;
		}
		return null;
	}
}
