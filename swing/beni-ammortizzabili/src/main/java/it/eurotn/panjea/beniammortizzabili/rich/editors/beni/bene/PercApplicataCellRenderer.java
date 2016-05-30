package it.eurotn.panjea.beniammortizzabili.rich.editors.beni.bene;

import it.eurotn.panjea.beniammortizzabili2.domain.QuotaAmmortamentoFiscale;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class PercApplicataCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -8421079767779397558L;

	public static final EditorContext PERC_ANTICIPATO_APPLICATA_CONTEXT = new EditorContext(
			"PERC_ANTICIPATO_APPLICATA_CONTEXT");
	public static final EditorContext PERC_ORDINARIO_APPLICATA_CONTEXT = new EditorContext(
			"PERC_ORIDNARIO_APPLICATA_CONTEXT");

	/**
	 * Costruttore.
	 *
	 */
	public PercApplicataCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		@SuppressWarnings("unchecked")
		DefaultBeanTableModel<QuotaAmmortamentoFiscale> tableModel = (DefaultBeanTableModel<QuotaAmmortamentoFiscale>) TableModelWrapperUtils
				.getActualTableModel(table.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow == -1) {
			return label;
		}

		QuotaAmmortamentoFiscale quota = tableModel.getObject(actualRow);
		if (quota.isPercPrimoAnnoApplicata()) {
			if (getEditorContext().equals(PERC_ORDINARIO_APPLICATA_CONTEXT)) {
				label.setText("(" + quota.getPercQuotaAmmortamentoOrdinario() + ") " + label.getText());
			} else {
				label.setText("(" + quota.getPercQuotaAmmortamentoAnticipato() + ") " + label.getText());
			}
		}

		return label;
	}
}
