package it.eurotn.panjea.magazzino.rich.renderer;

import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

/**
 * Cell renderer per le classi sconto che allinea a destra il valore visualizzato.
 * 
 * @author
 * 
 */
public class ScontoContextSensitiveCellRenderer extends IconContextSensitiveCellRenderer {

	private static final long serialVersionUID = 670232616824233246L;

	/**
	 * Costruttore.
	 */
	public ScontoContextSensitiveCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// per non far vedere l'icona se lo sconto e vuoto
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (label.getText().isEmpty()) {
			label.setIcon(null);
		}
		setHorizontalTextPosition(SwingConstants.LEFT);
		return label;

	}

}
