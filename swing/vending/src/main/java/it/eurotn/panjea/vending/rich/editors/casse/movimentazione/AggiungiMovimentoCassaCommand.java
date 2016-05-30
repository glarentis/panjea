package it.eurotn.panjea.vending.rich.editors.casse.movimentazione;

import java.util.Calendar;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.vending.domain.Cassa;
import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.panjea.vending.domain.RigaMovimentoCassa;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.panjea.vending.rich.bd.VendingAnagraficaBD;
import it.eurotn.panjea.vending.rich.editors.movimenticassa.MovimentoCassaForm;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

public class AggiungiMovimentoCassaCommand extends ActionCommand {

    private class NewMovimentoDialog extends PanjeaTitledPageApplicationDialog {

        private IVendingAnagraficaBD vendingAnagraficaBD;

        /**
         * Costruttore.
         *
         */
        public NewMovimentoDialog() {
            super(new MovimentoCassaForm(), null);
            vendingAnagraficaBD = RcpSupport.getBean(VendingAnagraficaBD.BEAN_ID);
        }

        @Override
        protected void onCancel() {
            super.onCancel();
            movimentoCassaModificato = false;
        }

        @Override
        protected boolean onFinish() {
            ((FormBackedDialogPage) getDialogPage()).getBackingFormPage().commit();
            MovimentoCassa movimentoCassa = (MovimentoCassa) ((FormBackedDialogPage) getDialogPage())
                    .getBackingFormPage().getFormObject();

            for (RigaMovimentoCassa riga : movimentoCassa.getRighe()) {
                riga.setMovimentoCassa(movimentoCassa);
            }
            vendingAnagraficaBD.salvaMovimentoCassa(movimentoCassa);
            movimentoCassaModificato = true;

            return true;
        }

        public void setMovimentazione(MovimentoCassa movimentoCassa) {
            FormBackedDialogPage page = (FormBackedDialogPage) getDialogPage();
            MovimentoCassaForm form = (MovimentoCassaForm) page.getBackingFormPage();
            form.getNewFormObjectCommand().execute();
            form.setFormObject(movimentoCassa);
        }

    }

    public static final String PARAM_CASSA = "paramCassa";
    public static final String PARAM_MOVIMENTO = "paramMovimento";

    private NewMovimentoDialog movimentoDialog = new NewMovimentoDialog();

    private boolean movimentoCassaModificato = false;

    /**
     * Costruttore.
     */
    public AggiungiMovimentoCassaCommand() {
        super("aggiungiMovimentoCassaCommand");
        RcpSupport.configure(this);

    }

    @Override
    protected void doExecuteCommand() {

        Cassa cassa = (Cassa) getParameter(PARAM_CASSA, null);
        if (cassa == null) {
            return;
        }

        MovimentoCassa movimentoCassa = (MovimentoCassa) getParameter(PARAM_MOVIMENTO, null);
        if (movimentoCassa == null) {
            movimentoCassa = new MovimentoCassa();
            movimentoCassa.setCassa(cassa);
            movimentoCassa.setData(Calendar.getInstance().getTime());
        }

        movimentoCassaModificato = false;
        movimentoDialog.setMovimentazione(movimentoCassa);
        movimentoDialog.showDialog();
    }

    /**
     * @return the movimentoCassaModificato
     */
    public boolean isMovimentoCassaModificato() {
        return movimentoCassaModificato;
    }

    @Override
    protected void onButtonAttached(AbstractButton button) {
        button.setFocusable(false);
        super.onButtonAttached(button);
    }
}