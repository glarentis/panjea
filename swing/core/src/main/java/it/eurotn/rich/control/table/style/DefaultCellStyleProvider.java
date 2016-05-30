/**
 * 
 */
package it.eurotn.rich.control.table.style;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.table.TableModel;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.RowStripeCellStyleProvider;
import com.jidesoft.pivot.AggregateTableModel;

/**
 * Lo stile applica un background diverso alle righe pari e dispari della tabella e un background e font diverso per le
 * righe di tipo summary o grand total.
 * 
 * @author fattazzo
 * 
 */
public class DefaultCellStyleProvider extends RowStripeCellStyleProvider {

    public static final Color EVEN_COLOR = new Color(254, 254, 254);
    public static final Color ODD_COLOR = new Color(237, 243, 254);
    public static final Color SUMMARY_COLOR = new Color(240, 253, 244);

    public static final CellStyle SUMMARY_STYLE = new CellStyle();

    static {
        SUMMARY_STYLE.setBackground(SUMMARY_COLOR);
        SUMMARY_STYLE.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        SUMMARY_STYLE.setFontStyle(Font.ITALIC);
    }

    @Override
    public Color[] getAlternativeBackgroundColors() {
        return new Color[] { EVEN_COLOR, ODD_COLOR };
    }

    @Override
    public CellStyle getCellStyleAt(TableModel tablemodel, int i, int j) {
        AggregateTableModel model = (AggregateTableModel) tablemodel;

        if (model.getPivotDataModel().getRowHeaderTableModel().isSubtotalRowOrColumn(i)
                || model.getPivotDataModel().getRowHeaderTableModel().isGrandTotalRowOrColumn(i)) {
            return SUMMARY_STYLE;
        } else {
            return super.getCellStyleAt(tablemodel, i, j);
        }
    }

}
