package it.eurotn.rich.editors.table;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;

import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.IPageEditor;

public class SaveActionCommandInterceptor<T> implements ActionCommandInterceptor {

    private IPageEditor editPage;
    private JideTableWidget<T> tableWidget;

    private EditFrame<T> editFrame;

    /**
     * Costruttore di default.
     *
     * @param tableWidget
     *            tableWidget
     * @param editPage
     *            editPage
     * @param editFrame
     *            editFrame
     */
    public SaveActionCommandInterceptor(final JideTableWidget<T> tableWidget, final IPageEditor editPage,
            final EditFrame<T> editFrame) {
        this.editPage = editPage;
        this.tableWidget = tableWidget;
        this.editFrame = editFrame;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postExecution(ActionCommand arg0) {
        // imposto subito il delay di selezione a 0 altrimenti viene eseguita la
        // replaceOrAddRow, viene lanciata la
        // quick action e corro il rischio che il timer scatti e mi lanci
        // l'evento di selezione annullando
        // la quick action
        int tableWidgetDelay = tableWidget.getDelayForSelection();
        tableWidget.setDelayForSelection(0);

        T formObject = this.editFrame.getTableManagedObject(editPage.getPageObject());
        if (formObject != null) {
            formObject = (T) PanjeaSwingUtil.cloneObject(formObject);
            tableWidget.replaceOrAddRowObject(formObject, formObject, editFrame);
        }
        tableWidget.setDelayForSelection(tableWidgetDelay);

        editFrame.getQuickAction().executeAction();
    }

    @Override
    public boolean preExecution(ActionCommand arg0) {
        return true;
    }
}