package it.eurotn.panjea.vending.rich.commands;

import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.manutenzioni.rich.commands.OpenArticoloMICommand;
import it.eurotn.panjea.vending.domain.Distributore;

public class OpenDistributoreCommand extends OpenArticoloMICommand {

    /**
     * Costruttore.
     */
    public OpenDistributoreCommand() {
        super("openDistributoreCommand");
        RcpSupport.configure(this);

        Distributore distributore = new Distributore();
        distributore.setProprietaCliente(true);
        setArticoloMIToOpen(distributore);
    }

    @Override
    protected DialogPage getArticoloMIPage() {
        return RcpSupport.getBean("distributoreCompositePage");
    }

}
