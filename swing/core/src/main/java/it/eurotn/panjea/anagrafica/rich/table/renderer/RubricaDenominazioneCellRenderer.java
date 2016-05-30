package it.eurotn.panjea.anagrafica.rich.table.renderer;

import it.eurotn.panjea.anagrafica.rich.editors.rubrica.RubricaTableModel;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class RubricaDenominazioneCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = 695292175637979723L;

	public static final EditorContext RUBRICA_CONTEXT = new EditorContext("RUBRICA_CONTEXT");

	/**
	 * Costruttore.
	 */
	public RubricaDenominazioneCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.LEFT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		RubricaTableModel rubricaTableModel = (RubricaTableModel) TableModelWrapperUtils.getActualTableModel(
				table.getModel(), RubricaTableModel.class);
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		String className = rubricaTableModel.getRowAt(actualRow).getRubricaDTO().getRowClass().getName();
		Icon icon = RcpSupport.getIcon(className);
		label.setIcon(icon);
		Font font = label.getFont().deriveFont(Font.BOLD);
		if (!className.contains("Contatto")) {
			label.setFont(font);
		}
		return label;
	}
}
