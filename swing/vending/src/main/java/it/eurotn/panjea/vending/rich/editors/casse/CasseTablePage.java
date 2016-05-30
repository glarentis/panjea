package it.eurotn.panjea.vending.rich.editors.casse;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;

import it.eurotn.panjea.vending.domain.Cassa;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class CasseTablePage extends AbstractTablePageEditor<Cassa> {

    private class ChiudiCasseCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            Set<Integer> casseId = new TreeSet<>();

            List<Cassa> casse = getTable().getSelectedObjects();
            for (Cassa cassa : casse) {
                casseId.add(cassa.getId());
            }
            command.addParameter(ChiudiCasseCommand.PARAM_ID_CASSE, casseId.toArray(new Integer[casseId.size()]));
            return !casseId.isEmpty();
        }

    }

    public static final String PAGE_ID = "casseTablePage";

    private IVendingAnagraficaBD vendingAnagraficaBD = null;

    private ChiudiCasseCommand chiudiCasseCommand;

    /**
     * Costruttore di default.
     */
    public CasseTablePage() {
        super(PAGE_ID, new CassaTableModel());
    }

    /**
     * @return the chiudiCasseCommand
     */
    private ChiudiCasseCommand getChiudiCasseCommand() {
        if (chiudiCasseCommand == null) {
            chiudiCasseCommand = new ChiudiCasseCommand();
            chiudiCasseCommand.addCommandInterceptor(new ChiudiCasseCommandInterceptor());
        }

        return chiudiCasseCommand;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getChiudiCasseCommand() };
    }

    @Override
    public Collection<Cassa> loadTableData() {
        return vendingAnagraficaBD.caricaCasse();
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
    public Collection<Cassa> refreshTableData() {
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
