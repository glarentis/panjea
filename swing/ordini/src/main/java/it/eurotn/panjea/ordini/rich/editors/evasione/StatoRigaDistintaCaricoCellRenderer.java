package it.eurotn.panjea.ordini.rich.editors.evasione;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico.StatoRiga;
import it.eurotn.rich.control.table.navigationloader.NavigatioLoaderContextSensitiveCellRenderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.EditorContext;

public class StatoRigaDistintaCaricoCellRenderer extends NavigatioLoaderContextSensitiveCellRenderer {

	public static final EditorContext STATO_RIGA_DISTINTA_CARICO_CONTEXT = new EditorContext(
			"STATO_RIGA_DISTINTA_CARICO_CONTEXT");

	private static final long serialVersionUID = 8489590159770782232L;
	public static final String KEY_ICON_SELEZIONABILE = "selezionabile.icon";
	public static final String KEY_ICON_AGGIUNTA = "aggiuntoCarrello.icon";
	public static final String KEY_ICON_SELEZIONATA = "daAggiungereCarrello.icon";
	private static final Icon ICON_SELEZIONABILE = RcpSupport.getIcon(KEY_ICON_SELEZIONABILE);
	private static final Icon ICON_SELEZIONATA = RcpSupport.getIcon(KEY_ICON_SELEZIONATA);
	private static final Icon ICON_AGGIUNTO_CARRELLO = RcpSupport.getIcon(KEY_ICON_AGGIUNTA);
	protected static final Icon[] ICONS = new Icon[] { ICON_SELEZIONABILE, ICON_SELEZIONATA, ICON_AGGIUNTO_CARRELLO };

	/**
	 * Costruttore.
	 * 
	 */
	public StatoRigaDistintaCaricoCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setText("");
		StatoRiga stato = (StatoRiga) value;
		label.setIcon(ICONS[stato.ordinal()]);
		return label;
	}
}
