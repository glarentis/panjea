package it.eurotn.panjea.ordini.rich.editors.produzione;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import com.jidesoft.grid.DefaultExpandableRow;
import com.jidesoft.grid.NavigableTableModel;
import com.jidesoft.grid.TreeTableModel;

public class RigheEvaseTableModel extends TreeTableModel<DefaultExpandableRow> implements NavigableTableModel {

	private static final long serialVersionUID = 1148480824440763113L;

	@Override
	public Class<?> getColumnClass(int columnIndex) {

		switch (columnIndex) {
		case 0:
			return ArticoloLite.class;
		case 1:
			return Double.class;
		default:
			return super.getColumnClass(columnIndex);
		}
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Articolo";
		case 1:
			return "Quantità";
		default:
			return super.getColumnName(column);
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if (getRowAt(row) instanceof RigaComponenteRow && col == 1) {
			return true;
		}
		return super.isCellEditable(row, col);
	}

	@Override
	public boolean isNavigableAt(int row, int col) {

		if (getRowAt(row) instanceof RigaComponenteRow && col == 1) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isNavigationOn() {
		return true;
	}

	@Override
	public void setValueAt(Object object, int row, int col) {
		super.setValueAt(object, row, col);

		DefaultExpandableRow rowAt = getRowAt(row) ;
		if (rowAt instanceof RigaComponenteRow && col == 1) {
			((RigaComponenteRow)rowAt).getRigaComponente().setQta((Double) object);
		}
	}



}
