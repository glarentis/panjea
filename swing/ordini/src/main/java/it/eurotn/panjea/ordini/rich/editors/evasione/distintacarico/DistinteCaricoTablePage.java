package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.filter.Filter;
import com.jidesoft.grid.AbstractTableFilter;
import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.FilterableTableModel;
import com.jidesoft.grid.GroupTable;
import com.jidesoft.grid.IFilterableTableModel;
import com.jidesoft.grid.IndexReferenceRow;
import com.jidesoft.grid.SortableTreeTableModel;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico.StatoRiga;
import it.eurotn.panjea.ordini.rich.bd.IAnagraficaOrdiniBD;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.evasione.EvasionePanel;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.rich.binding.PanjeaTextFieldDateEditor;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget.TableType;
import it.eurotn.rich.editors.AbstractTablePageEditor;

/**
 * Gestisce le distinte di carico.
 *
 * @author giangi
 * @version 1.0, 29/gen/2011
 *
 */
public class DistinteCaricoTablePage extends AbstractTablePageEditor<RigaDistintaCarico> {
    private class CancellaRigheCommand extends ApplicationWindowAwareCommand {

        /**
         * Costruttore.
         */
        public CancellaRigheCommand() {
            super("deleteCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            Set<RigaDistintaCarico> righeDaCancellare = new HashSet<RigaDistintaCarico>();
            // Controllo le righe selezionate. Se è una groupable devo aggiungere tutte le righe
            int[] righeSelezionate = getTable().getTable().getSelectedRows();
            SortableTreeTableModel<?> groupableTableModel = (SortableTreeTableModel<?>) getTable().getTable()
                    .getModel();
            for (int i : righeSelezionate) {
                // Controllo se è un gruppo
                if (groupableTableModel.getValueAt(i, 0) instanceof DefaultGroupRow) {
                    righeDaCancellare = selezionaRigheDaCancellare(
                            (DefaultGroupRow) groupableTableModel.getValueAt(i, 0), righeDaCancellare);
                } else {
                    int indexRow = TableModelWrapperUtils.getActualRowAt(getTable().getTable().getModel(), i);
                    if (indexRow >= 0) {
                        RigaDistintaCarico riga = getTable().getRows().get(indexRow);
                        righeDaCancellare.add(riga);
                    }
                }
            }

            ordiniDocumentoBD.cancellaRigheDistintaCarico(righeDaCancellare);
            for (RigaDistintaCarico i : righeDaCancellare) {
                getTable().removeRowObject(i);
            }
        }

        /**
         *
         * @param row
         *            .
         * @param righe
         *            .
         * @return righe da cancellare.
         */
        private Set<RigaDistintaCarico> selezionaRigheDaCancellare(DefaultGroupRow row, Set<RigaDistintaCarico> righe) {
            for (Object riga : row.getChildren()) {
                if (riga instanceof DefaultGroupRow) {
                    righe = selezionaRigheDaCancellare((DefaultGroupRow) riga, righe);
                } else {
                    @SuppressWarnings("unchecked")
                    DefaultBeanTableModel<RigaDistintaCarico> tableModel = (DefaultBeanTableModel<RigaDistintaCarico>) TableModelWrapperUtils
                            .getActualTableModel(getTable().getTable().getModel());
                    RigaDistintaCarico rigaEvasione = tableModel.getObject(((IndexReferenceRow) riga).getRowIndex());
                    righe.add(rigaEvasione);
                }
            }
            return righe;
        }
    }

    public class EvadiCarrelloCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            componentDataEvasione.setDate(Calendar.getInstance().getTime());
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            if (componentDataEvasione.getDate() == null) {
                MessageDialog messageDialog = new MessageDialog("Dati non validi",
                        new DefaultMessage("Data evasione non valida", Severity.WARNING));
                messageDialog.showDialog();
                return false;
            }

            Calendar tmpCalendar = Calendar.getInstance();
            Calendar evasioneCalendar = Calendar.getInstance();
            evasioneCalendar.setTime(componentDataEvasione.getDate());
            evasioneCalendar.set(Calendar.HOUR_OF_DAY, tmpCalendar.get(Calendar.HOUR_OF_DAY));
            evasioneCalendar.set(Calendar.MINUTE, tmpCalendar.get(Calendar.MINUTE));
            evasioneCalendar.set(Calendar.SECOND, tmpCalendar.get(Calendar.SECOND));
            evasioneCalendar.set(Calendar.MILLISECOND, tmpCalendar.get(Calendar.MILLISECOND));

            actioncommand.addParameter(EvadiCarrelloCommand.PARAM_DATA_EVASIONE, evasioneCalendar.getTime());
            DistinteCaricoTableModel tableModel = (DistinteCaricoTableModel) TableModelWrapperUtils
                    .getActualTableModel(getTable().getTable().getModel(), DistinteCaricoTableModel.class);
            // La getVisibleObject della getTable non funziona se ho una raggruppata di mezzo
            // finchè non giro i tablemodel Groupable-->Sortable-->Filterable ciclo e prendo le
            // righe selezionate
            List<RigaDistintaCarico> righeDaEvadere = new ArrayList<RigaDistintaCarico>();
            for (RigaDistintaCarico rigaDistintaCarico : tableModel.getObjects()) {
                if (rigaDistintaCarico.getStato() == StatoRiga.SELEZIONATA) {
                    righeDaEvadere.add(rigaDistintaCarico);
                }
            }
            if (righeDaEvadere.isEmpty()) {
                String messageContent = RcpSupport.getMessage("nessunarigadaevadere");
                Message message = new DefaultMessage(messageContent, Severity.WARNING);
                String title = RcpSupport.getMessage("nessunarigadaevadere.title");
                new MessageDialog(title, message).showDialog();
                return false;
            }

            actioncommand.addParameter(EvadiCarrelloCommand.PARAM_RIGHE, righeDaEvadere);
            actioncommand.addParameter(EvadiCarrelloCommand.PARAM_DISTINTE_CARICO_PAGE, DistinteCaricoTablePage.this);
            showEvasione();
            return true;
        }
    }

    private class FiltraDistinteFatturazioneAgenteCommand extends JideToggleCommand {

        public static final String COMMAND_ID = "filtraDistinteFatturazioneAgenteCommand";

        /**
         * Costruttore.
         */
        public FiltraDistinteFatturazioneAgenteCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void onDeselection() {
            super.onDeselection();
            filtroFatturazioneAgenti.setVisualizzaAgentiFatturazione(false);
            FilterableTableModel filterableTableModel = (FilterableTableModel) TableModelWrapperUtils
                    .getActualTableModel(getTable().getTable().getModel(), FilterableTableModel.class);
            filterableTableModel.refresh();
        }

        @Override
        protected void onSelection() {
            super.onSelection();
            filtroFatturazioneAgenti.setVisualizzaAgentiFatturazione(true);
            FilterableTableModel filterableTableModel = (FilterableTableModel) TableModelWrapperUtils
                    .getActualTableModel(getTable().getTable().getModel(), FilterableTableModel.class);
            filterableTableModel.refresh();
        }
    }

    private class FiltraDistintePronteCommand extends JideToggleCommand {

        public static final String COMMAND_ID = "filtraDistintePronteCommand";
        private Filter<Object> filtro;

        /**
         * Costruttore.
         *
         * @param filtro
         *            da abilitare
         */
        public FiltraDistintePronteCommand(final Filter<Object> filtro) {
            super(COMMAND_ID);
            this.filtro = filtro;
            RcpSupport.configure(this);
        }

        @Override
        protected void onDeselection() {
            super.onDeselection();
            filtro.setEnabled(false);
            FilterableTableModel filterableTableModel = (FilterableTableModel) TableModelWrapperUtils
                    .getActualTableModel(getTable().getTable().getModel(), FilterableTableModel.class);
            filterableTableModel.refresh();
        }

        @Override
        protected void onSelection() {
            super.onSelection();
            filtro.setEnabled(true);
            FilterableTableModel filterableTableModel = (FilterableTableModel) TableModelWrapperUtils
                    .getActualTableModel(getTable().getTable().getModel(), FilterableTableModel.class);
            filterableTableModel.refresh();
        }
    }

    private final class FiltroFatturazioneAgenti extends AbstractTableFilter<Object> {
        private static final long serialVersionUID = 8902060457200249359L;

        private boolean visualizzaAgentiFatturazione = false;

        /**
         *
         * Costruttore.
         */
        private FiltroFatturazioneAgenti() {
            super("filtroFatturazioneAgenti");
            setEnabled(true);
        }

        @Override
        public boolean isValueFiltered(Object value) {
            if (value instanceof DefaultGroupRow) {
                return true;
            }

            if (value instanceof AgenteLite) {
                AgenteLite agenteLite = (AgenteLite) value;

                return visualizzaAgentiFatturazione != agenteLite.isFatturazioneAgente();
            }

            return false;
        }

        /**
         * @param visualizzaAgentiFatturazione
         *            The visualizzaAgentiFatturazione to set.
         */
        public void setVisualizzaAgentiFatturazione(boolean visualizzaAgentiFatturazione) {
            this.visualizzaAgentiFatturazione = visualizzaAgentiFatturazione;
        }
    }

    private final class FiltroRigheConEvasione extends AbstractTableFilter<Object> {
        private static final long serialVersionUID = 8902060457200249359L;

        /**
         *
         * Costruttore.
         */
        private FiltroRigheConEvasione() {
            super("FfltroRigheConEvasione");
            setEnabled(false);
        }

        @Override
        public boolean isValueFiltered(Object value) {
            if (value instanceof DefaultGroupRow) {
                return true;
            }
            return Double.compare((double) value, 0.0) == 0;
        }
    }

    private final class FiltroRigheSelezionate extends AbstractTableFilter<Object> {
        private static final long serialVersionUID = 8902060457200249359L;

        /**
         *
         * Costruttore.
         */
        private FiltroRigheSelezionate() {
            super("filtroRighePronte");
            setEnabled(false);
        }

        @Override
        public boolean isValueFiltered(Object value) {
            if (value instanceof DefaultGroupRow) {
                return true;
            }
            return ((StatoRiga) value) != StatoRiga.SELEZIONATA;
        }
    }

    public static final String PAGE_ID = "distinteCaricoTablePage";

    public static final String CARD_TABLE = "cardCarrello";

    public static final String CARD_PROGRESS_EVASIONE = "cardProgressEvasione";
    private IOrdiniDocumentoBD ordiniDocumentoBD;
    private IAnagraficaOrdiniBD anagraficaOrdiniBD;

    private FiltraDistintePronteCommand filtraDistintePronteCommand;

    private FiltraDistinteFatturazioneAgenteCommand filtraDistinteFatturazioneAgenteCommand;

    private Filter<Object> filtroRigheSelezionate;
    private FiltroFatturazioneAgenti filtroFatturazioneAgenti;

    private JPanel rootPanel;

    private EvasionePanel evasionePanel;

    private EvadiCarrelloCommand evadiCarrelloCommand;

    private JDateChooser componentDataEvasione;

    private AbstractCommand cancellaRigheCommand;

    private FiltroRigheConEvasione filtroRigheConEvasione;

    private DashBoardPanel dashBoardPanel;

    /**
     * Costruttore.
     */
    public DistinteCaricoTablePage() {
        super(PAGE_ID, new DistinteCaricoTableModel());
        getTable().setTableType(TableType.GROUP);
        new SelectRowListener((GroupTable) getTable().getTable());
        filtroRigheSelezionate = new FiltroRigheSelezionate();
        filtroRigheConEvasione = new FiltroRigheConEvasione();
        filtroFatturazioneAgenti = new FiltroFatturazioneAgenti();
        FilterableTableModel filterableTableModel = (FilterableTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel(), FilterableTableModel.class);
        filterableTableModel.addFilter(0, filtroRigheSelezionate);
        filterableTableModel.addFilter(5, filtroRigheConEvasione);
        filterableTableModel.addFilter(IFilterableTableModel.ALL_COLUMNS, filtroFatturazioneAgenti);
        filterableTableModel.setFiltersApplied(true);
        filterableTableModel.refresh();
    }

    @Override
    protected JComponent createControl() {
        rootPanel = getComponentFactory().createPanel(new CardLayout());
        rootPanel.add(super.createControl(), CARD_TABLE);
        evasionePanel = new EvasionePanel();
        rootPanel.add(evasionePanel, CARD_PROGRESS_EVASIONE);
        return rootPanel;
    }

    /**
     * Crea i componenti per la data evasione.
     *
     * @return componenti creati
     */
    private JComponent createDataEvasioneComponent() {
        JLabel labelData = getComponentFactory().createLabel("dataEvasione");
        labelData.setIcon(getIconSource().getIcon("java.util.Date"));

        IDateEditor textFieldDateEditor = new PanjeaTextFieldDateEditor("dd/MM/yy", "dd/MM/yy", '_');
        componentDataEvasione = ((PanjeaComponentFactory) getComponentFactory()).createDateChooser(textFieldDateEditor);
        componentDataEvasione.setDate(Calendar.getInstance().getTime());

        JPanel panel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(labelData);
        panel.add(componentDataEvasione);

        return panel;
    }

    @Override
    public JComponent createToolbar() {

        JPanel toolbarPanel = getComponentFactory().createPanel(new HorizontalLayout(5));
        // toolbarPanel.add(getFiltraDistinteFatturazioneAgenteCommand().createButton());
        toolbarPanel.add(getFiltraDistintePronteCommand().createButton());
        toolbarPanel.add(createDataEvasioneComponent());
        toolbarPanel.add(super.createToolbar());

        JPanel toolBarOuterPanel = new JPanel(new BorderLayout());
        toolBarOuterPanel.add(toolbarPanel, BorderLayout.SOUTH);

        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        dashBoardPanel = new DashBoardPanel();
        panel.add(dashBoardPanel, BorderLayout.CENTER);
        panel.add(toolBarOuterPanel, BorderLayout.EAST);
        return panel;
    }

    /**
     * @return Returns the anagraficaOrdiniBD.
     */
    public IAnagraficaOrdiniBD getAnagraficaOrdiniBD() {
        return anagraficaOrdiniBD;
    }

    /**
     *
     * @return command per cancellare le righe selezionate
     */
    private AbstractCommand getCancellaRigheCommand() {
        if (cancellaRigheCommand == null) {
            cancellaRigheCommand = new CancellaRigheCommand();
        }
        return cancellaRigheCommand;
    }

    @Override
    public AbstractCommand[] getCommands() {
        return new AbstractCommand[] { getEvadiCarrelloCommand(), getCancellaRigheCommand(), getRefreshCommand() };
    }

    /**
     *
     * @return command per l'evasione delle righe selezionate
     */
    private AbstractCommand getEvadiCarrelloCommand() {
        if (evadiCarrelloCommand == null) {
            evadiCarrelloCommand = new EvadiCarrelloCommand();
            evadiCarrelloCommand.addCommandInterceptor(new EvadiCarrelloCommandInterceptor());
        }
        return evadiCarrelloCommand;
    }

    /**
     * @return Returns the filtraDistinteFatturazioneAgenteCommand.
     */
    public FiltraDistinteFatturazioneAgenteCommand getFiltraDistinteFatturazioneAgenteCommand() {
        if (filtraDistinteFatturazioneAgenteCommand == null) {
            filtraDistinteFatturazioneAgenteCommand = new FiltraDistinteFatturazioneAgenteCommand();
        }

        return filtraDistinteFatturazioneAgenteCommand;
    }

    /**
     * @return command per filtrare le distinte pronte
     */
    public FiltraDistintePronteCommand getFiltraDistintePronteCommand() {
        if (filtraDistintePronteCommand == null) {
            if (anagraficaOrdiniBD.caricaOrdiniSettings().isSelezioneMissioniDaEvadereManuale()) {
                filtraDistintePronteCommand = new FiltraDistintePronteCommand(filtroRigheConEvasione);
            } else {
                filtraDistintePronteCommand = new FiltraDistintePronteCommand(filtroRigheSelezionate);
            }
        }
        return filtraDistintePronteCommand;
    }

    @Override
    public JComponent getHeaderControl() {
        return new JPanel();
    }

    @Override
    public Collection<RigaDistintaCarico> loadTableData() {
        dashBoardPanel.carica();
        return ordiniDocumentoBD.caricaRigheInMagazzino();
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public Collection<RigaDistintaCarico> refreshTableData() {
        return null;
    }

    /**
     * @param anagraficaOrdiniBD
     *            The anagraficaOrdiniBD to set.
     */
    public void setAnagraficaOrdiniBD(IAnagraficaOrdiniBD anagraficaOrdiniBD) {
        this.anagraficaOrdiniBD = anagraficaOrdiniBD;
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof DistintaCarico) {
            if (((DistintaCarico) object).getDataCreazione() == null) {
                getFiltraDistintePronteCommand().setSelected(true);
            }
        }
    }

    /**
     * @param ordiniDocumentoBD
     *            The ordiniDocumentoBD to set.
     */
    public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
        this.ordiniDocumentoBD = ordiniDocumentoBD;
    }

    /**
     * Visualizza il carrello di evasione.
     */
    public void showCarrello() {
        CardLayout cl = (CardLayout) (rootPanel.getLayout());
        cl.show(rootPanel, CARD_TABLE);
    }

    /**
     * Visualizza la prograssione dell'evasione.
     */
    public void showEvasione() {
        CardLayout cl = (CardLayout) (rootPanel.getLayout());
        cl.show(rootPanel, CARD_PROGRESS_EVASIONE);
        evasionePanel.reset();
    }

}
