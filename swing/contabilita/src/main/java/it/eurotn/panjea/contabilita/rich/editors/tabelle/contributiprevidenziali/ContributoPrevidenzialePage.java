package it.eurotn.panjea.contabilita.rich.editors.tabelle.contributiprevidenziali;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.form.Form;

import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.rich.bd.IRitenutaAccontoBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * @author fattazzo
 *
 */
public abstract class ContributoPrevidenzialePage extends FormBackedDialogPageEditor {

    private IRitenutaAccontoBD ritenutaAccontoBD;

    /**
     * Costruttore.
     * 
     * @param parentPageId
     *            id della pagina
     * @param backingFormPage
     *            form
     */
    public ContributoPrevidenzialePage(final String parentPageId, final Form backingFormPage) {
        super(parentPageId, backingFormPage);
    }

    @Override
    protected Object doDelete() {
        ritenutaAccontoBD
                .cancellaContributoPrevidenziale((ContributoPrevidenziale) getBackingFormPage().getFormObject());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        return ritenutaAccontoBD.salvaContributoPrevidenziale((ContributoPrevidenziale) this.getForm().getFormObject());
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return toolbarPageEditor.getDefaultCommand(true);
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public void refreshData() {
    }

    /**
     * @param ritenutaAccontoBD
     *            the ritenutaAccontoBD to set
     */
    public void setRitenutaAccontoBD(IRitenutaAccontoBD ritenutaAccontoBD) {
        this.ritenutaAccontoBD = ritenutaAccontoBD;
    }

}
