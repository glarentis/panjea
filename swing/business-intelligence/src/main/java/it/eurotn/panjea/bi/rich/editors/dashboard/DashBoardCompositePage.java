package it.eurotn.panjea.bi.rich.editors.dashboard;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.docking.PopupMenuCustomizer;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotConstants;
import com.jidesoft.pivot.PivotField;

import it.eurotn.panjea.bi.domain.dashboard.DashBoard;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard.LayoutFiltri;
import it.eurotn.panjea.bi.domain.dashboard.DashBoardAnalisi;
import it.eurotn.panjea.bi.rich.bd.BusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiPersistenceUtil;
import it.eurotn.panjea.bi.rich.editors.analisi.model.AnalisiBiDataSource;
import it.eurotn.panjea.bi.rich.editors.analisi.model.AnalisiBiPivotDataModel;
import it.eurotn.panjea.bi.rich.editors.dashboard.filtri.DashBoardFiltriFrame;
import it.eurotn.panjea.bi.rich.editors.dashboard.filtri.DashboardFiltriPage;
import it.eurotn.panjea.magazzino.exception.AnalisiNonPresenteException;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.renderer.MeseCellRenderer;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;

public class DashBoardCompositePage extends DockingCompositeDialogPage {

    /**
     * @author fattazzo
     *
     */
    private final class AnalisiBiTransferHandler extends TransferHandler {
        private static final long serialVersionUID = 5016421300795247986L;

        @Override
        public boolean canImport(TransferSupport support) {

            // voglio solo le stringhe che arrivano da un drop ( non quindi ad esempio dal paste
            // della clipboard)
            if (!support.isDrop() && !support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return false;
            }

            String objectString;
            try {
                objectString = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
            } catch (final Exception e) {
                logger.error("-->errore durante il caricamento dell'oggetto del transferable", e);
                return false;
            }

            return objectString != null && "AnalisiBi".equals(objectString.split("#")[0]);
        }

        @Override
        public boolean importData(TransferSupport support) {

            if (!canImport(support)) {
                return false;
            }

            try {
                final String objectString = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);

                final String[] objectSplit = objectString.split("#");
                final String categoria = objectSplit[1];
                final String nome = objectSplit[2];

                aggiungiAnalisi(categoria, nome);
            } catch (final Exception e) {
                logger.error("-->errore durante il caricamento dell'oggetto del transferable", e);
            }

            return true;
        }
    }

    private class AssociazioneFiltriPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            final Object source = evt.getSource();
            @SuppressWarnings("unchecked")
            final Map<String, Object[]> associazioneFiltri = (Map<String, Object[]>) evt.getNewValue();

            for (final PivotField field : filterPivotDataModel.getFields()) {
                if (associazioneFiltri.containsKey(field.getName())) {
                    final Object[] values = associazioneFiltri.get(field.getName());
                    if (values == null) {
                        field.setFilter(null);
                    }
                    field.setSelectedPossibleValues(values);
                }
            }

            filterPivotDataModel.updateFields();
            if (!dashboardFiltriPage.equals(source)) {
                dashboardFiltriPage.setFormObject(filterPivotDataModel);
                dashboardFiltriPage.refreshData();
            }
            for (final Object dashboardPageObject : getPages()) {
                if (dashboardPageObject instanceof DashBoardPage) {
                    final DashBoardPage dashBoardPage = (DashBoardPage) dashboardPageObject;
                    if (!dashBoardPage.equals(source) && dashBoardPage.isAnalisiCaricata()) {
                        dashBoardPage.caricaDati(filterPivotDataModel);
                    }
                }
            }
        }
    }

    private class ToggleEditDashBoardCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public ToggleEditDashBoardCommand() {
            super("toggleEditDashBoardCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            getDockingManager().setRearrangable(!getDockingManager().isRearrangable());
            lockLayout(!getDockingManager().isRearrangable());
        }

    }

    private static Logger logger = Logger.getLogger(DashBoardCompositePage.class);

    private final AssociazioneFiltriPropertyChangeListener associazioneFiltriPropertyChangeListener;

    protected IBusinessIntelligenceBD businessIntelligenceBD;

    protected DashBoard dashBoard;

    private final DashboardFiltriPage dashboardFiltriPage;

    private IPivotDataModel filterPivotDataModel;

    private boolean readOnly;

    private ToolBarDashBoard toolBarDashBoard;

    /**
     * Costruttore di default con id dashBoard.
     */
    public DashBoardCompositePage() {
        this("dashBoard");
    }

    /**
     * Costruttore.
     *
     * @param nomeDashBoard
     *            nome della dashBoard.
     */
    public DashBoardCompositePage(final String nomeDashBoard) {
        super(nomeDashBoard);
        businessIntelligenceBD = RcpSupport.getBean(BusinessIntelligenceBD.BEAN_ID);
        dashboardFiltriPage = new DashboardFiltriPage();
        associazioneFiltriPropertyChangeListener = new AssociazioneFiltriPropertyChangeListener();
    }

    @Override
    protected void addPages() {
        super.addPages();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
    }

    /**
     * @param categoriaAnalisi
     *            categoria analisi
     * @param nomeAnalisi
     *            aggiunge un'analisi alla dashboard.
     */
    private void aggiungiAnalisi(String categoriaAnalisi, String nomeAnalisi) {
        if (DashboardFiltriPage.PAGE_ID.equals(nomeAnalisi)) {
            return;
        }

        final DashBoardAnalisi analisi = dashBoard.aggiungiAnalisi(categoriaAnalisi, nomeAnalisi);
        if (analisi != null) {
            DashBoardPage dashBoardPage;
            try {
                dashBoardPage = new DashBoardPage(analisi, businessIntelligenceBD);
                addPageAndFrame(dashBoardPage);
                dashBoardPage.addPropertyChangeListener(DashBoardPage.ASSOCIAZIONE_FILTER_PROPERTY,
                        associazioneFiltriPropertyChangeListener);
                dashBoardPage.setFilterPivotDataModel(filterPivotDataModel);
                dashBoardPage.preSetFormObject(currentObject);
                dashBoardPage.setFormObject(currentObject);
                dashBoardPage.postSetFormObject(currentObject);
                dashBoardPage.caricaAnalisi();
                salva();

                // se viene inserita una analisi mi metto in modifica in automatico
                lockLayout(false);
            } catch (final AnalisiNonPresenteException e) {
                // Se l'analisi non è presente rimuovo l'analisi da dashBoardEntity
                dashBoard.removeAnalisi(analisi);
            }
        }
    }

    /**
     * Aggiorna i filtri nelle pagine.
     */
    private void applyFilter() {
        dashboardFiltriPage.refreshData();
        for (final Object dashboardPageObject : getPages()) {
            if (dashboardPageObject instanceof DashBoardPage) {
                final DashBoardPage dashBoardPage = (DashBoardPage) dashboardPageObject;
                dashBoardPage.setFilterPivotDataModel(filterPivotDataModel);
            }
        }
    }

    @Override
    protected void configureFrame(DockableFrame frame) {
        super.configureFrame(frame);
        ((DashBoardFrame) frame).configureFrame();
    }

    @Override
    protected JComponent createControl() {
        final JComponent pagine = super.createControl();
        pagine.setTransferHandler(new AnalisiBiTransferHandler());

        final JPanel controlliEditor = new JPanel();
        controlliEditor.setLayout(new BorderLayout());

        toolBarDashBoard = new ToolBarDashBoard(this, businessIntelligenceBD);

        controlliEditor.add(toolBarDashBoard, BorderLayout.NORTH);
        controlliEditor.add(pagine, BorderLayout.CENTER);

        holderPanel.getDockingManager().setXmlFormat(true);
        holderPanel.getDockingManager().setXmlEncoding("UTF-8");
        holderPanel.getDockingManager().setPopupMenuCustomizer(new PopupMenuCustomizer() {

            @Override
            public void customizePopupMenu(JPopupMenu paramJPopupMenu, DockingManager paramDockingManager,
                    DockableFrame paramDockableFrame, boolean paramBoolean) {
                final ToggleEditDashBoardCommand toggleEditDashBoardCommand = new ToggleEditDashBoardCommand();
                paramJPopupMenu.add(toggleEditDashBoardCommand.createMenuItem());
            }
        });

        toolBarDashBoard.setVisible(false);
        // JScrollPane pane = new JScrollPane(controlliEditor);
        // DragScrollListener dl = new DragScrollListener(holderPanel);
        // pane.addMouseListener(dl);
        // pane.addMouseMotionListener(dl);
        return controlliEditor;
    }

    /**
     * crea il frame docked
     *
     * @param title
     *            titolo del frame
     * @param icon
     *            icona del frame
     * @param page
     *            pagina da associare
     * @return frame
     */
    @Override
    public DockableFrame createDockableFrame(String title, Icon icon, DialogPage page) {
        if (page instanceof DashBoardPage) {
            return new DashBoardAnalisiFrame(this);
        } else if (page instanceof DashboardFiltriPage) {
            return new DashBoardFiltriFrame();
        }
        throw new UnsupportedOperationException("Tipo pagina non gestita");
    }

    @Override
    public void dispose() {
        for (final DockableFrame frame : getFrames()) {
            frame.dispose();
        }

        super.dispose();
    }

    /**
     * @return Returns the dashBoard.
     */
    public DashBoard getDashBoard() {
        return dashBoard;
    }

    @Override
    public String getDisplayName() {
        if (dashBoard == null) {
            return super.getDisplayName();
        }
        return dashBoard.getNome();
    }

    /**
     *
     * @return fields presenti nella dashboard. Somma di tutti i field nelle analisi
     */
    public Map<String, PivotField> getFieldsPresenti() {
        final Map<String, PivotField> fields = new HashMap<>();
        for (final Object dashboardPageObject : getPages()) {
            if (dashboardPageObject instanceof DashBoardPage) {
                final DashBoardPage dashBoardPage = (DashBoardPage) dashboardPageObject;
                for (final PivotField field : dashBoardPage.getPivotDataModel().getFields()) {
                    fields.put(field.getName(), field);
                }
            }
        }
        return fields;
    }

    /**
     * @return Returns the filterpivotDataModel.
     */
    public IPivotDataModel getFilterPivotDataModel() {
        if (filterPivotDataModel == null) {
            filterPivotDataModel = new AnalisiBiPivotDataModel(new AnalisiBiDataSource());
            final NumberWithDecimalConverterContext qtaContext = new NumberWithDecimalConverterContext(4);
            for (final PivotField field : filterPivotDataModel.getFields()) {
                field.setCustomFilterAllowed(false);
                field.setPreferSelectedPossibleValues(true);
                field.setAreaType(PivotConstants.AREA_FILTER);
                field.setSortable(false);
                if (field.getType() == BigDecimal.class) {
                    field.setConverterContext(new ConverterContext("BIGDECIMAL", 2));
                } else if (field.getType() == double.class) {
                    field.setConverterContext(qtaContext);
                } else if (field.getType() == Double.class) {
                    field.setConverterContext(qtaContext);
                }
                if ("mese".equals(field.getName())) {
                    field.setEditorContext(MeseCellRenderer.MESE_CONTEXT);
                }
            }
        }
        return filterPivotDataModel;
    }

    private void initPageFiltri() {
        if (getDockingManager().getFrame(DashboardFiltriPage.PAGE_ID) == null) {
            addPageAndFrame(dashboardFiltriPage);
            dashboardFiltriPage.setFiltriPrivati(dashBoard.getFiltriPrivati());
            dashboardFiltriPage.setFormObject(filterPivotDataModel);
            dashboardFiltriPage.addPropertyChangeListener(DashBoardPage.ASSOCIAZIONE_FILTER_PROPERTY,
                    associazioneFiltriPropertyChangeListener);

            final LayoutFiltri layoutFiltri = dashBoard == null ? LayoutFiltri.FILL : dashBoard.getLayoutFiltri();
            dashboardFiltriPage.applyLayout(layoutFiltri);

            getDockingManager().getFrame(DashboardFiltriPage.PAGE_ID).setVisible(false);
        }
    }

    /**
     *
     * @return true se la pagina dei filtri è visibile
     */
    public boolean isFitriVisible() {
        initPageFiltri();
        return getDockingManager().getFrame(DashboardFiltriPage.PAGE_ID).isVisible();
    }

    /**
     *
     * @return true se la pagina è bloccata.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void lockLayout(boolean lock) {
        toolBarDashBoard.setVisible(!lock);
        super.lockLayout(lock);
    }

    @Override
    protected void objectChange(Object domainObject, DialogPage pageSource) {
        super.objectChange(domainObject, pageSource);
        final Object o = domainObject;
        dashBoard = (DashBoard) domainObject;
        // if (isControlCreated()) {
        // new Thread() {
        // @Override
        // public void run() {
        // openDashBoard(o);
        // };
        // }.start();
        // }
        setTitle(dashBoard.getNome());
        final Timer timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                openDashBoard(o);
            }
        });
        timer.setRepeats(false);
        // timer.start();
        openDashBoard(o);
    }

    /**
     *
     * @param domainObject
     *            init per l'analisi.
     */
    protected void openDashBoard(Object domainObject) {
        dashBoard = (DashBoard) domainObject;
        // Rimuovo i filtri dal docked e riaggiungo i frame basandomi sulle pagine presenti (la
        // pagina FilterPage è
        // sempre presente)

        // Rimuovo il listener dai frame altrimenti salva la dashboard
        for (final DockableFrame frame : getFrames()) {
            frame.dispose();
        }

        getDockingManager().removeAllFrames();

        // Aggiungo le pagine di analisi.
        // Se le analisi non ci sono più rimuovo le pagine dalla dashboard e la salvo.

        boolean salvaDashboard = false;

        AnalisiBiPersistenceUtil.applicaFiltri(getFilterPivotDataModel(), dashBoard.getFiltri());

        for (final DashBoardAnalisi analisi : dashBoard.getAnalisi().values()) {
            if (DashboardFiltriPage.PAGE_ID.equals(analisi.getNomeAnalisi())) {
                continue;
            }
            final DashBoardPage page;
            try {
                page = new DashBoardPage(analisi, businessIntelligenceBD);
                addPageAndFrame(page);
                // chiamo subito la start per visualizzare il pannello di waiting. Con la
                // invokelater partirebbe tardi.
                page.setFilterPivotDataModel(filterPivotDataModel);
                page.addPropertyChangeListener(DashBoardPage.ASSOCIAZIONE_FILTER_PROPERTY,
                        associazioneFiltriPropertyChangeListener);
                final DashBoardAnalisi analisiDaCaricare = analisi;
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            page.caricaAnalisi();
                        } catch (final AnalisiNonPresenteException e) {
                            dashBoard.removeAnalisi(analisiDaCaricare);
                        }
                    }
                });
                page.startAnalisi();
            } catch (final AnalisiNonPresenteException e) {
                dashBoard.removeAnalisi(analisi);
                salvaDashboard = true;
            }
        }

        if (salvaDashboard) {
            salva();
        }

        // applico il layout associato alla dashboard
        if (dashBoard.getDockingLayout() != null) {
            final ByteArrayInputStream dockingInputStream = new ByteArrayInputStream(dashBoard.getDockingLayout());
            getDockingManager().loadLayoutFrom(dockingInputStream);
        }

        initPageFiltri();
        if (dashBoard.isFrameFiltriVisible()) {
            getDockingManager().setFrameAvailable(DashboardFiltriPage.PAGE_ID);
            dashboardFiltriPage.loadData();

        }
        setReadOnly(true);
        // Rilancia all'editor una propertyChange di displayName che aggiorna il displayName
        // dell'editor
    }

    @Override
    public void restoreState(Settings settings) {
    }

    /**
     * Ricarica i dati delle singole analisi contenute nelle dashboard.
     */
    public void ricaricaDatiDashBoard() {
        for (final Object pagina : getPages()) {
            if (pagina instanceof DashBoardPage) {
                ((DashBoardPage) pagina).caricaDati();
            }
        }
    }

    /***
     * Salva la dashBoard.
     */
    public void salva() {
        final ByteArrayOutputStream dockingOutputStream = new ByteArrayOutputStream();
        try {
            getDockingManager().saveLayoutTo(dockingOutputStream);
            dashBoard.setDockingLayout(dockingOutputStream.toByteArray());
            dashBoard.setWidth(getDockingManager().getMainContainer().getWidth());
            dashBoard.setHeight(getDockingManager().getMainContainer().getHeight());

            dashBoard.setFrameFiltriVisible(isFitriVisible());
            LayoutFiltri layoutFiltri = LayoutFiltri.FILL;
            final Collection<String> frames = getDockingManager().getAllFrames();
            for (final String frameKey : frames) {
                final DockableFrame frame = getDockingManager().getFrame(frameKey);
                if (frame instanceof DashBoardFiltriFrame && frame.isValid()) {
                    layoutFiltri = ((DashBoardFiltriFrame) frame).getLayoutFiltri();
                    dashBoard.setLayoutFiltri(layoutFiltri);
                } else if (frame instanceof DashBoardAnalisiFrame && frame.isValid()) {
                    // aggiorna le dashboard analisi
                    final DashBoardAnalisi dashBoardAnalisi = ((DashBoardAnalisiFrame) frame).getAnalisi();
                    dashBoard.getAnalisi().put(frameKey, dashBoardAnalisi);
                }
            }
        } catch (final IOException e) {
            logger.error("-->errore nel salvare il layout", e);
        }
        dashBoard.setFiltri(AnalisiBiPersistenceUtil.creaFiltri(getFilterPivotDataModel()));
        dashBoard = businessIntelligenceBD.salvaDashBoard(dashBoard);
        firePropertyChange(JecCompositeDialogPage.CURRENT_OBJECT_CHANGED_PROPERTY, null, dashBoard);
    }

    @Override
    public void saveState(Settings settings) {
        salva();
    }

    /**
     * Setta un pivotDataModel con i field da impostare come filtri. Per visualizzare i dati con i filtri applicati
     * chiamare la RicaricaAnalisiCommand.
     *
     * @param pivotDataModel
     *            pivotDataModel da impostare per utilizzare i fltri sui field
     */
    public void setFilterPivotDataModel(IPivotDataModel pivotDataModel) {
        this.filterPivotDataModel = pivotDataModel;
        dashboardFiltriPage.setFormObject(filterPivotDataModel);
        applyFilter();
    }

    /**
     *
     * @param readOnly
     *            true rende la pagina in sola lettura.
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;

        // for (DockableFrame frame : getFrames()) {
        // ((DashBoardFrame) frame).setReadonly(readOnly);
        // }

        // getDockingManager().setShowDividerGripper(!readOnly);
        // getDockingManager().setShowTitleBar(!readOnly);
        // getDockingManager().setShowGripper(!readOnly);
        // getDockingManager().setShowTitleOnOutline(true);

        // ((JPanel) getDockingManager().getContentContainer()).setOpaque(true);
        // ((JPanel) getDockingManager().getContentContainer()).setBackground(Color.yellow);
        // ((JPanel) getDockingManager().getContentContainer()).setForeground(Color.yellow);
        // ((JPanel)
        // getDockingManager().getContentContainer()).setBorder(BorderFactory.createEmptyBorder());

        // getDockingManager().getMainContainer().setOpaque(true);
        // getDockingManager().getMainContainer().setBackground(Color.white);
        // getDockingManager().getMainContainer().setForeground(Color.black);
        // getDockingManager().getMainContainer().setBorder(BorderFactory.createEmptyBorder());

        // getDockingManager().getMainContainer().setBorder(BorderFactory.createEmptyBorder());
        // getDockingManager().getMainContainer().setBackground(Color.white);
        // getDockingManager().getMainContainer().setOpaque(true);
        // ((JPanel) getDockingManager().getContentContainer()).setBackground(Color.yellow);
    }

    /**
     * Visualizza la pagina dei filtri.
     *
     */
    public void visualizzaFiltri() {
        if (isFitriVisible()) {
            getDockingManager().setFrameUnavailable(DashboardFiltriPage.PAGE_ID);
            // DashBoardAnalisi filterAnalisi = new DashBoardAnalisi();
            // filterAnalisi.setFrameKey(DashboardFiltriPage.KEY_FRAME_ID);
            // dashBoard.removeAnalisi(filterAnalisi);
        } else {
            getDockingManager().setFrameAvailable(DashboardFiltriPage.PAGE_ID);
        }
    }
}
