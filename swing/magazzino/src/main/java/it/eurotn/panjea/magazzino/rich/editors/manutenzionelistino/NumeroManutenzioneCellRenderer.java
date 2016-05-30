package it.eurotn.panjea.magazzino.rich.editors.manutenzionelistino;

import it.eurotn.panjea.magazzino.util.parametriricerca.RigaManutenzioneListino;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.image.IconSource;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.IndexReferenceRow;
import com.jidesoft.grid.TableModelWrapperUtils;

public class NumeroManutenzioneCellRenderer extends ContextSensitiveCellRenderer {

	public static final EditorContext NUMERO_MANUTENZIONE_CELL_RENDERER_CONTEXT = new EditorContext(
			"NUMERO_MANUTENZIONE_CELL_RENDERER_CONTEXT");
	private static final long serialVersionUID = -6805650962597510176L;

	private final IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(
			IconSource.class);

	/**
	 * Costruttore.
	 */
	public NumeroManutenzioneCellRenderer() {
		super();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = null;
		if (value instanceof DefaultGroupRow) {
			DefaultGroupRow groupRow = (DefaultGroupRow) value;
			Object objectToRender = groupRow.getConditionValue(groupRow.getLevel());
			label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (column == 1) {
				label.setIcon(iconSource.getIcon("note.icon"));
				label.setText("<HTML><b>Numero manutenzione: <b><HTML>" + objectToRender.toString());

				Object child = groupRow.getChildAt(0);
				while (!(child instanceof IndexReferenceRow)) {
					child = ((DefaultGroupRow) child).getChildAt(0);
				}
				row = ((IndexReferenceRow) child).getRowIndex();
				RigheManutenzioneListinoTableModel tableModel = (RigheManutenzioneListinoTableModel) TableModelWrapperUtils
						.getActualTableModel(table.getModel());
				// row = JideTableWidget.getActualRow(row, table.getModel());
				if (row != -1) {
					// row = JideTableWidget.getActualRow(row, table.getModel());
					RigaManutenzioneListino rigaManutenzioneListino = tableModel.getElementAt(row);
					label.setToolTipText(rigaManutenzioneListino.getDescrizione());
				}
			}
		} else {
			// Sono sul numero documento
			label = new JLabel();
			label.setOpaque(false);
			label.setBorder(BorderFactory.createEmptyBorder());
			RigheManutenzioneListinoTableModel tableModel = (RigheManutenzioneListinoTableModel) TableModelWrapperUtils
					.getActualTableModel(table.getModel());
			row = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
			if (row != -1) {
				RigaManutenzioneListino rigaManutenzioneListino = tableModel.getElementAt(row);
				Icon icon = iconSource.getIcon("note.icon");
				label.setToolTipText(rigaManutenzioneListino.getDescrizione());
				label.setIcon(icon);
				label.setText("Numero manutenzione: " + rigaManutenzioneListino.getNumero());
			}
		}
		return label;
	}
}
