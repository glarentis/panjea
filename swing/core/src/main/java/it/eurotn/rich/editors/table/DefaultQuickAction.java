package it.eurotn.rich.editors.table;

import it.eurotn.rich.control.table.JideTableWidget;

public class DefaultQuickAction<T> extends AbstractQuickAction<T> {

    /**
     * Costruttore.
     * 
     * @param tableWidget
     *            table widget
     * @param editFrame
     *            editFrame
     */
    public DefaultQuickAction(final JideTableWidget<T> tableWidget, final EditFrame<T> editFrame) {
        super(tableWidget, editFrame);
    }

    @Override
    public void executeAction() {
        tableWidget.getTable().requestFocusInWindow();
    }

}
