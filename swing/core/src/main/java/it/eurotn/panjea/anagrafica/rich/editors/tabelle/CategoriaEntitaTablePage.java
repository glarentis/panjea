/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.tabelle;

import java.util.Collection;
import java.util.Observable;

import javax.swing.JComponent;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockableHolderPanel;

import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.dialog.DockingLayoutManager;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * @author fattazzo
 *
 */
public class CategoriaEntitaTablePage extends AbstractTablePageEditor<CategoriaEntita> {

    public static final String PAGE_ID = "categoriaEntitaTablePage";

    private IAnagraficaBD anagraficaBD;

    private DockableFrame tableFrame;

    private JideTableWidget<SedeEntita> entitaCollegateTable;

    /**
     * Costruttore.
     */
    protected CategoriaEntitaTablePage() {
        super(PAGE_ID, new String[] { "sezione", "descrizione" }, CategoriaEntita.class);
    }

    @Override
    protected JComponent createControl() {
        entitaCollegateTable = new JideTableWidget<>("entitaCollegateTable",
                new String[] { "entita", "sede.descrizione", "sede.indirizzo", "sede.datiGeografici.localita" },
                SedeEntita.class);
        tableFrame = new DockableFrame();
        tableFrame.setKey("Entita");
        tableFrame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        tableFrame.getContext().setInitIndex(1);
        tableFrame.getContentPane().add(entitaCollegateTable.getComponent());
        tableFrame.setAvailableButtons(0);
        tableFrame.setShowContextMenu(false);
        DockableHolderPanel holderPanel = (DockableHolderPanel) super.createControl();
        holderPanel.getDockingManager().setRearrangable(true);
        // aggiungo il frame della tabella
        holderPanel.getDockingManager().addFrame(tableFrame);
        return holderPanel;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return null;
    }

    @Override
    public Collection<CategoriaEntita> loadTableData() {
        return anagraficaBD.caricaCategorieEntita("descrizione", null);
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public Collection<CategoriaEntita> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void restoreState(Settings settings) {
        super.restoreState(settings);
        if (holderPanel != null) {
            try {
                DockingLayoutManager.restoreLayout(holderPanel.getDockingManager(), this.getId());
            } catch (Exception ex) {
                holderPanel.getDockingManager().resetToDefault();
                logger.error("-->errore nel restore del layout.NV", ex);
            }
        }
    }

    @Override
    public void saveState(Settings settings) {
        super.saveState(settings);
        DockingLayoutManager.saveLayout(holderPanel.getDockingManager(), this.getId());
    }

    /**
     *
     * @param anagraficaBD
     *            .
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
    }

    @Override
    public void update(Observable observable, Object obj) {
        super.update(observable, obj);
        entitaCollegateTable.setRows(anagraficaBD.caricaEntitaByCategorie(getTable().getSelectedObjects()));
    }

}
