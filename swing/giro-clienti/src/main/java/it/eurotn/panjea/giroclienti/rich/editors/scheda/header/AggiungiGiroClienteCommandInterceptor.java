package it.eurotn.panjea.giroclienti.rich.editors.scheda.header;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;

import it.eurotn.panjea.giroclienti.rich.editors.scheda.SchedeGiroClientiPage;

public class AggiungiGiroClienteCommandInterceptor extends ActionCommandInterceptorAdapter {

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
    public AggiungiGiroClienteCommandInterceptor(final SchedeGiroClientiPage schedePage,
            final HeaderPanel headerPanel) {
        super();
        this.schedePage = schedePage;
        this.headerPanel = headerPanel;
    }

    @Override
    public void postExecution(ActionCommand command) {
        boolean giroAggiunto = ((AggiungiGiroClienteCommand) command).isGiroAggiunto();

        if (giroAggiunto) {
            schedePage.refreshData();
        }
    }

    @Override
    public boolean preExecution(ActionCommand command) {
        command.addParameter(AggiungiGiroClienteCommand.PARAM_GIORNO, headerPanel.getGiornoCorrente());
        command.addParameter(AggiungiGiroClienteCommand.PARAM_UTENTE, headerPanel.getUtenteSelezionato());

        if (schedePage.getRigheGiroTable().getSelectedObject() != null) {
            command.addParameter(AggiungiGiroClienteCommand.PARAM_ORA,
                    schedePage.getRigheGiroTable().getSelectedObject().getOra());
        }
        return true;
    }
}