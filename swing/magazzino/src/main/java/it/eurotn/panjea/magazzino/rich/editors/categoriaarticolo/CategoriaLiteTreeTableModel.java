package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.richclient.util.RcpSupport;

public class CategoriaLiteTreeTableModel extends DefaultTreeTableModel {

	/**
	 * Crea il modello dell'albero.
	 * 
	 * @param node
	 *            nodo principale dell'albero con i suoi figli
	 */
	public CategoriaLiteTreeTableModel(final TreeTableNode node) {
		super(node);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int column) {
		switch (column) {
		case 0:
			return TreeTableModel.class;
		default:
			return super.getColumnClass(column);
		}
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public String getColumnName(final int column) {
		return RcpSupport.getMessage("CategorieDTOTableColonna" + column);
	}

	@Override
	public Object getValueAt(Object node, int column) {
		if (column == 0) {
			return super.getValueAt(node, column);
		}
		return "";
	}

	@Override
	public boolean isCellEditable(Object arg0, int arg1) {
		return false;
	}
}