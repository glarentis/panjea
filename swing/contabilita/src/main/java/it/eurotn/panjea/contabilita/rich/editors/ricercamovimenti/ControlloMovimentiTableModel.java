package it.eurotn.panjea.contabilita.rich.editors.ricercamovimenti;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.TreeTableModel;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;

public class ControlloMovimentiTableModel extends TreeTableModel<ControlloMovimentoRow> {

    private static final long serialVersionUID = 4796873399258612760L;

    private static final ConverterContext IMPORTO_CONTEXT = new NumberWithDecimalConverterContext();

    private static final String[] COLUMN_NAMES = { "statoAreaContabile", "dataRegistrazione", "dataDocumento",
            "numeroDocumento", "registroProtocollo", "numeroProtocollo", "tipoDocumento", "note", "sbilancio",
            "importoDare", "importoAvere" };

    {
        IMPORTO_CONTEXT.setUserObject(2);
    }

    /**
     * Costruttore.
     *
     * @param rows
     *            rows
     */
    public ControlloMovimentiTableModel(final List<ControlloMovimentoRow> rows) {
        super(rows);
    }

    @Override
    public Class<?> getCellClassAt(int row, int col) {
        return super.getCellClassAt(row, col);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return StatoAreaContabile.class;
        case 1:
        case 2:
            return Date.class;
        case 3:
            return CodiceDocumento.class;
        case 4:
            return String.class;
        case 5:
            return CodiceDocumento.class;
        case 6:
        case 7:
            return String.class;
        case 8:
        case 9:
        case 10:
            return BigDecimal.class;
        default:
            return super.getColumnClass(columnIndex);
        }
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return RcpSupport.getMessage(COLUMN_NAMES[column]);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 8:
        case 9:
        case 10:
            return IMPORTO_CONTEXT;
        default:
            return null;
        }
    }

}
