package it.eurotn.panjea.rich.editors.dms;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.dms.domain.DmsSettings;
import it.eurotn.panjea.rich.bd.IDmsBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class DmsSettingsPage extends FormBackedDialogPageEditor {

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
            DmsSettingsPage.this.refreshData();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            button.setName(REFRESH_COMMAND_ID);
        }

    }

    public static final String PAGE_ID = "dmsSettingsPage";

    public static final String REFRESH_COMMAND_ID = PAGE_ID + ".refreshCommand";
    private IDmsBD dmsBD;

    private RefreshCommand refreshCommand;

    /**
     *
     * Costruttore.
     */
    public DmsSettingsPage() {
        super(PAGE_ID, new DmsSettingsForm());
    }

    @Override
    protected Object doSave() {
        DmsSettings dmsSettings = (DmsSettings) getBackingFormPage().getFormObject();

        dmsSettings = dmsBD.salvaDmsSettings(dmsSettings);
        return dmsSettings;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), getRefreshCommand() };
    }

    /**
     * @return the refreshCommand
     */
    private RefreshCommand getRefreshCommand() {
        if (refreshCommand == null) {
            refreshCommand = new RefreshCommand();
        }

        return refreshCommand;
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
    public void refreshData() {
        setFormObject(dmsBD.caricaDmsSettings());
    }

    /**
     * @param dmsBD
     *            the dmsBD to set
     */
    public void setDmsBD(IDmsBD dmsBD) {
        this.dmsBD = dmsBD;
    }

    @Override
    public void setFormObject(Object object) {
        super.setFormObject(dmsBD.caricaDmsSettings());
    }

}
