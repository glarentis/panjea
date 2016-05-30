package it.eurotn.panjea.rich.editors.stampe;

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

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.editors.stampe.nuovo.NuovoLayoutStampaDocumentoCommand;
import it.eurotn.panjea.rich.stampe.ILayoutStampeManager;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class GestioneStampeDocumentoPage extends AbstractTablePageEditor<LayoutStampaDocumento> {

    private class NuovoLayoutStampaCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public void postExecution(ActionCommand command) {
            LayoutStampaDocumento nuovoLayoutStampa = (LayoutStampaDocumento) ((NuovoLayoutStampaDocumentoCommand) command)
                    .getLayoutStampa();
            if (nuovoLayoutStampa != null) {
                PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(
                        LifecycleApplicationEvent.CREATED, nuovoLayoutStampa, this);
                Application.instance().getApplicationContext().publishEvent(event);
                getTable().addRowObject(nuovoLayoutStampa, null);
            }
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            command.addParameter(NuovoLayoutStampaDocumentoCommand.PARAM_ENTITA, entita);
            return super.preExecution(command);
        }
    }

    public static final String PAGE_ID = "gestioneStampeDocumentoPage";

    private ILayoutStampeManager layoutStampeManager;

    private NuovoLayoutStampaDocumentoCommand nuovoLayoutStampaCommand;
    private NuovoLayoutStampaCommandInterceptor nuovoLayoutStampaCommandInterceptor;

    private Entita entita;

    /**
     * Costruttore.
     */
    protected GestioneStampeDocumentoPage() {
        super(PAGE_ID, new GestioneStampeDocumentoTableModel());
        getTable().setAggregatedColumns(
                new String[] { "tipoArea.tipoDocumento.classeTipoDocumento", "tipoArea.tipoDocumento" });

        // nascondo le colonne di soloTest,batch e confermaNumeroCopie perchè vengono usate solo dai
        // renderer per
        // settare i valori
        TableColumnChooser.hideColumn(getTable().getTable(), 8);
        TableColumnChooser.hideColumn(getTable().getTable(), 9);
        TableColumnChooser.hideColumn(getTable().getTable(), 10);
        TableColumnChooser.hideColumn(getTable().getTable(), 11);
        TableColumnChooser.hideColumn(getTable().getTable(), 12);
        TableColumnChooser.hideColumn(getTable().getTable(), 13);

        getTable().getTable().getColumnModel().getColumn(4)
                .setCellEditor(new GestioneStampeButtonsCellEditorRenderer());
        getTable().getTable().getColumnModel().getColumn(4)
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
    public NuovoLayoutStampaDocumentoCommand getAggiungiLayoutStampaCommand() {
        if (nuovoLayoutStampaCommand == null) {
            nuovoLayoutStampaCommand = new NuovoLayoutStampaDocumentoCommand();
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
    public List<LayoutStampaDocumento> loadTableData() {
        if (entita != null) {
            return layoutStampeManager.caricaLayoutStampe(entita.getId());
        }
        return layoutStampeManager.caricaLayoutStampaPerDocumenti();
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public List<LayoutStampaDocumento> refreshTableData() {
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
        entita = null;
        if (object instanceof Entita) {
            entita = (Entita) object;
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
