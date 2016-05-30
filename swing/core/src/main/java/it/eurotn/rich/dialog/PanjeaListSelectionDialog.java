package it.eurotn.rich.dialog;

import java.awt.Window;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.springframework.richclient.selection.dialog.ListSelectionDialog;

public class PanjeaListSelectionDialog extends ListSelectionDialog {

    /**
     * Costruttore.
     * 
     * @param title
     *            titolo
     * @param items
     *            oggetti da selezionare
     */
    public PanjeaListSelectionDialog(final String title, final List items) {
        super(title, items);
    }

    /**
     * Costruttore.
     * 
     * @param title
     *            tipolo
     * @param parent
     *            parent
     * @param items
     *            oggetti da selezionare
     */
    public PanjeaListSelectionDialog(final String title, final Window parent, final List items) {
        super(title, parent, items);
    }

    @Override
    protected JComponent createSelectionComponent() {
        JScrollPane scrollPane = (JScrollPane) super.createSelectionComponent();

        scrollPane.getViewport().getComponent(0).setName("StrutturaPartiteList");

        return scrollPane;
    }

}
