package it.eurotn.panjea.vending.rich.editors.distributore.modelli;

import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

/**
 * Ricarica tutte i modelli presenti.
 *
 * @author fattazzo
 *
 */
public class RefreshModelliCommand extends ApplicationWindowAwareCommand {

    private static final String COMMAND_ID = "refreshCommand";

    private final ModelliTreeTablePage modelliTreeTablePage;

    /**
     * Costruttore.
     *
     * @param modelliTreeTablePage
     *            tree table dei modelli
     */
    public RefreshModelliCommand(final ModelliTreeTablePage modelliTreeTablePage) {
        super(COMMAND_ID);
        this.modelliTreeTablePage = modelliTreeTablePage;
        RcpSupport.configure(this);

    }

    @Override
    protected void doExecuteCommand() {
        DefaultTreeTableModel myModel = (DefaultTreeTableModel) this.modelliTreeTablePage.getTreeTable()
                .getTreeTableModel();

        myModel.setRoot(this.modelliTreeTablePage.createNode());
        modelliTreeTablePage.getTreeTable().expandAll();
    }

}
