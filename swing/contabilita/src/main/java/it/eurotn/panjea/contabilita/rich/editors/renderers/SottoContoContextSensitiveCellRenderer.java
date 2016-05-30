package it.eurotn.panjea.contabilita.rich.editors.renderers;

import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * Cell renderer per le classi sottoconto che allinea a destra il valore visualizzato.
 * 
 * @author fattazzo
 * 
 */
public class SottoContoContextSensitiveCellRenderer extends IconContextSensitiveCellRenderer {

	private static final long serialVersionUID = 670232616824233246L;

	/**
	 * Costruttore.
	 */
	public SottoContoContextSensitiveCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.LEFT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// per non far vedere l'icona se il sottoconto e vuoto
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (label.getText().isEmpty()) {
			label.setIcon(null);
		}

		return label;
	}

}
