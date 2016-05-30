package it.eurotn.panjea.vending.rich.editors.tipicomunicazione;

import java.util.Collection;

import it.eurotn.panjea.vending.domain.TipoComunicazione;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class TipiComunicazioneTablePage extends AbstractTablePageEditor<TipoComunicazione> {

    public static final String PAGE_ID = "tipiComunicazioneTablePage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    /**
     * Costruttore di default.
     */
    public TipiComunicazioneTablePage() {
        super(PAGE_ID, new String[] { "codice", "descrizione", "comunicazioneAsl", "comunicazioneComuni" },
                TipoComunicazione.class);
    }

    @Override
    public Collection<TipoComunicazione> loadTableData() {
        return vendingAnagraficaBD.caricaTipiComunicazione();
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
    public Collection<TipoComunicazione> refreshTableData() {
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
