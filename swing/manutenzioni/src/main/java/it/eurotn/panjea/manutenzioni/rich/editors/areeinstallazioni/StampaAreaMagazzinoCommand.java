package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public class StampaAreaMagazzinoCommand extends ActionCommand {

    public static final String AREA_MAGAZZINO_ID_PARAM = "areaMagazzinoIdParam";

    /**
     * Costruttore.
     */
    public StampaAreaMagazzinoCommand() {
        super("stampaAreaMagazzinoCommand");
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        Integer idAreaMagazzino = (Integer) getParameter(AREA_MAGAZZINO_ID_PARAM, null);
        if (idAreaMagazzino == null) {
            return;
        }
    }

}
