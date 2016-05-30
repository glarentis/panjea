/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.noteautomatiche;

import it.eurotn.panjea.anagrafica.domain.NotaAutomatica;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

/**
 * @author leonardo
 */
public class ArticoliNotaAutomaticaTableModel extends DefaultBeanEditableTableModel<ArticoloLite> {

	private static final long serialVersionUID = 8599815579985743887L;

	/**
	 * Costruttore.
	 * 
	 * @param notaAutomatica
	 *            notaAutomatica
	 */
	public ArticoliNotaAutomaticaTableModel(final NotaAutomatica notaAutomatica) {
		super("articoli", new String[] { "codice", "descrizione" }, ArticoloLite.class);
	}

	@Override
	protected boolean isAllowInsert() {
		return false;
	}

	@Override
	protected boolean isAllowRemove() {
		return true;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
