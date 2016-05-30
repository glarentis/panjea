package it.eurotn.panjea.vending.rich.editors.tipicomunicazione;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.TipoComunicazione;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class TipoComunicazionePage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "tipoComunicazionePage";
    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore.
     */
    public TipoComunicazionePage() {
        super(PAGE_ID, new TipoComunicazioneForm());
    }

    @Override
    protected Object doDelete() {
        vendingAnagraficaBD
                .cancellaTipoComunicazione(((TipoComunicazione) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        TipoComunicazione tipoComunicazione = (TipoComunicazione) this.getForm().getFormObject();
        return vendingAnagraficaBD.salvaTipoComunicazione(tipoComunicazione);
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
