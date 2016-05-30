package it.eurotn.panjea.magazzino.rich.control.table.renderer;

import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class StatoSpedizioneRenderer extends IconContextSensitiveCellRenderer {
	private static final long serialVersionUID = -5278686990215142270L;

	/**
	 * Costruttore.
	 */
	public StatoSpedizioneRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setToolTipText(label.getText());
		label.setText("");

		return label;
	}
}
