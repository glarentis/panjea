package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti;

import it.eurotn.panjea.tesoreria.domain.Effetto.StatoEffetto;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto;
import it.eurotn.panjea.tesoreria.util.SituazioneEffetto.StatoCarrello;
import it.eurotn.rich.control.table.editor.AbstractCellEditorFactory;

import java.awt.Component;
import java.util.List;

import javax.swing.CellEditor;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.ContextSensitiveCellEditor;
import com.jidesoft.grid.FilterableTableModel;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.pivot.CompoundKey;
import com.jidesoft.pivot.PivotConstants;
import com.jidesoft.pivot.PivotField;
import com.jidesoft.pivot.Values;

public class StatoEffettoCellEditorFactory extends AbstractCellEditorFactory {

	private class StatoEffettoCellEditor extends ContextSensitiveCellEditor {

		private static final long serialVersionUID = 1326026286430791000L;

		private StatoEffetto statoEffetto;

		/**
		 * Costruttore.
		 * 
		 * @param type
		 *            tipo del dato nell'editor.
		 */
		public StatoEffettoCellEditor(final Class<?> type) {
			super();
			setType(type);
		}

		@Override
		public Object getCellEditorValue() {
			return statoEffetto;
		}

		@Override
		public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean select, int row, int column) {

			JLabel label = new JLabel();

			RisultatiRicercaEffettiTableModel tableModel = (RisultatiRicercaEffettiTableModel) TableModelWrapperUtils
					.getActualTableModel(jtable.getModel());

			Values values = ((AggregateTable) jtable).getAggregateTableModel().getPivotDataModel()
					.getRowHeaderTableModel().getValuesAt(row);
			Object[] keys = new Object[column + 1];
			for (int i = 0; i < keys.length; i++) {
				keys[i] = values.getValueAt(i).getValue();
			}
			CompoundKey rowKey = CompoundKey.newInstance(keys);
			final List<Integer> list = ((AggregateTable) jtable).getAggregateTableModel().getPivotDataModel()
					.getDataAt(rowKey, CompoundKey.newInstance(new Object[0]));

			List<Integer> righe = ((AggregateTable) jtable).getAggregateTableModel().getPivotDataModel()
					.getDataAt(row, column);

			SortableTableModel sortableTableModel = (SortableTableModel) TableModelWrapperUtils.getActualTableModel(
					jtable.getModel(), SortableTableModel.class);

			FilterableTableModel filterableTableModelTableModel = (FilterableTableModel) TableModelWrapperUtils
					.getActualTableModel(jtable.getModel(), FilterableTableModel.class);

			int actualRow = filterableTableModelTableModel.getActualRowAt(row);
			actualRow = sortableTableModel.getActualRowAt(actualRow);
			actualRow = ((AggregateTable) jtable).getAggregateTableModel().getActualRowAt(actualRow);

			SituazioneEffetto situazioneEffetto = null;
			if (actualRow == -1) {
				PivotField field = ((AggregateTable) jtable).getAggregateTableModel().getPivotDataModel().getField(4);

				if (field != null
						&& (field.getSubtotalType() == PivotConstants.SUBTOTAL_NONE || field.getGrandTotalSummaryType() == PivotConstants.SUMMARY_NONE)) {
					actualRow = righe.get(0);
				} else {
					return label;
				}
			}
			situazioneEffetto = tableModel.getObject(actualRow);
			statoEffetto = situazioneEffetto.getEffetto().getStatoEffetto();

			StatoCarrello statoCarrello;

			switch (situazioneEffetto.getStatoCarrello()) {
			case SELEZIONABILE:
				if (situazioneEffetto.getStatoCarrelloFromStatoEffetto() == StatoCarrello.NON_SELEZIONABILE) {
					statoCarrello = StatoCarrello.NON_SELEZIONABILE;
				} else {
					statoCarrello = StatoCarrello.DA_AGGIUNGERE;
				}
				break;
			case DA_AGGIUNGERE:
				statoCarrello = StatoCarrello.SELEZIONABILE;
				break;
			case AGGIUNTO_CARRELLO:
				// non faccio nulla
				statoCarrello = StatoCarrello.AGGIUNTO_CARRELLO;
				break;
			case NON_SELEZIONABILE:
				// non faccio nulla
				statoCarrello = StatoCarrello.NON_SELEZIONABILE;
				break;
			default:
				throw new UnsupportedOperationException("stato carrello non valido "
						+ situazioneEffetto.getStatoCarrello());
			}

			label.setText(ObjectConverterManager.toString(situazioneEffetto.getEffetto().getStatoEffetto()));
			label.setOpaque(false);

			label.setIcon(ICONS[statoCarrello.ordinal()]);

			// aggiorno tutte le righe del raggruppamento
			if (righe != null) {
				for (Integer riga : righe) {
					situazioneEffetto = tableModel.getObject(riga);
					situazioneEffetto.setStatoCarrello(statoCarrello);
				}
			} else {
				situazioneEffetto = tableModel.getObject(actualRow);
				situazioneEffetto.setStatoCarrello(statoCarrello);
			}

			return label;
		}

		@Override
		public boolean isAutoStopCellEditing() {
			return true;
		}
	}

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
	public CellEditor create() {
		return new StatoEffettoCellEditor(getType());
	}

}
