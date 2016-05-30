package it.eurotn.panjea.giroclienti.rich.editors.scheda.header;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;

import it.eurotn.panjea.giroclienti.rich.editors.scheda.SchedeGiroClientiPage;

public class RimuoviGiroClienteCommandInterceptor extends ActionCommandInterceptorAdapter {

    private SchedeGiroClientiPage page;

    /**
     * Costruttore.
     *
     * @param page
     *            pagina
     */
    public RimuoviGiroClienteCommandInterceptor(final SchedeGiroClientiPage page) {
        super();
        this.page = page;
    }

    @Override
    public void postExecution(ActionCommand command) {
        boolean giroRimosso = ((RimuoviGiroClienteCommand) command).isGiroRimosso();

        if (giroRimosso) {
            page.refreshData();
        }
    }

    @Override
    public boolean preExecution(ActionCommand command) {
        if (page.getRigheGiroTable().getSelectedObject() != null) {
            command.addParameter(RimuoviGiroClienteCommand.PARAM_GIRO_DA_RIMUOVERE,
                    page.getRigheGiroTable().getSelectedObject());
        }
        return true;
    }
}