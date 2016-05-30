package it.eurotn.panjea.manutenzioni.rich.editors.operatore;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;

import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.panjea.manutenzioni.rich.editors.operatore.sostituzione.SostituisciOperatoreCommand;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class OperatorePage extends FormBackedDialogPageEditor {

    private IManutenzioniBD manutenzioniBD;

    private SostituisciOperatoreCommand sostituisciOperatoreCommand;

    /**
     * Costruttore.
     *
     */
    public OperatorePage() {
        super("operatorePage", new OperatoreForm());
    }

    @Override
    protected Object doDelete() {
        Operatore operatore = (Operatore) getBackingFormPage().getFormObject();
        manutenzioniBD.cancellaOperatore(operatore.getId());
        return operatore;
    }

    @Override
    protected Object doSave() {
        Operatore operatore = (Operatore) getBackingFormPage().getFormObject();
        operatore = manutenzioniBD.salvaOperatore(operatore);
        return operatore;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { toolbarPageEditor.getNewCommand(), toolbarPageEditor.getLockCommand(),
                toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
                toolbarPageEditor.getDeleteCommand(), getSostituisciOperatoreCommand() };
    }

    /**
     * @return the sostituisciOperatoreCommand
     */
    private SostituisciOperatoreCommand getSostituisciOperatoreCommand() {
        if (sostituisciOperatoreCommand == null) {
            sostituisciOperatoreCommand = new SostituisciOperatoreCommand();
            sostituisciOperatoreCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {

                @Override
                public void postExecution(ActionCommand command) {
                    Operatore operatore = (Operatore) getPageObject();

                    if (operatore != null && !operatore.isNew()) {
                        OperatorePage.this.setFormObject(manutenzioniBD.caricaOperatoreById(operatore.getId()));
                    }
                }

                @Override
                public boolean preExecution(ActionCommand command) {
                    Operatore operatore = (Operatore) getPageObject();

                    command.addParameter(SostituisciOperatoreCommand.PARAM_OPERATORE, operatore);
                    return operatore != null && !operatore.isNew();
                }
            });
        }

        return sostituisciOperatoreCommand;
    }

    @Override
    public void loadData() {
        // Non utilizzato
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void refreshData() {
        // Non utilizzato
    }

    /**
     * @param manutenzioniBD
     *            the manutenzioniBD to set
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

    @Override
    public void updateCommands() {
        super.updateCommands();

        getSostituisciOperatoreCommand().setEnabled(toolbarPageEditor.getDeleteCommand().isEnabled());
    }

}
