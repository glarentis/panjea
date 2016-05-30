package it.eurotn.panjea.fatturepa.rich.editors.settings;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.fatturepa.domain.FatturaPASettings;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePAAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * @author fattazzo
 *
 */
public class FatturaPASettingsPage extends FormBackedDialogPageEditor {

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
            FatturaPASettingsPage.this.refreshData();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            button.setName(REFRESH_COMMAND_ID);
        }

    }

    public static final String PAGE_ID = "fatturaPASettingsPage";
    public static final String REFRESH_COMMAND_ID = PAGE_ID + ".refreshCommand";

    private IFatturePAAnagraficaBD fatturePAAnagraficaBD;

    private RefreshCommand refreshCommand;

    /**
     * Costruttore.
     */
    public FatturaPASettingsPage() {
        super(PAGE_ID, new FatturaPASettingsForm());
    }

    @Override
    protected Object doSave() {
        FatturaPASettings fatturaPASettings = (FatturaPASettings) getBackingFormPage().getFormObject();
        String registroProtocollo = fatturaPASettings.getRegistroProtocollo();
        String[] protocolloSplit = registroProtocollo.split(" - ");
        fatturaPASettings.setRegistroProtocollo(protocolloSplit[0]);

        fatturaPASettings = fatturePAAnagraficaBD.salvaFatturaPASettings(fatturaPASettings);
        return fatturaPASettings;
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
        FatturaPASettings fatturaPASettings = fatturePAAnagraficaBD.caricaFatturaPASettings();
        setFormObject(fatturaPASettings);
    }

    /**
     * @param fatturePAAnagraficaBD
     *            the fatturePAAnagraficaBD to set
     */
    public void setFatturePAAnagraficaBD(IFatturePAAnagraficaBD fatturePAAnagraficaBD) {
        this.fatturePAAnagraficaBD = fatturePAAnagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
        FatturaPASettings fatturaPASettings = fatturePAAnagraficaBD.caricaFatturaPASettings();
        super.setFormObject(fatturaPASettings);
    }

}
