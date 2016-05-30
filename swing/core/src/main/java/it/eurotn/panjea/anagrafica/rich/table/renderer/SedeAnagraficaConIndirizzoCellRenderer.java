package it.eurotn.panjea.anagrafica.rich.table.renderer;

import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.jidesoft.grid.ContextSensitiveCellRenderer;

public class SedeAnagraficaConIndirizzoCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = 695292175637979723L;

	/**
	 * Costruttore.
	 */
	public SedeAnagraficaConIndirizzoCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.LEFT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value != null && value instanceof SedeAnagrafica) {
			SedeAnagrafica sedeAnagrafica = (SedeAnagrafica) value;

			StringBuilder sb = new StringBuilder();
			if (sedeAnagrafica.getIndirizzo() != null) {
				sb.append(sedeAnagrafica.getIndirizzo());
			}
			if (sedeAnagrafica.getDatiGeografici().getDescrizioneLocalita() != null
					&& !sedeAnagrafica.getDatiGeografici().getDescrizioneLocalita().isEmpty()) {
				if (sb.length() >= 0) {
					sb.append(" - ");
				}
				sb.append(sedeAnagrafica.getDatiGeografici().getDescrizioneLocalita());
			}

			label.setText(sb.toString());
		}
		return label;
	}
}
