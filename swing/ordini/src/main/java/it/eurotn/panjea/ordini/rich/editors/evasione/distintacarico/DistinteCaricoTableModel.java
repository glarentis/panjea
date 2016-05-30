package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico;

import java.awt.Color;
import java.awt.Font;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.StyleModel;

import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.rich.bd.AnagraficaOrdiniBD;
import it.eurotn.panjea.ordini.rich.bd.IAnagraficaOrdiniBD;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.bd.OrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.editors.evasione.StatoRigaDistintaCaricoCellRenderer;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class DistinteCaricoTableModel extends DefaultBeanTableModel<RigaDistintaCarico>implements StyleModel {
    private static class RigheDaEvadereCellStyle extends CellStyle {

        @Override
        public int getFontStyle() {
            return Font.BOLD;
        }

        @Override
        public Color getForeground() {
            return new Color(230, 138, 0);
        }
    }

    private static final long serialVersionUID = 7835136378206552142L;

    private static final ConverterContext NUMBERQTACONVERSIONCONTEXT = new NumberWithDecimalConverterContext();

    private static final EditorContext NUMBERQTAEDITORCONTEXT = new EditorContext("numberQtaEditorContext");

    private static final int FIXEDCOLUMNS = 16;

    private static RigheDaEvadereCellStyle righeDaEvadereCellStyle;

    {
        NUMBERQTACONVERSIONCONTEXT.setUserObject(6);
        NUMBERQTAEDITORCONTEXT.setUserObject(6);
        righeDaEvadereCellStyle = new RigheDaEvadereCellStyle();
    }

    private IOrdiniDocumentoBD ordiniDocumentoBD;
    private IAnagraficaOrdiniBD anagraficaOrdiniBD;
    private String[] attributiPresenti = null;

    /**
     *
     * Costruttore.
     */
    public DistinteCaricoTableModel() {
        super("distinteCaricoTableModel",
                new String[] { "stato", "distintaCarico.id", "entita", "articolo", "qtaDaEvadere", "qtaEvasa",
                        "forzata", "numeroDocumento", "sedeEntita",
                        "sedeEntita.sede.datiGeografici.livelloAmministrativo1.nome",
                        "sedeEntita.sede.datiGeografici.livelloAmministrativo2.nome",
                        "sedeEntita.sede.datiGeografici.livelloAmministrativo3.nome", "rigaArticolo.areaOrdine.agente",
                        "rigaArticolo.areaOrdine.dataInizioTrasporto",
                        "rigaArticolo.areaOrdine.riferimentiOrdine.dataOrdine",
                        "rigaArticolo.areaOrdine.riferimentiOrdine.numeroOrdine" },
                RigaDistintaCarico.class);
        this.ordiniDocumentoBD = RcpSupport.getBean(OrdiniDocumentoBD.BEAN_ID);
        this.anagraficaOrdiniBD = RcpSupport.getBean(AnagraficaOrdiniBD.BEAN_ID);
        String attributiPresentiSettings = anagraficaOrdiniBD.caricaOrdiniSettings().getAttributiMissioni();
        attributiPresenti = attributiPresentiSettings.split(",");
    }

    @Override
    protected String[] createColumnNames(String[] propertyColumnNames) {
        String[] colNames = super.createColumnNames(propertyColumnNames);
        return ArrayUtils.addAll(colNames, attributiPresenti);
    }

    @Override
    public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
        RigaDistintaCarico riga = getElementAt(rowIndex);
        if (riga.getQtaEvasa() > 0) {
            return righeDaEvadereCellStyle;
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column < FIXEDCOLUMNS) {
            return super.getColumnClass(column);
        }
        return String.class;
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 4:
        case 5:
            return NUMBERQTACONVERSIONCONTEXT;
        default:
            return null;
        }
    }

    @Override
    public EditorContext getEditorContextAt(int row, int column) {
        if (row == -1) {
            return StatoRigaDistintaCaricoCellRenderer.STATO_RIGA_DISTINTA_CARICO_CONTEXT;
        }
        switch (column) {
        case 4:
        case 5:
            return NUMBERQTAEDITORCONTEXT;
        default:
            return null;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex < FIXEDCOLUMNS) {
            return super.getValueAt(rowIndex, columnIndex);
        }
        RigaDistintaCarico riga = getElementAt(rowIndex);
        if (riga != null) {
            for (AttributoRiga attributoRiga : riga.getRigaArticolo().getAttributi()) {
                if (attributoRiga.getTipoAttributo().getCodice()
                        .equals(attributiPresenti[columnIndex - FIXEDCOLUMNS])) {
                    return attributoRiga.getValore();
                }
            }
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column) {
        case 5:
            return true;
        default:
            return false;
        }
    }

    @Override
    public boolean isCellStyleOn() {
        return true;
    }

    @Override
    public void setValueAt(Object editedValue, int row, int column) {
        super.setValueAt(editedValue, row, column);

        // se si stà azzerando la quantità evasa vado a cancellare tutte le eventuali righe lotto
        // presenti
        if (column == 5 && editedValue != null && ((Double) editedValue) == 0.0) {
            ordiniDocumentoBD.cancellaRigheDistintaCaricoLotti(getElementAt(row));
        }
    }

}
