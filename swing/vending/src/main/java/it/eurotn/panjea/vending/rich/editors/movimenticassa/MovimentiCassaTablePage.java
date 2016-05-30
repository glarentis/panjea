package it.eurotn.panjea.vending.rich.editors.movimenticassa;

import java.util.Collection;

import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class MovimentiCassaTablePage extends AbstractTablePageEditor<MovimentoCassa> {

    public static final String PAGE_ID = "movimentiCassaTablePage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore di default.
     */
    public MovimentiCassaTablePage() {
        super(PAGE_ID, new MovimentiCassaTableModel());
    }

    @Override
    public Collection<MovimentoCassa> loadTableData() {
        return vendingAnagraficaBD.caricaMovimentiCassa(false);
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
    public Collection<MovimentoCassa> refreshTableData() {
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
