package it.eurotn.panjea.magazzino.rich.editors.articolo.statistiche;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.pivot.DataTableModel;
import com.jidesoft.pivot.HeaderTableModel;

public class DisponibilitaCellStyleProvider implements com.jidesoft.pivot.PivotCellStyleProvider {

	private static final CellStyle SUMMARY_STYLE = new CellStyle();
	private static final CellStyle DEFAULT_STYLE = new CellStyle();
	private static final CellStyle HEADER_STYLE = new CellStyle();
	private static final CellStyle DAY_STYLE = new CellStyle();
	private static final CellStyle INC_STYLE = new CellStyle();
	private static final CellStyle DISP_STYLE = new CellStyle();

	static {
		DAY_STYLE.setBackground(new Color(209, 246, 243));
		INC_STYLE.setBackground(new Color(189, 226, 223));
		DISP_STYLE.setBackground(new Color(169, 216, 213));
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
	public DisponibilitaCellStyleProvider() {
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
			switch (rowIndex) {
			case 0:
			case 1:
				return DAY_STYLE;
			case 2:
			case 3:
				return INC_STYLE;
			case 4:
				return DISP_STYLE;
			default:
				return DAY_STYLE;
			}
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
