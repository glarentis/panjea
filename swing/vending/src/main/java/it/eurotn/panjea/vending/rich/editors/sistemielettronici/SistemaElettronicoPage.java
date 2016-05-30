package it.eurotn.panjea.vending.rich.editors.sistemielettronici;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.SistemaElettronico;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class SistemaElettronicoPage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "sistemaElettronicoPage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore.
     */
    public SistemaElettronicoPage() {
        super(PAGE_ID, new SistemaElettronicoForm());
    }

    @Override
    protected Object doDelete() {
        vendingAnagraficaBD
                .cancellaSistemaElettronico(((SistemaElettronico) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        SistemaElettronico sistemaElettronico = (SistemaElettronico) this.getForm().getFormObject();
        return vendingAnagraficaBD.salvaSistemaElettronico(sistemaElettronico);
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
