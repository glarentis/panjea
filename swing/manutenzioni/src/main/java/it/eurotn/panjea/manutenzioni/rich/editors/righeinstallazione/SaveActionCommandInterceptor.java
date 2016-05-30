package it.eurotn.panjea.manutenzioni.rich.editors.righeinstallazione;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.ITable;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.table.EditFrame;

/**
 *
 * Rispetto al save standard della EditFrame aggiorno il record selezionato
 *
 * @author giangi
 * @version 1.0, 10 dic 2015
 *
 */
public class SaveActionCommandInterceptor implements ActionCommandInterceptor {

    private IPageEditor editPage;
    private JideTableWidget<RigaInstallazione> tableWidget;

    private EditFrame<RigaInstallazione> editFrame;

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
    public SaveActionCommandInterceptor(final JideTableWidget<RigaInstallazione> tableWidget,
            final IPageEditor editPage, final EditFrame<RigaInstallazione> editFrame) {
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

        RigaInstallazione formObject = this.editFrame.getTableManagedObject(editPage.getPageObject());
        if (formObject != null) {
            formObject = (RigaInstallazione) PanjeaSwingUtil.cloneObject(formObject);

            int rowSelected = tableWidget.getTable().getSelectedRow();
            int[] rows = ((ITable<RigaInstallazione>) tableWidget.getTable()).getActualRowsAt(rowSelected, 0);
            if (rows.length > 0) {
                @SuppressWarnings("rawtypes")
                DefaultBeanTableModel tableModel = (DefaultBeanTableModel) TableModelWrapperUtils
                        .getActualTableModel(tableWidget.getTable().getModel(), DefaultBeanTableModel.class);
                tableModel.setObject(formObject, rowSelected);
                tableWidget.selectRow(rowSelected, null);
            }
        }
        tableWidget.setDelayForSelection(tableWidgetDelay);

        editFrame.getQuickAction().executeAction();
    }

    @Override
    public boolean preExecution(ActionCommand arg0) {
        return true;
    }
}