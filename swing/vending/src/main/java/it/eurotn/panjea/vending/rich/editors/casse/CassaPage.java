package it.eurotn.panjea.vending.rich.editors.casse;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.Cassa;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class CassaPage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "cassaPage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore.
     */
    public CassaPage() {
        super(PAGE_ID, new CassaForm());
    }

    @Override
    protected Object doDelete() {
        vendingAnagraficaBD.cancellaCassa(((Cassa) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        Cassa cassa = (Cassa) this.getForm().getFormObject();
        return vendingAnagraficaBD.salvaCassa(cassa);
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
        Cassa cassa = (Cassa) object;
        if (!cassa.isNew()) {
            cassa = vendingAnagraficaBD.caricaCassaById(cassa.getId());
        }
        super.setFormObject(cassa);
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}
