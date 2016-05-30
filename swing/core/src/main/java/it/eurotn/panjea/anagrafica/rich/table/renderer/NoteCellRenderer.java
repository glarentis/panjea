package it.eurotn.panjea.anagrafica.rich.table.renderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;

public class NoteCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -8858256895081477066L;

	public static final EditorContext NOTE_CONTEXT = new EditorContext("NOTE_CONTEXT");
	public static final String VISUALIZZA_NOTA_INLINE = "visualizzaNota";

	private static final String NOTE_ICON = "note.icon";
	private final Icon noteIcon = RcpSupport.getIcon(NOTE_ICON);

	/**
	 * Costruttore.
	 */
	public NoteCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		String visualizzaNotaInline = (String) super.getEditorContext().getUserObject();
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		String valueString = (String) value;
		label.setIcon(null);
		label.setToolTipText(null);
		if (valueString != null && !valueString.isEmpty()) {
			label.setIcon(noteIcon);
			label.setToolTipText("<HTML>" + label.getText().replaceAll(",", "<BR>") + "</HTML>");
		}
		if (!VISUALIZZA_NOTA_INLINE.equals(visualizzaNotaInline)) {
			label.setText(null);
		}

		setHorizontalAlignment(SwingConstants.LEFT);

		return label;
	}

}
