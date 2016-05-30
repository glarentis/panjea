package it.eurotn.panjea.rich.editors.stampe;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.grid.TableColumnChooser;
import com.jidesoft.swing.JideScrollPane;

import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.editors.stampe.nuovo.NuovoLayoutStampaCommand;
import it.eurotn.panjea.rich.stampe.ILayoutStampeManager;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class GestioneStampePage extends AbstractTablePageEditor<LayoutStampa> {

    private class NuovoLayoutStampaCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public void postExecution(ActionCommand command) {
            LayoutStampa nuovoLayoutStampa = ((NuovoLayoutStampaCommand) command).getLayoutStampa();
            if (nuovoLayoutStampa != null) {
                PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
                        LifecycleApplicationEvent.CREATED, nuovoLayoutStampa, this);
                Application.instance().getApplicationContext().publishEvent(event);
                getTable().addRowObject(nuovoLayoutStampa, null);
            }
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            return super.preExecution(command);
        }
    }

    public static final String PAGE_ID = "gestioneStampePage";

    private ILayoutStampeManager layoutStampeManager;

    private NuovoLayoutStampaCommand nuovoLayoutStampaCommand;
    private NuovoLayoutStampaCommandInterceptor nuovoLayoutStampaCommandInterceptor;

    /**
     * Costruttore.
     */
    protected GestioneStampePage() {
        super(PAGE_ID, new GestioneStampeTableModel());

        // nascondo le colonne di soloTest,batch e confermaNumeroCopie perchè vengono usate solo dai
        // renderer per
        // settare i valori
        TableColumnChooser.hideColumn(getTable().getTable(), 4);
        TableColumnChooser.hideColumn(getTable().getTable(), 5);
        TableColumnChooser.hideColumn(getTable().getTable(), 6);
        TableColumnChooser.hideColumn(getTable().getTable(), 7);

        getTable().getTable().getColumnModel().getColumn(0)
                .setCellEditor(new GestioneStampeButtonsCellEditorRenderer());
        getTable().getTable().getColumnModel().getColumn(0)
                .setCellRenderer(new GestioneStampeButtonsCellEditorRenderer());
        getTable().getTable().getTableHeader().setReorderingAllowed(false);

        // tolgo la possibilità di vedere le opzioni della tabella visto che non mi servono
        getTable().getOverlayTable().getOptionsPanel().setVisible(false);
        ((JideScrollPane) ((JPanel) getTable().getOverlayTable().getActualComponent()).getComponent(1))
                .getCorner(JScrollPane.UPPER_RIGHT_CORNER).setVisible(false);
    }

    @Override
    public void dispose() {
        getAggiungiLayoutStampaCommand().removeCommandInterceptor(getNuovoLayoutStampaCommandInterceptor());
        super.dispose();
    }

    /**
     * @return Returns the aggiungiLayoutStampaCommand.
     */
    public NuovoLayoutStampaCommand getAggiungiLayoutStampaCommand() {
        if (nuovoLayoutStampaCommand == null) {
            nuovoLayoutStampaCommand = new NuovoLayoutStampaCommand();
            nuovoLayoutStampaCommand.addCommandInterceptor(getNuovoLayoutStampaCommandInterceptor());
        }

        return nuovoLayoutStampaCommand;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getRefreshCommand(), getAggiungiLayoutStampaCommand() };
    }

    /**
     * @return Returns the nuovoLayoutStampaCommandInterceptor.
     */
    public NuovoLayoutStampaCommandInterceptor getNuovoLayoutStampaCommandInterceptor() {
        if (nuovoLayoutStampaCommandInterceptor == null) {
            nuovoLayoutStampaCommandInterceptor = new NuovoLayoutStampaCommandInterceptor();
        }

        return nuovoLayoutStampaCommandInterceptor;
    }

    @Override
    public List<LayoutStampa> loadTableData() {
        return layoutStampeManager.caricaLayoutStampe();
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public List<LayoutStampa> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void restoreState(Settings settings) {
    }

    @Override
    public void saveState(Settings settings) {
    }

    @Override
    public void setFormObject(Object object) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = clipboard.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if (hasTransferableText && contents != null) {
            try {
                String clipboardContent = (String) contents.getTransferData(DataFlavor.stringFlavor);
                if (clipboardContent.startsWith("layout-")) {
                    getAggiungiLayoutStampaCommand().execute();
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                logger.error("-->errore nel recuperare il contenuto della clipboard", ex);
            }
        }
    }

    /**
     * @param layoutStampeManager
     *            The layoutStampeManager to set.
     */
    public void setLayoutStampeManager(ILayoutStampeManager layoutStampeManager) {
        this.layoutStampeManager = layoutStampeManager;
    }

}
