package it.eurotn.panjea.vending.rich.editors.lettureselezionatrice;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.LetturaSelezionatrice;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class LetturaSelezionatricePage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "letturaSelezionatricePage";

    private IVendingDocumentoBD vendingDocumentoBD = null;

    /**
     * Costruttore.
     */
    public LetturaSelezionatricePage() {
        super(PAGE_ID, new LetturaSelezionatriceForm());
    }

    @Override
    protected Object doDelete() {
        vendingDocumentoBD
                .cancellaLetturaSelezionatrice(((LetturaSelezionatrice) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        LetturaSelezionatrice letturaSelezionatrice = (LetturaSelezionatrice) this.getForm().getFormObject();
        letturaSelezionatrice = vendingDocumentoBD.salvaLetturaSelezionatrice(letturaSelezionatrice);
        return letturaSelezionatrice;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
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
        LetturaSelezionatrice lettura = (LetturaSelezionatrice) object;
        if (!lettura.isNew()) {
            lettura = vendingDocumentoBD.caricaLetturaSelezionatriceById(lettura.getId());
        }
        super.setFormObject(lettura);
    }

    /**
     * @param vendingDocumentoBD
     *            the vendingDocumentoBD to set
     */
    public void setVendingDocumentoBD(IVendingDocumentoBD vendingDocumentoBD) {
        this.vendingDocumentoBD = vendingDocumentoBD;
    }

}
