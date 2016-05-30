package it.eurotn.panjea.tesoreria.solleciti.editors;

import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.DefaultGroupTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;

public class SelectRowListener extends MouseAdapter {

	private JideTableWidget<Sollecito> table;
	private DefaultBeanTableModel<Sollecito> tableModel;
	private DefaultGroupTableModel groupTableModel;
	private long lastEvent;

	/**
	 * Costruttore.
	 * 
	 * @param table
	 *            tabella da agganciare al listener
	 */
	@SuppressWarnings("unchecked")
	public SelectRowListener(final JideTableWidget<Sollecito> table) {
		super();
		this.table = table;
		table.getTable().addMouseListener(this);
		tableModel = (DefaultBeanTableModel<Sollecito>) TableModelWrapperUtils.getActualTableModel(table.getTable()
				.getModel());
		groupTableModel = (DefaultGroupTableModel) TableModelWrapperUtils.getActualTableModel(table.getTable()
				.getModel(), DefaultGroupTableModel.class);
	}

	@Override
	public void mouseClicked(MouseEvent mouseevent) {
		// Non so perchè l'evento viene chiamato due volte. (ved. MouseClicked
		// su eventMulticaster).
		// controllo che l'evento sia generato in tempi diversi

		if (mouseevent.getWhen() == lastEvent) {
			return;
		} else {
			lastEvent = mouseevent.getWhen();
		}

		int rowIndex = table.getTable().rowAtPoint(mouseevent.getPoint());
		int columnIndex = table.getTable().columnAtPoint(mouseevent.getPoint());
		int columnIndexConvert = table.getTable().convertRowIndexToModel(
				table.getTable().convertColumnIndexToModel(columnIndex));
		int rowIndexConvert = table.getTable()
				.convertRowIndexToModel(table.getTable().convertRowIndexToModel(rowIndex));
		Object value = table.getTable().getModel().getValueAt(rowIndexConvert, columnIndexConvert);

		int groupCols = groupTableModel.getGroupColumnCount();

		if (value instanceof DefaultGroupRow) {
			return;
		}

		Sollecito sollecito = tableModel.getObject(TableModelWrapperUtils.getActualRowAt(table.getTable().getModel(),
				rowIndex));
		switch (columnIndexConvert + groupCols) {
		case 1:
			sollecito.setEmail((sollecito.hasEmail()) ? !sollecito.isEmail() : false);
			break;
		case 2:
			sollecito.setStampa(!sollecito.isStampa());
			break;
		case 3:
			sollecito.setTelefono(!sollecito.isTelefono());
			break;
		case 4:
			sollecito.setFax(!sollecito.isFax());
			break;
		default:
			break;
		}

		table.replaceOrAddRowObject(sollecito, sollecito, null);

		groupTableModel.refresh();
		table.getTable().setColumnSelectionInterval(columnIndex, columnIndex);
	}
}
