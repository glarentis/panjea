package it.eurotn.panjea.anagrafica.rich.editors.tabelle.riepilogodatibancari;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class RiepilogoDatiBancariTablePage extends AbstractTablePageEditor<RapportoBancarioSedeEntita> {

    private IAnagraficaBD anagraficaBD;

    private SostituisciDatiBancariCommand sostituisciDatiBancariCommand;

    /**
     *
     * Costruttore.
     */
    protected RiepilogoDatiBancariTablePage() {
        super("riepilogoDatiBancariTablePage", new String[] { "banca", "filiale", "sedeEntita.entita", "sedeEntita" },
                RapportoBancarioSedeEntita.class);
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getSostituisciDatiBancariCommand(), getRefreshCommand() };
    }

    /**
     * @return the sostituisciDatiBancariCommand
     */
    private SostituisciDatiBancariCommand getSostituisciDatiBancariCommand() {
        if (sostituisciDatiBancariCommand == null) {
            sostituisciDatiBancariCommand = new SostituisciDatiBancariCommand();
            sostituisciDatiBancariCommand.addCommandInterceptor(new ActionCommandInterceptor() {

                @Override
                public void postExecution(ActionCommand arg0) {
                    refreshData();
                }

                @Override
                public boolean preExecution(ActionCommand command) {
                    command.addParameter(SostituisciDatiBancariCommand.PARAM_RAPPORTI_BANCARI,
                            getTable().getSelectedObjects());
                    return CollectionUtils.isNotEmpty(getTable().getSelectedObjects());
                }
            });
        }

        return sostituisciDatiBancariCommand;
    }

    @Override
    public Collection<RapportoBancarioSedeEntita> loadTableData() {
        return anagraficaBD.caricaRiepilogoDatiBancari();
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public Collection<RapportoBancarioSedeEntita> refreshTableData() {
        return loadTableData();
    }

    /**
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
    }

}
