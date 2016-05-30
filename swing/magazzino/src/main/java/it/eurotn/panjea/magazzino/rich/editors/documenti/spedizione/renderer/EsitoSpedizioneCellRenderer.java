package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.utils.HtmlUtils;

public class EsitoSpedizioneCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -5251914557805550664L;

	public static final EditorContext ESITO_SPEDIZIONE_CONTEXT = new EditorContext("ESITO_SPEDIZIONE_CONTEXT");
	public static final Icon ICON_EDIT = RcpSupport.getIcon("edit");

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		// label.setOpaque(false);

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow == -1) {
			return label;
		}

		label.setIcon(null);
		label.setHorizontalAlignment(SwingUtilities.LEFT);
		label.setToolTipText("<html>" + HtmlUtils.formatHtmlSubString(label.getText()) + "</html>");

		label.setForeground(null);
		if (StringUtils.contains((CharSequence) value, "Errore")) {
			label.setForeground(Color.RED);
		}

		if (!StringUtils.isBlank((CharSequence) value)) {
			label.setIcon(ICON_EDIT);
		}

		return label;
	}
}
