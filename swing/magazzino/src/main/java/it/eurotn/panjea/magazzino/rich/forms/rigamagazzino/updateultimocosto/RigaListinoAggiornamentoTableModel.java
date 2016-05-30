package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.updateultimocosto;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.NavigableModel;

public class RigaListinoAggiornamentoTableModel extends DefaultBeanTableModel<RigaListinoAggioramento> implements
		NavigableModel {
	private static final long serialVersionUID = -597458913344700341L;

	private static ConverterContext converterContext;
	private static EditorContext editorContext;

	static {
		converterContext = new NumberWithDecimalConverterContext();
		editorContext = new EditorContext("NumberWithDecimalEditorContext");
	}

	/**
	 * Costruttore.
	 */
	public RigaListinoAggiornamentoTableModel() {
		super(DialogAggiornamento.DIALOG_ID, new String[] { "rigaListino.versioneListino.listino", "aggiorna",
				"rigaListino.articolo", "prezzoListino", "prezzoDaAggiornare", "percVariazione", "importoVariazione" },
				RigaListinoAggioramento.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 3:
		case 4:
			RigaListinoAggioramento rigaListinoAggioramento = getElementAt(row);
			converterContext.setUserObject(rigaListinoAggioramento.getNumeroDecimali());
			return converterContext;
		case 5:
			converterContext.setUserObject(2);
			return converterContext;
		case 6:
			converterContext.setUserObject(6);
			return converterContext;
		default:
			return super.getConverterContextAt(row, column);
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 3:
		case 4:
			RigaListinoAggioramento rigaListinoAggioramento = getElementAt(row);
			editorContext.setUserObject(rigaListinoAggioramento.getNumeroDecimali());
			return editorContext;
		case 5:
			editorContext.setUserObject(2);
			return editorContext;
		case 6:
			editorContext.setUserObject(6);
			return editorContext;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		switch (column) {
		case 1:
		case 4:
		case 5:
		case 6:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean isNavigableAt(int row, int column) {
		switch (column) {
		case 2:
		case 4:
		case 5:
		case 6:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean isNavigationOn() {
		return true;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		super.setValueAt(value, row, col);
		fireTableRowsUpdated(row, col);
	}
}
