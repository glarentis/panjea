package it.eurotn.panjea.magazzino.rich.editors.fatturazione.consultazione;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;

public class NumeroDocumentoNonAssegnatoCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -6526605820703910193L;

	public static final EditorContext NUMERO_DOC_NON_ASSEGNATO_CONTEXT = new EditorContext(
			"NUMERO_DOC_NON_ASSEGNATO_CONTEXT");

	/**
	 * Costruttore.
	 * 
	 */
	public NumeroDocumentoNonAssegnatoCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setText("");

		if (((Integer) value) > 0) {
			label.setText(value.toString());
		}

		return label;
	}

}
