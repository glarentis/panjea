package it.eurotn.panjea.preventivi.rich.editors.evasione;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.preventivi.util.RigaEvasione;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.ITable;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import com.jidesoft.grid.TableModelWrapperUtils;

public class SelectRowListener extends MouseAdapter implements KeyListener {

	private JTable table;
	private DefaultBeanTableModel<RigaEvasione> tableModel;
	private long lastEvent;

	/**
	 * Costruttore.
	 * 
	 * @param table
	 *            tabella da agganciare al listener
	 */
	@SuppressWarnings("unchecked")
	public SelectRowListener(final JTable table) {
		super();
		this.table = table;
		table.addMouseListener(this);
		table.addKeyListener(this);
		tableModel = (DefaultBeanEditableTableModel<RigaEvasione>) TableModelWrapperUtils.getActualTableModel(table
				.getModel());
	}

	@Override
	public void keyPressed(KeyEvent keyevent) {
	}

	@Override
	public void keyReleased(KeyEvent keyevent) {
		if (keyevent.getKeyCode() == 32) {
			int rowIndex = table.getSelectedRow();
			int columnIndex = table.getSelectedColumn();
			if (columnIndex == -1 || rowIndex == -1) {
				return;
			}

			@SuppressWarnings("unchecked")
			ITable<AreaMagazzinoRicerca> iTable = (ITable<AreaMagazzinoRicerca>) table;
			if (iTable.getActualColumn(columnIndex) == 0) {
				int[] indiciRighe = iTable.getActualRowsAt(rowIndex, columnIndex);
				updateRigaEvasione(indiciRighe);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent keyevent) {
	}

	@Override
	public void mouseClicked(MouseEvent mouseevent) {
		// Non so perchè l'evento viene chiamato due volte. (ved. MouseClicked
		// su eventMulticaster).
		// controllo che l'evento sia generato in tempi diversi

		if (mouseevent.getWhen() == lastEvent) {
			return;
		}
		lastEvent = mouseevent.getWhen();

		@SuppressWarnings("unchecked")
		ITable<AreaMagazzinoRicerca> iTable = (ITable<AreaMagazzinoRicerca>) table;
		if (iTable.checkColumn(mouseevent, 0)) {
			int[] indiciRighe = iTable.getActualRowsAt(table.rowAtPoint(mouseevent.getPoint()),
					table.columnAtPoint(mouseevent.getPoint()));
			updateRigaEvasione(indiciRighe);
		}
	}

	/**
	 * 
	 * @param indiciRighe
	 *            indici delle righe da aggiornare
	 */
	private void updateRigaEvasione(int[] indiciRighe) {
		for (int i : indiciRighe) {
			RigaEvasione rigaEvasione = tableModel.getObject(i);
			rigaEvasione.setSelezionata(!rigaEvasione.isSelezionata());
		}
		table.repaint();
	}
}
