package it.eurotn.rich.dialog;

import java.awt.Window;

import org.springframework.richclient.selection.dialog.AbstractSelectionDialog;

public abstract class AbstractFilterSelectionDialog extends AbstractSelectionDialog {

    private String filter = null;

    /**
     * Costruttore.
     * 
     * @param title
     *            titolo
     * @param parent
     *            parent
     */
    public AbstractFilterSelectionDialog(final String title, final Window parent) {
        super(title, parent);
    }

    /**
     * @return the filter
     */
    public String getFilter() {
        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }

}
