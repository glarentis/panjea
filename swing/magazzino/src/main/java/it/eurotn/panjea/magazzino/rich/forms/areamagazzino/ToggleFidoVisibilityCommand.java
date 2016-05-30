package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class ToggleFidoVisibilityCommand extends ActionCommand {

    private PannelloInformazioniSede pannelloInformazioniSede;

    /**
     * Costruttore.
     *
     * @param pannelloInformazioniSede
     *            pannello informazioni sede
     */
    public ToggleFidoVisibilityCommand(final PannelloInformazioniSede pannelloInformazioniSede) {
        super("toggleFidoVisibilityCommand");
        RcpSupport.configure(this);
        this.pannelloInformazioniSede = pannelloInformazioniSede;
    }

    @Override
    protected void doExecuteCommand() {
        if (this.pannelloInformazioniSede != null) {
            this.pannelloInformazioniSede.toggleVisible();
        }
    }

}
