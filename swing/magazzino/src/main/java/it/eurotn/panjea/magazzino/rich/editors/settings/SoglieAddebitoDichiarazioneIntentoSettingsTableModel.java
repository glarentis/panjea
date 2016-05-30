/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.settings;

import it.eurotn.panjea.magazzino.domain.SogliaAddebitoDichiarazioneSettings;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

/**
 * @author leonardo
 */
public class SoglieAddebitoDichiarazioneIntentoSettingsTableModel extends
		DefaultBeanEditableTableModel<SogliaAddebitoDichiarazioneSettings> {

	private static final long serialVersionUID = -76819087487345144L;

	private static ConverterContext numberPrezzoContext = new NumberWithDecimalConverterContext(2);
	private static EditorContext numberPrezzoEditorContext = new EditorContext("DecimalNumberEditorContext", 2);

	/**
	 * Costruttore.
	 */
	public SoglieAddebitoDichiarazioneIntentoSettingsTableModel() {
		super("soglieAddebitoDichiarazioneIntentoSettingsTableModel", new String[] { "prezzo", "dataVigore" },
				SogliaAddebitoDichiarazioneSettings.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		if (column == 0) {
			return numberPrezzoContext;
		}
		return super.getConverterContextAt(row, column);
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 0:
			return numberPrezzoEditorContext;
		default:
			break;
		}
		return super.getEditorContextAt(row, column);
	}

}
