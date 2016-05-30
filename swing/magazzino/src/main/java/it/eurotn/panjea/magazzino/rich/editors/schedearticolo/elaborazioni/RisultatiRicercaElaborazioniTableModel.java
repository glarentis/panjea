/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.schedearticolo.elaborazioni;

import it.eurotn.panjea.magazzino.util.ElaborazioneSchedaArticoloDTO;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import com.jidesoft.grid.EditorContext;

/**
 * @author fattazzo
 * 
 */
public class RisultatiRicercaElaborazioniTableModel extends DefaultBeanTableModel<ElaborazioneSchedaArticoloDTO> {

	private static final long serialVersionUID = 6233917680511441468L;

	/**
	 * Costruttore.
	 */
	public RisultatiRicercaElaborazioniTableModel() {
		super("RisultatiRicercaElaborazioniTableModel", new String[] { "nota", "articolo", "stato" },
				ElaborazioneSchedaArticoloDTO.class);
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 0:
			return ElaborazioneSchedeArticoloCellRenderer.ELABORAZIONE_SCHEDE_ARTICOLO_CONTEXT;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

}
