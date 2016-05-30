package it.eurotn.panjea.ordini.rich.editors.evasione.carrello;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.rich.control.table.style.DefaultCellStyleProvider;

import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class QtaDaEvadereCellRenderer extends ContextSensitiveCellRenderer {

	public static final EditorContext QTA_DA_EVADERE_CELL_RENDERER_CONTEXT = new EditorContext(
			"QTA_DA_EVADERE_CELL_RENDERER_CONTEXT", 6);
	private static final long serialVersionUID = -4137497054372646800L;

	private DecimalFormat format = new DecimalFormat("#,##0.000000");

	/**
	 * Costruttore.
	 */
	public QtaDaEvadereCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setToolTipText(null);

		CarrelloEvasioneOrdiniTableModel tableModel = (CarrelloEvasioneOrdiniTableModel) TableModelWrapperUtils
				.getActualTableModel(table.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow == -1) {
			// Sono su un totale
			label.setIcon(null);
			label.setBackground(DefaultCellStyleProvider.SUMMARY_COLOR);
			return label;
		}

		RigaDistintaCarico rigaEvasione = tableModel.getObject(actualRow);

		if (rigaEvasione.getQtaDaEvadere() == null) {
			rigaEvasione.setQtaDaEvadere(0.0);
		}

		String text = "";
		if (rigaEvasione.getQtaDaEvadereSostituzione().compareTo(new Double(0.0)) != 0) {
			Double qtaDaEvadere = rigaEvasione.getQtaDaEvadere() + rigaEvasione.getQtaDaEvadereSostituzione();

			text = " (" + rigaEvasione.getQtaDaEvadere() + " + " + rigaEvasione.getQtaDaEvadereSostituzione() + ")  "
					+ format.format(qtaDaEvadere);
		} else {
			text = format.format(rigaEvasione.getQtaDaEvadere());
		}
		label.setText(text);

		return label;
	}
}
