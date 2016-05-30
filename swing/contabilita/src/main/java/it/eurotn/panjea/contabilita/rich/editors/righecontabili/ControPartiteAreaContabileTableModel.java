package it.eurotn.panjea.contabilita.rich.editors.righecontabili;

import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.NavigableTableModel;

public class ControPartiteAreaContabileTableModel extends DefaultBeanTableModel<ControPartita> implements
		NavigableTableModel {

	private static final long serialVersionUID = -2441740679381283428L;

	private final ConverterContext context;
	private static EditorContext numberPrezzoEditorContext;

	{
		context = new NumberWithDecimalConverterContext();

		numberPrezzoEditorContext = new EditorContext("DecimalNumberEditorContext", 2);
	}

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 *            id modello
	 */
	public ControPartiteAreaContabileTableModel(final String modelId) {
		super(modelId, new String[] { "descrizione", "importo", "note" }, ControPartita.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 1:
			context.setUserObject(2);
			return context;
		default:
			return super.getConverterContextAt(row, column);
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 1:
			return numberPrezzoEditorContext;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column > 0;
	}

	@Override
	public boolean isNavigableAt(int row, int column) {
		return column > 0;
	}

	@Override
	public boolean isNavigationOn() {
		return true;
	}
}
