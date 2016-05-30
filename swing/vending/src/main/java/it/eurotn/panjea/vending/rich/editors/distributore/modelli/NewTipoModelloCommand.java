package it.eurotn.panjea.vending.rich.editors.distributore.modelli;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.domain.TipoModello;

/**
 * Command per la creazione di un {@link Modello}.
 *
 */
class NewTipoModelloCommand extends ApplicationWindowAwareCommand {

    private static final String NEW_COMMAND = "newTipoModelloCommand";
    protected final ModelliTreeTablePage modelliTreeTablePage;

    /**
     * Costruttore.
     *
     * @param modelliTreeTablePage
     *            pagina dei modelli
     */
    public NewTipoModelloCommand(final ModelliTreeTablePage modelliTreeTablePage) {
        super(NEW_COMMAND);
        this.modelliTreeTablePage = modelliTreeTablePage;
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        this.modelliTreeTablePage.openTipoModelloPage(new TipoModello());
    }
}