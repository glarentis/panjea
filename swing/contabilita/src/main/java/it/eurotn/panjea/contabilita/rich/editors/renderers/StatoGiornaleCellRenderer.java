package it.eurotn.panjea.contabilita.rich.editors.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;

public class StatoGiornaleCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = 7945535194469848777L;

	public static final String KEY_ICON_NON_SELEZIONABILE = "nonSelezionabile.icon";
	public static final String KEY_ICON_SELEZIONABILE = "selezionabile.icon";
	public static final String KEY_ICON_AGGIUNTA = "aggiuntoCarrello.icon";
	public static final String KEY_ICON_DA_AGGIUNGERE = "daAggiungereCarrello.icon";

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setToolTipText(null);
		label.setText(null);
		if (value != null) {
			label.setIcon(RcpSupport.getIcon(value.getClass().getName() + "#" + ((Enum<?>) value).name()));
			label.setText(RcpSupport.getMessage(value.getClass().getName() + "#" + ((Enum<?>) value).name()));
		}
		return label;
	}
}
