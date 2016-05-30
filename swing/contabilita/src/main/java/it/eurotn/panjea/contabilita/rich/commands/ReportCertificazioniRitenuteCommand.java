package it.eurotn.panjea.contabilita.rich.commands;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.contabilita.rich.editors.ritenuteacconto.certificazioni.CertificazioniCompensiDialogPage;

/**
 * @author fattazzo
 *
 */
public class ReportCertificazioniRitenuteCommand extends ActionCommand {

    /**
     * Costruttore.
     */
    public ReportCertificazioniRitenuteCommand() {
        super("reportCertificazioniRitenuteCommand");
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        CertificazioniCompensiDialogPage compensiDialogPage = new CertificazioniCompensiDialogPage();
        compensiDialogPage.setCallingCommand(this);
        compensiDialogPage.showDialog();
    }

}
