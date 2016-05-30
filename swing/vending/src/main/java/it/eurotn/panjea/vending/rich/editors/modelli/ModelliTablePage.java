package it.eurotn.panjea.vending.rich.editors.modelli;

import java.util.Collection;

import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class ModelliTablePage extends AbstractTablePageEditor<Modello> {

    public static final String PAGE_ID = "modelliTablePage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore di default.
     */
    public ModelliTablePage() {
        super(PAGE_ID, new String[] { "codice", "descrizione", "tipoModello", "cassettaPresente",
                "obbligoLetturaCassetta", "ritiroCassetta" }, Modello.class);
    }

    @Override
    public Collection<Modello> loadTableData() {
        return vendingAnagraficaBD.caricaModelli();
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
    public Collection<Modello> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
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
