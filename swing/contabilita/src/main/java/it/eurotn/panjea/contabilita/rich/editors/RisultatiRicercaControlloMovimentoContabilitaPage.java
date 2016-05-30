package it.eurotn.panjea.contabilita.rich.editors;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.core.io.ClassPathResource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.PopupMenuMouseListener;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.AutoFilterTableHeader;
import com.jidesoft.grid.RootExpandableRow;
import com.jidesoft.grid.TableColumnChooserPopupMenuCustomizer;
import com.jidesoft.grid.TableHeaderPopupMenuInstaller;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TreeTable;
import com.jidesoft.grid.TreeTableSearchable;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.commands.EliminaAreaContabileCommand;
import it.eurotn.panjea.contabilita.rich.editors.ricercamovimenti.ControlloMovimentiTableModel;
import it.eurotn.panjea.contabilita.rich.editors.ricercamovimenti.ControlloMovimentoRow;
import it.eurotn.panjea.contabilita.rich.editors.ricercamovimenti.ControlloMovimentoRow.ERowType;
import it.eurotn.panjea.contabilita.rich.editors.ricercamovimenti.TipoDocumentoCellRenderer;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.RigaContabileDTO;
import it.eurotn.panjea.rich.bd.DmsBD;
import it.eurotn.panjea.rich.bd.IDmsBD;
import it.eurotn.panjea.rich.editors.dms.allegati.AllegatiListTransferHandler;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.control.table.AutoResizePopupMenuCustomizer;
import it.eurotn.rich.control.table.command.CopyCommand;
import it.eurotn.rich.control.table.command.ExportPDFCommand;
import it.eurotn.rich.control.table.command.ExportXLSCommand;
import it.eurotn.rich.control.table.command.ExportXLSNoChooseCommand;
import it.eurotn.rich.control.table.command.OpenReportCommand;
import it.eurotn.rich.control.table.command.PrintCommand;
import it.eurotn.rich.control.table.command.SelectAllCommand;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.report.FooterBean;
import it.eurotn.rich.report.HeaderBean;
import it.eurotn.rich.report.JecLocalReport;

public class RisultatiRicercaControlloMovimentoContabilitaPage extends AbstractDialogPage
        implements IPageLifecycleAdvisor {

    private class EliminaAreeContabiliCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand command) {
            loadData();
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            List<Integer> idAreeContabili = new ArrayList<Integer>();

            int[] rowsIndex = TableModelWrapperUtils.getActualRowsAt(treeTable.getModel(), treeTable.getSelectedRows(),
                    false);
            for (Integer riga : rowsIndex) {
                ControlloMovimentoRow row = tableModel.getRowAt(riga);
                if (row.getRowType() == ERowType.AREA) {
                    idAreeContabili.add(row.getAreaContabileDTO().getId());
                }
            }

            ((EliminaAreaContabileCommand) command).setIdAreeContabili(idAreeContabili);
            return true;
        }
    }

    /**
     * Command che gestisce la funzione di expand/collapse sulla tree table.
     *
     * @author Leonardo
     */
    private class ExpandCommand extends ActionCommand {

        private static final String ESPANDI_COMMAND = ".expandCommand";
        private static final String EXPAND_STATE = ".expand";
        private static final String COLLAPSE_STATE = ".collapse";
        private boolean expandTree;
        private CommandFaceDescriptor expandDescriptor;
        private CommandFaceDescriptor collapseDescriptor;

        /**
         * Costruttore.
         */
        public ExpandCommand() {
            super("abstractTreeTableDialogPageEditor" + ESPANDI_COMMAND);
            CommandConfigurer configurer = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);

            setSecurityControllerId("abstractTreeTableDialogPageEditor" + ESPANDI_COMMAND);
            configurer.configure(this);
            expandTree = false;

            String toExpandString = getMessage("abstractTreeTableDialogPageEditor" + EXPAND_STATE + ".label");
            String toCollapseString = getMessage("abstractTreeTableDialogPageEditor" + COLLAPSE_STATE + ".label");
            Icon toExpandIcon = getIconSource().getIcon("abstractTreeTableDialogPageEditor" + EXPAND_STATE + ".icon");
            Icon toCollapseIcon = getIconSource()
                    .getIcon("abstractTreeTableDialogPageEditor" + COLLAPSE_STATE + ".icon");
            collapseDescriptor = new CommandFaceDescriptor(toExpandString, toExpandIcon, null);
            expandDescriptor = new CommandFaceDescriptor(toCollapseString, toCollapseIcon, null);
            setFaceDescriptor(collapseDescriptor);
        }

        @Override
        protected void doExecuteCommand() {
            if (!expandTree) {
                treeTable.expandAll();
                setFaceDescriptor(expandDescriptor);
            } else {
                treeTable.collapseAll();
                setFaceDescriptor(collapseDescriptor);
            }
            this.expandTree = !expandTree;
        }
    }

    /**
     * Costruttore.
     */
    public class OpenAreaContabileEditorCommand extends ActionCommand {

        /**
         * Costruttore.
         *
         */
        public OpenAreaContabileEditorCommand() {
            super(PAGE_ID + OPEN_EDITOR_COMMAND);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {

            int[] rowsIndex = TableModelWrapperUtils.getActualRowsAt(treeTable.getModel(), treeTable.getSelectedRows(),
                    false);

            if (rowsIndex != null && rowsIndex.length > 0) {

                Integer idArea = null;

                ControlloMovimentoRow row = tableModel.getRowAt(rowsIndex[0]);
                switch (row.getRowType()) {
                case AREA:
                    idArea = row.getAreaContabileDTO().getId();
                    break;
                default:
                    idArea = ((RigaContabileDTO) row.getAreaContabileDTO()).getAreaContabileDTO().getId();
                    break;
                }

                AreaContabileFullDTO area = contabilitaBD.caricaAreaContabileFullDTO(idArea);
                LifecycleApplicationEvent event = new OpenEditorEvent(area);
                Application.instance().getApplicationContext().publishEvent(event);
            }
        }
    }

    private class StampaCommand extends ActionCommand {

        /**
         * Costruttore.
         *
         */
        public StampaCommand() {
            super(STAMPA_COMMAND);
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            this.setSecurityControllerId(PAGE_ID + "." + STAMPA_COMMAND);
            c.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            JecLocalReport jecJasperReport = new JecLocalReport();
            jecJasperReport.setReportName("Controllo Movimenti Contabili");
            jecJasperReport.setXmlReportResource(new ClassPathResource(
                    "/it/eurotn/panjea/contabilita/rich/reports/resources/StampaControlloMovimentiContabili.jasper"));
            jecJasperReport.setMessages("it.eurotn.panjea.messages.cont.reports.messages");
            jecJasperReport.setDataReport(righeContabiliDTO);
            HeaderBean headerBean = new HeaderBean();
            headerBean.setCodiceAzienda(aziendaCorrente.getDenominazione());
            headerBean.setUtenteCorrente(PanjeaSwingUtil.getUtenteCorrente().getUserName());

            Image facsimile = getImageSource().getImage("imageAzienda.report");
            jecJasperReport.getReportParameters().put("REPORT_IMAGE_AZIENDA", facsimile);
            jecJasperReport.getReportParameters().put("htmlParameters",
                    parametriRicercaMovimentiContabili.getHtmlParameters());

            Image docSquadratoImg = getImageSource()
                    .getImage("risultatiRicercaControlloMovimentoContabilitaPage.report.documentoSquadrato.image");
            jecJasperReport.getReportParameters().put("imgDocSquadrato", docSquadratoImg);
            // aggiungo il parametro filtri report per stampare i filtri utilizzati
            FooterBean footerBean = new FooterBean();
            jecJasperReport.setDataHeader(headerBean);
            jecJasperReport.setDataFooter(footerBean);
            jecJasperReport.execute();
        }
    }

    private final class TablePopupMouseListener extends PopupMenuMouseListener {
        @Override
        protected JPopupMenu getPopupMenu() {
            return createPopupContextMenu();
        }
    }

    private final class TreeTableSearchableExtension extends TreeTableSearchable {

        /**
         * Costruttore.
         *
         * @param arg0
         *            table
         */
        private TreeTableSearchableExtension(final JTable arg0) {
            super(arg0);
        }

        @Override
        protected String convertElementToString(Object item) {
            if (item instanceof ControlloMovimentoRow) {
                return "";
            }
            String value = ObjectConverterManager.toString(item);
            if (value == null) {
                value = super.convertElementToString(item);
            }
            return value;
        }
    }

    public static final String PAGE_ID = "risultatiRicercaControlloMovimentoContabilitaPage";

    private static final String OPEN_EDITOR_COMMAND = ".openEditorCommand";
    private static final String STAMPA_COMMAND = "stampaCommand";
    private IContabilitaBD contabilitaBD = null;

    private AziendaCorrente aziendaCorrente = null;

    private ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili;

    private List<RigaContabileDTO> righeContabiliDTO;
    private TreeTable treeTable;
    private ControlloMovimentiTableModel tableModel;
    private TablePopupMouseListener tablePopupMouseListener;
    private CopyCommand copyCommand;
    private PrintCommand printCommand;
    private SelectAllCommand selectAllCommand;

    private OpenReportCommand openReportCommand;
    private ExportXLSCommand exportXLSCommand;

    private ExportXLSNoChooseCommand exportXLSNoChooseCommand;

    private ExportPDFCommand exportPDFCommand;
    private StampaCommand stampaCommand;
    private ExpandCommand expandCommand;

    private EliminaAreaContabileCommand eliminaAreaContabileCommand = null;

    private EliminaAreeContabiliCommandInterceptor eliminaAreeContabiliCommandInterceptor = null;

    private TreeTableSearchable treeTableSearchable;

    private JECCommandGroup commandGroup;

    private OpenAreaContabileEditorCommand openEditorCommand;

    private MouseAdapter propertyMouseAdapter;

    private AllegatiListTransferHandler allegatiTransferHandler;

    /**
     * Costruttore.
     */
    protected RisultatiRicercaControlloMovimentoContabilitaPage() {
        super(PAGE_ID);
        allegatiTransferHandler = new AllegatiListTransferHandler((IDmsBD) RcpSupport.getBean(DmsBD.BEAN_ID), null);
    }

    @Override
    protected JComponent createControl() {

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

        createTreeTable();
        rootPanel.add(new JScrollPane(treeTable), BorderLayout.CENTER);

        this.copyCommand = new CopyCommand(treeTable);
        this.selectAllCommand = new SelectAllCommand(treeTable);
        this.printCommand = new PrintCommand(treeTable);
        this.exportXLSCommand = new ExportXLSCommand(treeTable);
        this.exportXLSNoChooseCommand = new ExportXLSNoChooseCommand(exportXLSCommand);
        this.exportPDFCommand = new ExportPDFCommand(treeTable);
        JECCommandGroup group = new JECCommandGroup();
        group.add(exportPDFCommand);
        group.add(exportXLSCommand);
        group.add(printCommand);
        this.openReportCommand = new OpenReportCommand(treeTable);

        rootPanel.add(createToolbar(), BorderLayout.SOUTH);

        return rootPanel;
    }

    /**
     * Crea il menù di default per la tabella.
     *
     * @return popup
     */
    private JPopupMenu createPopupContextMenu() {
        CommandGroup popupMenu = new CommandGroup();
        // aggiungo i pulsanti di gestione della tabella
        popupMenu.add(copyCommand);
        popupMenu.add(selectAllCommand);
        popupMenu.add(exportXLSNoChooseCommand);
        popupMenu.add(openReportCommand);

        return popupMenu.createPopupMenu();
    }

    /**
     * Crea la toolbar da posizionare sotto la tabella.
     *
     * @return toolbar creata
     */
    private JComponent createToolbar() {
        commandGroup = new JECCommandGroup();
        commandGroup.add(getExpandCommand());
        commandGroup.add(getEliminaAreaCommand());
        commandGroup.add(getStampaCommand());

        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.add(commandGroup.createToolBar(), BorderLayout.EAST);

        return panel;
    }

    /**
     * Crea la {@link TreeTable} settando tutte le proprietà.
     *
     */
    private void createTreeTable() {
        tableModel = new ControlloMovimentiTableModel(new ArrayList<ControlloMovimentoRow>());
        treeTable = new TreeTable(tableModel);
        AutoFilterTableHeader header = new AutoFilterTableHeader(treeTable);
        header.setShowFilterIcon(false);
        header.setAutoFilterEnabled(true);
        header.setUseNativeHeaderRenderer(true);
        header.setAllowMultipleValues(true);
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        treeTable.setTableHeader(header);
        treeTable.getColumnModel().getColumn(6).setCellRenderer(new TipoDocumentoCellRenderer());
        treeTable.setOptimized(true);
        treeTable.setSortable(true);
        treeTable.setRowHeight(20);
        treeTable.setShowTreeLines(false);
        treeTable.setName(getId() + "TableWidget");
        treeTable.setTransferHandler(allegatiTransferHandler);

        tablePopupMouseListener = new TablePopupMouseListener();
        treeTable.addMouseListener(tablePopupMouseListener);

        TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(treeTable);
        installer.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
        installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());

        treeTableSearchable = new TreeTableSearchableExtension(treeTable);
        treeTableSearchable.setSearchColumnIndices(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 });

        treeTable.addMouseListener(getPropertyMouseAdapter());
        treeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent event) {
                int[] rowsSel = treeTable.getSelectedRows();
                getEliminaAreaCommand().setEnabled(rowsSel != null && rowsSel.length > 0);
            }
        });
    }

    @Override
    public void dispose() {
        righeContabiliDTO = null;

        commandGroup.reset();
        commandGroup = null;

        eliminaAreaContabileCommand.removeCommandInterceptor(eliminaAreeContabiliCommandInterceptor);
        eliminaAreeContabiliCommandInterceptor = null;
        eliminaAreaContabileCommand = null;

        treeTableSearchable.uninstallListeners();
        treeTableSearchable = null;

        treeTable.removeMouseListener(tablePopupMouseListener);
        treeTable.putClientProperty("Searchable", null);

        treeTable.removeMouseListener(getPropertyMouseAdapter());

        treeTable = null;

        stampaCommand = null;
        expandCommand = null;
        openEditorCommand = null;
        allegatiTransferHandler.dispose();
    }

    /**
     * Restituisce una lista di aree contabili calcolando il totale dare e avere in base alle righe.
     * Ogni area contabile inoltre contiene le sue righe.
     *
     * @param righe
     *            righe contabili
     * @return aree contabili
     */
    private Collection<AreaContabileDTO> getAreeContabiliDTO(List<RigaContabileDTO> righe) {

        Map<Integer, AreaContabileDTO> areeContabili = new HashMap<Integer, AreaContabileDTO>();

        for (RigaContabileDTO rigaContabileDTO : righe) {

            switch (rigaContabileDTO.getTipoRigaContabileDTO()) {
            case ROW:
                AreaContabileDTO area = areeContabili.get(rigaContabileDTO.getAreaContabileDTO().getId());
                if (area == null) {
                    area = rigaContabileDTO.getAreaContabileDTO();
                    area.setRigheContabili(new ArrayList<RigaContabileDTO>());
                    area.setImportoAvere(BigDecimal.ZERO);
                    area.setImportoDare(BigDecimal.ZERO);
                }

                area.getRigheContabili().add(rigaContabileDTO);
                area.setImportoAvere(area.getImportoAvere().add(rigaContabileDTO.getImportoAvere()));
                area.setImportoDare(area.getImportoDare().add(rigaContabileDTO.getImportoDare()));
                areeContabili.put(area.getId(), area);
                break;
            default:
                // qui ci sono tutte le aree senza righe
                area = rigaContabileDTO.getAreaContabileDTO();
                area.setRigheContabili(new ArrayList<RigaContabileDTO>());
                area.setImportoAvere(BigDecimal.ZERO);
                area.setImportoDare(BigDecimal.ZERO);
                areeContabili.put(area.getId(), area);
                break;
            }
        }

        return areeContabili.values();
    }

    /**
     * Restituisce le righe gestite dal modello della tree partendo dalle aree contabili passate
     * come parametro.
     *
     * @param areeContabili
     *            aree
     * @return righe generate
     */
    private List<ControlloMovimentoRow> getControlloMovimentoRows(Collection<AreaContabileDTO> areeContabili) {

        List<ControlloMovimentoRow> rows = new ArrayList<ControlloMovimentoRow>();

        for (AreaContabileDTO areaContabileDTO : areeContabili) {
            rows.add(new ControlloMovimentoRow(areaContabileDTO));
        }

        return rows;
    }

    /**
     * @return the EliminaAreeContabiliCommand to get
     */
    private EliminaAreaContabileCommand getEliminaAreaCommand() {
        if (eliminaAreaContabileCommand == null) {
            eliminaAreaContabileCommand = new EliminaAreaContabileCommand(PAGE_ID);
            eliminaAreaContabileCommand.setContabilitaBD(contabilitaBD);
            eliminaAreaContabileCommand.setEnabled(false);
            eliminaAreaContabileCommand.addCommandInterceptor(getEliminaAreeContabiliCommandInterceptor());
        }
        return eliminaAreaContabileCommand;
    }

    /**
     * @return the EliminaAreeContabiliCommandInterceptor to get
     */
    private EliminaAreeContabiliCommandInterceptor getEliminaAreeContabiliCommandInterceptor() {
        if (eliminaAreeContabiliCommandInterceptor == null) {
            eliminaAreeContabiliCommandInterceptor = new EliminaAreeContabiliCommandInterceptor();
        }
        return eliminaAreeContabiliCommandInterceptor;
    }

    /**
     * Command per chiamare la expand/collapse sulla treeTable.
     *
     * @return AbstractCommand
     */
    private AbstractCommand getExpandCommand() {
        if (expandCommand == null) {
            expandCommand = new ExpandCommand();
        }
        return expandCommand;
    }

    /**
     *
     * @return the OpenAreaContabileEditorCommand
     */
    private OpenAreaContabileEditorCommand getOpenAreaContabileEditor() {
        if (openEditorCommand == null) {
            openEditorCommand = new OpenAreaContabileEditorCommand();
        }
        return openEditorCommand;
    }

    /**
     * Listener per l'apertura dell'area contabile selezionata.
     *
     * @return listener
     */
    private MouseAdapter getPropertyMouseAdapter() {
        if (propertyMouseAdapter == null) {
            propertyMouseAdapter = new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent event) {

                    if (event.getClickCount() == 2) {
                        getOpenAreaContabileEditor().execute();
                    }
                }

            };
        }

        return propertyMouseAdapter;
    }

    /**
     * Command che chiama la funzione di stampa delle righe contabili visualizzate.
     *
     * @return il command di stampa
     */
    private AbstractCommand getStampaCommand() {
        if (stampaCommand == null) {
            stampaCommand = new StampaCommand();
            stampaCommand.setEnabled(false);
        }
        return stampaCommand;
    }

    @Override
    public void loadData() {
        if (this.parametriRicercaMovimentiContabili != null
                && this.parametriRicercaMovimentiContabili.isEffettuaRicerca()) {

            righeContabiliDTO = contabilitaBD.ricercaControlloAreeContabili(parametriRicercaMovimentiContabili);
        } else {
            righeContabiliDTO = new ArrayList<RigaContabileDTO>();
        }

        Collection<AreaContabileDTO> areeContabili = getAreeContabiliDTO(righeContabiliDTO);
        List<ControlloMovimentoRow> rows = getControlloMovimentoRows(areeContabili);

        // pulisco la tabella e inserisco le righe caricate
        RootExpandableRow rootERow = (RootExpandableRow) tableModel.getRoot();
        int total = rootERow.getChildrenCount();
        for (int i = total - 1; i >= 0; i--) {
            rootERow.removeChild(rootERow.getChildAt(i));
            tableModel.refresh();
        }
        tableModel.addRows(rows);

        getStampaCommand().setEnabled(righeContabiliDTO.size() != 0);
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {
        loadData();
    }

    @Override
    public void restoreState(Settings arg0) {
    }

    @Override
    public void saveState(Settings arg0) {
    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param contabilitaBD
     *            The contabilitaBD to set.
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof ParametriRicercaMovimentiContabili) {
            this.parametriRicercaMovimentiContabili = (ParametriRicercaMovimentiContabili) object;
        } else {
            this.parametriRicercaMovimentiContabili = new ParametriRicercaMovimentiContabili();
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

}
