package it.eurotn.panjea.rich.dialogs;

import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.selection.dialog.FilterListSelectionDialog;

import ca.odell.glazedlists.FilterList;

public class PanjeaFilterListSelectionDialog extends FilterListSelectionDialog {

    private FilterList<?> filterList;

    /**
     * Costruttore.
     *
     * @param title
     *            titolo
     * @param parent
     *            parent
     * @param filterList
     *            filterList
     */
    public PanjeaFilterListSelectionDialog(final String title, final Window parent, final FilterList<?> filterList) {
        super(title, parent, filterList);
        this.filterList = filterList;

    }

    @Override
    protected JComponent createFilterComponent() {
        JTextField textField = (JTextField) super.createFilterComponent();
        textField.setName("filterComponent");
        return textField;
    }

    @Override
    protected JComponent createSelectionComponent() {
        JComponent selectionComponent = super.createSelectionComponent();
        getList().setName("selectionComponent");
        return selectionComponent;
    }

    @Override
    protected boolean onFinish() {
        // se clicco nella selection dialog senza elementi visualizzati, viene sollevata una IOOBE (finestra di errore
        // per l'utente), verifico se la filter list contiene degli elementi prima di selezionare un elemento
        Object selectedObject = null;
        if (getList().getSelectedIndex() < 0 || getList().getSelectedIndex() > getList().getModel().getSize() - 1) {
            return false;
        }
        if (filterList.size() > 0) {
            selectedObject = getSelectedObject();
        }
        if (selectedObject != null) {
            onSelect(selectedObject);
            return true;
        }
        return true;
    }

}
