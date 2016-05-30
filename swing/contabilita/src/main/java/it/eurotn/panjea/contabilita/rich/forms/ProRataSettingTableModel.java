package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.contabilita.domain.ProRataSetting;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class ProRataSettingTableModel extends DefaultBeanEditableTableModel<ProRataSetting> {
	private static final long serialVersionUID = 2980868237507152529L;

	private static ConverterContext percContext = new NumberWithDecimalConverterContext(2);

	private static EditorContext percEditorContext = new EditorContext("DecimalNumberEditorContext", 2);

	/**
	 * Costruttore.
	 */
	public ProRataSettingTableModel() {
		super("proRataSettingTableModel", new String[] { "anno", "percentuale", "registroIva" }, ProRataSetting.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		if (column == 1) {
			return percContext;
		}
		return super.getConverterContextAt(row, column);
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 1:
			return percEditorContext;
		case 2:
			return new SearchContext("descrizione");
		default:
			return super.getEditorContextAt(row, column);
		}
	}
}
