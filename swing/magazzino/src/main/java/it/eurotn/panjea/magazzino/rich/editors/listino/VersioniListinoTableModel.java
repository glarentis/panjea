package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.grid.EditorContext;

public class VersioniListinoTableModel extends DefaultBeanTableModel<VersioneListino> {

	private static final long serialVersionUID = 4898889981266457435L;
	private VersioneListino versioneListinoInVigore = null;

	/**
	 * Costruttore.
	 */
	public VersioniListinoTableModel() {
		super(VersioniListinoTablePage.PAGE_ID, new String[] { "codice", "dataVigore" }, VersioneListino.class);
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 1:
			VersioneListinoDataCellRenderer.VERSIONE_LISTINO_DATA_MAGAZZINO_CONTEXT
					.setUserObject(versioneListinoInVigore);
			return VersioneListinoDataCellRenderer.VERSIONE_LISTINO_DATA_MAGAZZINO_CONTEXT;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	/**
	 * @return the versioneListinoInVigore
	 */
	public VersioneListino getVersioneListinoInVigore() {
		return versioneListinoInVigore;
	}

	/**
	 * @param versioneListinoInVigore
	 *            the versioneListinoInVigore to set
	 */
	public void setVersioneListinoInVigore(VersioneListino versioneListinoInVigore) {
		this.versioneListinoInVigore = versioneListinoInVigore;
	}

}
