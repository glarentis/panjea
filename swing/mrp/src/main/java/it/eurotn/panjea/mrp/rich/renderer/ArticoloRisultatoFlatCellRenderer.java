package it.eurotn.panjea.mrp.rich.renderer;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.rich.factory.table.CustomArticoloRenderer;
import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.mrp.rich.editors.risultato.RisultatoTableModel;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class ArticoloRisultatoFlatCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -8421079767779397558L;

	public static final EditorContext ARTICOLO_RISULTATO_FLAT_CONTEXT = new EditorContext(
			"ARTICOLO_RISULTATO_FLAT_CONTEXT");

	/**
	 * Costruttore.
	 *
	 */
	public ArticoloRisultatoFlatCellRenderer() {
		super();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		RisultatoTableModel tableModel = (RisultatoTableModel) TableModelWrapperUtils.getActualTableModel(table
				.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow == -1) {
			return label;
		}

		RisultatoMrpFlat rigaRisultato = tableModel.getObject(actualRow);

		if (!rigaRisultato.getArticolo().isDistinta()) {
			label.setIcon(RcpSupport.getIcon(Articolo.class.getName()));
		} else {
			if (rigaRisultato.getDistinta().equals(rigaRisultato.getArticolo())) {
				label.setIcon(RcpSupport.getIcon("distintaMrp.icon"));
			} else {
				label.setIcon(RcpSupport.getIcon(CustomArticoloRenderer.KEY_ICON_ARTICOLO_DISTINTA));
			}
		}
		return label;
	}
}
