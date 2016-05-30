package it.eurotn.panjea.manutenzioni.rich.editors.ubicazioniinstallazione;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.manutenzioni.domain.UbicazioneInstallazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class UbicazioneInstallazionePage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "ubicazioneInstallazionePage";

    private IManutenzioniBD manutenzioniBD = null;

    /**
     * Costruttore.
     */
    public UbicazioneInstallazionePage() {
        super(PAGE_ID, new UbicazioneInstallazioneForm());
    }

    @Override
    protected Object doDelete() {
        manutenzioniBD.cancellaUbicazioneInstallazione(((UbicazioneInstallazione) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        UbicazioneInstallazione ubicazioneInstallazione = (UbicazioneInstallazione) this.getForm().getFormObject();
        return manutenzioniBD.salvaUbicazioneInstallazione(ubicazioneInstallazione);
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
