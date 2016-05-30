package it.eurotn.panjea.tesoreria.rich.editors.acconto;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class UtilizzoAccontoTableModel extends DefaultBeanTableModel<SituazioneRataUtilizzoAcconto> {

	private static final long serialVersionUID = 1529200325432863792L;

	private static ConverterContext numberPrezzoContext;
	private static EditorContext numberPrezzoEditorContext;

	static {
		numberPrezzoContext = new NumberWithDecimalConverterContext();
		numberPrezzoContext.setUserObject(new Integer(2));

		numberPrezzoEditorContext = new EditorContext("DecimalNumberEditorContext", 2);
	}

	/**
	 * Costruttore.
	 * 
	 * @param modelId
	 *            modelid
	 */
	public UtilizzoAccontoTableModel(final String modelId) {
		super(modelId, new String[] { "dataScadenza", "numeroDocumento", "tipoDocumento", "entita", "importo",
				"importoAcconto", "importoResiduo" }, SituazioneRataUtilizzoAcconto.class);

	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 4:
		case 5:
		case 6:
			return numberPrezzoContext;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 5:
			return numberPrezzoEditorContext;
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		switch (column) {
		case 5:
			return true;
		default:
			return false;
		}
	}
}
