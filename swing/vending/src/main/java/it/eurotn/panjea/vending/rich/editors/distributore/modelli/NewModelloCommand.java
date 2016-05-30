package it.eurotn.panjea.vending.rich.editors.distributore.modelli;

import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.domain.TipoModello;

/**
 * Command per la creazione di un {@link Modello}.
 *
 */
class NewModelloCommand extends ApplicationWindowAwareCommand {

    private static final String NEW_COMMAND = "newModelloCommand";
    protected final ModelliTreeTablePage modelliTreeTablePage;

    /**
     * Costruttore.
     *
     * @param modelliTreeTablePage
     *            pagina dei modelli
     */
    public NewModelloCommand(final ModelliTreeTablePage modelliTreeTablePage) {
        super(NEW_COMMAND);
        this.modelliTreeTablePage = modelliTreeTablePage;
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        Modello modelloNew = new Modello();

        if (this.modelliTreeTablePage.getTreeTable().getTreeSelectionModel().getSelectionPath() != null) {
            TreePath selPath = this.modelliTreeTablePage.getTreeTable().getTreeSelectionModel().getSelectionPath();
            DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) selPath.getLastPathComponent();

            if (node.getUserObject() instanceof Modello) {
                Modello modello = (Modello) node.getUserObject();
                modelloNew.setTipoModello(modello.getTipoModello());
            } else if (node.getUserObject() instanceof TipoModello) {
                modelloNew.setTipoModello((TipoModello) node.getUserObject());
            }
        }
        this.modelliTreeTablePage.openModelloPage(modelloNew);
    }
}