package it.eurotn.panjea.vending.rich.editors.tipimodello;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class TipoModelloPage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "tipoModelloPage";
    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore.
     */
    public TipoModelloPage() {
        super(PAGE_ID, new TipoModelloForm());
    }

    @Override
    protected Object doDelete() {
        vendingAnagraficaBD.cancellaTipoModello(((TipoModello) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        TipoModello tipoModello = (TipoModello) this.getForm().getFormObject();
        return vendingAnagraficaBD.salvaTipoModello(tipoModello);
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
