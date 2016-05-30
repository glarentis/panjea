package it.eurotn.panjea.magazzino.rich.renderer;

import it.eurotn.panjea.magazzino.domain.RulesValidationErrors;
import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.springframework.richclient.util.RcpSupport;

public class RulesValidationErrorsRenderer extends IconContextSensitiveCellRenderer {

	private static final long serialVersionUID = -7660451163975366136L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setBorder(null);

		setBorder(null);

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel.setBorder(null);
		panel.setOpaque(true);
		panel.setToolTipText(label.getText());

		if (isSelected) {
			panel.setBackground(table.getSelectionBackground());
		} else {
			panel.setBackground(table.getBackground());
		}

		if (value != null && value instanceof RulesValidationErrors) {

			RulesValidationErrors rules = (RulesValidationErrors) value;

			for (String string : rules.getRules()) {
				JLabel labelIcon = new JLabel(getIconSource().getIcon(string));
				labelIcon.setText("");
				labelIcon.setToolTipText(RcpSupport.getMessage(string));
				panel.add(labelIcon);
			}
		}

		return panel;
	}
}
