package it.eurotn.panjea.giroclienti.rich.commands;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.giroclienti.rich.editors.riepilogo.RiepilogoGiornalieroForm;
import it.eurotn.panjea.giroclienti.rich.editors.riepilogo.RiepilogoGiornalieroPM;
import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.rich.editors.PanjeaTitledApplicationDialog;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.report.StampaCommand;

public class StampaRiepilogoSchedeCommand extends StampaCommand {

    private static final String CONTROLLER_ID = "stampaRiepilogoSchedeCommand";

    private RiepilogoGiornalieroPM riepilogoGiornalieroPM = null;

    private ISicurezzaBD sicurezzaBD;

    /**
     * Costruttore.
     */
    public StampaRiepilogoSchedeCommand() {
        super(CONTROLLER_ID, CONTROLLER_ID);
        sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
    }

    @Override
    protected void doExecuteCommand() {

        List<Utente> utenti = sicurezzaBD.caricaUtenti("userName", null);

        riepilogoGiornalieroPM = new RiepilogoGiornalieroPM();
        riepilogoGiornalieroPM.setData(Calendar.getInstance().getTime());
        riepilogoGiornalieroPM.setUtenti(utenti);

        PanjeaTitledApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(
                new RiepilogoGiornalieroForm(riepilogoGiornalieroPM, utenti), null) {

            @Override
            protected String getTitle() {
                return "Parametri di stampa";
            }

            @Override
            protected boolean onFinish() {

                FormBackedDialogPage dialogPage = (FormBackedDialogPage) getDialogPage();
                RiepilogoGiornalieroForm riepilogoGiornalieroForm = (RiepilogoGiornalieroForm) dialogPage
                        .getBackingFormPage();

                if (!riepilogoGiornalieroForm.hasErrors() && riepilogoGiornalieroForm.getFormModel().isCommittable()) {
                    riepilogoGiornalieroForm.commit();

                    riepilogoGiornalieroPM = (RiepilogoGiornalieroPM) riepilogoGiornalieroForm.getFormObject();
                    return true;
                }

                return false;
            }
        };
        dialog.showDialog();

        super.doExecuteCommand();
    }

    @Override
    protected Map<Object, Object> getParametri() {

        Map<Object, Object> parametri = new HashMap<>();
        parametri.put("data", DateFormatUtils.format(riepilogoGiornalieroPM.getData(), "dd/MM/yyyy"));
        String idUtenti = "";
        String separator = "";
        for (Utente utente : riepilogoGiornalieroPM.getUtenti()) {
            idUtenti = idUtenti + separator + utente.getId();
            separator = ",";
        }
        parametri.put("idUtenti", idUtenti);
        parametri.put("daEseguire", riepilogoGiornalieroPM.isDaEseguire() ? "true" : "false");
        return parametri;
    }

    @Override
    protected String getReportName() {
        return "Stampa riepilogo giornaliero";
    }

    @Override
    protected String getReportPath() {
        return "GiroClienti/riepilogo";
    }

}
