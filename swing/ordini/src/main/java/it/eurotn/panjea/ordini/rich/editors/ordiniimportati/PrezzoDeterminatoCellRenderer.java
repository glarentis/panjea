package it.eurotn.panjea.ordini.rich.editors.ordiniimportati;

import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class PrezzoDeterminatoCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -5251914557805550664L;

	public static final EditorContext PREZZO_DETERMINATO_CONTEXT = new EditorContext("PREZZO_DETERMINATO_CONTEXT");
	public static final Icon ICON_COPIA_PREZZO = RcpSupport.getIcon("prezzoDeterminatoCopia.icon");
	public static final Icon ICON_VERIFICA_PREZZO = RcpSupport.getIcon("prezzoDeterminatoVerifica.icon");

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setOpaque(false);

		@SuppressWarnings("unchecked")
		DefaultBeanTableModel<RigaOrdineImportata> tableModel = (DefaultBeanTableModel<RigaOrdineImportata>) TableModelWrapperUtils
				.getActualTableModel(table.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow == -1) {
			return label;
		}

		RigaOrdineImportata rigaOrdine = tableModel.getObject(actualRow);
		BigDecimal prezzoDeterminato = rigaOrdine.getTotaleRigaDeterminato();

		label.setIcon(null);
		label.setHorizontalAlignment(SwingUtilities.RIGHT);

		JLabel labelCopiaPrezzo = null;
		labelCopiaPrezzo = new JLabel();
		labelCopiaPrezzo.setIcon(ICON_COPIA_PREZZO);
		labelCopiaPrezzo.setOpaque(false);
		labelCopiaPrezzo.setBorder(BorderFactory.createEmptyBorder());

		JLabel labelVerificaPrezzo = new JLabel("");
		labelVerificaPrezzo.setIcon(ICON_VERIFICA_PREZZO);
		labelVerificaPrezzo.setOpaque(false);
		labelVerificaPrezzo.setBorder(BorderFactory.createEmptyBorder());

		JPanel panelIcons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panelIcons.setBorder(BorderFactory.createEmptyBorder());
		panelIcons.setOpaque(true);
		panelIcons.setBackground(label.getBackground());
		panelIcons.add(labelVerificaPrezzo);
		if (prezzoDeterminato.compareTo(BigDecimal.ZERO) != 0) {
			panelIcons.add(labelCopiaPrezzo);
		}

		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder());
		panel.setBackground(label.getBackground());
		panel.add(panelIcons, BorderLayout.WEST);
		panel.add(label, BorderLayout.CENTER);

		return panel;
	}
}
