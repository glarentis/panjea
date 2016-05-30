package it.eurotn.panjea.bi.rich.editors.analisi.model;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIDomain;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBIResult;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Table Model per la pivot.<br/>
 * Wrappa la classe {@link AnalisiBIDomain} implementando {@link TableModel}.<br/>
 * La classe {@link AnalisiBIDomain} non implementa direttamente il table model altrimenti il business avrebbe degli
 * <code>import</code> relativi alla GUI (swing).
 * 
 * @author giangi
 * 
 */
public class AnalisiBiTableModel implements TableModel {
	private AnalisiBIResult analisiBIResult;

	/**
	 * Costruttore.
	 * 
	 * @param analisiBIResult
	 *            modello di "business" del datawarehouse
	 */
	public AnalisiBiTableModel(final AnalisiBIResult analisiBIResult) {
		super();
		this.analisiBIResult = analisiBIResult;
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return AnalisiBIDomain.getColonna(columnIndex).getColumnClass();
	}

	@Override
	public int getColumnCount() {
		return AnalisiBIDomain.getColonne().size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return AnalisiBIDomain.getColonna(columnIndex).getNome();
	}

	/**
	 * 
	 * @return numero decimali articoli
	 */
	public int getNumDecimaliQtaMax() {
		return analisiBIResult.getNumDecimaliQtaMax();
	}

	@Override
	public int getRowCount() {
		return analisiBIResult.getNumRecord();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return analisiBIResult.getValueAt(rowIndex, columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

}
