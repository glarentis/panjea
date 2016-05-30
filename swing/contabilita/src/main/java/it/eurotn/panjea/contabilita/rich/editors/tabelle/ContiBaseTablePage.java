package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import java.util.Collection;

import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * Classe per la gestione dei conti base.
 *
 * @author fattazzo
 * @version 1.0, 27/ago/07
 *
 */
public class ContiBaseTablePage extends AbstractTablePageEditor<ContoBase> {

    private static final String PAGE_ID = "contiBaseTablePage";
    private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

    /**
     * Costruttore.
     */
    protected ContiBaseTablePage() {
        super(PAGE_ID, new String[] { "descrizione", "sottoConto.sottoContoCodice", "tipoContoBase" }, ContoBase.class);
    }

    @Override
    public Collection<ContoBase> loadTableData() {
        return contabilitaAnagraficaBD.caricaContiBase();
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public Collection<ContoBase> refreshTableData() {
        return null;
    }

    /**
     * @param contabilitaAnagraficaBD
     *            contabilitaAnagraficaBD to set
     */
    public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
        // non faccio niente
    }

}
