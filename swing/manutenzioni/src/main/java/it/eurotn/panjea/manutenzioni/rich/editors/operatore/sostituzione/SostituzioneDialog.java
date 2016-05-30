package it.eurotn.panjea.manutenzioni.rich.editors.operatore.sostituzione;

import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.panjea.manutenzioni.rich.bd.ManutenzioniBD;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

public class SostituzioneDialog extends PanjeaTitledPageApplicationDialog {

    private IManutenzioniBD manutenzioniBD;

    /**
     * Costruttore.
     *
     */
    public SostituzioneDialog() {
        super(new SostituzioneOperatoreForm(), null);
        // setPreferredSize(new Dimension(800, 600));

        this.manutenzioniBD = RcpSupport.getBean(ManutenzioniBD.BEAN_ID);
    }

    @Override
    protected String getTitle() {
        return "Sostituzione operatore";
    }

    @Override
    protected boolean isMessagePaneVisible() {
        return false;
    }

    @Override
    protected boolean onFinish() {
        ((FormBackedDialogPage) getDialogPage()).getBackingFormPage().commit();
        SostituzioneOperatorePM pm = (SostituzioneOperatorePM) ((FormBackedDialogPage) getDialogPage())
                .getBackingFormPage().getFormObject();

        manutenzioniBD.sostituisciOperatore(pm.getOperatoreDaSostituire().getId(), pm.getOperatore().getId(),
                pm.isTecnico(), pm.isCaricatore());

        return true;
    }

    /**
     * @param pm
     *            setter of sostituzioneOperatorePM
     */
    public void setSostituzioneOperatorePM(SostituzioneOperatorePM pm) {
        ((FormBackedDialogPage) getDialogPage()).getBackingFormPage().revert();
        ((FormBackedDialogPage) getDialogPage()).getBackingFormPage().setFormObject(pm);
    }

}