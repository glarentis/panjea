package it.eurotn.panjea.contabilita.rich.editors.tabelle.causaliritenutaacconto;

import java.util.Collection;

import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.rich.bd.IRitenutaAccontoBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * @author fattazzo
 *
 */
public class CausaliRitenutaAccontoTablePage extends AbstractTablePageEditor<CausaleRitenutaAcconto> {

    private IRitenutaAccontoBD ritenutaAccontoBD;

    /**
     * Costruttore.
     */
    protected CausaliRitenutaAccontoTablePage() {
        super("causaliRitenutaAccontoTablePage", new CausaliRitenutaAccontoTableModel());
    }

    @Override
    public Collection<CausaleRitenutaAcconto> loadTableData() {
        return ritenutaAccontoBD.caricaCausaliRitenuteAcconto(null);
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public Collection<CausaleRitenutaAcconto> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        // non faccio niente
    }

    /**
     * @param ritenutaAccontoBD
     *            the ritenutaAccontoBD to set
     */
    public void setRitenutaAccontoBD(IRitenutaAccontoBD ritenutaAccontoBD) {
        this.ritenutaAccontoBD = ritenutaAccontoBD;
    }

}
