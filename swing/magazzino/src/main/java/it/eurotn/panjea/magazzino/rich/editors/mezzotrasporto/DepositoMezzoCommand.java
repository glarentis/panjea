package it.eurotn.panjea.magazzino.rich.editors.mezzotrasporto;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.rich.editors.mezzotrasporto.DepositoMezzoForm.DepositoAction;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

public class DepositoMezzoCommand extends ApplicationWindowAwareCommand {

    public static final String COMMAND_ID = "depositoMezzoCommand";

    public static final String PARAM_MEZZO_TRASPORTO = "paramMezzoTrasporto";

    private DepositoAction depositoAction;
    private DepositoLite deposito;

    /**
     * Costruttore.
     */
    public DepositoMezzoCommand() {
        super(COMMAND_ID);
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {

        depositoAction = null;

        MezzoTrasporto mezzoTrasporto = (MezzoTrasporto) getParameter(PARAM_MEZZO_TRASPORTO);

        PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(
                new DepositoMezzoForm(mezzoTrasporto), null) {

            @Override
            protected boolean onFinish() {
                FormBackedDialogPage dialogPage = (FormBackedDialogPage) getDialogPage();
                DepositoMezzoForm depositoMezzoForm = (DepositoMezzoForm) dialogPage.getBackingFormPage();

                if (depositoMezzoForm.getDepositoAction() != null) {
                    depositoAction = depositoMezzoForm.getDepositoAction();
                    deposito = ((MezzoTrasporto) depositoMezzoForm.getFormObject()).getDeposito();
                }

                return depositoMezzoForm.getDepositoAction() != null;
            }
        };
        dialog.showDialog();
    }

    /**
     * @return the deposito
     */
    public DepositoLite getDeposito() {
        return deposito;
    }

    /**
     * @return the depositoAction
     */
    public DepositoAction getDepositoAction() {
        return depositoAction;
    }

}
