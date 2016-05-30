package it.eurotn.panjea.bi.rich.editors.dashboard.widget.analisi.commands;

import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardPage;

public class CambiaVisualizzazioneCommand extends ApplicationWindowAwareCommand {

    private static final String COMMAND_ID = "cambiaVisualizzazioneCommand";
    private final DashBoardPage page;

    /**
     *
     * Costruttore.
     *
     * @param dashBoardCompositePage
     *
     * @param page
     *            pagina collegata al comando
     */
    public CambiaVisualizzazioneCommand(final DashBoardPage page) {
        super(COMMAND_ID);
        this.page = page;
        CommandConfigurer configurer = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        this.setSecurityControllerId(COMMAND_ID);
        configurer.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        page.cambiaTipoVisualizzazione();
    }

    /**
     * @return Returns the page.
     */
    public DashBoardPage getPage() {
        return page;
    }

}
