package it.eurotn.panjea.giroclienti.rich.editors.scheda.header;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;

import it.eurotn.panjea.giroclienti.rich.editors.scheda.header.copiascheda.CopiaSchedaCommand;

public class CopiaSchedaCommandInterceptor extends ActionCommandInterceptorAdapter {

    private HeaderPanel headerPanel;

    /**
     * Costruttore.
     *
     * @param headerPanel
     *            headerPanel
     */
    public CopiaSchedaCommandInterceptor(final HeaderPanel headerPanel) {
        super();
        this.headerPanel = headerPanel;
    }

    @Override
    public boolean preExecution(ActionCommand command) {
        command.addParameter(CopiaSchedaCommand.PARAM_UTENTE, headerPanel.getUtenteSelezionato());
        command.addParameter(CopiaSchedaCommand.PARAM_GIORNO, headerPanel.getGiornoCorrente());
        return true;
    }
}