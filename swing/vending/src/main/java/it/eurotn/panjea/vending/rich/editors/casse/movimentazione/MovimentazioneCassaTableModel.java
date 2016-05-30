package it.eurotn.panjea.vending.rich.editors.casse.movimentazione;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.StyleModel;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class MovimentazioneCassaTableModel extends DefaultBeanTableModel<MovimentoCassa>implements StyleModel {

    private static class AperturaStyle extends CellStyle {

        @Override
        public Color getBackground() {
            return Color.GREEN;
        }

        @Override
        public Border getBorder() {
            return BorderFactory.createLineBorder(Color.BLACK, 1);
        }

        @Override
        public int getFontStyle() {
            return Font.BOLD;
        }
    }

    private static final long serialVersionUID = -8913854939984857998L;

    private static ConverterContext numberPrezzoContext = new NumberWithDecimalConverterContext(2);

    private static final AperturaStyle APERTURA_STYLE = new AperturaStyle();

    /**
     * Costruttore.
     */
    public MovimentazioneCassaTableModel() {
        super("movimentazioneCassaTableModel", new String[] { "data", "totaleEntrate", "totaleUscite", "totale" },
                MovimentoCassa.class);
    }

    @Override
    public CellStyle getCellStyleAt(int row, int column) {
        MovimentoCassa movimentoCassa = getElementAt(row);

        return movimentoCassa.isApertura() ? APERTURA_STYLE : null;
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {

        if (column >= 1) {
            return numberPrezzoContext;
        }

        return super.getConverterContextAt(row, column);
    }

    @Override
    public boolean isCellStyleOn() {
        return true;
    }
}
