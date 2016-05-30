package it.eurotn.panjea.vending.rich.editors.anagraficheasl;

import java.util.Collection;

import it.eurotn.panjea.vending.domain.AnagraficaAsl;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class AnagraficheAslTablePage extends AbstractTablePageEditor<AnagraficaAsl> {

    public static final String PAGE_ID = "anagraficheAslTablePage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore di default.
     */
    public AnagraficheAslTablePage() {
        super(PAGE_ID, new String[] { "codice", "descrizione", "indirizzo" }, AnagraficaAsl.class);
    }

    @Override
    public Collection<AnagraficaAsl> loadTableData() {
        return vendingAnagraficaBD.caricaAnagraficheAsl();
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
    public Collection<AnagraficaAsl> refreshTableData() {
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
