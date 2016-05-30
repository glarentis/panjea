package it.eurotn.panjea.bi.rich.editors.analisi.model;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIDomain;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBIResult;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasure;
import it.eurotn.panjea.bi.rich.bd.BusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.event.TableModelListener;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.filter.Filter;
import com.jidesoft.pivot.PivotDataSource;

public class AnalisiBiDataSource implements PivotDataSource {

	private AnalisiBiTableModel analisiBiTableModel;
	private IBusinessIntelligenceBD businessIntelligenceBD;

	/**
	 * Costruttore.
	 */
	public AnalisiBiDataSource() {
		this(
				new AnalisiBiTableModel(new AnalisiBIResult(new ArrayList<Object[]>(), new HashMap<Integer, Integer>(),
						2)));
	}

	/**
	 *
	 * @param analisiBiTableModel
	 *            table mode ldell'analisi
	 */
	public AnalisiBiDataSource(final AnalisiBiTableModel analisiBiTableModel) {
		this.analisiBiTableModel = analisiBiTableModel;
		businessIntelligenceBD = RcpSupport.getBean(BusinessIntelligenceBD.BEAN_ID);
	}

	@Override
	public void addTableModelListener(TableModelListener paramTableModelListener) {

	}

	@Override
	public void applyFilters() {

	}

	@Override
	public void applyFilters(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3,
			int[] paramArrayOfInt4) {

	}

	@Override
	public void clearFilters() {

	}

	@Override
	public int getActualRowIndex(int paramInt) {
		return 0;
	}

	@Override
	public int getFieldCount() {
		return analisiBiTableModel.getColumnCount();
	}

	@Override
	public String getFieldName(int paramInt) {
		return AnalisiBIDomain.getColonna(paramInt).getKey();
	}

	@Override
	public String getFieldTitle(int paramInt) {
		return AnalisiBIDomain.getColonna(paramInt).getTitle();
	}

	@Override
	public Class<?> getFieldType(int paramInt) {
		return AnalisiBIDomain.getColonna(paramInt).getColumnClass();
	}

	@Override
	public int getFilteredRowIndex(int paramInt) {
		return 0;
	}

	/**
	 *
	 * @return .
	 */
	public int getNumDecimaliQtaMax() {
		return analisiBiTableModel.getNumDecimaliQtaMax();
	}

	@Override
	public Set<Object> getPossibleValues(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
		if (!(AnalisiBIDomain.getColonna(paramInt) instanceof ColumnMeasure)) {
			return businessIntelligenceBD.caricaValoriPerColonna(AnalisiBIDomain.getColonna(paramInt));
		}
		return null;
	}

	@Override
	public int getRowCount() {
		return analisiBiTableModel.getRowCount();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return analisiBiTableModel.getValueAt(rowIndex, columnIndex);
	}

	@Override
	public boolean hasFilter() {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener paramTableModelListener) {

	}

	@Override
	public void setExcludeFilter(Object[] paramArrayOfObject, int paramInt, boolean paramBoolean) {
	}

	@Override
	public void setFilter(@SuppressWarnings("rawtypes") Filter paramFilter, int paramInt, boolean paramBoolean) {
	}

	@Override
	public void setFilter(Object[] paramArrayOfObject, int paramInt, boolean paramBoolean) {
	}

	@Override
	public void setValueAt(Object paramObject, int paramInt1, int paramInt2) {

	}

}
