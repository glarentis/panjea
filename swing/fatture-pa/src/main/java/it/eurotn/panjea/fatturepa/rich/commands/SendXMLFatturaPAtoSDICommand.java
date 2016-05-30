package it.eurotn.panjea.fatturepa.rich.commands;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.fatturepa.rich.manager.InvioFatturaPAManager;

public class SendXMLFatturaPAtoSDICommand extends ActionCommand {

    public static final String COMMAND_ID = "sendXMLFatturaPAtoSDICommand";

    public static final String PARAM_ID_AREA_MAGAZZINO = "paramIdAreaMagazzino";

    private InvioFatturaPAManager invioFatturaPAManager;

    /**
     * Costruttore.
     */
    public SendXMLFatturaPAtoSDICommand() {
        super(COMMAND_ID);
        RcpSupport.configure(this);

        invioFatturaPAManager = new InvioFatturaPAManager();
    }

    @Override
    protected void doExecuteCommand() {

        Integer idArea = (Integer) getParameter(PARAM_ID_AREA_MAGAZZINO, null);
        if (idArea == null) {
            return;
        }

        invioFatturaPAManager.inviaFattura(idArea);
    }
}
