package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.tesoreria.util.SituazioneRata;
import it.eurotn.rich.control.table.style.DefaultCellStyleProvider;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.TableModelWrapperUtils;

public class StatoCarrelloCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -6925140977241451775L;

	public static final String KEY_ICON_NON_SELEZIONABILE = "nonSelezionabile.icon";

	public static final String KEY_ICON_SELEZIONABILE = "selezionabile.icon";
	public static final String KEY_ICON_AGGIUNTA = "aggiuntoCarrello.icon";
	public static final String KEY_ICON_DA_AGGIUNGERE = "daAggiungereCarrello.icon";
	private static final Icon ICON_SELEZIONABILE = RcpSupport.getIcon(KEY_ICON_SELEZIONABILE);
	private static final Icon ICON_AGGIUNTO_CARRELLO = RcpSupport.getIcon(KEY_ICON_AGGIUNTA);
	private static final Icon ICON_NON_SELEZIONABILE = RcpSupport.getIcon(KEY_ICON_NON_SELEZIONABILE);
	private static final Icon ICON_DA_AGGIUNGERE = RcpSupport.getIcon(KEY_ICON_DA_AGGIUNGERE);
	private static final Icon[] ICONS = new Icon[] { ICON_SELEZIONABILE, ICON_AGGIUNTO_CARRELLO,
			ICON_NON_SELEZIONABILE, ICON_DA_AGGIUNGERE };

	public static final String KEY_TOOLTIP_NON_SELEZIONABILE_CHIUSA = "nonSelezionabile.chiusa.tooltip";

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setToolTipText(null);

		// Recupero lo stato della rata
		RisultatiRicercaRateTableModel tableModel = (RisultatiRicercaRateTableModel) TableModelWrapperUtils
				.getActualTableModel(table.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);

		if (actualRow == -1) {
			// Sono su un totale
			label.setIcon(null);
			label.setBackground(DefaultCellStyleProvider.SUMMARY_COLOR);
			return label;
		}
		SituazioneRata situazioneRata = tableModel.getObject(actualRow);

		switch (situazioneRata.getStatoCarrello()) {
		case SELEZIONABILE:
			label.setIcon(ICONS[situazioneRata.getStatoCarrelloFromStatoRata().ordinal()]);
			break;
		default:
			label.setIcon(ICONS[situazioneRata.getStatoCarrello().ordinal()]);
		}

		label.setText(RcpSupport.getMessage(situazioneRata.getStatoRata().getClass().getName() + "."
				+ situazioneRata.getStatoRata().name()));

		return label;

	}
}
