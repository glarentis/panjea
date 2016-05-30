package it.eurotn.panjea.rich.factory.table;

import java.awt.Component;
import java.awt.Insets;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.image.IconSource;

public abstract class AbstractCustomTableCellRenderer extends DefaultTableCellRenderer implements Comparator<Object> {

    private static final long serialVersionUID = 5259483910333613873L;

    private static final int horizontalAlignment = SwingConstants.RIGHT;

    private IconSource iconSource;

    public AbstractCustomTableCellRenderer() {
        this(horizontalAlignment);
    }

    public AbstractCustomTableCellRenderer(int horizontalAlignment) {
        super();
        setHorizontalAlignment(horizontalAlignment);
    }

    @Override
    public int compare(Object o1, Object o2) {
        return getRendererText(o1, false, false).compareTo(getRendererText(o2, false, false));
    }

    public abstract String getIconKey(Object value, boolean isSelected, boolean hasFocus);

    public IconSource getIconSource() {
        if (iconSource == null) {
            iconSource = (IconSource) Application.services().getService(IconSource.class);
        }

        return iconSource;
    }

    public abstract String getRendererText(Object value, boolean isSelected, boolean hasFocus);

    public String getRendererToolTipText(Object value, boolean isSelected, boolean hasFocus) {
        return null;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        cell.setBorder(new CompoundBorder(new EmptyBorder(new Insets(1, 4, 1, 4)), cell.getBorder()));

        cell.setText(this.getRendererText(value, isSelected, hasFocus));

        String iconKey = getIconKey(value, isSelected, hasFocus);
        if (iconKey != null) {
            cell.setIcon(getIconSource().getIcon(iconKey));
        } else {
            cell.setIcon(getIconSource().getIcon("void.icon"));
        }

        setToolTipText(getRendererToolTipText(value, isSelected, hasFocus));

        return cell;

    }
}
