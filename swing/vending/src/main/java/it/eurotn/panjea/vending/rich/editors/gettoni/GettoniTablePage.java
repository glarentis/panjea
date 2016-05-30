package it.eurotn.panjea.vending.rich.editors.gettoni;

import java.util.Collection;

import it.eurotn.panjea.vending.domain.Gettone;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class GettoniTablePage extends AbstractTablePageEditor<Gettone> {

    public static final String PAGE_ID = "gettoniTablePage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore di default.
     */
    public GettoniTablePage() {
        super(PAGE_ID, new GettoniTableModel());
    }

    @Override
    public Collection<Gettone> loadTableData() {
        return vendingAnagraficaBD.caricaGettoni();
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
    public Collection<Gettone> refreshTableData() {
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
