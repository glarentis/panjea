package it.eurotn.rich.control.table.style;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import com.jidesoft.grid.CellStyle;

public class FocusCellStyle extends CellStyle {

    protected static final Color FOCUS_COLOR = Color.GREEN;

    @Override
    public Border getBorder() {
        return BorderFactory.createLineBorder(FOCUS_COLOR, 2);
    }

    @Override
    public int getFontStyle() {
        return Font.BOLD;
    }

}