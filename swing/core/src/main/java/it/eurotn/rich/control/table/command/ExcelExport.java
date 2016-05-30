package it.eurotn.rich.control.table.command;

import java.math.BigDecimal;
import java.util.Date;

import javax.swing.JTable;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.pivot.AggregateTableModel;
import com.jidesoft.pivot.HeaderTableModel;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

public class ExcelExport {

    private static final Logger LOGGER = Logger.getLogger(ExcelExport.class);

    private JTable table;
    private Workbook wb;
    private String[] formatDecimali;
    private CellStyle[] stiliColonne;
    private AggregateTableModel aggregateTableModel;

    {
        // Per prestazione creo dei formatter predefiniti
        formatDecimali = new String[7];
        formatDecimali[0] = "###,###,###,##0";
        formatDecimali[1] = "###,###,###,##0.0";
        formatDecimali[2] = "###,###,###,##0.00";
        formatDecimali[3] = "###,###,###,##0.000";
        formatDecimali[4] = "###,###,###,##0.0000";
        formatDecimali[5] = "###,###,###,##0.00000";
        formatDecimali[6] = "###,###,###,##0.000000";
    }

    /**
     *
     * Costruttore.
     *
     * @param table
     *            tabella
     * @param wb
     *            workbook
     */
    public ExcelExport(final JTable table, final Workbook wb) {
        this.table = table;
        this.wb = wb;
        this.aggregateTableModel = ((AggregateTable) table).getAggregateTableModel();
        stiliColonne = new CellStyle[table.getColumnCount()];
    }

    public Object converti(int row, int col, int visualizzazione) {
        Object value = table.getValueAt(row, col);
        if (value == null) {
            return "";
        }

        Object result = value;
        int actualColumn = TableModelWrapperUtils.getActualColumnAt(table.getModel(), col);
        ContextSensitiveTableModel cstm = (ContextSensitiveTableModel) TableModelWrapperUtils
                .getActualTableModel(table.getModel(), ContextSensitiveTableModel.class);

        if (value instanceof Number || value instanceof Date) {
            result = value;
        } else if (value instanceof Importo) {
            result = ((Importo) value).getImportoInValuta();
        } else {
            ConverterContext context = cstm.getConverterContextAt(row, actualColumn);
            ObjectConverter objectConverter = ObjectConverterManager.getConverter(value.getClass(), context);
            if (objectConverter instanceof PanjeaCompositeConverter) {
                result = ((PanjeaCompositeConverter) objectConverter).getCampo(value, visualizzazione);
            } else {
                result = objectConverter.toString(value, context);
            }
        }
        return result;
    }

    /**
     * @return header style
     */
    public CellStyle creaHeaderStyle() {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        return style;
    }

    public CellStyle creaStyle(int columnIndex) {
        if (stiliColonne[columnIndex] == null) {
            CellStyle style = wb.createCellStyle();
            style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
            style.setAlignment(getDataFormat(0, columnIndex) == 0x31 ? CellStyle.ALIGN_LEFT : CellStyle.ALIGN_RIGHT);
            style.setDataFormat(getDataFormat(0, columnIndex));
            stiliColonne[columnIndex] = style;
        }
        return stiliColonne[columnIndex];
    }

    public ObjectConverter getConverter(int rowIndex, int columnIndex) {
        Object value = table.getValueAt(rowIndex, columnIndex);
        if (value == null) {
            return null;
        }
        int actualColumn = TableModelWrapperUtils.getActualColumnAt(table.getModel(), columnIndex);
        ContextSensitiveTableModel cstm = (ContextSensitiveTableModel) TableModelWrapperUtils
                .getActualTableModel(table.getModel(), ContextSensitiveTableModel.class);
        ConverterContext context = cstm.getConverterContextAt(rowIndex, actualColumn);
        return ObjectConverterManager.getConverter(value.getClass(), context);
    }

    public short getDataFormat(int row, int col) {
        DataFormat df = wb.createDataFormat();
        short result = 0x31;
        int numeroDecimali = 6;
        ContextSensitiveTableModel cstm = (ContextSensitiveTableModel) TableModelWrapperUtils
                .getActualTableModel(table.getModel(), ContextSensitiveTableModel.class);
        ConverterContext context = cstm.getConverterContextAt(row, col);
        if (context != null && context instanceof NumberWithDecimalConverterContext) {
            if (context.getUserObject() != null && (Integer) context.getUserObject() <= 6) {
                numeroDecimali = (Integer) context.getUserObject();
            }
        }
        try {
            Object obj = table.getValueAt(row, col);
            if (obj != null && (obj.getClass().equals(BigDecimal.class) || obj.getClass().equals(Double.class))) {
                result = df.getFormat(formatDecimali[numeroDecimali]);
            } else if (obj instanceof Date) {
                result = 0xe;
            } else if (obj instanceof Integer) {
                result = 1;
            } else if (obj instanceof Importo) {
                result = df.getFormat(formatDecimali[numeroDecimali]);
            }
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("-->riga: " + row + " colonna: " + col);
            }
        }
        return result;
    }

    public boolean isColonnaAggregata(int columnIndex) {
        return columnIndex < aggregateTableModel.getAggregatedColumnCount();
    }

    public boolean isColonnaComposita(int columnIndex) {
        ObjectConverter converter = getConverter(0, columnIndex);
        return converter instanceof PanjeaCompositeConverter;
    }

    public boolean isRigaPrecedenteUguale(int rowIndex, int columnIndex) {
        boolean result = true;
        for (int i = 0; i <= columnIndex; i++) {
            result = result && ObjectUtils.equals(table.getModel().getValueAt(rowIndex, i),
                    table.getModel().getValueAt(rowIndex - 1, i));
        }
        return result;
    }

    public boolean isSubTotal(int rowIndex, int columnIndex) {
        HeaderTableModel rowHeaderModel = ((AggregateTable) table).getAggregateTableModel().getPivotDataModel()
                .getRowHeaderTableModel();
        HeaderTableModel columnHeaderModel = ((AggregateTable) table).getAggregateTableModel().getPivotDataModel()
                .getRowHeaderTableModel();
        return (rowHeaderModel.isSubtotalRowOrColumn(rowIndex) || columnHeaderModel.isSubtotalRowOrColumn(columnIndex)
                || rowHeaderModel.isGrandTotalRowOrColumn(rowIndex)
                || columnHeaderModel.isGrandTotalRowOrColumn(columnIndex));
    }
}
