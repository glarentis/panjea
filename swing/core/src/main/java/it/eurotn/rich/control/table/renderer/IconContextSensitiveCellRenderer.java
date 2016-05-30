/**
 *
 */
package it.eurotn.rich.control.table.renderer;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.image.IconSource;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.ContextSensitiveCellRenderer;

import it.eurotn.entity.EntityBase;

/**
 * @author fattazzo
 *
 */
public class IconContextSensitiveCellRenderer extends ContextSensitiveCellRenderer {

    private static final long serialVersionUID = 7860984729525805440L;

    private IconSource iconSource;
    private final int textPosition = SwingConstants.RIGHT;

    /**
     * Costruttore di default.
     */
    public IconContextSensitiveCellRenderer() {
        super();
    }

    /**
     *
     * @return {@link IconSource}
     */
    public IconSource getIconSource() {
        if (iconSource == null) {
            if (Application.isLoaded()) {
                iconSource = (IconSource) Application.services().getService(IconSource.class);
            }
        }

        return iconSource;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Icon icon = null;

        if (value != null) {
            try {
                if (value instanceof Enum<?>) {
                    icon = getIconSource().getIcon(getType().getName() + "#" + ((Enum<?>) value).name());
                } else if (value instanceof EntityBase) {
                    if (!((EntityBase) value).isNew()) {
                        icon = getIconSource().getIcon(getType().getName());
                    }
                } else {
                    icon = getIconSource().getIcon(getType().getName());
                }
            } catch (Exception e) {
                icon = null;
            }
        }
        label.setIcon(icon);
        label.setHorizontalTextPosition(getTextPosition());
        return label;
    }

    /**
     * @return posizione del testo rispetto all'incona.
     */
    public int getTextPosition() {
        return textPosition;
    }

    @Override
    protected void setValue(Object paramObject) {
        if (paramObject instanceof EntityBase) {
            setText(ObjectConverterManager.toString(paramObject, paramObject.getClass(), getConverterContext()));
        } else {
            super.setValue(paramObject);
        }
    }
}
