package it.eurotn.panjea.conai.rich.editor.articolo;

import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class ArticoliConaiComponenteTableModel extends DefaultBeanTableModel<ConaiComponente> {

	private static final long serialVersionUID = 7261814706748210631L;
	private static ConverterContext context = new NumberWithDecimalConverterContext(6);

	/**
	 * Costruttore.
	 */
	public ArticoliConaiComponenteTableModel() {
		super("articoliConaiComponenteTableModel", new String[] { "materiale", "tipoImballo", "pesoUnitario" },
				ConaiComponente.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		if (column == 2) {
			return context;
		}
		return super.getConverterContextAt(row, column);
	}
}
