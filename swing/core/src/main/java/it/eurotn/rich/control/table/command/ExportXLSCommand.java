package it.eurotn.rich.control.table.command;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.hssf.HssfTableFormat;
import com.jidesoft.hssf.HssfTableUtils;
import com.jidesoft.hssf.HssfTableUtils.CellValueConverter;
import com.jidesoft.pivot.AggregateTable;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.ITable;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

/**
 *
 * Esporta la tabella in un file in formato excel e apre il file.
 *
 * @author giangi
 * @version 1.0, 08/set/2014
 *
 */
public class ExportXLSCommand extends AbstractExportTableCommand {

    public enum ExcelCellStyle {
        TITLE, SUBTOTAL, GROUP
    }

    private class ExcelExportCellValueConverter extends HssfTableUtils.DefaultCellValueConverter {

        @Override
        public Object convert(JTable jtable, Object obj, int row, int col) {
            Object result = null;
            if (obj != null) {
                try {
                    result = obj;
                    int col2 = TableModelWrapperUtils.getActualColumnAt(jtable.getModel(), col);

                    ContextSensitiveTableModel cstm = (ContextSensitiveTableModel) TableModelWrapperUtils
                            .getActualTableModel(jtable.getModel(), ContextSensitiveTableModel.class);

                    ConverterContext context = cstm.getConverterContextAt(row, col2);

                    ObjectConverter objectConverter = ObjectConverterManager.getConverter(obj.getClass(), context);

                    if (obj instanceof Importo) {
                        result = ((Importo) obj).getImportoInValuta();
                    } else if (!(obj instanceof Number) && !(obj instanceof java.util.Date)) {
                        if (objectConverter.supportToString(obj, context)) {
                            result = objectConverter.toString(obj, context);
                        }
                    }
                } catch (Throwable e) {
                    logger.error("errore nella conversione del file " + e.getMessage());
                }
            }
            return result;
        }

        @Override
        public int getDataFormat(JTable jtable, Object obj, int row, int col) {
            HSSFWorkbook wb = (HSSFWorkbook) getTable().getClientProperty("HssfTableUtils.HSSFWorkbook");
            DataFormat df = wb.createDataFormat();
            int result = super.getDataFormat(jtable, obj, row, col);

            try {
                if (obj != null && (obj.getClass().equals(BigDecimal.class) || obj.getClass().equals(Double.class))) {
                    ContextSensitiveTableModel cstm = (ContextSensitiveTableModel) TableModelWrapperUtils
                            .getActualTableModel(jtable.getModel(), ContextSensitiveTableModel.class);

                    ConverterContext context = cstm.getConverterContextAt(row, col);

                    if (context != null && context instanceof NumberWithDecimalConverterContext) {
                        int numeroDecimali = 0;

                        if (context.getUserObject() != null) {
                            numeroDecimali = (Integer) context.getUserObject();
                            result = df.getFormat(NUMBER_FORMAT + "." + STR_ZERI.substring(0, numeroDecimali));
                        }
                    }
                } else if (obj instanceof Date) {
                    result = 0xe;
                } else if (obj instanceof Integer) {
                    result = 1;
                } else if (obj instanceof Importo) {
                    result = df.getFormat(NUMBER_FORMAT + "." + STR_ZERI.substring(0, 6));
                }
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("-->riga: " + row + " colonna: " + col);
                }
            }
            // se non ho associato nessun tipo per (-1) allora imposto il tipo Generale (0)
            if (result == -1) {
                result = 0x31;
            }
            return result;
        }
    }

    private class ExcelTableFormat extends HssfTableFormat {
        @Override
        public short getBottomBorder() {
            return CellStyle.BORDER_THIN;
        }

        @Override
        public CellValueConverter getCellValueConverter() {
            return new ExcelExportCellValueConverter();
        }

        @Override
        public String getHeader() {
            return "header";
        }

        @Override
        public short getLeftBorder() {
            return CellStyle.BORDER_THIN;
        }

        @Override
        public short getRightBorder() {
            return CellStyle.BORDER_THIN;
        }

        @Override
        public short getTopBorder() {
            return CellStyle.BORDER_THIN;
        }

        @Override
        public boolean isAutoSizeColumns() {
            return true;
        }

        @Override
        public boolean isFreezePanes() {
            return true;
        }

        @Override
        public boolean isIncludeTableHeader() {
            return true;
        }

        @Override
        public boolean isPrintFitToPage() {
            return true;
        }

        @Override
        public boolean isPrintLandscape() {
            return true;
        }

    }

    public static final String COMMAND_ID = "exportXLSCommand";

    private static final Logger LOGGER = Logger.getLogger(ExportXLSCommand.class);

    public static final String STR_ZERI = "0000000000000000000000000";
    private static final String NUMBER_FORMAT = "###,###,###,##0";

    /**
     * Command per esportare una tabella in un foglio excel.
     *
     * @param table
     *            tabella da esportare
     */
    public ExportXLSCommand(final JTable table) {
        super(COMMAND_ID, "xls", table);
    }

    private Map<ExcelCellStyle, HSSFCellStyle> createCellStyles(HSSFSheet sheet) {

        // Stile dei subtotali
        HSSFCellStyle subTotalCellStyle = sheet.getWorkbook().createCellStyle();
        HSSFFont font = sheet.getWorkbook().createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.BLACK.index);
        subTotalCellStyle.setFont(font);
        subTotalCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        subTotalCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        subTotalCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        subTotalCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        subTotalCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        subTotalCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        subTotalCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        Map<ExcelCellStyle, HSSFCellStyle> map = new HashMap<ExcelCellStyle, HSSFCellStyle>();
        map.put(ExcelCellStyle.SUBTOTAL, subTotalCellStyle);

        // Stile dei titoli
        font = sheet.getWorkbook().createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.BLACK.index);
        HSSFCellStyle titleCellStyle = sheet.getWorkbook().createCellStyle();
        titleCellStyle.setFont(font);
        titleCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        titleCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        titleCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        map.put(ExcelCellStyle.TITLE, titleCellStyle);

        // Stile righe raggruppate
        font = sheet.getWorkbook().createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.BLACK.index);
        HSSFCellStyle groupCellStyle = sheet.getWorkbook().createCellStyle();
        groupCellStyle.setFont(font);
        map.put(ExcelCellStyle.GROUP, groupCellStyle);
        return map;
    }

    @Override
    public boolean export(File fileToExport) {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("panjeaExport");

            if (getTable() instanceof ITable<?>) {

                HSSFSheet sheetOrig = wb.createSheet("panjeaExportTmp");
                HssfTableUtils.exportToSheet(getTable(), wb, sheetOrig, new ExcelTableFormat());
                Map<ExcelCellStyle, HSSFCellStyle> mapStyles = createCellStyles(sheet);

                // se ho il totale generale sulla prima riga devo prendere la riga successiva che non sia un subtotale.
                // teoricamente la seconda riga può andare bene ma per sicurezza ciclo finchè non trovo una riga
                // presente nel tablemodel originale

                TableModel tm = TableModelWrapperUtils.getActualTableModel(getTable().getModel());

                HSSFRow row = sheetOrig.getRow(0);
                int addedColumn = 0;
                int aggregateAddedColumn = 0;
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    int actualColumn = ((ITable<?>) getTable()).getActualColumn(i);
                    if (actualColumn == -1) {
                        continue;
                    }
                    HSSFCell cell = row.getCell(i);
                    Object valueTable = tm.getValueAt(0, actualColumn);

                    boolean isComposite = false;

                    if (valueTable != null) {
                        ObjectConverter converter = ObjectConverterManager.getConverter(valueTable.getClass());
                        isComposite = converter instanceof PanjeaCompositeConverter;
                        if (converter instanceof PanjeaCompositeConverter) {
                            Util.copyColumn(sheetOrig, sheet, cell.getColumnIndex(),
                                    cell.getColumnIndex() + addedColumn, mapStyles, getTable(), 1, addedColumn);
                            sheet.autoSizeColumn(cell.getColumnIndex());
                            addedColumn++;
                            if (getTable() instanceof AggregateTable) {
                                if (cell.getColumnIndex() <= ((AggregateTable) getTable()).getAggregateTableModel()
                                        .getAggregatedColumnCount()) {
                                    aggregateAddedColumn++;
                                }
                            }
                            Util.copyColumn(sheetOrig, sheet, cell.getColumnIndex(),
                                    cell.getColumnIndex() + addedColumn, mapStyles, getTable(), 2, addedColumn);
                            sheet.autoSizeColumn(cell.getColumnIndex());
                        }
                    }

                    if (!isComposite) {
                        Util.copyColumn(sheetOrig, sheet, cell.getColumnIndex(), cell.getColumnIndex() + addedColumn,
                                mapStyles, getTable(), -1, addedColumn);
                        sheet.autoSizeColumn(cell.getColumnIndex());
                    }
                }
                if (getTable() instanceof AggregateTable) {
                    sheet.createFreezePane(
                            ((AggregateTable) getTable()).getAggregateTableModel().getAggregatedColumnCount()
                                    + aggregateAddedColumn,
                            1);
                }

                wb.removeSheetAt(1);
            } else {
                HssfTableUtils.exportToSheet(getTable(), wb, sheet, new ExcelTableFormat());
            }

            try (FileOutputStream fileOut = new FileOutputStream(fileToExport)) {
                wb.write(fileOut);
                fileOut.flush();
                fileOut.close();
            }
        } catch (Exception e) {
            logger.error("-->errore nell'esportare la tabella", e);
            return false;
        }

        return true;
    }
}
