package it.eurotn.rich.control.table.style;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTable;

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.TableModelWrapperUtils;

public class GrupingRowStripeTableStyleProvider extends RowStripeTableStyleProvider {

    public static final Color SUMMARY_COLOR_LVL0 = new Color(90, 180, 200);
    public static final Color SUMMARY_COLOR_LVL1 = new Color(110, 210, 255);
    public static final Color SUMMARY_COLOR_LVL2 = new Color(90, 190, 50);
    public static final Color SUMMARY_COLOR_LVL3 = new Color(60, 210, 100);
    public static final Color SUMMARY_COLOR_LVL4 = new Color(180, 180, 255);
    public static final CellStyle SUMMARY_STYLE_LVL0 = new CellStyle();
    public static final CellStyle SUMMARY_STYLE_LVL1 = new CellStyle();
    public static final CellStyle SUMMARY_STYLE_LVL2 = new CellStyle();
    public static final CellStyle SUMMARY_STYLE_LVL3 = new CellStyle();
    public static final CellStyle SUMMARY_STYLE_LVL4 = new CellStyle();

    private static final CellStyle[] CELL_STYLES_LEVELS = new CellStyle[] { SUMMARY_STYLE_LVL0, SUMMARY_STYLE_LVL1,
            SUMMARY_STYLE_LVL2, SUMMARY_STYLE_LVL3, SUMMARY_STYLE_LVL4 };

    static {
        // SUMMARY_STYLE_LVL0.setForeground(SUMMARY_COLOR_LVL0);
        SUMMARY_STYLE_LVL0.setFontStyle(Font.BOLD);

        // SUMMARY_STYLE_LVL1.setForeground(SUMMARY_COLOR_LVL1);
        SUMMARY_STYLE_LVL1.setFontStyle(Font.BOLD);

        // SUMMARY_STYLE_LVL2.setForeground(SUMMARY_COLOR_LVL2);
        SUMMARY_STYLE_LVL2.setFontStyle(Font.BOLD);

        // SUMMARY_STYLE_LVL3.setForeground(SUMMARY_COLOR_LVL3);
        SUMMARY_STYLE_LVL3.setFontStyle(Font.BOLD);

        // SUMMARY_STYLE_LVL4.setForeground(SUMMARY_COLOR_LVL4);
        SUMMARY_STYLE_LVL4.setFontStyle(Font.BOLD);
    }

    /**
     * Costruttore.
     */
    public GrupingRowStripeTableStyleProvider() {
        super(new Color[] { DefaultCellStyleProvider.EVEN_COLOR, DefaultCellStyleProvider.ODD_COLOR });
    }

    @Override
    public CellStyle getCellStyleAt(JTable jtable, int i, int j) {
        if (TableModelWrapperUtils.getActualRowAt(jtable.getModel(), i) == -1) {
            DefaultGroupRow groupRow = (DefaultGroupRow) jtable.getModel().getValueAt(i, 0);
            int level = groupRow.getLevel();
            level = level > 4 ? 4 : level;
            return CELL_STYLES_LEVELS[level];
        }
        return super.getCellStyleAt(jtable, i, j);
    }
}
