package it.eurotn.panjea.manutenzioni.rich.editors.causaliinstallazione;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.manutenzioni.domain.CausaleInstallazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class CausaleInstallazionePage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "causaleInstallazionePage";

    private IManutenzioniBD manutenzioniBD = null;

    /**
     * Costruttore.
     */
    public CausaleInstallazionePage() {
        super(PAGE_ID, new CausaleInstallazioneForm());
    }

    @Override
    protected Object doDelete() {
        manutenzioniBD.cancellaCausaleInstallazione(((CausaleInstallazione) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        CausaleInstallazione causaleInstallazione = (CausaleInstallazione) this.getForm().getFormObject();
        return manutenzioniBD.salvaCausaleInstallazione(causaleInstallazione);
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return toolbarPageEditor.getDefaultCommand(true);
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

}
