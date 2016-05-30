package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ProRataSetting;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.forms.ContabilitaSettingsForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class ContabilitaSettingsPage extends FormBackedDialogPageEditor {

    private class RefreshCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public RefreshCommand() {
            super("refreshCommand");
            this.setSecurityControllerId(REFRESH_COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ContabilitaSettingsPage.this.loadData();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(PAGE_ID + ".refreshCommand");
        }

    }

    public static final String PAGE_ID = "contabilitaSettingsPage";

    public static final String REFRESH_COMMAND_ID = PAGE_ID + ".refreshCommand";
    private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

    private RefreshCommand refreshCommand;

    /**
     * Costruttore.
     */
    public ContabilitaSettingsPage() {
        super(PAGE_ID, new ContabilitaSettingsForm());
    }

    @Override
    protected Object doSave() {
        ContabilitaSettings contabilitaSettings = (ContabilitaSettings) getForm().getFormObject();
        for (ProRataSetting proRataSetting : contabilitaSettings.getProRataSettings()) {
            proRataSetting.setContabilitaSettings(contabilitaSettings);
        }
        contabilitaSettings = contabilitaAnagraficaBD.salvaContabilitaSettings(contabilitaSettings);
        return contabilitaSettings;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), getRefreshCommand() };
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
        ContabilitaSettings contabilitaSettings = contabilitaAnagraficaBD.caricaContabilitaSettings();
        setFormObject(contabilitaSettings);
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
        loadData();
    }

    /**
     *
     * @param contabilitaAnagraficaBD
     *            bd anagrafica
     */
    public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
        ContabilitaSettings contabilitaSettings = contabilitaAnagraficaBD.caricaContabilitaSettings();
        super.setFormObject(contabilitaSettings);
    }

}
