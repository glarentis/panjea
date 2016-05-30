package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.util.RigaPreventivoDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;

public class RighePreventivoEditFrame extends RigheDocumentoEditFrame<RigaPreventivo, RigaPreventivoDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5406863365192991077L;

	/**
	 * Costruttore.
	 * 
	 * @param editView
	 *            editView
	 * @param pageEditor
	 *            pageEditor
	 * @param startQuickAction
	 *            quick action
	 */
	public RighePreventivoEditFrame(final EEditPageMode editView,
			final AbstractTablePageEditor<RigaPreventivoDTO> pageEditor, final String startQuickAction) {
		super(editView, pageEditor, startQuickAction);
	}

}
