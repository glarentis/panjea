package it.eurotn.panjea.contabilita.rich.editors.tabelle.prestazioni;

import java.util.Collection;

import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.panjea.contabilita.rich.bd.IRitenutaAccontoBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * @author fattazzo
 *
 */
public class PrestazioniTablePage extends AbstractTablePageEditor<Prestazione> {

    private IRitenutaAccontoBD ritenutaAccontoBD;

    /**
     * Costruttore.
     */
    protected PrestazioniTablePage() {
        super("prestazioniTablePage", new PrestazioniTableModel());
    }

    @Override
    public Collection<Prestazione> loadTableData() {
        return ritenutaAccontoBD.caricaPrestazioni();
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public Collection<Prestazione> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        // non faccio niente passo per load e refresh data
    }

    /**
     * @param ritenutaAccontoBD
     *            the ritenutaAccontoBD to set
     */
    public void setRitenutaAccontoBD(IRitenutaAccontoBD ritenutaAccontoBD) {
        this.ritenutaAccontoBD = ritenutaAccontoBD;
    }

}
