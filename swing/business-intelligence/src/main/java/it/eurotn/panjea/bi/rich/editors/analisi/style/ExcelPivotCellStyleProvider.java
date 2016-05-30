/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.analisi.style;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.pivot.DataTableModel;
import com.jidesoft.pivot.HeaderTableModel;

/**
 * @author fattazzo
 *
 */
public class ExcelPivotCellStyleProvider extends PivotCellStyleProvider {

	static {
		DEFAULT_CELL_STYLE = new CellStyle();
	}

	private static final CellStyle DEFAULT_CELL_STYLE;

	/**
	 * Costruttore.
	 */
	public ExcelPivotCellStyleProvider() {
		super();
	}

	@Override
	public CellStyle getDataTableCellStyleAt(DataTableModel paramDataTableModel, HeaderTableModel rowHeaderModel,
			HeaderTableModel columnHeaderModel, int rowIndex, int columnIndex) {
		if (rowHeaderModel.isSubtotalRowOrColumn(rowIndex) || columnHeaderModel.isSubtotalRowOrColumn(columnIndex)
				|| rowHeaderModel.isGrandTotalRowOrColumn(rowIndex)
				|| columnHeaderModel.isGrandTotalRowOrColumn(columnIndex)) {
			return super.getDataTableCellStyleAt(paramDataTableModel, rowHeaderModel, columnHeaderModel, rowIndex,
					columnIndex);
		} else {
			return DEFAULT_CELL_STYLE;
		}
	}
}
