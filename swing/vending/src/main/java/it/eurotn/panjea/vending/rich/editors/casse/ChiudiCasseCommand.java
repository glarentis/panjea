package it.eurotn.panjea.vending.rich.editors.casse;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.panjea.vending.rich.bd.VendingAnagraficaBD;

public class ChiudiCasseCommand extends ActionCommand {

    public static final String PARAM_ID_CASSE = "paramIdCasse";

    private IVendingAnagraficaBD vendingAnagraficaBD;

    /**
     * Costruttore.
     */
    public ChiudiCasseCommand() {
        super("chiudiCasseCommand");
        RcpSupport.configure(this);

        this.vendingAnagraficaBD = RcpSupport.getBean(VendingAnagraficaBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        Integer[] casseId = (Integer[]) getParameter(PARAM_ID_CASSE, null);
        if (ArrayUtils.isEmpty(casseId)) {
            return;
        }

        vendingAnagraficaBD.chiudiCassa(casseId);

    }

}
