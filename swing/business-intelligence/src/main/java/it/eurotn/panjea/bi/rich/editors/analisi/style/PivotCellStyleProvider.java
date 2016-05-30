package it.eurotn.panjea.bi.rich.editors.analisi.style;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.pivot.DataTableModel;
import com.jidesoft.pivot.HeaderTableModel;

public class PivotCellStyleProvider implements com.jidesoft.pivot.PivotCellStyleProvider {

	private static final CellStyle HIGH_STYLE = new CellStyle();
	private static final CellStyle LOW_STYLE = new CellStyle();
	private static final CellStyle SUMMARY_STYLE = new CellStyle();
	private static final CellStyle DEFAULT_STYLE = new CellStyle();
	private static final CellStyle HEADER_STYLE = new CellStyle();
	static {
		HIGH_STYLE.setForeground(Color.WHITE);
		HIGH_STYLE.setBackground(Color.RED);
		LOW_STYLE.setForeground(Color.BLACK);
		LOW_STYLE.setBackground(Color.YELLOW);
		SUMMARY_STYLE.setForeground(Color.BLACK);
		SUMMARY_STYLE.setBackground(new Color(255, 255, 215));
		SUMMARY_STYLE.setFontStyle(Font.BOLD);
		DEFAULT_STYLE.setForeground(Color.BLACK);
		DEFAULT_STYLE.setBackground(Color.WHITE);
		DEFAULT_STYLE.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		HEADER_STYLE.setFontStyle(Font.BOLD);
		HEADER_STYLE.setForeground(Color.BLACK);
		HEADER_STYLE.setBackground(Color.LIGHT_GRAY);
	}

	private Map<String, CellStyle> styleCached;

	/**
	 *
	 * Costruttore.
	 *
	 */
	public PivotCellStyleProvider() {
		styleCached = new HashMap<String, CellStyle>();
	}

	@Override
	public CellStyle getColumnHeaderCellStyleAt(HeaderTableModel paramHeaderTableModel, int paramInt1, int paramInt2) {
		return HEADER_STYLE;
	}

	@Override
	public CellStyle getDataTableCellStyleAt(DataTableModel paramDataTableModel, HeaderTableModel rowHeaderModel,
			HeaderTableModel columnHeaderModel, int rowIndex, int columnIndex) {
		if (rowHeaderModel.isSubtotalRowOrColumn(rowIndex) || columnHeaderModel.isSubtotalRowOrColumn(columnIndex)
				|| rowHeaderModel.isGrandTotalRowOrColumn(rowIndex)
				|| columnHeaderModel.isGrandTotalRowOrColumn(columnIndex)) {
			return SUMMARY_STYLE;
		} else {
			int numeroMisure = paramDataTableModel.getPivotDataModel().getDataFields().length;
			int misura = 0;
			if (numeroMisure > 0) {
				misura = columnIndex - ((columnIndex / numeroMisure) * numeroMisure);
			}
			int r = 209 - (misura * 10);
			int g = 226 - (misura * 10);
			int b = 243 - (misura * 10);
			if (r < 0) {
				r = 100;
			}
			if (g < 0) {
				g = 100;
			}
			if (b < 0) {
				b = 100;
			}

			// Cerco se lo stile esiste già, altrimenti creo x numeri di stili per quante celle ho
			CellStyle detailStyle = styleCached.get(r + "-" + g + "-" + b);
			if (detailStyle == null) {
				detailStyle = new CellStyle();
				detailStyle.setBackground(new Color(r, g, b));
				styleCached.put(r + "-" + g + "-" + b, detailStyle);
			}

			return detailStyle;
		}
	}

	@Override
	public CellStyle getRowHeaderCellStyleAt(HeaderTableModel paramHeaderTableModel, int paramInt1, int paramInt2) {
		if (paramHeaderTableModel.isSubtotalRowOrColumn(paramInt1)
				|| paramHeaderTableModel.isSubtotalRowOrColumn(paramInt2)
				|| paramHeaderTableModel.isGrandTotalRowOrColumn(paramInt1)
				|| paramHeaderTableModel.isGrandTotalRowOrColumn(paramInt2)) {
			return SUMMARY_STYLE;
		}
		return HEADER_STYLE;
	}

}
