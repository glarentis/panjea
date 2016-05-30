package it.eurotn.panjea.manutenzioni.rich.editors.righeinstallazione;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;

import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class DeleteActionCommandInterceptor implements ActionCommandInterceptor {

    private AbstractTablePageEditor<RigaInstallazione> tablePage;
    private JideTableWidget<RigaInstallazione> tableWidget;

    /**
     * Costruttore
     *
     * @param tableWidget
     *            tableWidget
     * @param tablePage
     *            pagina
     */
    public DeleteActionCommandInterceptor(final JideTableWidget<RigaInstallazione> tableWidget,
            final AbstractTablePageEditor<RigaInstallazione> tablePage) {
        this.tablePage = tablePage;
        this.tableWidget = tableWidget;
    }

    @Override
    public void postExecution(ActionCommand paramActionCommand) {
        int selectedIndex = tableWidget.getTable().getSelectionModel().getMinSelectionIndex();
        tablePage.loadData();
        tableWidget.getTable().requestFocusInWindow();
        tableWidget.getTable().getSelectionModel().setSelectionInterval(selectedIndex, selectedIndex);
    }

    @Override
    public boolean preExecution(ActionCommand paramActionCommand) {
        return true;
    }

}
