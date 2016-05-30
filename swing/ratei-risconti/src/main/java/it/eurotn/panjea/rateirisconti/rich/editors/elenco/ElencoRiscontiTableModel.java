package it.eurotn.panjea.rateirisconti.rich.editors.elenco;

import java.awt.Color;
import java.util.Calendar;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.StyleModel;

import it.eurotn.panjea.contabilita.util.RigaElencoRiscontiDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class ElencoRiscontiTableModel extends DefaultBeanTableModel<RigaElencoRiscontiDTO> implements StyleModel {

    private static NumberWithDecimalConverterContext numberPrezzoContext;

    private static final long serialVersionUID = -5277469248616617407L;
    private static CellStyle styleRigaAnnoDiverso;
    private static CellStyle styleRigaAnnoDocumento;

    static {
        numberPrezzoContext = new NumberWithDecimalConverterContext();
        numberPrezzoContext.setUserObject(new Integer(2));

        styleRigaAnnoDocumento = new CellStyle();
        styleRigaAnnoDocumento.setForeground(Color.DARK_GRAY);
        styleRigaAnnoDocumento.setFontStyle(1);

        styleRigaAnnoDiverso = new CellStyle();
        styleRigaAnnoDiverso.setForeground(Color.BLACK);
        styleRigaAnnoDiverso.setFontStyle(0);
    }

    /**
     * Costruttore.
     */
    public ElencoRiscontiTableModel() {
        super("ElencoRiscontiTableModel",
                new String[] { "importo", "inizio", "fine", "giorni", "nota", "giorniAnno", "importoAnno",
                        "documentoAnno", "giorniSuccessivoAnno", "importoSuccessivoAnno" },
                RigaElencoRiscontiDTO.class);
    }

    @Override
    public CellStyle getCellStyleAt(int row, int column) {
        final RigaElencoRiscontiDTO riga = getElementAt(row);

        final Calendar dataInizio = Calendar.getInstance();
        dataInizio.setTime(riga.getInizio());

        final Calendar dataDocumento = Calendar.getInstance();
        dataDocumento.setTimeInMillis(0);
        if (riga.getDocumentoAnno() != null) {
            dataDocumento.setTime(riga.getDocumentoAnno().getDataDocumento());
        }

        if (dataInizio.get(Calendar.YEAR) == dataDocumento.get(Calendar.YEAR)) {
            return styleRigaAnnoDocumento;
        }
        return styleRigaAnnoDiverso;
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int col) {
        switch (col) {
        case 0:
        case 6:
        case 9:
            return numberPrezzoContext;
        default:
            return super.getConverterContextAt(row, col);
        }
    }

    @Override
    public boolean isCellStyleOn() {
        return true;
    }

}
