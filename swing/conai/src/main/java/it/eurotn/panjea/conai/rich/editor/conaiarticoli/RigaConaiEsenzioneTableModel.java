package it.eurotn.panjea.conai.rich.editor.conaiarticoli;

import it.eurotn.panjea.conai.domain.ConaiEsenzione;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;

public class RigaConaiEsenzioneTableModel extends DefaultBeanTableModel<ConaiEsenzione> {

	private static final long serialVersionUID = 4857044749882085659L;
	private static ConverterContext context;

	static {
		context = new NumberWithDecimalConverterContext();
		context.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public RigaConaiEsenzioneTableModel() {
		super("conaiEsenzione", new String[] { "entita", "percentualeEsenzione" }, ConaiEsenzione.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		if (column == 1) {
			return context;
		}
		return null;
	}

}
