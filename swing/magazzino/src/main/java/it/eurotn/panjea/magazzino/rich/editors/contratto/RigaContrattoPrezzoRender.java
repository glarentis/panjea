package it.eurotn.panjea.magazzino.rich.editors.contratto;

import it.eurotn.panjea.magazzino.domain.RigaContratto;
import it.eurotn.panjea.rich.factory.table.CustomBigDecimalCellRenderer;

import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.springframework.richclient.table.support.GlazedTableModel;

public class RigaContrattoPrezzoRender extends CustomBigDecimalCellRenderer {
	private static final long serialVersionUID = 1749963540355006053L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		GlazedTableModel glazedTableModel = (GlazedTableModel) table.getModel();
		RigaContratto rigaContratto = (RigaContratto) glazedTableModel.getElementAt(row);
		this.numeroDecimali = rigaContratto.getNumeroDecimaliPrezzo();

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value != null) {
			BigDecimal bigDecimal = (BigDecimal) value;
			Format format = new DecimalFormat("###,###,###,##0." + strZeri.substring(0, numeroDecimali));
			label.setText(format.format(bigDecimal));
		}

		return label;
	}

}
