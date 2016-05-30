package it.eurotn.panjea.manutenzioni.rich.editors.righeinstallazione;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.documento.AreaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione;
import it.eurotn.panjea.manutenzioni.domain.documento.RigaInstallazione.TipoMovimento;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.command.SeparatorActionCommand;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class RigaInstallazionePage extends FormBackedDialogPageEditor {

    private class ModificaInstallazioneCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand paramActionCommand) {
            // ApriInstallazioneCommand command = (ApriInstallazioneCommand) paramActionCommand;
            // Installazione instCorrente = (Installazione) RigaInstallazionePage.this.getForm()
            // .getValueModel("installazione").getValue();
            // if (!instCorrente.getVersion().equals(command.getInst().getVersion())) {
            // //
            // RigaInstallazionePage.this.getForm().getValueModel("installazione").setValueSilently(command.getInst(),
            // // null);
            // // getEditorSaveCommand().execute();
            // }
        }

        @Override
        public boolean preExecution(ActionCommand paramActionCommand) {
            Installazione inst = (Installazione) RigaInstallazionePage.this.getForm().getValueModel("installazione")
                    .getValue();
            paramActionCommand.addParameter(ApriInstallazioneCommand.SELECT_INSTALLAZIONE_KEY, inst);
            return true;
        }
    }

    private class PrimaInstallazioneCommand extends ActionCommand {
        public PrimaInstallazioneCommand() {
            super("primaInstallazioneCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            getForm().getValueModel("tipoMovimento").setValue(TipoMovimento.INSTALLAZIONE);
            ((Focussable) getForm()).grabFocus();
        }

    }

    private class RitiroInstallazioneCommand extends ActionCommand {
        public RitiroInstallazioneCommand() {
            super("ritiroInstallazioneCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            getForm().getValueModel("tipoMovimento").setValue(TipoMovimento.RITIRO);
            ((Focussable) getForm()).grabFocus();
        }
    }

    private class SostituzioneInstallazioneCommand extends ActionCommand {
        public SostituzioneInstallazioneCommand() {
            super("sostituzioneInstallazioneCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            getForm().getValueModel("tipoMovimento").setValue(TipoMovimento.SOSTITUZIONE);
            ((Focussable) getForm()).grabFocus();
        }
    }

    private static final String PAGE_ID = "rigaInstallazionePage";

    private IManutenzioniBD manutenzioniBD = null;

    private AreaInstallazione areaInstallazione;

    private PrimaInstallazioneCommand primaInstallazioneCommand;

    private SostituzioneInstallazioneCommand sostituzioneInstallazioneCommand;

    private RitiroInstallazioneCommand ritiroInstallazioneCommand;

    private ApriInstallazioneCommand apriInstallazioneCommand;

    /**
     * Costruttore.
     */
    public RigaInstallazionePage() {
        super(PAGE_ID, new RigaInstallazioneForm());
        apriInstallazioneCommand = new ApriInstallazioneCommand();
        apriInstallazioneCommand.addCommandInterceptor(new ModificaInstallazioneCommandInterceptor());
    }

    @Override
    protected Object doDelete() {
        manutenzioniBD.cancellaRigaInstallazione(((RigaInstallazione) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        RigaInstallazione rigaInstallazione = (RigaInstallazione) this.getForm().getFormObject();
        rigaInstallazione.setAreaInstallazione(areaInstallazione);
        return manutenzioniBD.salvaRigaInstallazioneInizializza(rigaInstallazione);
    }

    @Override
    protected AbstractCommand[] getCommand() {
        primaInstallazioneCommand = new PrimaInstallazioneCommand();
        sostituzioneInstallazioneCommand = new SostituzioneInstallazioneCommand();
        ritiroInstallazioneCommand = new RitiroInstallazioneCommand();
        primaInstallazioneCommand.setEnabled(false);
        ritiroInstallazioneCommand.setEnabled(false);
        sostituzioneInstallazioneCommand.setEnabled(false);
        apriInstallazioneCommand.setEnabled(false);
        return new AbstractCommand[] { primaInstallazioneCommand, sostituzioneInstallazioneCommand,
                ritiroInstallazioneCommand, toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand(), new SeparatorActionCommand(),
                apriInstallazioneCommand };
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
    public void postSetFormObject(Object object) {
        super.postSetFormObject(object);
        RigaInstallazione ri = (RigaInstallazione) object;
        primaInstallazioneCommand.setEnabled(!ri.isNew());
        ritiroInstallazioneCommand.setEnabled(!ri.isNew());
        sostituzioneInstallazioneCommand.setEnabled(!ri.isNew());
        apriInstallazioneCommand.setEnabled(!ri.isNew());
        if (!ri.isNew() && ri.getInstallazione() != null) {
            primaInstallazioneCommand.setEnabled(ri.getInstallazione().getArticolo() == null);
            ritiroInstallazioneCommand.setEnabled(ri.getInstallazione().getArticolo() != null);
            sostituzioneInstallazioneCommand.setEnabled(ri.getInstallazione().getArticolo() != null);
        }
    }

    @Override
    public void refreshData() {
        // Non utilizzato
    }

    /**
     * @param areaInstallazione
     *            The areaInstallazione to set.
     */
    public void setAreaInstallazione(AreaInstallazione areaInstallazione) {
        this.areaInstallazione = areaInstallazione;
        setReadOnly(false);
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
        RigaInstallazione rigaInstallazione = (RigaInstallazione) getForm().getFormObject();

        if (rigaInstallazione == null || rigaInstallazione.getInstallazione() == null
                || rigaInstallazione.getTipoMovimento() != null) {
            primaInstallazioneCommand.setEnabled(false);
            sostituzioneInstallazioneCommand.setEnabled(false);
            ritiroInstallazioneCommand.setEnabled(false);
        } else {
            boolean articoloInstallazionePresente = rigaInstallazione.getInstallazione().getArticolo() != null
                    && !rigaInstallazione.getInstallazione().getArticolo().isNew();
            primaInstallazioneCommand.setEnabled(!articoloInstallazionePresente);
            sostituzioneInstallazioneCommand.setEnabled(articoloInstallazionePresente);
            ritiroInstallazioneCommand.setEnabled(articoloInstallazionePresente);
        }
    }

}
