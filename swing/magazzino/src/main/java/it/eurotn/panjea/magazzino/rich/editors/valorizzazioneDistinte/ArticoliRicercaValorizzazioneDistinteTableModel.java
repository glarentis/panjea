package it.eurotn.panjea.magazzino.rich.editors.valorizzazioneDistinte;

import it.eurotn.panjea.magazzino.rich.editors.articolo.ArticoliRicercaTablePage;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.CodiceArticoloCellRenderer;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

import com.jidesoft.grid.EditorContext;

public class ArticoliRicercaValorizzazioneDistinteTableModel extends DefaultBeanEditableTableModel<ArticoloRicerca> {

	private static final long serialVersionUID = -927849048413981476L;

	/**
	 * Costruttore.
	 */
	public ArticoliRicercaValorizzazioneDistinteTableModel() {
		super(ArticoliRicercaTablePage.PAGE_ID, new String[] { "codice", "descrizione" }, ArticoloRicerca.class);
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
	protected boolean isAllowInsert() {
		return false;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
