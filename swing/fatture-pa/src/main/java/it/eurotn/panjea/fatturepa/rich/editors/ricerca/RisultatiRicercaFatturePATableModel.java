package it.eurotn.panjea.fatturepa.rich.editors.ricerca;

import java.awt.Color;
import java.awt.Font;

import javax.swing.UIManager;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.StyleModel;

import it.eurotn.panjea.fatturepa.rich.editors.ricerca.xmlaction.FileXMLFatturaCellEditorRenderer;
import it.eurotn.panjea.fatturepa.util.AreaMagazzinoFatturaPARicerca;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class RisultatiRicercaFatturePATableModel extends DefaultBeanTableModel<AreaMagazzinoFatturaPARicerca>
        implements StyleModel {

    private static class DefaultCellStyle extends CellStyle {

        @Override
        public int getFontStyle() {
            return Font.PLAIN;
        }

        @Override
        public Color getForeground() {
            return UIManager.getColor("Label.foreground");
        }
    }

    private static class EsitoNegativoCellStyle extends CellStyle {

        @Override
        public int getFontStyle() {
            return Font.BOLD;
        }

        @Override
        public Color getForeground() {
            return Color.RED;
        }
    }

    private static final long serialVersionUID = -6971923108008422202L;

    private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

    private static final CellStyle DEFAULT_STYLE = new DefaultCellStyle();
    private static final CellStyle ESITO_NEGATIVO_STYLE = new EsitoNegativoCellStyle();

    {
        TOTALE_CONTEXT.setUserObject(2);
    }

    /**
     * Costruttore.
     */
    public RisultatiRicercaFatturePATableModel() {
        super("risultatiRicercaFatturePATableModel",
                new String[] { "codice", "dataDocumento", "documento.tipoDocumento", "totale", "entitaDocumento",
                        "sedeEntita", "dataRegistrazione", "stato", "fileXmlFattura", "statoFatturaPA",
                        "progressivoInvio" },
                AreaMagazzinoFatturaPARicerca.class);
    }

    @Override
    public CellStyle getCellStyleAt(int row, int column) {

        if (column != 9) {
            return DEFAULT_STYLE;
        }

        AreaMagazzinoFatturaPARicerca area = getElementAt(row);

        CellStyle style = DEFAULT_STYLE;
        if (!area.isStatoFatturaEsitoPositivo()) {
            style = ESITO_NEGATIVO_STYLE;
        }

        return style;
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int col) {
        if (col == 3) {
            return TOTALE_CONTEXT;
        }
        return null;

    }

    @Override
    public EditorContext getEditorContextAt(int row, int col) {
        if (col == 8) {
            return FileXMLFatturaCellEditorRenderer.CONTEXT;
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 8;
    }

    @Override
    public boolean isCellStyleOn() {
        return true;
    }
}
