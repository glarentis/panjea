package it.eurotn.panjea.magazzino.rich.editors.importazioni;

import it.eurotn.panjea.magazzino.importer.util.AbstractValidationObjectImport;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.renderer.BooleanContextSensitiveCellRenderer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.apache.commons.lang3.StringUtils;

import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class ValidazioneImportCellRenderer extends BooleanContextSensitiveCellRenderer {

	private static final long serialVersionUID = -5251914557805550664L;

	public static final EditorContext VALIDAZIONE_DOCUMENTO_IMPORT_CONTEXT = new EditorContext(
			"VALIDAZIONE_DOCUMENTO_IMPORT_CONTEXT");

	/**
	 * Costruttore.
	 * 
	 */
	public ValidazioneImportCellRenderer() {
		super();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		@SuppressWarnings("unchecked")
		DefaultBeanTableModel<AbstractValidationObjectImport> tableModel = (DefaultBeanTableModel<AbstractValidationObjectImport>) TableModelWrapperUtils
				.getActualTableModel(table.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow == -1) {
			return label;
		}

		AbstractValidationObjectImport objectImport = tableModel.getObject(actualRow);

		String toolTipString = null;
		if (!StringUtils.isBlank(objectImport.getValidationMessageHtml())) {
			toolTipString = objectImport.getValidationMessageHtml();
		}

		setToolTipText(toolTipString);
		return label;
	}
}
