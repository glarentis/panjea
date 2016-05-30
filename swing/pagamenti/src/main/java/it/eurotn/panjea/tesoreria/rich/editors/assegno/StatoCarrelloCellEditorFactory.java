package it.eurotn.panjea.tesoreria.rich.editors.assegno;

import it.eurotn.panjea.tesoreria.util.AssegnoDTO;
import it.eurotn.panjea.tesoreria.util.AssegnoDTO.StatoCarrello;
import it.eurotn.rich.control.table.editor.AbstractCellEditorFactory;

import java.awt.Component;

import javax.swing.CellEditor;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellEditor;
import com.jidesoft.grid.FilterableTableModel;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTable;

public class StatoCarrelloCellEditorFactory extends AbstractCellEditorFactory {

	private class StatoCarrelloCellEditor extends ContextSensitiveCellEditor {

		private static final long serialVersionUID = 1326026286430791000L;

		private StatoCarrello statoCarrello;

		/**
		 * Costruttore.
		 * 
		 * @param type
		 *            tipo del dato nell'editor.
		 */
		public StatoCarrelloCellEditor(final Class<?> type) {
			super();
			setType(type);
		}

		@Override
		public Object getCellEditorValue() {
			return statoCarrello;
		}

		@Override
		public Component getTableCellEditorComponent(JTable jtable, Object obj, boolean select, int row, int j) {

			JLabel label = new JLabel();
			RisultatiRicercaAssegniTableModel tableModel = (RisultatiRicercaAssegniTableModel) TableModelWrapperUtils
					.getActualTableModel(jtable.getModel());

			SortableTableModel sortableTableModel = (SortableTableModel) TableModelWrapperUtils.getActualTableModel(
					jtable.getModel(), SortableTableModel.class);

			FilterableTableModel filterableTableModelTableModel = (FilterableTableModel) TableModelWrapperUtils
					.getActualTableModel(jtable.getModel(), FilterableTableModel.class);

			int actualRow = filterableTableModelTableModel.getActualRowAt(row);
			actualRow = sortableTableModel.getActualRowAt(actualRow);
			actualRow = ((AggregateTable) jtable).getAggregateTableModel().getActualRowAt(actualRow);

			AssegnoDTO assegnoDTO = tableModel.getObject(actualRow);
			// seleziono la riga altrimenti vedrei selezionata la riga precedente quando faccio apparire
			// la message dialog e potrebbe portere a confusione.
			jtable.getSelectionModel().setSelectionInterval(row, row);
			switch (assegnoDTO.getStatoCarrello()) {
			case SELEZIONABILE:
				if (assegnoDTO.getStatoCarrelloFromStatoAssegno() == StatoCarrello.NON_SELEZIONABILE) {

					String title = RcpSupport.getMessage("pagamentoRataChiusa.ask.title");
					String message = RcpSupport.getMessage("pagamentoRataChiusa.ask.message");
					ConfirmationDialog dialog = new ConfirmationDialog(title, message) {

						@Override
						protected void onCancel() {
							statoCarrello = StatoCarrello.SELEZIONABILE;
							super.onCancel();
						}

						@Override
						protected void onConfirm() {
							statoCarrello = StatoCarrello.DA_AGGIUNGERE;

						}
					};
					dialog.showDialog();

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
				statoCarrello = StatoCarrello.NON_SELEZIONABILE;
				break;
			default:
				throw new UnsupportedOperationException("stato carrello non valido " + assegnoDTO.getStatoCarrello());
			}

			label.setText(RcpSupport.getMessage(assegnoDTO.getStatoAssegno().getClass().getName() + "."
					+ assegnoDTO.getStatoAssegno().name()));
			label.setOpaque(false);

			if (statoCarrello == StatoCarrello.SELEZIONABILE) {
				label.setIcon(ICONS[assegnoDTO.getStatoCarrelloFromStatoAssegno().ordinal()]);
			} else {
				label.setIcon(ICONS[statoCarrello.ordinal()]);
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
		return new StatoCarrelloCellEditor(getType());
	}

}
