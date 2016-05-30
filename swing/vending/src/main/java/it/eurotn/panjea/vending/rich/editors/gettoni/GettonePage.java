package it.eurotn.panjea.vending.rich.editors.gettoni;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.Gettone;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class GettonePage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "gettonePage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore.
     */
    public GettonePage() {
        super(PAGE_ID, new GettoneForm());
    }

    @Override
    protected Object doDelete() {
        vendingAnagraficaBD.cancellaGettone(((Gettone) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        Gettone gettone = (Gettone) this.getForm().getFormObject();
        return vendingAnagraficaBD.salvaGettone(gettone);
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
