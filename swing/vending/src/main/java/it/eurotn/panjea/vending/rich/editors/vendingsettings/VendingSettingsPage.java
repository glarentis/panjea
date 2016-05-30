package it.eurotn.panjea.vending.rich.editors.vendingsettings;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.vending.domain.EvaDtsImportFolder;
import it.eurotn.panjea.vending.domain.VendingSettings;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class VendingSettingsPage extends FormBackedDialogPageEditor {

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
            VendingSettingsPage.this.loadData();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(PAGE_ID + ".refreshCommand");
        }

    }

    private static final String PAGE_ID = "vendingSettingsPage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;
    private RefreshCommand refreshCommand = null;

    /**
     * Costruttore.
     */
    public VendingSettingsPage() {
        super(PAGE_ID, new VendingSettingsForm());
    }

    @Override
    protected Object doSave() {
        VendingSettings vendingSettings = (VendingSettings) this.getForm().getFormObject();
        for (EvaDtsImportFolder evaDtsImportFolder : vendingSettings.getImportFolder()) {
            evaDtsImportFolder.setVendingSettings(vendingSettings);
        }
        return vendingAnagraficaBD.salvaVendingSettings(vendingSettings);
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
        VendingSettings vendingSettings = vendingAnagraficaBD.caricaVendingSettings();
        setFormObject(vendingSettings);
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
        VendingSettings vendingSettings = vendingAnagraficaBD.caricaVendingSettings();
        super.setFormObject(vendingSettings);
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}
