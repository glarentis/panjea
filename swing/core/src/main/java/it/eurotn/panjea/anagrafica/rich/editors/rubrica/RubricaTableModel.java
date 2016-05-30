package it.eurotn.panjea.anagrafica.rich.editors.rubrica;

import it.eurotn.panjea.anagrafica.rich.table.renderer.RubricaDenominazioneCellRenderer;

import java.util.List;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TreeTableModel;

public class RubricaTableModel extends TreeTableModel<RubricaRow> {
	private static final long serialVersionUID = 929408622127667190L;

	/**
	 * Costruttore.
	 *
	 * @param righe
	 *            righe del tableModel
	 */
	public RubricaTableModel(final List<? extends RubricaRow> righe) {
		super(righe);
	}

	@Override
	public int getColumnCount() {
		return 16;
	}

	@Override
	public String getColumnName(int colonna) {
		switch (colonna) {
		case 0:
			return RcpSupport.getMessage("denominazione");
		case 1:
			return RcpSupport.getMessage("partiteIva");
		case 2:
			return RcpSupport.getMessage("indirizzo");
		case 3:
			return RcpSupport.getMessage("localita");
		case 4:
			return RcpSupport.getMessage("cap");
		case 5:
			return RcpSupport.getMessage("livelloAmministrativo4");
		case 6:
			return RcpSupport.getMessage("livelloAmministrativo3");
		case 7:
			return RcpSupport.getMessage("livelloAmministrativo2");
		case 8:
			return RcpSupport.getMessage("livelloAmministrativo1");
		case 9:
			return RcpSupport.getMessage("nazione");
		case 10:
			return RcpSupport.getMessage("email");
		case 11:
			return RcpSupport.getMessage("indirizzoPEC");
		case 12:
			return RcpSupport.getMessage("indirizzoMailSpedizione");
		case 13:
			return RcpSupport.getMessage("telefono");
		case 14:
			return RcpSupport.getMessage("cellulare");
		case 15:
			return RcpSupport.getMessage("fax");
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int col) {
		EditorContext context = RubricaDenominazioneCellRenderer.RUBRICA_CONTEXT;
		context.setUserObject(getRowAt(row).getRubricaDTO().getRowClass().getName());
		return context;
	}
}
