package it.eurotn.panjea.ordini.rich.editors.evasione.carrello;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.UIManager;

import org.apache.commons.lang3.ObjectUtils;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.NavigableTableModel;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.utils.DateUtils;

import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class CarrelloEvasioneOrdiniTableModel extends DefaultBeanTableModel<RigaDistintaCarico>
        implements NavigableTableModel, StyleModel {

    private static class CarrelloDefaultCellStyle extends CellStyle {

        @Override
        public int getFontStyle() {
            return Font.PLAIN;
        }

        @Override
        public Color getForeground() {
            return UIManager.getColor("Label.foreground");
        }
    }

    private static class CarrelloEvasoParzialeCellStyle extends CellStyle {

        @Override
        public int getFontStyle() {
            return Font.BOLD;
        }

        @Override
        public Color getForeground() {
            return EVASO_PARZIALE_COLOR;
        }
    }

    private static class CarrelloGiacenzaZeroCellStyle extends CellStyle {

        @Override
        public int getFontStyle() {
            return Font.BOLD;
        }

        @Override
        public Color getForeground() {
            return new Color(230, 138, 0);
        }
    }

    private static class CarrelloResiduoNegativoCellStyle extends CellStyle {

        @Override
        public int getFontStyle() {
            return Font.BOLD;
        }

        @Override
        public Color getForeground() {
            return RESIDUO_NEGATIVO_COLOR;
        }
    }

    private static final long serialVersionUID = 1063283163610414948L;

    private static final ConverterContext NUMBERQTACONVERSIONCONTEXT = new NumberWithDecimalConverterContext();

    private static final EditorContext NUMBERQTAEDITORCONTEXT = new EditorContext("numberQtaEditorContext");

    public static final Color EVASO_PARZIALE_COLOR = new Color(0, 153, 255);

    public static final Color RESIDUO_NEGATIVO_COLOR = Color.RED;

    private static CarrelloResiduoNegativoCellStyle carrelloResiduoNegativoCellStyle;
    private static CarrelloDefaultCellStyle carrelloDefaultCellStyle;
    private static CarrelloEvasoParzialeCellStyle carrelloEvasoParzialeCellStyle;
    private static CarrelloGiacenzaZeroCellStyle carrelloGiacenzaZeroCellStyle;

    static {
        carrelloResiduoNegativoCellStyle = new CarrelloResiduoNegativoCellStyle();
        carrelloDefaultCellStyle = new CarrelloDefaultCellStyle();
        carrelloEvasoParzialeCellStyle = new CarrelloEvasoParzialeCellStyle();
        carrelloGiacenzaZeroCellStyle = new CarrelloGiacenzaZeroCellStyle();
        NUMBERQTACONVERSIONCONTEXT.setUserObject(6);
        NUMBERQTAEDITORCONTEXT.setUserObject(6);
    }

    private GiacenzaCalculator giacenzaCalculator;

    /**
     * Costruttore.
     */
    public CarrelloEvasioneOrdiniTableModel() {
        super("carrelloEvasioneOrdiniTableModel",
                new String[] { "dataConsegna", "entita", "deposito", "articolo", "sostituita", "qtaOrdinata",
                        "qtaEvasa", "qtaDaEvadere", "qtaResidua", "qtaGiacenza", "qtaGiacenzaAttuale", "forzata",
                        "numeroDocumento", "sedeEntita", "sedeEntita.sede.datiGeografici.livelloAmministrativo1.nome",
                        "sedeEntita.sede.datiGeografici.livelloAmministrativo2.nome",
                        "sedeEntita.sede.datiGeografici.livelloAmministrativo3.nome", },
                RigaDistintaCarico.class);
    }

    @Override
    public void addObject(RigaDistintaCarico object) {
        Collection<RigaDistintaCarico> objects = new ArrayList<RigaDistintaCarico>(1);
        objects.add(object);
        this.giacenzaCalculator.calculate(objects);
        super.addObject(object);
        aggiornaGiacenzaAttuale();
    }

    @Override
    public void addObjects(List<RigaDistintaCarico> objects) {
        this.giacenzaCalculator.calculate(objects);
        super.addObjects(objects);
        aggiornaGiacenzaAttuale();
    }

    /**
     * Aggiorna la giacenza attuale di tutte le righe.
     */
    public void aggiornaGiacenzaAttuale() {
        GiacenzaAttualeCalculator.calcola(getObjects(), giacenzaCalculator.getMapGiacenze());
    }

    @Override
    public CellStyle getCellStyleAt(int row, int col) {
        int actualRow = TableModelWrapperUtils.getActualRowAt(this, row);
        if (actualRow == -1) {
            return null;
        }

        RigaDistintaCarico rigaEvasione = getObject(actualRow);

        Double qtaGiacAttuale = ObjectUtils.defaultIfNull(rigaEvasione.getQtaGiacenzaAttuale(), 0.0);
        if (qtaGiacAttuale.compareTo(new Double(0.0)) < 0) {
            return carrelloResiduoNegativoCellStyle;
        } else {
            Double qtaGiacenza = ObjectUtils.defaultIfNull(rigaEvasione.getQtaGiacenza(), 0.0);
            if (qtaGiacenza.compareTo(0.0) == 0 && rigaEvasione.getRigheSostituzione().isEmpty()) {
                return carrelloGiacenzaZeroCellStyle;
            }

            Double qtaResidua = rigaEvasione.getQtaResidua();
            if (qtaResidua.compareTo(new Double(0.0)) < 0) {
                return carrelloResiduoNegativoCellStyle;
            } else if (qtaResidua.compareTo(new Double(0.0)) > 0) {
                return carrelloEvasoParzialeCellStyle;
            }
        }
        return carrelloDefaultCellStyle;
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
            return NUMBERQTACONVERSIONCONTEXT;
        default:
            return null;
        }
    }

    @Override
    public EditorContext getEditorContextAt(int row, int column) {
        switch (column) {
        case 7:
            return QtaDaEvadereCellRenderer.QTA_DA_EVADERE_CELL_RENDERER_CONTEXT;
        default:
            return null;
        }
    }

    /**
     * @return the giacenzaCalculator
     */
    public GiacenzaCalculator getGiacenzaCalculator() {
        return giacenzaCalculator;
    }

    /**
     * Restituisce la data maggiore delle aree ordine contenute nel modello.
     *
     * @return data massima
     */
    public Date getMaxDate() {

        List<Date> dates = new ArrayList<Date>();

        for (RigaDistintaCarico rigaEvasione : getObjects()) {
            dates.add(rigaEvasione.getDataRegistrazione());
        }

        return DateUtils.maxDates(dates);
    }

    /**
     * Resituisce le righe da evadere non considerando quelle con quantità da evadere uguale a 0.
     *
     * @return lista di {@link RigaDistintaCarico}
     */
    public List<RigaDistintaCarico> getRigheDaEvadere() {

        List<RigaDistintaCarico> righe = new ArrayList<RigaDistintaCarico>();

        for (RigaDistintaCarico rigaEvasione : getObjects()) {
            if (rigaEvasione.getQtaDaEvadere().compareTo(0.0) != 0) {
                righe.add(rigaEvasione);
            }
        }

        return righe;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column) {
        case 7:
        case 11:
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
    public boolean isNavigableAt(int row, int column) {
        return isCellEditable(row, column);
    }

    @Override
    public boolean isNavigationOn() {
        return true;
    }

    /**
     * @return <code>true</code> se i dati del carrello sono validi
     */
    public boolean isValid() {

        boolean valid = true;

        // controllo che non ci siano righe con qta residua o giacenza attuale
        // minori di 0
        for (RigaDistintaCarico rigaEvasione : getObjects()) {
            if (rigaEvasione.getQtaResidua().compareTo(0.0) < 0
                    || rigaEvasione.getQtaGiacenzaAttuale().compareTo(0.0) < 0) {
                valid = false;
                break;
            }
        }

        return valid;
    }

    @Override
    public void removeObject(RigaDistintaCarico riga) {
        super.removeObject(riga);
        // aggiorno la giacenza attuale per le righe che hanno l'articolo e il
        // deposito della riga modificata
        GiacenzaAttualeCalculator.calcola(getObjects(), riga.getArticolo(), riga.getDeposito(),
                giacenzaCalculator.getMapGiacenze());
    }

    /**
     * Aggiorna la giacenza attuale di tutte le righe ricalcolando le giacenze.
     */
    public void ricalcolaGiacenze() {
        giacenzaCalculator.ricalculate(getObjects());
        GiacenzaAttualeCalculator.calcola(getObjects(), giacenzaCalculator.getMapGiacenze());
    }

    /**
     * @param giacenzaCalculator
     *            the giacenzaCalculator to set
     */
    public void setGiacenzaCalculator(GiacenzaCalculator giacenzaCalculator) {
        this.giacenzaCalculator = giacenzaCalculator;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        super.setValueAt(value, row, col);
        // aggiorno la giacenza attuale per le righe che hanno l'articolo e il
        // deposito della riga modificata
        RigaDistintaCarico riga = getObject(row);
        GiacenzaAttualeCalculator.calcola(getObjects(), riga.getArticolo(), riga.getDeposito(),
                giacenzaCalculator.getMapGiacenze());
    }
}
