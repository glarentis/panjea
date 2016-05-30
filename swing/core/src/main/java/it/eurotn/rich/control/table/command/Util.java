package it.eurotn.rich.control.table.command;

import it.eurotn.rich.control.table.JecGroupTable;
import it.eurotn.rich.control.table.JecGroupableTableModel;
import it.eurotn.rich.control.table.command.ExportXLSCommand.ExcelCellStyle;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JTable;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.pivot.HeaderTableModel;

/**
 *
 * @author jk getted from
 *         http://jxls.cvs.sourceforge.net/jxls/jxls/src/java/org/jxls/util/Util.java?revision=1.8&view=markup by Leonid
 *         Vysochyn and modified (adding styles copying) modified by Philipp Löpmeier (replacing deprecated classes and
 *         methods, using generic types)
 */
public class Util {

	public static void copyCell(HSSFCell oldCell, HSSFCell newCell, Map<Integer, HSSFCellStyle> styleMap) {
		if (oldCell == null || newCell == null) {
			return;
		}
		newCell.setCellStyle(oldCell.getCellStyle());

		switch (oldCell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			newCell.setCellValue(oldCell.getStringCellValue());
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			newCell.setCellValue(oldCell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			newCell.setCellType(HSSFCell.CELL_TYPE_BLANK);
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			newCell.setCellValue(oldCell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			newCell.setCellErrorValue(oldCell.getErrorCellValue());
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			newCell.setCellFormula(oldCell.getCellFormula());
			break;
		default:
			break;
		}

	}

	public static void copyColumn(HSSFSheet srcSheet, HSSFSheet destSheet, int columnOrigine, int columnDestinazione,
			Map<ExcelCellStyle, HSSFCellStyle> styleMap, JTable table, int compositeValue, int addedColumn) {
		List<CellRangeAddress> groupRegions = new ArrayList<CellRangeAddress>();
		Set<CellRangeAddress> mergedRegions = new TreeSet<CellRangeAddress>(new Comparator<CellRangeAddress>() {

			@Override
			public int compare(CellRangeAddress o1, CellRangeAddress o2) {
				if (o1.getFirstColumn() == o2.getFirstColumn() && o1.getLastColumn() == o2.getLastColumn()
						&& o1.getFirstRow() == o2.getFirstRow() && o1.getLastRow() == o2.getLastRow()) {
					return 0;
				} else if (o1.getFirstColumn() < o2.getFirstColumn()) {
					return 1;
				}
				return -1;
			}
		});

		boolean subTotaliPresenti = false;

		for (int i = srcSheet.getFirstRowNum(); i <= srcSheet.getLastRowNum(); i++) {
			HSSFRow row = srcSheet.getRow(i);
			HSSFRow rowDestinazione = destSheet.getRow(i);
			if (rowDestinazione == null) {
				rowDestinazione = destSheet.createRow(i);
			}
			HSSFCell cellOrigine = row.getCell(columnOrigine);
			if (cellOrigine == null) {
				continue;
			}
			HSSFCell cellDestinazione = rowDestinazione.createCell(columnDestinazione);
			copyCell(cellOrigine, cellDestinazione, null);

			if (row.getRowNum() == 0) {
				cellDestinazione.setCellStyle(styleMap.get(ExcelCellStyle.TITLE));
			} else if (isSubTotal(table, row.getRowNum() - 1, cellOrigine.getColumnIndex())) {
				subTotaliPresenti = true;
				cellDestinazione.setCellStyle(styleMap.get(ExcelCellStyle.SUBTOTAL));
			} else {
				HSSFFont font = cellDestinazione.getCellStyle().getFont(destSheet.getWorkbook());
				font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
				font.setColor(HSSFColor.BLACK.index);
				cellDestinazione.getCellStyle().setFont(font);
				cellDestinazione.getCellStyle().setFont(font);
				cellDestinazione.getCellStyle().setFillPattern(CellStyle.NO_FILL);
				cellDestinazione.getCellStyle().setBorderBottom(HSSFCellStyle.BORDER_NONE);
				cellDestinazione.getCellStyle().setBorderTop(HSSFCellStyle.BORDER_NONE);
				cellDestinazione.getCellStyle().setBorderLeft(HSSFCellStyle.BORDER_NONE);
				cellDestinazione.getCellStyle().setBorderRight(HSSFCellStyle.BORDER_NONE);
			}

			if (row.getRowNum() > 0 && compositeValue != -1) {

				Object valueTable = table.getValueAt(row.getRowNum() - 1, cellOrigine.getColumnIndex());
				if (valueTable != null) {
					ObjectConverter converter = ObjectConverterManager.getConverter(valueTable.getClass());
					if (converter instanceof PanjeaCompositeConverter) {

						String value = ((PanjeaCompositeConverter) converter).getCampo(valueTable, compositeValue);

						if (table instanceof JecGroupTable<?>) {
							JecGroupableTableModel tm = (JecGroupableTableModel) TableModelWrapperUtils
									.getActualTableModel(table.getModel(), JecGroupableTableModel.class);
							if (cellOrigine.getColumnIndex() > 0 && tm.hasGroupColumns()
									&& tm.getRowAt(row.getRowNum() - 1) instanceof DefaultGroupRow) {
								value = "";
							}
						}

						cellDestinazione.setCellValue(value);
					}
				}
			}

			if (table instanceof AggregateTable) {
				CellRangeAddress mergedRegion = getMergedRegion(srcSheet, row.getRowNum(),
						(short) (cellOrigine.getColumnIndex()));
				if (mergedRegion != null) {
					if (mergedRegion.getLastColumn() == mergedRegion.getFirstColumn()
							&& mergedRegion.getLastRow() != mergedRegion.getFirstRow()) {
						CellRangeAddress newMergedRegion = new CellRangeAddress(mergedRegion.getFirstRow(),
								mergedRegion.getLastRow(), mergedRegion.getFirstColumn() + addedColumn,
								mergedRegion.getFirstColumn() + addedColumn);

						HeaderTableModel columnHeaderModel = ((AggregateTable) table).getAggregateTableModel()
								.getPivotDataModel().getColumnHeaderTableModel();
						if (!columnHeaderModel.isRunningSummaryRowOrColumn(cellOrigine.getColumnIndex())
								&& isNewMergedRegion(newMergedRegion, mergedRegions)) {
							if (mergedRegions.add(newMergedRegion) && compositeValue != 2) {
								groupRegions.add(newMergedRegion);
							}
							destSheet.addMergedRegion(newMergedRegion);
						}
					}
				} else {
					if (row.getRowNum() > 0
							&& compositeValue != 2
							&& !isSubTotal(table, row.getRowNum() - 1, cellOrigine.getColumnIndex())
							&& cellOrigine.getColumnIndex() < ((AggregateTable) table).getAggregateTableModel()
									.getAggregatedColumnCount()) {
						CellRangeAddress newMergedRegion = new CellRangeAddress(cellDestinazione.getRowIndex(),
								cellDestinazione.getRowIndex(), cellDestinazione.getColumnIndex(),
								cellDestinazione.getColumnIndex());
						groupRegions.add(newMergedRegion);
					}
				}
			} else if (table instanceof JecGroupTable) {
				JecGroupableTableModel tm = (JecGroupableTableModel) TableModelWrapperUtils.getActualTableModel(
						table.getModel(), JecGroupableTableModel.class);
				if (tm.hasGroupColumns() && tm.getRowAt(row.getRowNum() - 1) instanceof DefaultGroupRow) {
					switch (cellOrigine.getColumnIndex()) {
					case 0:
						cellDestinazione.setCellStyle(styleMap.get(ExcelCellStyle.GROUP));
						break;
					default:
						cellDestinazione.setCellValue("");
						break;
					}
				}
			}
		}

		if (subTotaliPresenti) {
			for (CellRangeAddress cellRangeAddress : groupRegions) {
				destSheet.groupRow(cellRangeAddress.getFirstRow(), cellRangeAddress.getLastRow());
			}
		}
	}

	public static void copyRow(HSSFSheet srcSheet, HSSFSheet destSheet, HSSFRow srcRow, HSSFRow destRow,
			Map<Integer, HSSFCellStyle> styleMap) {
		Set<CellRangeAddress> mergedRegions = new TreeSet<CellRangeAddress>(new Comparator<CellRangeAddress>() {

			@Override
			public int compare(CellRangeAddress o1, CellRangeAddress o2) {
				if (o1.getFirstColumn() == o2.getFirstColumn() && o1.getLastColumn() == o2.getLastColumn()
						&& o1.getFirstRow() == o2.getFirstRow() && o1.getLastRow() == o2.getLastColumn()) {
					return 0;
				} else if (o1.getFirstColumn() < o2.getFirstColumn()) {
					return 1;
				}
				return -1;
			}
		});
		destRow.setHeight(srcRow.getHeight());
		for (int j = srcRow.getFirstCellNum(); j <= srcRow.getLastCellNum(); j++) {
			HSSFCell oldCell = srcRow.getCell(j);
			HSSFCell newCell = destRow.getCell(j);
			if (oldCell != null) {
				if (newCell == null) {
					newCell = destRow.createCell(j);
				}
				copyCell(oldCell, newCell, styleMap);
				CellRangeAddress mergedRegion = getMergedRegion(srcSheet, srcRow.getRowNum(),
						(short) oldCell.getColumnIndex());
				if (mergedRegion != null) {
					CellRangeAddress newMergedRegion = new CellRangeAddress(mergedRegion.getFirstRow(),
							mergedRegion.getFirstColumn(), mergedRegion.getLastRow(), mergedRegion.getLastColumn());
					if (isNewMergedRegion(newMergedRegion, mergedRegions)) {
						mergedRegions.add(newMergedRegion);
						destSheet.addMergedRegion(newMergedRegion);
					}
				}
			}
		}

	}

	public static void copySheets(HSSFSheet newSheet, HSSFSheet sheet) {
		copySheets(newSheet, sheet, true);
	}

	public static void copySheets(HSSFSheet newSheet, HSSFSheet sheet, boolean copyStyle) {
		int maxColumnNum = 0;
		Map<Integer, HSSFCellStyle> styleMap = (copyStyle) ? new HashMap<Integer, HSSFCellStyle>() : null;
		for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
			HSSFRow srcRow = sheet.getRow(i);
			HSSFRow destRow = newSheet.createRow(i);
			if (srcRow != null) {
				Util.copyRow(sheet, newSheet, srcRow, destRow, styleMap);
				if (srcRow.getLastCellNum() > maxColumnNum) {
					maxColumnNum = srcRow.getLastCellNum();
				}
			}
		}
		for (int i = 0; i <= maxColumnNum; i++) {
			newSheet.setColumnWidth(i, sheet.getColumnWidth(i));
		}
	}

	public static CellRangeAddress getMergedRegion(HSSFSheet sheet, int rowNum, short cellNum) {
		for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
			CellRangeAddress merged = sheet.getMergedRegion(i);
			if (merged.isInRange(rowNum, cellNum)) {
				return merged;
			}
		}
		return null;
	}

	private static boolean isNewMergedRegion(CellRangeAddress newMergedRegion,
			Collection<CellRangeAddress> mergedRegions) {
		return !mergedRegions.contains(newMergedRegion);
	}

	public static boolean isSubTotal(JTable table, int rowIndex, int columnIndex) {
		boolean result = false;

		if (table instanceof AggregateTable) {
			HeaderTableModel rowHeaderModel = ((AggregateTable) table).getAggregateTableModel().getPivotDataModel()
					.getRowHeaderTableModel();
			HeaderTableModel columnHeaderModel = ((AggregateTable) table).getAggregateTableModel().getPivotDataModel()
					.getColumnHeaderTableModel();

			result = (rowHeaderModel.isSubtotalRowOrColumn(rowIndex)
					|| columnHeaderModel.isSubtotalRowOrColumn(columnIndex)
					|| rowHeaderModel.isGrandTotalRowOrColumn(rowIndex) || columnHeaderModel
					.isGrandTotalRowOrColumn(columnIndex));
		}
		return result;
	}
}
