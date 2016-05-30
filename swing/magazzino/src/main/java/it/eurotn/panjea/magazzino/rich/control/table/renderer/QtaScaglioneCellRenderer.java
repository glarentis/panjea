package it.eurotn.panjea.magazzino.rich.control.table.renderer;

import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.RigaManutenzioneListino;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.StringUtils;

import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class QtaScaglioneCellRenderer extends IconContextSensitiveCellRenderer {
	private static final long serialVersionUID = 8301042112478704859L;

	public static final EditorContext QTA_SCAGLIONE_CONTEXT = new EditorContext("QTA_SCAGLIONE_CONTEXT");

	private DecimalFormat format = new DecimalFormat();

	@Override
	public EditorContext getEditorContext() {
		return QTA_SCAGLIONE_CONTEXT;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setText(null);

		if (DefaultBeanTableModel.class.isAssignableFrom(TableModelWrapperUtils.getActualTableModel(table.getModel())
				.getClass())) {
			@SuppressWarnings("unchecked")
			DefaultBeanTableModel<RigaManutenzioneListino> tableModel = (DefaultBeanTableModel<RigaManutenzioneListino>) TableModelWrapperUtils
					.getActualTableModel(table.getModel());
			int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
			if (actualRow == -1) {
				return label;
			}

			RigaManutenzioneListino rigaManutenzioneListino = tableModel.getObject(actualRow);
			label.setHorizontalAlignment(SwingConstants.RIGHT);

			if (rigaManutenzioneListino.getListino() == null
					|| rigaManutenzioneListino.getListino().getTipoListino() == ETipoListino.NORMALE) {
				return label;
			}

			if (ScaglioneListino.MAX_SCAGLIONE.equals(value)) {
				label.setText("OLTRE");
			} else if (value instanceof Double) {

				format.applyPattern("#,##0");
				if (rigaManutenzioneListino.getArticolo().getNumeroDecimaliQta() > 0) {
					format.applyPattern("#,##0."
							+ StringUtils.repeat("0", rigaManutenzioneListino.getArticolo().getNumeroDecimaliQta()));
				}
				label.setText("fino a " + format.format(value));
			}
			return label;
		}
		return label;
	}
}
