package it.eurotn.panjea.magazzino.rich.editors.inventarioarticolo;

import it.eurotn.panjea.magazzino.domain.InventarioArticolo;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.ContextSensitiveCellRenderer;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.TableModelWrapperUtils;

public class GiacenzaRealeCellRenderer extends ContextSensitiveCellRenderer {

	private static final long serialVersionUID = -8421079767779397558L;

	public static final EditorContext GIACENZA_REALE_CONTEXT = new EditorContext("GIACENZA_REALE_CONTEXT", 6);

	private Icon editIcon;

	/**
	 * Costruttore.
	 * 
	 */
	public GiacenzaRealeCellRenderer() {
		super();
		setHorizontalAlignment(SwingConstants.RIGHT);
		editIcon = RcpSupport.getIcon("edit");
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		@SuppressWarnings("unchecked")
		DefaultBeanTableModel<InventarioArticolo> tableModel = (DefaultBeanTableModel<InventarioArticolo>) TableModelWrapperUtils
				.getActualTableModel(table.getModel());

		int actualRow = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);
		if (actualRow == -1) {
			return label;
		}

		String textValue = label.getText().isEmpty() ? "0,000000" : label.getText();
		label.setIcon(null);
		label.setText(textValue);
		InventarioArticolo inventarioArticolo = tableModel.getObject(actualRow);
		if (inventarioArticolo.getNumeroRighe() > 0) {
			label.setText("(" + inventarioArticolo.getNumeroRighe() + ") " + textValue);
			label.setIcon(editIcon);
		}

		label.setToolTipText(null);

		return label;
	}
}
