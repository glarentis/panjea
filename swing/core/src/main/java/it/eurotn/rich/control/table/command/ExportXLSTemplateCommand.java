package it.eurotn.rich.control.table.command;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.JTable;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.hssf.HssfTableFormat;
import com.jidesoft.hssf.HssfTableUtils;
import com.jidesoft.hssf.HssfTableUtils.CellValueConverter;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;

public class ExportXLSTemplateCommand extends AbstractExportTableCommand {

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
                    LOGGER.error("errore nella conversione del file " + e.getMessage());
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
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("-->riga: " + row + " colonna: " + col);
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

    private static final Logger LOGGER = Logger.getLogger(ExportXLSTemplateCommand.class);

    public static final String STR_ZERI = "0000000000000000000000000";
    private static final String NUMBER_FORMAT = "###,###,###,##0";

    /**
     * Command per esportare una tabella in un foglio excel.
     *
     * @param table
     *            tabella da esportare
     */
    public ExportXLSTemplateCommand(final JTable table) {
        super(COMMAND_ID, "xls", table);
    }

    @Override
    public boolean export(File fileToExport) {
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("panjeaExport");

            HssfTableUtils.exportToSheet(getTable(), wb, sheet, new ExcelTableFormat());

            FileOutputStream fileOut = new FileOutputStream(fileToExport);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            LOGGER.error("-->errore nell'esportare la tabella", e);
            return false;
        }

        return true;
    }
}
