package it.eurotn.rich.editors.table;

import it.eurotn.rich.control.table.JideTableWidget;

/**
 * @author fattazzo
 *
 */
public class InsertQuickAction<T> extends AbstractQuickAction<T> {

    /**
     * Costruttore.
     *
     * @param tableWidget
     *            table widget
     * @param editFrame
     *            editFrame
     */
    public InsertQuickAction(final JideTableWidget<T> tableWidget, final EditFrame<T> editFrame) {
        super(tableWidget, editFrame);
    }

    @Override
    public void executeAction() {
        editFrame.getCurrentEditPage().getEditorNewCommand().execute();
    }
}
