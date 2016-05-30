package it.eurotn.panjea.conai.rich.editor.conaiarticoli;

import it.eurotn.panjea.conai.domain.ConaiListino;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RigaConaiListinoTableModel extends DefaultBeanTableModel<ConaiListino> {

	private static final long serialVersionUID = 4857044749882085659L;
	private static ConverterContext context;

	static {
		context = new NumberWithDecimalConverterContext();
		context.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public RigaConaiListinoTableModel() {
		super("conaiListino", new String[] { "dataInizio", "dataFine", "prezzo" }, ConaiListino.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		if (column == 2) {
			return context;
		}
		return null;
	}

}
