/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.settings;

import it.eurotn.panjea.magazzino.domain.AddebitoDichiarazioneIntentoSettings;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

/**
 * @author leonardo
 */
public class AddebitoDichiarazioneIntentoSettingsTableModel extends
		DefaultBeanEditableTableModel<AddebitoDichiarazioneIntentoSettings> {

	private static final SearchContext ARTICOLO_EDITOR_CONTEXT = new SearchContext("codice");
	private static final SearchContext ARTICOLO_DESCRIZIONE_EDITOR_CONTEXT = new SearchContext("descrizione");
	private static final long serialVersionUID = -76819087487345144L;

	private static ConverterContext numberPrezzoContext = new NumberWithDecimalConverterContext(6);
	private static EditorContext numberPrezzoEditorContext = new EditorContext("DecimalNumberEditorContext", 6);

	static {
		ARTICOLO_EDITOR_CONTEXT.setSearchObjectClassKey(ArticoloLite.class);
		ARTICOLO_DESCRIZIONE_EDITOR_CONTEXT.setSearchObjectClassKey(ArticoloLite.class);
	}

	/**
	 * Costruttore.
	 */
	public AddebitoDichiarazioneIntentoSettingsTableModel() {
		super("addebitoDichiarazioneIntentoSettingsTableModel", new String[] { "articolo", "dataVigore", "prezzo" },
				AddebitoDichiarazioneIntentoSettings.class);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		if (column == 2) {
			return numberPrezzoContext;
		}
		return super.getConverterContextAt(row, column);
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 0:
			return ARTICOLO_EDITOR_CONTEXT;
		case 2:
			return numberPrezzoEditorContext;
		default:
			break;
		}
		return super.getEditorContextAt(row, column);
	}

}
