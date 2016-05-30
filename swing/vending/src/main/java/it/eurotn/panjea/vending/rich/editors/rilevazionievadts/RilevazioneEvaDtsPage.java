package it.eurotn.panjea.vending.rich.editors.rilevazionievadts;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.evadts.RilevazioneEvaDts;
import it.eurotn.panjea.vending.rich.bd.IVendingDocumentoBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class RilevazioneEvaDtsPage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "rilevazioneEvaDtsPage";

    private IVendingDocumentoBD vendingDocumentoBD = null;

    /**
     * Costruttore.
     */
    public RilevazioneEvaDtsPage() {
        super(PAGE_ID, new RilevazioneEvaDtsForm());
    }

    @Override
    protected Object doDelete() {
        vendingDocumentoBD
                .cancellaRilevazioneEvaDts(((RilevazioneEvaDts) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { toolbarPageEditor.getDeleteCommand() };
    }

    @Override
    public AbstractCommand getEditorLockCommand() {
        return null;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return null;
    }

    @Override
    public AbstractCommand getNewCommand() {
        return null;
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
     * @param vendingDocumentoBD
     *            the vendingDocumentoBD to set
     */
    public void setVendingDocumentoBD(IVendingDocumentoBD vendingDocumentoBD) {
        this.vendingDocumentoBD = vendingDocumentoBD;
    }

}
