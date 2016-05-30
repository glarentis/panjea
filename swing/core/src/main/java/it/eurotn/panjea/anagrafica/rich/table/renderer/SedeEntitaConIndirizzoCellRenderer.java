package it.eurotn.panjea.anagrafica.rich.table.renderer;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;

public class SedeEntitaConIndirizzoCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = 695292175637979723L;

	public static final EditorContext SEDE_ENTITA_CON_INDIRIZZO_CONTEXT = new EditorContext(
			"SEDE_ENTITA_CON_INDIRIZZO_CONTEXT");

	/**
	 * Costruttore.
	 */
	public SedeEntitaConIndirizzoCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.LEFT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value != null && value instanceof SedeEntita) {
			SedeEntita sedeEntita = (SedeEntita) value;

			StringBuilder sb = new StringBuilder(label.getText());
			if (sedeEntita.getSede().getIndirizzo() != null) {
				sb.append(" - " + sedeEntita.getSede().getIndirizzo());
			}
			if (sedeEntita.getSede().getDatiGeografici().getDescrizioneLocalita() != null
					&& !sedeEntita.getSede().getDatiGeografici().getDescrizioneLocalita().isEmpty()) {
				sb.append(" - " + sedeEntita.getSede().getDatiGeografici().getDescrizioneLocalita());
			}

			label.setText(sb.toString());
		}
		return label;
	}
}
