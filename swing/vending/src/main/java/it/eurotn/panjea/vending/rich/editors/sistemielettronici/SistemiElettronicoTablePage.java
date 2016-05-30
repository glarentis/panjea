package it.eurotn.panjea.vending.rich.editors.sistemielettronici;

import java.util.Collection;

import it.eurotn.panjea.vending.domain.SistemaElettronico;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class SistemiElettronicoTablePage extends AbstractTablePageEditor<SistemaElettronico> {

    public static final String PAGE_ID = "sistemiElettronicoTablePage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore di default.
     */
    public SistemiElettronicoTablePage() {
        super(PAGE_ID, new String[] { "codice", "descrizione", "caricaChiave", "chiave", "cassetta", "resto",
                "tipoComunicazione", "rx", "tx", "baud", "start9600" }, SistemaElettronico.class);
    }

    @Override
    public Collection<SistemaElettronico> loadTableData() {
        return vendingAnagraficaBD.caricaSistemiElettronici();
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
    public Collection<SistemaElettronico> refreshTableData() {
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
