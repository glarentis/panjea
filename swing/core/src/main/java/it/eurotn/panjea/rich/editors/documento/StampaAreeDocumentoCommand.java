package it.eurotn.panjea.rich.editors.documento;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;

import com.jidesoft.utils.SwingWorker;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.report.JecReport;
import it.eurotn.rich.report.JecReportDocumento;
import it.eurotn.rich.report.JecReports;
import it.eurotn.rich.report.ReportManager;

public class StampaAreeDocumentoCommand extends ApplicationWindowAwareCommand {

    public static final String COMMAND_ID = "stampaAreeMagazzinoCommand";
    public static final String PARAM_AREE_DA_STAMPARE = "areeDaStampare";
    public static final String PARAM_FORZA_STAMPA = "forzaStampa";

    /**
     * Costruttore.
     *
     * @param reportManager
     *            report manager
     */
    public StampaAreeDocumentoCommand(final ReportManager reportManager) {
        super(COMMAND_ID);
        CommandConfigurer conf = (CommandConfigurer) ApplicationServicesLocator.services()
                .getService(CommandConfigurer.class);
        this.setSecurityControllerId(COMMAND_ID);
        conf.configure(this);
    }

    protected JecReport createReportDocumento(IAreaDocumento areaDocumentoDaStampare) {
        return new JecReportDocumento(areaDocumentoDaStampare);
    }

    @Override
    protected void doExecuteCommand() {
        @SuppressWarnings("unchecked")
        List<IAreaDocumento> listAreeDaStampare = (List<IAreaDocumento>) getParameter(PARAM_AREE_DA_STAMPARE);
        Boolean forzaStampaParametro = (Boolean) getParameter(PARAM_FORZA_STAMPA);
        int key = (int) getParameter(MODIFIERS_PARAMETER_KEY);
        final boolean visualizzaDialogoStampa = (key & InputEvent.CTRL_MASK) != 0;
        final Boolean forzaStampa = ObjectUtils.defaultIfNull(forzaStampaParametro, false);
        if (listAreeDaStampare.isEmpty()) {
            return;
        }

        final List<IAreaDocumento> areeDaStampare = new ArrayList<IAreaDocumento>();
        for (IAreaDocumento areaMagazzino : listAreeDaStampare) {
            areeDaStampare.add(areaMagazzino);
        }
        Message message = new DefaultMessage("Stampa in corso...", Severity.INFO);
        final MessageAlert alert = new MessageAlert(message, 0);
        alert.showAlert();
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                printReports(areeDaStampare, forzaStampa, visualizzaDialogoStampa);
                return null;
            }

            @Override
            protected void done() {
                alert.closeAlert();
            }
        }.execute();
    }

    private void printReports(List<IAreaDocumento> areeDaStampare, Boolean forzaStampa,
            boolean visualizzaDialogoStampa) {
        List<JecReport> reportsDocumenti = new ArrayList<>();
        for (IAreaDocumento areaDocumentoDaStampare : areeDaStampare) {
            JecReport reportDocumento = createReportDocumento(areaDocumentoDaStampare);
            reportsDocumenti.add(reportDocumento);
        }
        JecReports.stampaReports(reportsDocumenti, forzaStampa, visualizzaDialogoStampa, false);
    }
}
