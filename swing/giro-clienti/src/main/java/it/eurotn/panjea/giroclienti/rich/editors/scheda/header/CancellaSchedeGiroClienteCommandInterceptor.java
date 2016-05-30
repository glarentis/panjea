package it.eurotn.panjea.giroclienti.rich.editors.scheda.header;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;

import it.eurotn.panjea.giroclienti.rich.editors.scheda.SchedeGiroClientiPage;

public class CancellaSchedeGiroClienteCommandInterceptor extends ActionCommandInterceptorAdapter {

    private SchedeGiroClientiPage schedePage;
    private HeaderPanel headerPanel;

    /**
     * Costruttore.
     *
     * @param schedePage
     *            schedePage
     * @param headerPanel
     *            headerPanel
     */
    public CancellaSchedeGiroClienteCommandInterceptor(final SchedeGiroClientiPage schedePage,
            final HeaderPanel headerPanel) {
        super();
        this.schedePage = schedePage;
        this.headerPanel = headerPanel;
    }

    @Override
    public void postExecution(ActionCommand command) {
        boolean schedeCancellate = ((CancellaSchedeGiroClienteCommand) command).isSchedeCancellate();

        if (schedeCancellate) {
            schedePage.refreshData();
        }
    }

    @Override
    public boolean preExecution(ActionCommand command) {
        command.addParameter(CancellaSchedeGiroClienteCommand.PARAM_UTENTE, headerPanel.getUtenteSelezionato());
        return true;
    }
}