package it.eurotn.panjea.giroclienti.rich.editors.settings;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.giroclienti.domain.GiroClientiSettings;
import it.eurotn.panjea.giroclienti.rich.bd.IGiroClientiAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * @author fattazzo
 *
 */
public class GiroClientiSettingsPage extends FormBackedDialogPageEditor {

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
            GiroClientiSettingsPage.this.refreshData();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            button.setName(REFRESH_COMMAND_ID);
        }

    }

    public static final String PAGE_ID = "giroClientiSettingsPage";
    public static final String REFRESH_COMMAND_ID = PAGE_ID + ".refreshCommand";

    private IGiroClientiAnagraficaBD giroClientiAnagraficaBD;

    private RefreshCommand refreshCommand;

    /**
     * Costruttore.
     */
    public GiroClientiSettingsPage() {
        super(PAGE_ID, new GiroClientiSettingsForm());
    }

    @Override
    protected Object doSave() {
        GiroClientiSettings giroClientiSettings = (GiroClientiSettings) getBackingFormPage().getFormObject();

        giroClientiSettings = giroClientiAnagraficaBD.salvaGiroClientiSettings(giroClientiSettings);
        return giroClientiSettings;
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
        // non faccio niente
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void refreshData() {
        GiroClientiSettings giroClientiSettings = giroClientiAnagraficaBD.caricaGiroClientiSettings();
        setFormObject(giroClientiSettings);
    }

    @Override
    public void setFormObject(Object object) {
        GiroClientiSettings giroClientiSettings = giroClientiAnagraficaBD.caricaGiroClientiSettings();
        super.setFormObject(giroClientiSettings);
    }

    /**
     * @param giroClientiAnagraficaBD
     *            the giroClientiAnagraficaBD to set
     */
    public void setGiroClientiAnagraficaBD(IGiroClientiAnagraficaBD giroClientiAnagraficaBD) {
        this.giroClientiAnagraficaBD = giroClientiAnagraficaBD;
    }

}
