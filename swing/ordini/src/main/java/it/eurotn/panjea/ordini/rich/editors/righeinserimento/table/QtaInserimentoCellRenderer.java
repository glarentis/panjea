package it.eurotn.panjea.ordini.rich.editors.righeinserimento.table;

import java.awt.Component;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.ParseException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.renderer.IconContextSensitiveCellRenderer;

public class QtaInserimentoCellRenderer extends IconContextSensitiveCellRenderer {

	private static final long serialVersionUID = 7541360529351674600L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel labelQtaIns = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
				column);
		labelQtaIns.setHorizontalAlignment(getTextPosition());
		labelQtaIns.setOpaque(false);
		JLabel labelQta = new JLabel();
		labelQta.setBackground(labelQtaIns.getBackground());
		labelQta.setForeground(labelQtaIns.getForeground());
		labelQta.setHorizontalAlignment(getTextPosition());

		if (DefaultBeanTableModel.class
				.isAssignableFrom(TableModelWrapperUtils.getActualTableModel(table.getModel()).getClass())) {
			@SuppressWarnings("unchecked")
			DefaultBeanTableModel<RigaOrdineInserimento> tableModel = (DefaultBeanTableModel<RigaOrdineInserimento>) TableModelWrapperUtils
					.getActualTableModel(table.getModel());
			int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
			if (actualRow == -1) {
				return labelQtaIns;
			}

			RigaOrdineInserimento riga = tableModel.getObject(actualRow);
			String valoreString;
			String valoreInsString;
			try {
				valoreString = new DefaultNumberFormatterFactory("#,##0", riga.getNumeroDecimaliQuantita(),
						BigDecimal.class).getDefaultFormatter().valueToString(riga.getQta());
				valoreInsString = new DefaultNumberFormatterFactory("#,##0", riga.getNumeroDecimaliQuantita(),
						BigDecimal.class).getDefaultFormatter().valueToString(riga.getQtaInserimento());
			} catch (ParseException e) {
				valoreString = "";
				valoreInsString = "";
			}
			labelQta.setText(valoreString);
			labelQtaIns.setText(valoreInsString);
		}

		JPanel panel = new JPanel(new GridLayout(1, 2));
		panel.setBackground(labelQtaIns.getBackground());
		panel.add(labelQta);
		panel.add(labelQtaIns);
		return panel;
	}
}
