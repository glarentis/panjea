package it.eurotn.rich.control;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.table.DefaultTableCellRenderer;

public class RowHeaderRenderer extends DefaultTableCellRenderer implements ListCellRenderer {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -4999185686114271575L;

    protected Border noFocusBorder, focusBorder;

    public RowHeaderRenderer() {
        setOpaque(true);
        setBorder(noFocusBorder);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        Border cell = UIManager.getBorder("TableHeader.cellBorder");
        Border focus = UIManager.getBorder("Table.focusCellHighlightBorder");

        focusBorder = new BorderUIResource.CompoundBorderUIResource(cell, focus);

        Insets i = focus.getBorderInsets(this);

        noFocusBorder = new BorderUIResource.CompoundBorderUIResource(cell,
                BorderFactory.createEmptyBorder(i.top, i.left, i.bottom, i.right));

        /*
         * Alternatively, if focus shouldn't be supported:
         * 
         * focusBorder = noFocusBorder = cell;
         */
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected,
            boolean focused) {
        if (list != null) {
            if (selected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            setFont(list.getFont());

            setEnabled(list.isEnabled());
        } else {
            setBackground(UIManager.getColor("TableHeader.background"));
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setFont(UIManager.getFont("TableHeader.font"));
            setEnabled(true);
        }

        if (focused) {
            setBorder(focusBorder);
        } else {
            setBorder(noFocusBorder);
        }

        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setValue(value);

        return this;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused,
            int row, int column) {
        if (table != null) {
            if (selected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            setFont(table.getFont());

            setEnabled(table.isEnabled());
        } else {
            setBackground(UIManager.getColor("TableHeader.background"));
            setForeground(UIManager.getColor("TableHeader.foreground"));
            setFont(UIManager.getFont("TableHeader.font"));
            setEnabled(true);
        }

        if (focused) {
            setBorder(focusBorder);
        } else {
            setBorder(noFocusBorder);
        }

        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        setValue(value);

        return this;
    }
}
