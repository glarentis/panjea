package it.eurotn.panjea.vending.rich.editors.movimenticassa;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.panjea.vending.domain.RigaMovimentoCassa;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class MovimentoCassaPage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "movimentoCassaPage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore.
     */
    public MovimentoCassaPage() {
        super(PAGE_ID, new MovimentoCassaForm());
    }

    @Override
    protected Object doDelete() {
        vendingAnagraficaBD.cancellaMovimentoCassa(((MovimentoCassa) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        MovimentoCassa movimentoCassa = (MovimentoCassa) this.getForm().getFormObject();
        for (RigaMovimentoCassa riga : movimentoCassa.getRighe()) {
            riga.setMovimentoCassa(movimentoCassa);
        }
        return vendingAnagraficaBD.salvaMovimentoCassa(movimentoCassa);
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
        MovimentoCassa movimentoCassa = (MovimentoCassa) object;
        if (!movimentoCassa.isNew()) {
            movimentoCassa = vendingAnagraficaBD.caricaMovimentoCassaById(movimentoCassa.getId());
        }
        super.setFormObject(movimentoCassa);
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}
