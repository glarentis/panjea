package it.eurotn.rich.control.table.renderer;

import it.eurotn.entity.EntityBase;
import it.eurotn.rich.search.AbstractSearchObject;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.image.IconSource;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.HyperlinkTableCellEditorRenderer;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class IconHyperlinkContextSensitiveCellRenderer extends HyperlinkTableCellEditorRenderer {

    private final class OpenEditorActionListener implements ActionListener {

        private Object value;

        /**
         * Costruttore.
         * 
         * @param value
         *            object
         */
        private OpenEditorActionListener(final Object value) {
            this.value = value;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            openEditor(value);
        }
    }

    private static final long serialVersionUID = 7609135894671274156L;

    private IconSource iconSource;

    private AbstractSearchObject searchObject;

    @Override
    public void configureTableCellEditorRendererComponent(JTable table, Component editorRendererComponent,
            boolean forRenderer, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            super.configureTableCellEditorRendererComponent(table, editorRendererComponent, forRenderer, value,
                    isSelected, hasFocus, row, column);
        }

        if (editorRendererComponent instanceof AbstractButton) {
            Icon icon = null;
            if (value != null) {
                try {
                    icon = getIconSource().getIcon(getType().getName());
                    if (value instanceof Enum<?>) {
                        icon = getIconSource().getIcon(getType().getName() + "#" + ((Enum<?>) value).name());
                    }
                    if (value instanceof EntityBase) {
                        if (((EntityBase) value).getId() != null) {
                            icon = getIconSource().getIcon(getType().getName());
                        }
                    }
                } catch (Exception e) {
                    icon = null;
                }
                ((AbstractButton) editorRendererComponent).setHorizontalAlignment(getHorizontalAlignament());
                ((AbstractButton) editorRendererComponent)
                        .setText(ObjectConverterManager.toString(value, getType(), getConverterContext()));

                ((AbstractButton) editorRendererComponent).addActionListener(new OpenEditorActionListener(value));
            }
            ((AbstractButton) editorRendererComponent).setIcon(icon);
        }
    }

    /**
     * @return Returns the horizontalAlignament.
     */
    public int getHorizontalAlignament() {
        return SwingConstants.LEFT;
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

    /**
     * Apre l'editor per l'oggetto indicato.
     * 
     * @param value
     *            oggetto
     */
    protected void openEditor(Object value) {

        // se value è nullo significa che mi trovo su un totale quindi non faccio niente
        if (value == null) {
            return;
        }

        if (searchObject != null) {
            searchObject.openDialogPage(value);
            searchObject.openEditor(value);
        } else {
            LifecycleApplicationEvent event = new OpenEditorEvent(value);
            Application.instance().getApplicationContext().publishEvent(event);
        }
    }

    @Override
    public void setActionListener(ActionListener actionListener) {
        throw new UnsupportedOperationException(
                "Sovrascrivere il metodo openEditor chè verrà eseguito come actionlistener del pulsante.");
    }

    /**
     * @param searchObject
     *            The searchObject to set.
     */
    public void setSearchObject(AbstractSearchObject searchObject) {
        this.searchObject = searchObject;
    }
}
