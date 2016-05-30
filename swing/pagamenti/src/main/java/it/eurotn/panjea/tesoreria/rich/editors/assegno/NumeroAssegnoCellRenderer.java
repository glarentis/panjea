package it.eurotn.panjea.tesoreria.rich.editors.assegno;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;

public class NumeroAssegnoCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = 3089668258160343979L;

	private static final Icon ICON_ASSEGNO = RcpSupport.getIcon("assegno");

	public static final EditorContext NUMERO_ASSEGNO_CONTEXT = new EditorContext("NUMERO_ASSEGNO_CONTEXT");

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		label.setIcon(ICON_ASSEGNO);

		return label;
	}
}
