package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti;

import it.eurotn.panjea.tesoreria.domain.Effetto.StatoEffetto;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.style.DefaultCellStyleProvider;

import java.awt.Component;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.pivot.PivotConstants;
import com.jidesoft.pivot.PivotField;

public class StatoEffettoCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = 7945535194469848777L;

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

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setToolTipText(null);

		List<Integer> righe = ((AggregateTable) table).getAggregateTableModel().getPivotDataModel()
				.getDataAt(row, column);

		DefaultBeanTableModel<?> tableModel = (DefaultBeanTableModel<?>) TableModelWrapperUtils
				.getActualTableModel(table.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow == -1) {

			PivotField field = ((AggregateTable) table).getAggregateTableModel().getPivotDataModel().getField(4);

			if (righe != null
					&& field != null
					&& righe.size() > 0
					&& (field.getSubtotalType() == PivotConstants.SUBTOTAL_NONE || field.getGrandTotalSummaryType() == PivotConstants.SUMMARY_NONE)) {
				actualRow = righe.get(0);
			} else {
				// Sono su un totale
				label.setIcon(null);
				label.setBackground(DefaultCellStyleProvider.SUMMARY_COLOR);
				return label;
			}
		}

		EditorContext editorContext = tableModel.getEditorContextAt(actualRow, ((AggregateTable) table)
				.getAggregateTableModel().getPivotDataModel().getField(4).getModelIndex());

		if (editorContext != null) {

			SituazioneEffetto situazioneEffetto = (SituazioneEffetto) editorContext.getUserObject();

			switch (situazioneEffetto.getStatoCarrello()) {
			case SELEZIONABILE:
				label.setIcon(ICONS[situazioneEffetto.getStatoCarrelloFromStatoEffetto().ordinal()]);
				break;
			default:
				label.setIcon(ICONS[situazioneEffetto.getStatoCarrello().ordinal()]);
			}
		} else {
			if (((StatoEffetto) value) != null) {
				label.setIcon(RcpSupport.getIcon((StatoEffetto.class.getName() + "." + ((StatoEffetto) value).name())));
			}
		}

		return label;

	}
}
