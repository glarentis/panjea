package it.eurotn.panjea.vending.rich.editors.tipimodello;

import java.util.Collection;

import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class TipiModelloTablePage extends AbstractTablePageEditor<TipoModello> {

    public static final String PAGE_ID = "tipiModelloTablePage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore di default.
     */
    public TipiModelloTablePage() {
        super(PAGE_ID, new String[] { "codice", "descrizione", "tipoComunicazione", "caldo", "freddo", "snack",
                "snackRefrigerati", "kit", "acqua", "gelato" }, TipoModello.class);
    }

    @Override
    public Collection<TipoModello> loadTableData() {
        return vendingAnagraficaBD.caricaTipiModello();
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
    public Collection<TipoModello> refreshTableData() {
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
