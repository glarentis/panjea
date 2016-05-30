package it.eurotn.panjea.vending.rich.editors.lettureselezionatrice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.manager.lettureselezionatrice.RisultatiChiusuraLettureDTO;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;
import it.eurotn.panjea.vending.rich.bd.VendingDocumentoBD;

public class ChiudiLettureCommand extends ActionCommand {

    public static final String PARAM_LETTURE = "paramLetture";

    private IVendingDocumentoBD vendingDocumentoBD;

    private RisultatiLettureDialog risultatiLettureDialog = new RisultatiLettureDialog();

    /**
     * Costruttore.
     */
    public ChiudiLettureCommand() {
        super("chiudiLettureCommand");
        RcpSupport.configure(this);
        vendingDocumentoBD = RcpSupport.getBean(VendingDocumentoBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {
        @SuppressWarnings("unchecked")
        List<LetturaSelezionatrice> letture = (List<LetturaSelezionatrice>) getParameter(PARAM_LETTURE,
                new ArrayList<>());

        if (!letture.isEmpty()) {
            RisultatiChiusuraLettureDTO risultati = vendingDocumentoBD.chiudiLettureSelezionatrice(letture);

            risultatiLettureDialog.setRisultatiLetture(risultati);
            risultatiLettureDialog.showDialog();
        }
    }

}
