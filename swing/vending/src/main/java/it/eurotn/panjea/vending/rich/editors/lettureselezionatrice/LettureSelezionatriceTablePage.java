package it.eurotn.panjea.vending.rich.editors.lettureselezionatrice;

import java.util.Collection;
import java.util.List;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;

import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

public class LettureSelezionatriceTablePage extends AbstractTablePageEditor<LetturaSelezionatrice> {

    private IVendingDocumentoBD vendingDocumentoBD;

    private ChiudiLettureCommand chiudiLettureCommand = null;

    /**
     * Costruttore.
     */
    public LettureSelezionatriceTablePage() {
        super("lettureSelezionatriceTablePage", new LettureSelezionatriceTableModel());
    }

    @Override
    protected EditFrame<LetturaSelezionatrice> createEditFrame() {
        return new LettureEditFrame(EEditPageMode.DETAIL, this, EditFrame.QUICK_ACTION_DEFAULT);
    }

    /**
     * @return the chiudiLettureCommand
     */
    private ChiudiLettureCommand getChiudiLettureCommand() {
        if (chiudiLettureCommand == null) {
            chiudiLettureCommand = new ChiudiLettureCommand();
            chiudiLettureCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
                @Override
                public void postExecution(ActionCommand command) {
                    refreshData();
                }

                @Override
                public boolean preExecution(ActionCommand command) {
                    List<LetturaSelezionatrice> letture = getTable().getRows();
                    command.addParameter(ChiudiLettureCommand.PARAM_LETTURE, letture);
                    return letture != null && !letture.isEmpty();
                }
            });
        }

        return chiudiLettureCommand;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getChiudiLettureCommand(), getRefreshCommand() };
    }

    @Override
    public Collection<LetturaSelezionatrice> loadTableData() {
        return vendingDocumentoBD.ricercaLettureSelezionatrice(null);
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public Collection<LetturaSelezionatrice> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        // Non utilizzato
    }

    /**
     * @param vendingDocumentoBD
     *            the vendingDocumentoBD to set
     */
    public void setVendingDocumentoBD(IVendingDocumentoBD vendingDocumentoBD) {
        this.vendingDocumentoBD = vendingDocumentoBD;
    }

}
