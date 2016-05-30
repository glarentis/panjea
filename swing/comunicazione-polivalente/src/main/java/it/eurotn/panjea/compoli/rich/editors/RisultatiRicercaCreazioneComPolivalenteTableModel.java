package it.eurotn.panjea.compoli.rich.editors;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.StyleModel;

import it.eurotn.panjea.contabilita.util.DocumentoSpesometro;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

/**
 * @author fattazzo
 *
 */
public class RisultatiRicercaCreazioneComPolivalenteTableModel extends DefaultBeanTableModel<DocumentoSpesometro>
        implements StyleModel {

    private static final long serialVersionUID = -2011875533979254621L;

    private static final CellStyle RED_CELL_STYLE;

    private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

    static {
        RED_CELL_STYLE = new CellStyle();
        RED_CELL_STYLE.setText(null);
        RED_CELL_STYLE.setToolTipText(null);
        RED_CELL_STYLE.setBorder(null);
        RED_CELL_STYLE.setIcon(null);
        RED_CELL_STYLE.setFontStyle(-1);
        RED_CELL_STYLE.setForeground(Color.BLACK);
        RED_CELL_STYLE.setBackground(Color.RED);
    }

    {
        TOTALE_CONTEXT.setUserObject(2);
    }

    /**
     * Costruttore.
     */
    public RisultatiRicercaCreazioneComPolivalenteTableModel() {
        super("risultatiRicercaCreazioneComPolivalenteTableModel",
                new String[] { "entita", "partitaIvaEntita", "codiceFiscaleEntita", "tipoDocumento", "codiceDocumento",
                        "dataRegistrazione", "imponibile", "imposta", "anagrafica", "riepilogativo" },
                DocumentoSpesometro.class);
    }

    @Override
    public CellStyle getCellStyleAt(int row, int column) {

        CellStyle cellStyle;

        DocumentoSpesometro documentoSpesometro = getElementAt(row);

        if (documentoSpesometro.getEntita().isRiepilogativo()) {
            return null;
        }

        switch (column) {
        case 1:
            cellStyle = getPartitaIvaCellStyle(documentoSpesometro);
            break;
        case 2:
            cellStyle = getCodiceFiscaleCellStyle(documentoSpesometro);
            break;
        default:
            cellStyle = null;
            break;
        }

        return cellStyle;
    }

    private CellStyle getCodiceFiscaleCellStyle(DocumentoSpesometro doc) {

        CellStyle cellStyle = null;

        // se il documento è attivo ma non c'è partita iva e codice fiscale e l'entità non è riepilogativa lo
        // evidenzio settando il background arancione
        if (doc.isAttivo() && !doc.getEntita().isRiepilogativo() && StringUtils.isEmpty(doc.getPartitaIvaEntita())
                && StringUtils.isEmpty(doc.getCodiceFiscaleEntita())) {
            cellStyle = RED_CELL_STYLE;
        }

        return cellStyle;
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 6:
        case 7:
            return TOTALE_CONTEXT;
        default:
            return null;
        }
    }

    private CellStyle getPartitaIvaCellStyle(DocumentoSpesometro doc) {

        CellStyle cellStyle = null;

        if (doc.isAttivo()) {
            // se il documento è attivo ma non c'è partita iva e codice fiscale e l'entità non è riepilogativa lo
            // evidenzio settando il background arancione
            if (StringUtils.isEmpty(doc.getPartitaIvaEntita()) && StringUtils.isEmpty(doc.getCodiceFiscaleEntita())) {
                cellStyle = RED_CELL_STYLE;
            }
        } else {
            // se il documento è passivo devo avere la partita iva, se non c'è coloro il background di rosso
            cellStyle = StringUtils.isEmpty(doc.getPartitaIvaEntita()) ? RED_CELL_STYLE : null;
        }

        return cellStyle;
    }

    @Override
    public boolean isCellStyleOn() {
        return true;
    }
}
