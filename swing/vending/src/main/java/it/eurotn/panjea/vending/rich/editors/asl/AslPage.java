package it.eurotn.panjea.vending.rich.editors.asl;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.Asl;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class AslPage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "aslPage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore.
     */
    public AslPage() {
        super(PAGE_ID, new AslForm());
    }

    @Override
    protected Object doDelete() {
        vendingAnagraficaBD.cancellaAsl(((Asl) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        Asl asl = (Asl) this.getForm().getFormObject();
        return vendingAnagraficaBD.salvaAsl(asl);
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
