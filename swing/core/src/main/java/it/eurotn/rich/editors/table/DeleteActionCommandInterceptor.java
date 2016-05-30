package it.eurotn.rich.editors.table;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;

import it.eurotn.rich.command.AbstractDeleteCommand;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.IPageEditor;

public class DeleteActionCommandInterceptor<T> implements ActionCommandInterceptor {

    private final IPageEditor editPage;
    private final JideTableWidget<T> tableWidget;
    private final EditFrame<T> editFrame;

    /**
     *
     * @param tableWidget
     *            widget delal tabella
     * @param editPage
     *            editPage
     * @param editFrame
     *            editFrame
     */
    public DeleteActionCommandInterceptor(final JideTableWidget<T> tableWidget, final IPageEditor editPage,
            final EditFrame<T> editFrame) {
        this.editPage = editPage;
        this.tableWidget = tableWidget;
        this.editFrame = editFrame;
    }

    @Override
    public void postExecution(ActionCommand arg0) {

        if (arg0 instanceof AbstractDeleteCommand && ((AbstractDeleteCommand) arg0).isExecuted()) {
            T formObject = this.editFrame.getTableManagedObject(editPage.getPageObject());
            tableWidget.removeRowObject(formObject);
            if (editFrame.getQuickAction() != null) {
                editFrame.getQuickAction().executeAction();
            }
        }
        tableWidget.getTable().requestFocusInWindow();
    }

    @Override
    public boolean preExecution(ActionCommand arg0) {
        return true;
    }
}