/**
 *
 */
package it.eurotn.panjea.preventivi.rich.editors.evasione;

import it.eurotn.panjea.preventivi.util.RigaEvasione;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

/**
 * @author fattazzo
 *
 */
public class EvasionePreventivoTableModel extends DefaultBeanEditableTableModel<RigaEvasione> {

	private static final long serialVersionUID = 6570573439548771807L;

	private static final ConverterContext NUMBER_QTA_CONTEXT = new NumberWithDecimalConverterContext();
	private static final ConverterContext NUMBER_PREZZO_CONTEXT = new NumberWithDecimalConverterContext();
	private static final ConverterContext NUMBER_PREZZO_TOT_CONTEXT = new NumberWithDecimalConverterContext(2);

	private static EditorContext qtaEditorContext;

	/**
	 * Costruttore.
	 */
	public EvasionePreventivoTableModel() {
		super("evasionePreventivoTableModel", new String[] { "selezionata", "rigaMovimentazione.articoloLite",
				"rigaMovimentazione.descrizioneRiga", "rigaMovimentazione.prezzoUnitario",
				"rigaMovimentazione.prezzoTotale", "quantitaRiga", "quantitaEvasione", "dataConsegna" },
				RigaEvasione.class);

		qtaEditorContext = new EditorContext("qtaEditorContext", 2);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		RigaEvasione rigaEvasione = getElementAt(row);
		if (rigaEvasione.getRigaMovimentazione() == null) {
			return null;
		}

		switch (column) {
		case 3:
			NUMBER_PREZZO_CONTEXT.setUserObject(rigaEvasione.getRigaMovimentazione().getNumeroDecimaliPrezzo());
			return NUMBER_PREZZO_CONTEXT;
		case 4:
			return NUMBER_PREZZO_TOT_CONTEXT;
		case 5:
		case 6:
			NUMBER_QTA_CONTEXT.setUserObject(rigaEvasione.getRigaMovimentazione().getNumeroDecimaliQuantita());
			return NUMBER_QTA_CONTEXT;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int col) {
		RigaEvasione rigaEvasione = getElementAt(row);
		if (rigaEvasione.getRigaMovimentazione() == null) {
			return null;
		}

		switch (col) {
		case 6:
			qtaEditorContext.setUserObject(rigaEvasione.getRigaMovimentazione().getNumeroDecimaliQuantita());
			return qtaEditorContext;
		default:
			break;
		}
		return super.getEditorContextAt(row, col);
	}

	@Override
	protected boolean isAllowInsert() {
		return false;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 6 || column == 7;
	}
}
