package it.eurotn.panjea.magazzino.rich.control.table.renderer;

import it.eurotn.panjea.magazzino.domain.AttributoCategoria;
import it.eurotn.panjea.magazzino.domain.AttributoMagazzino;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class AttributoMagazzinoValoreCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = 1503378619590468740L;

	public static final EditorContext ATTRIBUTO_MAGAZZINO_VALORE_CONTEXT = new EditorContext(
			"ATTRIBUTO_MAGAZZINO_VALORE_CONTEXT");

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		@SuppressWarnings("unchecked")
		DefaultBeanTableModel<AttributoCategoria> tableModel = (DefaultBeanTableModel<AttributoCategoria>) TableModelWrapperUtils
		.getActualTableModel(table.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);

		if (actualRow == -1) {
			return label;
		}

		AttributoMagazzino attributoMagazzino = tableModel.getObject(actualRow);

		label.setIcon(null);
		switch (attributoMagazzino.getTipoAttributo().getTipoDato()) {
		case BOOLEANO:
			label.setText("");
			label.setIcon(RcpSupport.getIcon(new Boolean((String) value).toString().toLowerCase()));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			break;
		case NUMERICO:
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			break;
		default:
			label.setHorizontalAlignment(SwingConstants.LEFT);
			break;
		}

		return label;
	}

}
