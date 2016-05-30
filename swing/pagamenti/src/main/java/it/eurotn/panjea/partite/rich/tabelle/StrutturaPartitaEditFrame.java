package it.eurotn.panjea.partite.rich.tabelle;

import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.domain.StrutturaPartitaLite;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

public class StrutturaPartitaEditFrame extends EditFrame<StrutturaPartitaLite> {

	private static final long serialVersionUID = 6209304295848835850L;

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
	public StrutturaPartitaEditFrame(final EEditPageMode editView,
			final AbstractTablePageEditor<StrutturaPartitaLite> pageEditor, final String startQuickAction) {
		super(editView, pageEditor, startQuickAction);
	}

	@Override
	public StrutturaPartitaLite getTableManagedObject(Object object) {

		StrutturaPartitaLite strutturaPartitaLite = new StrutturaPartitaLite();

		if (object instanceof StrutturaPartitaLite) {
			strutturaPartitaLite = (StrutturaPartitaLite) object;
		} else {
			if (object instanceof StrutturaPartita) {
				if (((StrutturaPartita) object).getId() != null) {
					strutturaPartitaLite = ((StrutturaPartita) object).getStrutturaPartitaLite();
					// se la riga è = a null vuol dire che l'ho cancellata
					// quindi ne creo una solamente
					// con l'id settato per fare in modo che la tabella si
					// cancelli la riga
					if (strutturaPartitaLite == null) {
						strutturaPartitaLite = new StrutturaPartitaLite();
						strutturaPartitaLite.setId(((StrutturaPartita) object).getId());
					}
				} else {
					// caso in cui mi ritrovo una riga articolo nuova
					strutturaPartitaLite = new StrutturaPartitaLite();
				}
			}
		}
		return strutturaPartitaLite;
	}
}
