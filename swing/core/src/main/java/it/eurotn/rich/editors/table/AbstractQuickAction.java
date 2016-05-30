package it.eurotn.rich.editors.table;

import it.eurotn.rich.control.table.JideTableWidget;

/**
 * 
 * @author fattazzo
 * 
 * @param <T>
 */
public abstract class AbstractQuickAction<T> {

    protected JideTableWidget<T> tableWidget;
    protected EditFrame<T> editFrame;

    /**
     * Costruttore di default.
     * 
     * @param tableWidget
     *            table widget
     * @param editFrame
     *            editFrame
     */
    public AbstractQuickAction(final JideTableWidget<T> tableWidget, final EditFrame<T> editFrame) {
        super();
        this.tableWidget = tableWidget;
        this.editFrame = editFrame;
    }

    /**
     * Esegue l'azione.
     */
    public abstract void executeAction();

    /**
     * Indica se il form deve essere messo in edit.
     * 
     * @return forceEditForm
     */
    public boolean forceEditForm() {
        return false;
    }
}
