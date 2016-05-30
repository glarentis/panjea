/**
 *
 */
package it.eurotn.panjea.anagrafica.mail;

import it.eurotn.panjea.anagrafica.domain.Destinatario;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.EditorContext;

/**
 * @author leonardo
 * 
 */
public class ListaDestinatariEditableTableModel extends DefaultBeanEditableTableModel<Destinatario> {

	private static final long serialVersionUID = -3978412655122538551L;

	private static final SearchContext ENTITA_CODICE_EDITOR_CONTEXT = new SearchContext("codice", "entita");
	private static final SearchContext ENTITA_DENOMINAZIONE_EDITOR_CONTEXT = new SearchContext(
			"anagrafica.denominazione", "entita");

	private PanjeaMailClient panjeaMailClient;

	/**
	 * Costruttore.
	 */
	public ListaDestinatariEditableTableModel() {
		super("listaDestinatariEditableTableModel", new String[] { "email", "entita.codice",
				"entita.anagrafica.denominazione", "nome" }, Destinatario.class);
		this.panjeaMailClient = RcpSupport.getBean(PanjeaMailClient.BEAN_ID);
	}

	/**
	 * Verifica se le 2 entita sono diverse.
	 * 
	 * @param oldEntita
	 *            oldEntita
	 * @param newEntita
	 *            newEntita
	 * @return <code>true</code> se diverse
	 */
	private boolean entitaChanged(EntitaLite oldEntita, EntitaLite newEntita) {
		if (oldEntita != null) {
			return !oldEntita.equals(newEntita);
		} else {
			return newEntita != null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int col) {
		switch (col) {
		case 1:
			return ENTITA_CODICE_EDITOR_CONTEXT;
		case 2:
			return ENTITA_DENOMINAZIONE_EDITOR_CONTEXT;
		default:
			break;
		}
		return super.getEditorContextAt(row, col);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column < 3;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {

		// solo se è cambiata l'entità vado a caricare la sua email.
		boolean entitaChanged = false;

		if (column == 1 || column == 2) {
			EntitaLite oldEntita = getElementAt(row).getEntita();
			entitaChanged = entitaChanged(oldEntita, (EntitaLite) value);
		}

		super.setValueAt(value, row, column);

		if (entitaChanged) {
			String email = value != null ? panjeaMailClient.getEMailValida(((EntitaLite) value).creaProxyEntita()) : "";
			super.setValueAt(email, row, 0);
		}
	}

}
