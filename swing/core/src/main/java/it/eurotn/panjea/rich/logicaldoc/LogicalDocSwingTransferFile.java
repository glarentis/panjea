package it.eurotn.panjea.rich.logicaldoc;

import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.dms.domain.DmsSettings;
import it.eurotn.panjea.dms.manager.transfer.LogicalDocTransferFile;
import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

public final class LogicalDocSwingTransferFile extends LogicalDocTransferFile {

    private DmsSettings dmsSettings;

    /**
     * Costruttore.
     *
     * @param dmsSettings
     *            settings dms
     */
    public LogicalDocSwingTransferFile(final DmsSettings dmsSettings) {
        super();
        this.dmsSettings = dmsSettings;
    }

    @Override
    public String getLogicalDocUrl() {
        return dmsSettings.getServiceUrl();
    }

    @Override
    public Utente getUtente() {
        ISicurezzaBD sicurezzaBd = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
        return sicurezzaBd.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());
    }
}
