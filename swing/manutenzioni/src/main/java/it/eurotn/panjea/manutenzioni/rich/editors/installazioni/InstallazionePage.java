package it.eurotn.panjea.manutenzioni.rich.editors.installazioni;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class InstallazionePage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "installazionePage";

    private IManutenzioniBD manutenzioniBD = null;

    /**
     * Costruttore.
     */
    public InstallazionePage() {
        super(PAGE_ID, new InstallazioneForm());
    }

    @Override
    protected Object doDelete() {
        manutenzioniBD.cancellaInstallazione(((Installazione) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        Installazione installazione = (Installazione) this.getForm().getFormObject();
        return manutenzioniBD.salvaInstallazione(installazione);
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

    @Override
    public void setFormObject(Object object) {
        Installazione installazione = (Installazione) object;
        if (!installazione.isNew()) {
            super.setFormObject(manutenzioniBD.caricaInstallazioneById(installazione.getId()));
        }
    }

    /**
     * @param manutenzioniBD
     *            the manutenzioniBD to set
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

}
