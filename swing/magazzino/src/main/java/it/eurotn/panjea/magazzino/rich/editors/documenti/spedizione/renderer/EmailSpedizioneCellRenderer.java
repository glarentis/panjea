package it.eurotn.panjea.magazzino.rich.editors.documenti.spedizione.renderer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

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

public class EmailSpedizioneCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -5251914557805550664L;

	public static final EditorContext EMAIL_SPEDIZIONE_CONTEXT = new EditorContext("EMAIL_SPEDIZIONE_CONTEXT");
	public static final Icon ICON_RUBRICA = RcpSupport.getIcon("rubricaEditor.image");

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setOpaque(false);

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow == -1) {
			return label;
		}

		label.setIcon(null);
		label.setHorizontalAlignment(SwingUtilities.LEFT);

		JLabel labelRubrica = null;
		labelRubrica = new JLabel();
		labelRubrica.setIcon(ICON_RUBRICA);
		labelRubrica.setOpaque(false);
		labelRubrica.setBorder(BorderFactory.createEmptyBorder());

		JPanel panelIcons = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
		panelIcons.setBorder(BorderFactory.createEmptyBorder());
		panelIcons.setOpaque(true);
		panelIcons.setBackground(label.getBackground());
		panelIcons.add(labelRubrica);

		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder());
		panel.setBackground(label.getBackground());
		panel.add(panelIcons, BorderLayout.WEST);
		panel.add(label, BorderLayout.CENTER);

		return panel;
	}
}
