package it.eurotn.rich.editors.table;

import java.awt.Component;
import java.awt.KeyboardFocusManager;

import it.eurotn.rich.control.table.JideTableWidget;

/**
 * @author fattazzo
 *
 */
public class LockQuickAction<T> extends AbstractQuickAction<T> {

    private Component focusComponent = null;

    private boolean selectNextRow = false;

    /**
     * Costruttore.
     *
     * @param tableWidget
     *            table widget
     * @param editFrame
     *            frame di edit
     */
    public LockQuickAction(final JideTableWidget<T> tableWidget, final EditFrame<T> editFrame) {
        super(tableWidget, editFrame);
        focusComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
    }

    @Override
    public void executeAction() {

        // la prima volta non seleziono la riga successiva.
        if (selectNextRow) {
            // imposto subito il delay di selezione a 0 altrimenti viene eseguita la selezione della riga
            // può avvenire dopo la execute del lock command della pagina annullando lo stato di modifica
            int tableWidgetDelay = tableWidget.getDelayForSelection();
            tableWidget.setDelayForSelection(0);
            tableWidget.selectNextRow();
            tableWidget.setDelayForSelection(tableWidgetDelay);
        }
        selectNextRow = true;

        editFrame.setCurrentPage(tableWidget.getSelectedObject());
        editFrame.getCurrentEditPage().getEditorLockCommand().execute();

        if (focusComponent != null) {
            focusComponent.requestFocusInWindow();
        }
    }

    @Override
    public boolean forceEditForm() {
        return true;
    }
}
