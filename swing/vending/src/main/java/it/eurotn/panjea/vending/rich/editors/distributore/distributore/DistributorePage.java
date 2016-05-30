package it.eurotn.panjea.vending.rich.editors.distributore.distributore;

import java.util.Objects;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.panjea.vending.rich.editors.distributore.DistributorePM;
import it.eurotn.panjea.vending.rich.editors.distributore.distributorimodello.DistributoriTablePage;
import it.eurotn.panjea.vending.rich.editors.distributore.modelli.ModelliTreeTablePage;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * Gestisce la visualizzazione dell'anagrafica del distributore.
 *
 */
public class DistributorePage extends FormBackedDialogPageEditor {

    private IVendingAnagraficaBD vendingAnagraficaBD;

    /**
     * Costruttore.
     */
    public DistributorePage() {
        super("distributorePage", new DistributoreForm());
    }

    @Override
    protected Object doDelete() {
        Distributore distributore = (Distributore) this.getForm().getFormObject();
        vendingAnagraficaBD.cancellaDistributore(distributore.getId());
        return distributore;
    }

    @Override
    protected Object doSave() {
        Distributore distributore = (Distributore) this.getForm().getFormObject();
        distributore = vendingAnagraficaBD.salvaDistributore(distributore);
        return distributore;
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
    public void refreshData() {
        // Non utilizzato
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof DistributorePM) {
            DistributorePM distributorePM = (DistributorePM) object;

            if (Objects.equals(ModelliTreeTablePage.PAGE_ID, distributorePM.getSource())) {
                ((DistributoreForm) getForm()).setModello(distributorePM.getModello());
            }
            if (Objects.equals(DistributoriTablePage.PAGE_ID, distributorePM.getSource())) {
                super.setFormObject(distributorePM.getDistributore());
            }
        } else {
            // potrei ricevere un distributore dalla searchObject degli articoliMI. in quel caso ho
            // solo id del distributor e devo caricare tutto
            Distributore distributore = (Distributore) object;
            if (distributore.getId() != null) {
                distributore = vendingAnagraficaBD.caricaDistributore(distributore.getId());
            }
            super.setFormObject(distributore);
        }
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public final void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}