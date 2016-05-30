package it.eurotn.panjea.contabilita.rich.editors.tabelle.prestazioni;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.panjea.contabilita.rich.bd.IRitenutaAccontoBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * @author fattazzo
 *
 */
public class PrestazionePage extends FormBackedDialogPageEditor {

    private IRitenutaAccontoBD ritenutaAccontoBD;

    /**
     * Costruttore.
     */
    public PrestazionePage() {
        super("prestazionePage", new PrestazioneForm());
    }

    @Override
    protected Object doDelete() {
        ritenutaAccontoBD.cancellaPrestazione((Prestazione) getBackingFormPage().getFormObject());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        return ritenutaAccontoBD.salvaPrestazione((Prestazione) this.getForm().getFormObject());
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return toolbarPageEditor.getDefaultCommand(true);
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
    public void refreshData() {
        // non faccio niente
    }

    /**
     * @param ritenutaAccontoBD
     *            the ritenutaAccontoBD to set
     */
    public void setRitenutaAccontoBD(IRitenutaAccontoBD ritenutaAccontoBD) {
        this.ritenutaAccontoBD = ritenutaAccontoBD;
    }

}
