package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.CodiceArticoloCellRenderer;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.rich.factory.navigationloader.NavigationLoaderContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.grid.EditorContext;

public class ArticoliRicercaTableModel extends DefaultBeanTableModel<ArticoloRicerca> {

	private static final long serialVersionUID = -927849048413981476L;
	public static final NavigationLoaderContext ARTICOLO_RICERCA_NAVIGATION_CONTEXT = new NavigationLoaderContext(
			"ARTICOLO_RICERCA_NAVIGATION_CONTEXT");

	/**
	 * Costruttore.
	 */
	public ArticoliRicercaTableModel() {
		super(ArticoliRicercaTablePage.PAGE_ID, new String[] { "codice", "descrizione", "abilitato", "barCode",
		"giacenza" }, ArticoloRicerca.class);
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 0:
			return CodiceArticoloCellRenderer.CODICE_ARTICOLO_CONTEXT;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	@Override
	public NavigationLoaderContext[] getNavigationLoadersContextAt(int row, int column) {
		if (column == 0) {
			return new NavigationLoaderContext[] { ARTICOLO_RICERCA_NAVIGATION_CONTEXT };
		}
		return super.getNavigationLoadersContextAt(row, column);
	}
}
