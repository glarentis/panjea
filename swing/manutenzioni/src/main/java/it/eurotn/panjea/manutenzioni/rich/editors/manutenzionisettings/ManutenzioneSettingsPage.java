package it.eurotn.panjea.manutenzioni.rich.editors.manutenzionisettings;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.manutenzioni.domain.ManutenzioneSettings;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class ManutenzioneSettingsPage extends FormBackedDialogPageEditor {

    private class RefreshCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public RefreshCommand() {
            super("refreshCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ManutenzioneSettingsPage.this.loadData();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(PAGE_ID + ".refreshCommand");
        }

    }

    private static final String PAGE_ID = "manutenzioneSettingsPage";

    private IManutenzioniBD manutenzioniBD = null;
    private RefreshCommand refreshCommand = null;

    /**
     * Costruttore.
     */
    public ManutenzioneSettingsPage() {
        super(PAGE_ID, new ManutenzioneSettingsForm());
    }

    @Override
    protected Object doSave() {
        ManutenzioneSettings manutenzioneSettings = (ManutenzioneSettings) this.getForm().getFormObject();
        return manutenzioniBD.salvaManutenzioneSettings(manutenzioneSettings);
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
                toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(), getRefreshCommand() };
        return abstractCommands;
    }

    /**
     *
     * @return command refresh.
     */
    public ActionCommand getRefreshCommand() {
        if (this.refreshCommand == null) {
            this.refreshCommand = new RefreshCommand();
        }

        return this.refreshCommand;
    }

    @Override
    public void loadData() {
        ManutenzioneSettings manutenzioniSettings = manutenzioniBD.caricaManutenzioniSettings();
        setFormObject(manutenzioniSettings);
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
        loadData();
    }

    @Override
    public void setFormObject(Object object) {
        ManutenzioneSettings manutenzioneSettings = manutenzioniBD.caricaManutenzioniSettings();
        super.setFormObject(manutenzioneSettings);
    }

    /**
     * @param manutenzioniBD
     *            the manutenzioniBD to set
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

}
