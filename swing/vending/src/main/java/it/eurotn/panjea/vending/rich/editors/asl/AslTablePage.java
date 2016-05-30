package it.eurotn.panjea.vending.rich.editors.asl;

import java.util.Collection;

import it.eurotn.panjea.vending.domain.Asl;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class AslTablePage extends AbstractTablePageEditor<Asl> {

    public static final String PAGE_ID = "aslTablePage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore di default.
     */
    public AslTablePage() {
        super(PAGE_ID, new String[] { "anagraficaAsl", "anagraficaAsl.indirizzo", "cap", "localita" }, Asl.class);
    }

    @Override
    public Collection<Asl> loadTableData() {
        return vendingAnagraficaBD.caricaAsl();
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
    public Collection<Asl> refreshTableData() {
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
