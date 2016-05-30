package it.eurotn.panjea.contabilita.rich.editors.tabelle.contributiprevidenziali;

import java.util.Collection;

import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.rich.bd.IRitenutaAccontoBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * @author fattazzo
 *
 */
public class ContributiPrevidenzialiTablePage extends AbstractTablePageEditor<ContributoPrevidenziale> {

    private IRitenutaAccontoBD ritenutaAccontoBD;

    /**
     * Costruttore.
     */
    protected ContributiPrevidenzialiTablePage() {
        super("contributiPrevidenzialiTablePage", new ContributiPrevidenzialiTableModel());
    }

    @Override
    public Collection<ContributoPrevidenziale> loadTableData() {
        return ritenutaAccontoBD.caricaContributiPrevidenziali();
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public Collection<ContributoPrevidenziale> refreshTableData() {
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
