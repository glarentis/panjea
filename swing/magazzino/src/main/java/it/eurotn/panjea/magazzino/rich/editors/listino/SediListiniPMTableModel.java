package it.eurotn.panjea.magazzino.rich.editors.listino;

import it.eurotn.panjea.anagrafica.rich.table.renderer.SedeEntitaConIndirizzoCellRenderer;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.grid.EditorContext;

public class SediListiniPMTableModel extends DefaultBeanTableModel<SedeListiniPM> {

	private static final long serialVersionUID = -6494173516693246491L;

	/**
	 * Costruttore.
	 * 
	 */
	public SediListiniPMTableModel() {
		super("SediListiniPMTableModel", new String[] { "sedeRiepilogo.entita", "sedeRiepilogo.sedeEntita",
				"listinoAssociato", "listinoAlternativoAssociato" }, SedeListiniPM.class);
	}

	@Override
	public EditorContext getEditorContextAt(int i, int j) {

		switch (j) {
		case 1:
			return SedeEntitaConIndirizzoCellRenderer.SEDE_ENTITA_CON_INDIRIZZO_CONTEXT;
		default:
			return super.getEditorContextAt(i, j);
		}
	}
}
