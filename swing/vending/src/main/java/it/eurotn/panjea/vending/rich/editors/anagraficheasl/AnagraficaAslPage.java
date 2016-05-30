package it.eurotn.panjea.vending.rich.editors.anagraficheasl;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.AnagraficaAsl;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class AnagraficaAslPage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "anagraficaAslPage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore.
     */
    public AnagraficaAslPage() {
        super(PAGE_ID, new AnagraficaAslForm());
    }

    @Override
    protected Object doDelete() {
        vendingAnagraficaBD.cancellaAnagraficaAsl(((AnagraficaAsl) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        AnagraficaAsl anagraficaAsl = (AnagraficaAsl) this.getForm().getFormObject();
        return vendingAnagraficaBD.salvaAnagraficaAsl(anagraficaAsl);
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
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}
