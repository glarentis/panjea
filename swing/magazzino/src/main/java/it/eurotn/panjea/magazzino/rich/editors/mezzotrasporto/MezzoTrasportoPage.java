package it.eurotn.panjea.magazzino.rich.editors.mezzotrasporto;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;

import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.mezzotrasporto.DepositoMezzoForm.DepositoAction;
import it.eurotn.panjea.magazzino.rich.forms.mezzotrasporto.MezzoTrasportoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 *
 * @author Leonardo
 */
public class MezzoTrasportoPage extends FormBackedDialogPageEditor {
    private static final String PAGE_ID = "mezziTrasportoPage";
    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;

    private DepositoMezzoCommand depositoMezzoCommand;

    /**
     * Costruttore di default,inizializza un nuovo form.
     */
    public MezzoTrasportoPage() {
        super(PAGE_ID, new MezzoTrasportoForm());

        depositoMezzoCommand = new DepositoMezzoCommand();
        depositoMezzoCommand.setEnabled(false);
        depositoMezzoCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
            @Override
            public void postExecution(ActionCommand command) {
                super.postExecution(command);

                DepositoAction depositoAction = ((DepositoMezzoCommand) command).getDepositoAction();
                if (depositoAction == null) {
                    return;
                }

                MezzoTrasporto mezzoTrasporto = (MezzoTrasporto) getBackingFormPage().getFormObject();
                switch (depositoAction) {
                case NUOVO:
                    mezzoTrasporto = magazzinoAnagraficaBD.creaNuovoDepositoMezzoDiTrasporto(mezzoTrasporto,
                            mezzoTrasporto.getTarga(), mezzoTrasporto.getDescrizione());
                    MezzoTrasportoPage.this.setFormObject(mezzoTrasporto);
                    break;
                case ASSOCIA:
                    mezzoTrasporto.setDeposito(((DepositoMezzoCommand) command).getDeposito());
                    mezzoTrasporto = magazzinoAnagraficaBD.salvaMezzoTrasporto(mezzoTrasporto);
                    MezzoTrasportoPage.this.setFormObject(mezzoTrasporto);
                    break;
                case RIMUOVI:
                    mezzoTrasporto = magazzinoAnagraficaBD.rimuoviDepositoDaMezzoDiTrasporto(mezzoTrasporto);
                    MezzoTrasportoPage.this.setFormObject(mezzoTrasporto);
                    break;
                default:
                    break;
                }
            }

            @Override
            public boolean preExecution(ActionCommand command) {
                command.addParameter(DepositoMezzoCommand.PARAM_MEZZO_TRASPORTO, getForm().getFormObject());
                return true;
            }
        });
    }

    @Override
    protected Object doDelete() {
        magazzinoAnagraficaBD.cancellaMezzoTrasporto((MezzoTrasporto) getBackingFormPage().getFormObject());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        MezzoTrasporto mezzoTrasporto = (MezzoTrasporto) this.getForm().getFormObject();
        return magazzinoAnagraficaBD.salvaMezzoTrasporto(mezzoTrasporto);
    }

    @Override
    protected AbstractCommand[] getCommand() {

        AbstractCommand[] commands = toolbarPageEditor.getDefaultCommand(true);
        return ArrayUtils.add(commands, depositoMezzoCommand);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void refreshData() {

    }

    /**
     * Set magazzinoAnagraficaBD.
     *
     * @param magazzinoAnagraficaBD
     *            il bd da utilizzare per accedere alle operazioni sui mezzi trasporto
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

    @Override
    public void updateCommands() {
        super.updateCommands();
        depositoMezzoCommand.setEnabled(toolbarPageEditor.getLockCommand().isEnabled());
    }

}
