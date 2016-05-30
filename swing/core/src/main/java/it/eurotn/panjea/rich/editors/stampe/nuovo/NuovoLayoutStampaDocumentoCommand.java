package it.eurotn.panjea.rich.editors.stampe.nuovo;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.rich.bd.ILayoutStampeBD;
import it.eurotn.panjea.rich.bd.LayoutStampeBD;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.util.RcpSupport;

public class NuovoLayoutStampaDocumentoCommand extends ApplicationWindowAwareCommand {

    public static final String COMMAND_ID = "aggiungiLayoutStampaCommand";

    private ILayoutStampeBD layoutStampeBD;

    public static final String PARAM_ENTITA = "paramEntita";

    {
        layoutStampeBD = RcpSupport.getBean(LayoutStampeBD.BEAN_ID);
    }

    private LayoutStampa layoutStampa;

    /**
     * Costruttore.
     */
    public NuovoLayoutStampaDocumentoCommand() {
        super(COMMAND_ID);
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        Entita entita = (Entita) getParameter(PARAM_ENTITA, null);

        layoutStampa = null;

        LayoutStampaPM layoutStampaPM = new LayoutStampaPM();
        if (entita != null) {
            layoutStampaPM.setEntita(entita.getEntitaLite());
            layoutStampaPM.setBloccaEntita(true);
        }

        PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(
                new NuovoLayoutStampaDocumentoForm(layoutStampaPM), null) {

            @Override
            protected String getTitle() {
                return "Abilita nuovo layout";
            }

            @Override
            protected boolean onFinish() {
                Form nuovoLayoutForm = ((FormBackedDialogPage) getDialogPage()).getBackingFormPage();

                if (nuovoLayoutForm.getFormModel().isCommittable()) {
                    // salvo il nuovo layout per il tipo area
                    LayoutStampaPM layoutStampaPM = (LayoutStampaPM) nuovoLayoutForm.getFormModel().getFormObject();
                    layoutStampa = layoutStampeBD.aggiungiLayoutStampa(layoutStampaPM.getTipoAreaDocumento(),
                            layoutStampaPM.getReportName(), layoutStampaPM.getEntita(), layoutStampaPM.getSedeEntita());
                }
                return nuovoLayoutForm.getFormModel().isCommittable();
            }
        };
        dialog.setTitlePaneTitle("<html>Seleziona il layout di stampa<br>da aggiungere al tipo documento</html>");
        dialog.showDialog();
    }

    /**
     * @return Returns the layoutStampa.
     */
    public LayoutStampa getLayoutStampa() {
        return layoutStampa;
    }

}
