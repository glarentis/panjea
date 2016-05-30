package it.eurotn.panjea.vending.rich.editors.modelli;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * @author fattazzo
 *
 */
public class ModelloPage extends FormBackedDialogPageEditor {

    public static final String PAGE_ID = "modelloPage";

    public static final String MODELLO_UPDATE = "modelloUpdate";

    private IVendingAnagraficaBD vendingAnagraficaBD;

    /**
     * Costruttore.
     */
    public ModelloPage() {
        super(PAGE_ID, new ModelloForm());
        getEditorLockCommand().execute();
    }

    @Override
    protected Object doDelete() {
        vendingAnagraficaBD.cancellaModello(((Modello) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {

        Modello modelloSave = (Modello) getBackingFormPage().getFormObject();

        Modello modelloResult = this.vendingAnagraficaBD.salvaModello(modelloSave);
        getBackingFormPage().setFormObject(modelloResult);

        PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(LifecycleApplicationEvent.MODIFIED,
                modelloResult);
        Application.instance().getApplicationContext().publishEvent(event);

        ModelloPage.this.firePropertyChange(MODELLO_UPDATE, null, modelloResult);

        return modelloResult;
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
    public boolean onUndo() {

        Modello modello = (Modello) getBackingFormPage().getFormObject();

        ModelloPage.this.firePropertyChange(MODELLO_UPDATE, null, modello);

        return super.onUndo();
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
