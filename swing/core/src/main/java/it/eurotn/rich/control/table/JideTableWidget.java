package it.eurotn.rich.control.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FieldFaceSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.Memento;
import org.springframework.richclient.util.Assert;
import org.springframework.richclient.util.PopupMenuMouseListener;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.richclient.widget.AbstractWidget;
import org.springframework.richclient.widget.Widget;
import org.springframework.rules.constraint.Constraint;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.grid.CellStyleTable;
import com.jidesoft.grid.FilterableTableModel;
import com.jidesoft.grid.HierarchicalTableComponentFactory;
import com.jidesoft.grid.JideCellEditorListener;
import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.RolloverTableUtils;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableHeaderPopupMenuInstaller;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pivot.AggregateTable;
import com.jidesoft.pivot.SummaryCalculatorFactory;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.TableSearchable;

import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.control.table.command.AbstractExportTableCommand;
import it.eurotn.rich.control.table.command.CopyCommand;
import it.eurotn.rich.control.table.command.ExportPDFCommand;
import it.eurotn.rich.control.table.command.ExportPDFNoChooseCommand;
import it.eurotn.rich.control.table.command.ExportXLSCommand;
import it.eurotn.rich.control.table.command.ExportXLSNoChooseCommand;
import it.eurotn.rich.control.table.command.OpenReportCommand;
import it.eurotn.rich.control.table.command.PrintCommand;
import it.eurotn.rich.control.table.command.SelectAllCommand;
import it.eurotn.rich.control.table.layout.DefaultTableLayoutView;
import it.eurotn.rich.control.table.navigationloader.NavigationLoaderUtils;
import it.eurotn.rich.control.table.options.AbstractTableSelectionColumnsOptionPane;
import it.eurotn.rich.control.table.options.AggregateTableSelectionColumnsOptionPane;
import it.eurotn.rich.control.table.options.TableSelectionColumnsOptionPane;
import it.eurotn.rich.control.table.style.DefaultCellStyleProvider;
import it.eurotn.rich.control.table.style.FocusCellStyle;
import it.eurotn.rich.control.table.style.GrupingRowStripeTableStyleProvider;
import it.eurotn.rich.report.editor.export.ExportToMailCommand;

public class JideTableWidget<T> extends AbstractWidget implements Memento {

    private class MetaKeyDownAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() != KeyEvent.VK_META || e.getKeyCode() != KeyEvent.VK_CONTROL
                    || e.getKeyCode() != KeyEvent.VK_ALT) {
                if (e.isControlDown() || e.isMetaDown() || e.isAltDown()) {

                    JComponent ancestor = (JComponent) SwingUtilities.getAncestorOfClass(JPanel.class,
                            JideTableWidget.this.getComponent());
                    if (ancestor != null) {
                        ancestor.dispatchEvent(e);
                    }
                    e.consume();
                    return;
                }
            }
            super.keyPressed(e);
        }

    }

    private final class NavigateLastCommand extends ActionCommand {
        /**
         * Costruttore.
         *
         * @param commandId
         *            id command
         */
        private NavigateLastCommand(final String commandId) {
            super(commandId);
        }

        @Override
        protected void doExecuteCommand() {
            FilterableTableModel filterableTableModel = (FilterableTableModel) TableModelWrapperUtils
                    .getActualTableModel(tableControl.getModel(), FilterableTableModel.class);
            int lastIndex = tableControl.getRowCount() - 1;
            if (filterableTableModel != null) {
                lastIndex = filterableTableModel.getRowCount() - 1;
            }
            tableControl.getSelectionModel().setSelectionInterval(lastIndex, lastIndex);
            scrollToSelectedRow();
        }
    }

    private final class NavigateNextCommand extends ActionCommand {
        /**
         * Costruttore.
         *
         * @param commandId
         *            id command
         */
        private NavigateNextCommand(final String commandId) {
            super(commandId);
        }

        @Override
        protected void doExecuteCommand() {
            int newIndex = tableControl.getSelectionModel().getAnchorSelectionIndex() + 1;
            FilterableTableModel filterableTableModel = (FilterableTableModel) TableModelWrapperUtils
                    .getActualTableModel(tableControl.getModel(), FilterableTableModel.class);
            int rowCount = tableControl.getRowCount() - 1;
            if (filterableTableModel != null) {
                rowCount = filterableTableModel.getRowCount() - 1;
            }
            newIndex = (newIndex >= rowCount) ? rowCount : newIndex;
            tableControl.getSelectionModel().setSelectionInterval(newIndex, newIndex);
            scrollToSelectedRow();
        }
    }

    private final class NavigatePreviousCommand extends ActionCommand {
        /**
         * Costruttore.
         *
         * @param commandId
         *            id command
         */
        private NavigatePreviousCommand(final String commandId) {
            super(commandId);
        }

        @Override
        protected void doExecuteCommand() {
            int newIndex = tableControl.getSelectionModel().getAnchorSelectionIndex() - 1;
            newIndex = (newIndex < 0) ? 0 : newIndex;
            tableControl.getSelectionModel().setSelectionInterval(newIndex, newIndex);
            scrollToSelectedRow();
        }
    }

    private class NavigationCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {

            command.addParameter(NavigateFirstCommand.PARAM_TABLE_WIDGET, JideTableWidget.this);

            // Se ho una tabella editabile cambio riga solamente se la riga è
            // valida

            return changeSelectionConstraint == null || changeSelectionConstraint.test(getSelectedObject());
        }

    }

    private class PropertyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER && propertyCommandExecutor != null
                    && getTable().getCellEditor() == null) {
                if (propertyCommandExecutor instanceof ActionCommand) {
                    ((ActionCommand) propertyCommandExecutor).addParameter(SELECTED_VALUE_PARAM, getSelectedObject());
                }
                propertyCommandExecutor.execute();
                e.consume();
            }
        }
    }

    private class PropertyMouseAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 2 && propertyCommandExecutor != null) {
                if (propertyCommandExecutor instanceof ActionCommand) {
                    ((ActionCommand) propertyCommandExecutor).addParameter(SELECTED_VALUE_PARAM, getSelectedObject());
                }
                propertyCommandExecutor.execute();
            }
        }
    }

    private class SelectionEventListener implements ListSelectionListener {
        private class SelectionActionListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (getSelectedObject() != null) {
                    PanjeaLifecycleApplicationEvent lifecycleApplicationEvent = new PanjeaLifecycleApplicationEvent(
                            PanjeaLifecycleApplicationEvent.SELECTED, getSelectedObject());
                    Application.instance().getApplicationContext().publishEvent(lifecycleApplicationEvent);
                    T selectObject = getSelectedObject();
                    if (selectionMonitor != null) {
                        selectionMonitor.setValue(selectObject);
                    }
                }
            }
        }

        private Timer selectionTimer;

        private final SelectionActionListener selectionActionListener = new SelectionActionListener();

        /**
         * Costruttore.
         */
        public SelectionEventListener() {
            super();
        }

        /**
         * @return the selectionTimer
         */
        public Timer getSelectionTimer() {
            if (selectionTimer == null) {
                selectionTimer = new Timer(0, selectionActionListener);
                selectionTimer.setRepeats(false);
            }

            return selectionTimer;
        }

        @Override
        public void valueChanged(ListSelectionEvent event) {
            logger.debug("--> Enter valueChanged");
            if (event.getValueIsAdjusting()) {
                return;
            }
            // se il tempo di attesa è uguale a 0 lancio subito l'evento alla
            // selezione altrimenti faccio ripartire
            // il timer.
            if (JideTableWidget.this.getDelayForSelection() == 0) {
                selectionActionListener.actionPerformed(new ActionEvent("", 0, null));
            } else {
                getSelectionTimer().stop();
                getSelectionTimer().setInitialDelay(JideTableWidget.this.getDelayForSelection());
                getSelectionTimer().start();
            }

            logger.debug("--> Exit valueChanged");
        }

    }

    private class SelectionNavigationListener implements ListSelectionListener {

        /**
         * @param event
         *            ListSelectionEvent
         */
        @Override
        public void valueChanged(ListSelectionEvent event) {
            logger.debug("--> Enter valueChanged");
            if (!event.getValueIsAdjusting()) {
                int selectedIndex = tableControl.getSelectedRow();

                FilterableTableModel filterableTableModel = (FilterableTableModel) TableModelWrapperUtils
                        .getActualTableModel(tableControl.getModel(), FilterableTableModel.class);
                int lastIndex = tableControl.getRowCount() - 1;
                if (filterableTableModel != null) {
                    lastIndex = filterableTableModel.getRowCount() - 1;
                }
                boolean emptyList = (lastIndex == -1);
                boolean onFirst = (selectedIndex == 0);
                boolean onLast = (selectedIndex == lastIndex);

                if (navigationCommands[NAVIGATE_FIRST] != null) {
                    navigationCommands[NAVIGATE_FIRST].setEnabled(!emptyList && !onFirst);
                }
                if (navigationCommands[NAVIGATE_PREVIOUS] != null) {
                    navigationCommands[NAVIGATE_PREVIOUS].setEnabled(!emptyList && !onFirst);
                }
                if (navigationCommands[NAVIGATE_NEXT] != null) {
                    navigationCommands[NAVIGATE_NEXT].setEnabled(!emptyList && !onLast);
                }
                if (navigationCommands[NAVIGATE_LAST] != null) {
                    navigationCommands[NAVIGATE_LAST].setEnabled(!emptyList && !onLast);
                }
            }
            logger.debug("--> Exit valueChanged");
        }
    }

    private final class TablePopupMouseListener extends PopupMenuMouseListener {
        @Override
        protected JPopupMenu getPopupMenu() {
            return createPopupContextMenu();
        }
    }

    public enum TableType {
        AGGREGATE, GROUP, HIERARCHICAL
    }

    private class TableValueMonitor extends Observable {
        private Object value;

        public void setValue(Object newValue) {
            if (this.value == newValue) {
                return;
            }
            setChanged();
            this.value = newValue;
            notifyObservers(this.value);
        }
    }

    protected static FocusCellStyle focusCellStyle;

    static {
        focusCellStyle = new FocusCellStyle();
    }

    public static final String DEFAULT_AGGREGATE_COLUMNS_PROPERTY = "defaultAggregateColumns";
    public static final String TABLE_LAYOUT_VIEW = "tableLayoutView";
    private static Logger logger = Logger.getLogger(JideTableWidget.class);

    public static final int NAVIGATE_FIRST = 0;

    public static final int NAVIGATE_PREVIOUS = 1;
    public static final int NAVIGATE_NEXT = 2;
    public static final int NAVIGATE_LAST = 3;
    /**
     * Id for the selection commands.
     */
    public static final String SELECT_ALL_ID = "select.all";

    /**
     * Command Id's for the navigation commands for the table {@link #getNavigationCommands()}.
     */
    private static final String NAVIGATE_LASTROW_CMDID = "lastrow";

    private static final String NAVIGATE_NEXTROW_CMDID = "nextrow";

    private static final String NAVIGATE_PREVIOUSROW_CMDID = "previousrow";

    private static final String NAVIGATE_FIRSTROW_CMDID = "firstrow";

    public static final String SELECTED_VALUE_PARAM = "selectedValue";

    private DefaultBeanTableModel<T> tableModel;

    protected JideTable tableControl;
    private SummaryCalculatorFactory summaryCalculatorFactory;

    private TableValueMonitor selectionMonitor = new TableValueMonitor();
    private ActionCommand[] navigationCommands;
    private String widgetId;

    private JECCommandGroup navigationCommandGroup;
    private ActionCommand selectCommand;
    private ActionCommandExecutor propertyCommandExecutor;

    private PropertyMouseAdapter propertyMouseAdapter;
    private PropertyKeyAdapter propertyKeyAdapter;
    private JideEmptyOverlayTableScrollPane overlayTable; // Overlay per i
    // messaggi che
    // verranno inseriti
    // sopra la
    private PrintCommand printCommand;
    private SelectAllCommand selectAllCommand;
    private CopyCommand copyCommand;
    private ExportXLSCommand exportXLSCommand;
    private ExportXLSNoChooseCommand exportXLSNoChooseCommand;

    private ExportPDFNoChooseCommand exportPDFNoChooseCommand;

    private ExportPDFCommand exportPDFCommand;

    private ExportToMailCommand exportToMailCommand;

    private OpenReportCommand openReportCommand;

    private List<AbstractCommand> externalPopupContextMenu;

    private String[] defaultAggregateColumns;
    private boolean adjustingMode = false;

    private JPanel rootPanel;

    private DefaultTableLayoutView layoutView;

    private int delayForSelection = 0;

    private FieldFaceSource fieldFaceSource;
    private Constraint changeSelectionConstraint;

    private TableType tableType;
    private SelectionNavigationListener selectionNavigationListener;

    private SelectionEventListener selectionEventListener;

    private TablePopupMouseListener tablePopupMouseListener;

    private TableSearchable tableScrollPaneSearchable;

    private NavigationCommandInterceptor navigationCommandInterceptor;

    private HierarchicalTableComponentFactory hierarchicalTableComponentFactory;

    private AbstractTableSelectionColumnsOptionPane tableSelectionColumnsOptionPane;

    private MetaKeyDownAdapter metaKeyDownAdapter;

    /**
     * Costruisce un tableWidget con il table model indicato.
     *
     * @param id
     *            id per il widget utilizzato per configurarlo.
     * @param tableModel
     *            table model da utilizzare nella tabella.
     */
    public JideTableWidget(final String id, final DefaultBeanTableModel<T> tableModel) {
        this.tableModel = tableModel;
        this.widgetId = id;
        tableType = TableType.AGGREGATE;
    }

    /**
     * Costruisce un tableWidget con le proprietà del bean indicato in colonne.
     *
     * @param id
     *            utilizzato come prefisso per risolvere i nomi per I18N delle proprietà
     * @param beanProperties
     *            proprieta da visualizzare come colonne.
     * @param classe
     *            classe contenuta nella lista che gestirà la tabella
     */
    public JideTableWidget(final String id, final String[] beanProperties, final Class<T> classe) {
        this(id, new DefaultBeanTableModel<T>(id, beanProperties, classe));
    }

    /**
     * Inizializza le proprietà standard della tabella.
     *
     * @param table
     *            tabella di riferimento
     */
    public static void customizeTable(JideTable table) {
        table.setColumnAutoResizable(false);
        table.setRowResizable(false);
        table.setRowHeight(22);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        if (table instanceof SortableTable) {
            ((SortableTable) table).setSortable(true);
            ((SortableTable) table).setShowSortOrderNumber(true);
            SortableTableModel sortableTableModel = (SortableTableModel) TableModelWrapperUtils
                    .getActualTableModel(table.getModel(), SortableTableModel.class);
            sortableTableModel.setAlwaysUseComparators(true);
        }

        if (table instanceof CellStyleTable) {
            if (table instanceof AggregateTable) {
                ((AggregateTable) table).getAggregateTableModel().setCellStyleProvider(new DefaultCellStyleProvider());
            } else {
                ((CellStyleTable) table).setTableStyleProvider(new GrupingRowStripeTableStyleProvider());
            }
            ((CellStyleTable) table).setFocusCellStyle(focusCellStyle);
        }

        if (table instanceof ITable<?>) {
            TableHeaderPopupMenuInstaller tablePopupInstaller = new TableHeaderPopupMenuInstaller(table);
            tablePopupInstaller
                    .addTableHeaderPopupMenuCustomizer(new com.jidesoft.grid.AutoResizePopupMenuCustomizer());
            tablePopupInstaller.addTableHeaderPopupMenuCustomizer(new JideTableLayoutPopup());
            ((ITable<?>) table).installMenu(tablePopupInstaller);
            table.setTableHeader(((ITable<?>) table).getTableHeader(table));
        }
        RolloverTableUtils.install(table);
        // table.setAutoSelectTextWhenStartsEditing(true);
        // table.setAutoStartCellEditing(true);
    }

    /**
     * Aggiunge un command da visualizzare nel popup della tabella.
     *
     * @param abstractCommand
     *            comando
     */
    public void addExternalPopupContextMenu(AbstractCommand abstractCommand) {
        if (externalPopupContextMenu == null) {
            externalPopupContextMenu = new ArrayList<AbstractCommand>();
        }

        externalPopupContextMenu.add(abstractCommand);
    }

    /**
     *
     * @param newObject
     *            .
     * @param observer
     *            .
     */
    public void addRowObject(T newObject, Observer observer) {
        logger.debug("--> Enter addRowObject");
        if (tableControl == null) {
            init();
        }
        String preference = TableUtils.getTablePreferenceByName(tableControl);
        tableModel.addObject(newObject);
        TableUtils.setTablePreferenceByName(tableControl, preference);
        selectRowObject(newObject, observer);
        // tableModel.fireTableStructureChanged();
        // tableModel.fireTableDataChanged();
        logger.debug("--> Exit addRowObject");
    }

    /**
     *
     * @param rows
     *            .
     * @param observer
     *            .
     */
    @SuppressWarnings("unchecked")
    public void addRows(List<T> rows, Observer observer) {
        logger.debug("--> Enter addRows");
        if (rows.isEmpty()) {
            return;
        }
        if (tableControl == null) {
            init();
        }
        String preference = TableUtils.getTablePreferenceByName(tableControl);
        tableModel.addObjects(rows);
        TableUtils.setTablePreferenceByName(tableControl, preference);
        selectRowObject((T) rows.toArray()[0], observer);
        // tableModel.fireTableStructureChanged();
        tableModel.fireTableDataChanged();
        logger.debug("--> Exit addRows");
    }

    /**
     *
     * @param observer
     *            .
     */
    public void addSelectionObserver(Observer observer) {
        logger.debug("--> Enter addSelectionObserver");
        this.selectionMonitor.addObserver(observer);
        logger.debug("--> Exit addSelectionObserver");
    }

    /**
     *
     * @param listener
     *            .
     */
    public void addTableModelListener(TableModelListener listener) {
        logger.debug("--> Enter addTableModelListener");
        tableModel.addTableModelListener(listener);
        logger.debug("--> Exit addTableModelListener");
    }

    @Override
    public boolean canClose() {
        return true;
    }

    /**
     * Cre il pannello delle opzioni del chart.
     *
     * @return pannello creato
     */
    protected CollapsiblePane createOptionsPanel() {

        CollapsiblePane optionsPanel = new CollapsiblePane("Opzioni");
        optionsPanel.setStyle(CollapsiblePane.DROPDOWN_STYLE);
        optionsPanel.setEmphasized(true);
        optionsPanel.setLayout(new BorderLayout(0, 5));
        optionsPanel.collapse(true);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        optionsPanel.setBackground(new Color(204, 204, 214));
        optionsPanel.setSlidingDirection(SwingConstants.WEST);

        JPanel panes = new JPanel(new FormLayout("f:pref:grow", "max(30dlu;pref),fill:pref:grow"));
        CellConstraints cc = new CellConstraints();
        panes.add(getTableLayoutView().getControl(), cc.xy(1, 1));
        switch (tableType) {
        case AGGREGATE:
            tableSelectionColumnsOptionPane = new AggregateTableSelectionColumnsOptionPane(tableControl);
            break;
        default:
            tableSelectionColumnsOptionPane = new TableSelectionColumnsOptionPane(tableControl);
            break;
        }
        panes.add(tableSelectionColumnsOptionPane.getControl(), cc.xy(1, 2));

        optionsPanel.add(panes, BorderLayout.CENTER);

        return optionsPanel;
    }

    /**
     * Crea il menù di default per la tabella.
     *
     * @return popup
     */
    protected JPopupMenu createPopupContextMenu() {
        CommandGroup popupMenu = new CommandGroup();
        // aggiungo i pulsanti di gestione della tabella
        popupMenu.add(copyCommand);
        popupMenu.add(selectAllCommand);
        popupMenu.add(exportXLSNoChooseCommand);
        popupMenu.add(exportPDFNoChooseCommand);
        popupMenu.add(exportToMailCommand);
        popupMenu.add(printCommand);
        popupMenu.add(openReportCommand);
        if (externalPopupContextMenu != null) {
            for (AbstractCommand command : externalPopupContextMenu) {
                popupMenu.add(command);
            }
        }
        return popupMenu.createPopupMenu();
    }

    /**
     * Esegue il dispose.
     */
    public void dispose() {

        if (overlayTable != null) {
            overlayTable.dispose();
            overlayTable = null;
        }

        getTableLayoutView().dispose();
        propertyCommandExecutor = null;
        selectionMonitor = null;

        if (navigationCommands != null) {
            for (ActionCommand navigationCommand : this.navigationCommands) {
                navigationCommand.removeCommandInterceptor(navigationCommandInterceptor);
                navigationCommandGroup.remove(navigationCommand);
                navigationCommand = null;
            }
        }
        navigationCommands = new ActionCommand[] {};
        navigationCommandGroup = null;

        if (tableControl != null) {
            if (tableType == TableType.AGGREGATE) {
                ((AggregateTable) tableControl).setTableHeader(null);
            }

            tableControl.removeKeyListener(getPropertyKeyAdapter());
            tableControl.removeMouseListener(getPropertyMouseAdapter());

            ((ITable<?>) tableControl).setChangeSelectionConstraint(null);
            changeSelectionConstraint = null;

            tableControl.getSelectionModel().removeListSelectionListener(selectionNavigationListener);
            selectionNavigationListener = null;

            tableControl.getSelectionModel().removeListSelectionListener(selectionEventListener);
            selectionEventListener = null;

            tableControl.removeMouseListener(propertyMouseAdapter);
            tableControl.removeMouseListener(tablePopupMouseListener);

            tableControl.removeKeyListener(propertyKeyAdapter);
            tableControl.removeKeyListener(metaKeyDownAdapter);

            SearchableUtils.uninstallSearchable(tableScrollPaneSearchable);
            tableScrollPaneSearchable = null;

            tableControl.putClientProperty(TABLE_LAYOUT_VIEW, null);
            tableControl.putClientProperty(DEFAULT_AGGREGATE_COLUMNS_PROPERTY, null);

            NavigationLoaderUtils.uninstall(tableControl);

            // TODO scrivere a jide
            tableControl.putClientProperty("Searchable", null);

            tableControl = null;
        }
    }

    /**
     * @return obj.
     */
    public Class<T> getClassObj() {
        return this.tableModel.getClassObj();
    }

    @Override
    public List<? extends AbstractCommand> getCommands() {
        return null;
    }

    @Override
    public JComponent getComponent() {
        if (tableControl == null) {
            init();
        }
        return rootPanel;
    }

    /**
     *
     * @return commandGroup di default per esportazione
     */
    protected JECCommandGroup getDefaultExportCommandGroup() {
        JECCommandGroup group = new JECCommandGroup();
        group.add(getExportPDFCommand());
        group.add(getExportXLSCommand());
        group.add(getPrintCommand());
        group.add(new ExportToMailCommand(
                new AbstractExportTableCommand[] { getExportPDFCommand(), getExportXLSCommand() }));
        return group;
    }

    /**
     * @return the delayForSelection
     */
    public int getDelayForSelection() {
        return delayForSelection;
    }

    /**
     * @return Returns the exportPDFCommand.
     */
    public ExportPDFCommand getExportPDFCommand() {
        return exportPDFCommand;
    }

    /**
     * @return Returns the exportCommand.
     */
    public ExportXLSCommand getExportXLSCommand() {
        return exportXLSCommand;
    }

    /**
     * @return the FieldFaceSource
     */
    protected FieldFaceSource getFieldFaceSource() {
        if (fieldFaceSource == null) {
            fieldFaceSource = (FieldFaceSource) ApplicationServicesLocator.services().getService(FieldFaceSource.class);
        }
        return fieldFaceSource;
    }

    /**
     * @return id.
     */
    public String getId() {
        return this.widgetId;
    }

    /**
     * @return MetaKeyDownAdapter
     */
    private MetaKeyDownAdapter getMetaKeyDownAdapter() {
        if (metaKeyDownAdapter == null) {
            metaKeyDownAdapter = new MetaKeyDownAdapter();
        }
        return metaKeyDownAdapter;
    }

    /**
     * @return commands.
     */
    public AbstractCommand[] getNavigationCommands() {
        return navigationCommands;
    }

    /**
     * @return the overlayTable
     */
    public JideEmptyOverlayTableScrollPane getOverlayTable() {
        return overlayTable;
    }

    /**
     * @return Returns the printCommand.
     */
    public PrintCommand getPrintCommand() {
        return printCommand;
    }

    /**
     *
     * @return executor per l'azione di edit sul record selezionato
     */
    public ActionCommandExecutor getPropertyCommandExecutor() {
        return propertyCommandExecutor;
    }

    /**
     * @return PropertyKeyAdapter
     */
    private PropertyKeyAdapter getPropertyKeyAdapter() {
        if (propertyKeyAdapter == null) {
            propertyKeyAdapter = new PropertyKeyAdapter();
        }
        return propertyKeyAdapter;
    }

    /**
     * @return PropertyMouseAdapter
     */
    private PropertyMouseAdapter getPropertyMouseAdapter() {
        if (propertyMouseAdapter == null) {
            propertyMouseAdapter = new PropertyMouseAdapter();
        }
        return propertyMouseAdapter;
    }

    /**
     * Returns the current list of objects behind the table. It does not take into account local filtering and/or
     * sorting.
     *
     * @return List the list with objects
     * @see #getVisibleRows()
     */
    public List<T> getRows() {
        return tableModel.getObjects();
    }

    /**
     * @return tag.
     */
    public ActionCommand getSelectCommand() {
        return selectCommand;
    }

    /**
     * @return tag.
     */
    @SuppressWarnings("unchecked")
    public T getSelectedObject() {
        if (tableControl == null) {
            return null;
        }

        return ((ITable<T>) tableControl).getSelectedObject();
    }

    /**
     * @return List of objects that are currently selected
     */
    @SuppressWarnings("unchecked")
    public List<T> getSelectedObjects() {
        List<T> selectedObject = new ArrayList<T>();

        if (tableControl == null) {
            // devo ancora creare i controlli, quindi non ho sicuramente righe
            // selezionate
            return selectedObject;
        }

        selectedObject = ((ITable<T>) tableControl).getSelectedObjects();
        return selectedObject;
    }

    /**
     * @return Returns the summaryCalculator.
     */
    public SummaryCalculatorFactory getSummaryCalculatorFactory() {
        return summaryCalculatorFactory;
    }

    /**
     * Returns the table, getComponent() does not need to return only table (a scrollpane can be returned too containing
     * the table), as this method will enable you to get to the table.
     *
     * @return The table
     * @see Widget#getComponent()
     */
    public JTable getTable() {
        if (tableControl == null) {
            init();
        }
        return tableControl;
    }

    /**
     * @return DefaultTableLayoutView
     */
    public DefaultTableLayoutView getTableLayoutView() {

        if (layoutView == null) {
            layoutView = new DefaultTableLayoutView(this);
        }

        return layoutView;
    }

    /**
     * @return Returns the tableScrollPaneSearchable.
     */
    public TableSearchable getTableScrollPaneSearchable() {
        return tableScrollPaneSearchable;
    }

    /**
     * @return the tableSelectionColumnsOptionPane
     */
    public AbstractTableSelectionColumnsOptionPane getTableSelectionColumnsOptionPane() {
        return tableSelectionColumnsOptionPane;
    }

    /**
     * Returns the current list of objects visible in the table. It takes into account local filtering and/or sorting.
     *
     * @return List the visible rows
     * @see #getRows()
     */
    public List<T> getVisibleObjects() {
        List<T> visibleObjects = new ArrayList<T>();
        if (tableControl == null) {
            // devo ancora creare i controlli, quindi non ho sicuramente righe
            // selezionate
            return visibleObjects;
        }

        FilterableTableModel filterableTableModel = (FilterableTableModel) TableModelWrapperUtils
                .getActualTableModel(tableControl.getModel(), FilterableTableModel.class);
        if (filterableTableModel != null && filterableTableModel.hasFilter()) {
            for (int index : filterableTableModel.getIndexes()) {
                // int actualIndex =
                // TableModelWrapperUtils.getActualRowAt(filterableTableModel,
                // index);
                // if (actualIndex != -1) {
                // visibleObjects.add(tableModel.getObject(actualIndex));
                // }
                visibleObjects.add(tableModel.getObject(index));
            }
        } else {
            visibleObjects.addAll(getRows());
        }
        return visibleObjects;
    }

    /**
     * @return true if there is an active selection
     */
    public boolean hasSelection() {
        return tableControl.getSelectedRowCount() > 0;
    }

    /**
     * Inizializza il widget.
     */
    protected void init() {
        if (summaryCalculatorFactory == null) {
            setSummaryCalculatorFactory(new JecSummaryCalculator());
        }

        if (overlayTable == null) {
            setOverlayTable(new JideEmptyOverlayTableScrollPane());
        }

        switch (tableType) {
        case AGGREGATE:
            tableControl = new JecAggregateTable<T>(tableModel);
            ((AggregateTable) tableControl).getAggregateTableModel().getPivotDataModel()
                    .setSummaryCalculatorFactory(getSummaryCalculatorFactory());
            ((AggregateTable) tableControl).getAggregateTableModel().getPivotDataModel()
                    .setSummaryCalculator(getSummaryCalculatorFactory().create());
            break;
        case GROUP:
            tableControl = new JecGroupTable<T>(tableModel);
            break;
        case HIERARCHICAL:
            tableControl = new JecHierarchicalTable<T>(tableModel);
            Assert.notNull(hierarchicalTableComponentFactory, "HierarchicalTableComponentFactory non impostato");
            ((JecHierarchicalTable<T>) tableControl).setComponentFactory(hierarchicalTableComponentFactory);
            break;
        default:
            throw new UnsupportedOperationException();
        }

        tableControl.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        if (tableModel instanceof DefaultBeanEditableTableModel) {
            tableControl.getInputMap(JTable.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
                    "cancella");
            tableControl.getActionMap().put("cancella", new AbstractAction() {
                private static final long serialVersionUID = -6415705716217138086L;

                @Override
                public void actionPerformed(ActionEvent event) {
                    if (!((DefaultBeanEditableTableModel<T>) tableModel).isInsertObject(getSelectedObject())) {
                        removeRowObject(getSelectedObject());
                    }
                }
            });

            tableControl.addKeyListener(getMetaKeyDownAdapter());
        }

        tableControl.setName(getId());

        tableControl.addMouseListener(getPropertyMouseAdapter());
        ((ITable<T>) tableControl).setChangeSelectionConstraint(changeSelectionConstraint);
        tableControl.addKeyListener(getPropertyKeyAdapter());
        tableControl.putClientProperty(TABLE_LAYOUT_VIEW, getTableLayoutView());

        selectionNavigationListener = new SelectionNavigationListener();
        tableControl.getSelectionModel().addListSelectionListener(selectionNavigationListener);

        selectionEventListener = new SelectionEventListener();
        tableControl.getSelectionModel().addListSelectionListener(selectionEventListener);

        tableControl.setNonContiguousCellSelection(false);
        tableControl.setRowSelectionAllowed(true);
        tableControl.setColumnSelectionAllowed(false);
        tableControl.setBorder(BorderFactory.createEmptyBorder());

        customizeTable(tableControl);
        installSearchable();
        CollapsiblePane optionPanel = null;
        if (getOverlayTable().isShowOptionPanel()) {
            optionPanel = createOptionsPanel();
        }
        getOverlayTable().setTable(tableControl, optionPanel, getTableLayoutView(), tableSelectionColumnsOptionPane);
        getOverlayTable().setBorder(BorderFactory.createEmptyBorder());
        initializeNavigationCommands();
        initializeSelectCommands();

        if (defaultAggregateColumns != null) {
            // tableControl.getAggregateTableModel().setAggregatedColumns(defaultAggregateColumns);
            // tableControl.getAggregateTableModel().aggregate();
            tableControl.putClientProperty(DEFAULT_AGGREGATE_COLUMNS_PROPERTY, defaultAggregateColumns);
            ((ITable<T>) tableControl).setAggregatedColumns(defaultAggregateColumns);
        }

        this.printCommand = new PrintCommand(getTable());
        this.selectAllCommand = new SelectAllCommand(getTable());
        this.copyCommand = new CopyCommand(getTable());
        this.exportXLSCommand = new ExportXLSCommand(getTable());
        this.exportXLSNoChooseCommand = new ExportXLSNoChooseCommand(exportXLSCommand);
        this.exportPDFCommand = new ExportPDFCommand(getTable());
        this.exportPDFNoChooseCommand = new ExportPDFNoChooseCommand(exportPDFCommand);
        this.openReportCommand = new OpenReportCommand(getTable());

        if (rootPanel == null) {
            rootPanel = getComponentFactory().createPanel(new BorderLayout());
            rootPanel.add(overlayTable, BorderLayout.CENTER);
        }

        tableControl.setSurrendersFocusOnKeystroke(false);
        // tableControl.setAutoStartCellEditing(true);
        // tableControl.setAutoSelectTextWhenStartsEditing(false);

        // installo il popup menu
        // Add the context menu listener
        tablePopupMouseListener = new TablePopupMouseListener();
        getTable().addMouseListener(tablePopupMouseListener);
        ((JideTable) getTable()).addCellEditorListener(new JideCellEditorListener() {

            @Override
            public void editingCanceled(ChangeEvent event) {
                ((CellStyleTable) getTable()).setFocusCellStyle(focusCellStyle);
            }

            @Override
            public void editingStarted(ChangeEvent paramChangeEvent) {
            }

            @Override
            public boolean editingStarting(ChangeEvent paramChangeEvent) {
                ((CellStyleTable) getTable()).setFocusCellStyle(null);
                return true;
            }

            @Override
            public void editingStopped(ChangeEvent event) {
                ((CellStyleTable) getTable()).setFocusCellStyle(focusCellStyle);
            }

            @Override
            public boolean editingStopping(ChangeEvent paramChangeEvent) {
                return true;
            }
        });
        NavigationLoaderUtils.install(tableControl);
    }

    /**
     * Inizializza i comandi di navigazione.
     */
    private void initializeNavigationCommands() {
        this.navigationCommands = new ActionCommand[4];
        this.navigationCommands[NAVIGATE_FIRST] = new NavigateFirstCommand(NAVIGATE_FIRSTROW_CMDID);
        this.navigationCommands[NAVIGATE_PREVIOUS] = new NavigatePreviousCommand(NAVIGATE_PREVIOUSROW_CMDID);
        this.navigationCommands[NAVIGATE_NEXT] = new NavigateNextCommand(NAVIGATE_NEXTROW_CMDID);
        this.navigationCommands[NAVIGATE_LAST] = new NavigateLastCommand(NAVIGATE_LASTROW_CMDID);

        navigationCommandInterceptor = new NavigationCommandInterceptor();

        this.navigationCommandGroup = new JECCommandGroup();
        for (ActionCommand navigationCommand : this.navigationCommands) {
            RcpSupport.configure(navigationCommand);
            navigationCommand.setEnabled(false);
            navigationCommand.addCommandInterceptor(navigationCommandInterceptor);
            navigationCommandGroup.add(navigationCommand);
        }
    }

    /**
     * inizializza i comandi per la selezione.
     */
    private void initializeSelectCommands() {
        selectCommand = new ActionCommand(SELECT_ALL_ID) {
            @Override
            protected void doExecuteCommand() {
                selectAll();
            }
        };
    }

    /**
     * Installa la funzione di ricerca contestuale nella tabella.
     */
    private void installSearchable() {
        tableScrollPaneSearchable = new JideTableSearchable(tableControl);

        tableScrollPaneSearchable.setMainIndex(-1);
        tableScrollPaneSearchable.setRepeats(true);
        tableScrollPaneSearchable.setHideSearchPopupOnEvent(true);

        // tableScrollPaneSearchable.setPopupTimeout(1000);

        // Setto a true per evitare una NPE su ricerche veloci, generata dal
        // fatto che non viene trovato il JLayeredPane
        tableScrollPaneSearchable.setHeavyweightComponentEnabled(true);
    }

    /**
     * @return the adjustingMode
     */
    public boolean isAdjustingMode() {
        return adjustingMode;
    }

    /**
     * @return true se non ci sono righe nella tabella.
     */
    public boolean isEmpty() {
        return tableModel.getRowCount() == 0;
    }

    @Override
    public boolean isShowing() {
        return false;
    }

    /**
     * Carica il layout alla tabella.
     *
     * @param inputStream
     *            layout
     */
    public void loadLayout(InputStream inputStream) {
        tableSelectionColumnsOptionPane.resetColumns();
        ((ITable<?>) getTable()).loadLayout(inputStream);
        tableSelectionColumnsOptionPane.update();
    }

    /**
     * @return numero di righe della tabella
     */
    public int nrOfRows() {
        return tableModel.getRowCount();
    }

    @Override
    public void onAboutToHide() {
    }

    @Override
    public void onAboutToShow() {
        super.onAboutToShow();
        this.tableControl.requestFocusInWindow();
    }

    /**
     * @param objectToRemove
     *            l'elemento da rimuovere
     */
    public void removeRowObject(T objectToRemove) {
        if (tableControl == null) {
            init();
        }
        int viewIndex = this.tableControl.getSelectedRow();
        String preference = TableUtils.getTablePreferenceByName(tableControl);
        tableModel.removeObject(objectToRemove);
        TableUtils.setTablePreferenceByName(tableControl, preference);
        if (!getRows().isEmpty()) {
            if (viewIndex == 0 || (viewIndex > 0 && getRows().size() > viewIndex)) {
                this.tableControl.getSelectionModel().setSelectionInterval(viewIndex, viewIndex);
            } else if (viewIndex > 0 && getRows().size() <= viewIndex) {
                this.tableControl.getSelectionModel().setSelectionInterval(viewIndex - 1, viewIndex - 1);
            }
        } else {
            selectionMonitor.setValue(null);
        }
    }

    /**
     * @param objectsToRemove
     *            gli elementi da rimuovere
     */
    public void removeRowsObject(List<T> objectsToRemove) {
        if (objectsToRemove.isEmpty()) {
            return;
        }
        if (tableControl == null) {
            init();
        }
        int viewIndex = this.tableControl.getSelectedRow();
        String preference = TableUtils.getTablePreferenceByName(tableControl);
        for (T objectToRemove : objectsToRemove) {
            tableModel.removeObject(objectToRemove);
        }
        TableUtils.setTablePreferenceByName(tableControl, preference);
        if (viewIndex == 0 || (viewIndex > 0 && getRows().size() > viewIndex)) {
            this.tableControl.getSelectionModel().setSelectionInterval(viewIndex, viewIndex);
        } else if (viewIndex > 0 && getRows().size() <= viewIndex) {
            this.tableControl.getSelectionModel().setSelectionInterval(viewIndex - 1, viewIndex - 1);
        }
    }

    /**
     *
     * @param observer
     *            .
     */
    public void removeSelectionObserver(Observer observer) {
        this.selectionMonitor.deleteObserver(observer);
    }

    /**
     *
     * @param listener
     *            .
     */
    public void removeTableModelListener(TableModelListener listener) {
        tableModel.removeTableModelListener(listener);
    }

    /**
     *
     * @param oldObject
     *            .
     * @param newObject
     *            .
     * @param originatingObserver
     *            .
     */
    public void replaceOrAddRowObject(T oldObject, T newObject, Observer originatingObserver) {
        int index = this.tableModel.getObjects().indexOf(oldObject);
        if (index != -1) {
            replaceRowObject(oldObject, newObject, originatingObserver);
        } else {
            addRowObject(newObject, originatingObserver);
        }
    }

    /**
     *
     * @param oldObject
     *            .
     * @param newObject
     *            .
     * @param originatingObserver
     *            .
     */
    public void replaceRowObject(T oldObject, T newObject, Observer originatingObserver) {
        int index = this.tableModel.getObjects().indexOf(oldObject);
        if (index != -1) {
            if (originatingObserver != null) {
                selectionMonitor.deleteObserver(originatingObserver);
            }

            String preference = TableUtils.getTablePreferenceByName(tableControl);
            tableModel.setObject(newObject, index);
            TableUtils.setTablePreferenceByName(tableControl, preference);
            selectRowObject(newObject, null);

            if (originatingObserver != null) {
                selectionMonitor.addObserver(originatingObserver);
            }
        }
    }

    @Override
    public void restoreState(Settings settings) {
        logger.debug("--> Enter restoreState");
        // se la tabella è un aggregate, contiene parecchie righe
        // e deve ancora essere visualizzata applicando il layout crea i render
        // per tutte le righe
        // Per sicurezza rimuovo le righe e le reinserisco
        List<T> righe = getRows();
        try {
            setRows(new ArrayList<T>());
            getTableLayoutView().applicaCorrente();
        } finally {
            setRows(righe);
            selectRow(0, null);
        }
        logger.debug("--> Exit restoreState");
    }

    @Override
    public void saveState(Settings settings) {
        logger.debug("--> Enter saveState");
        getTableLayoutView().getLayoutManager().salva(getTableLayoutView().getCurrentLayout());
        logger.debug("--> Exit saveState");
    }

    /**
     *
     */
    public void scrollToSelectedRow() {
        logger.debug("--> Enter scrollToSelectedRow");
        ((JideTable) getTable()).scrollRowToVisible(getTable().getSelectionModel().getAnchorSelectionIndex());
        logger.debug("--> Exit scrollToSelectedRow");
    }

    /**
     *
     */
    public void selectAll() {
        Runnable doUnselectAll = new Runnable() {
            @Override
            public void run() {
                tableControl.getSelectionModel().setSelectionInterval(0, tableModel.getRowCount());
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            doUnselectAll.run();
        } else {
            SwingUtilities.invokeLater(doUnselectAll);
        }
    }

    /**
     *
     */
    public void selectNextRow() {
        logger.debug("--> Enter selectNextRow");
        navigationCommands[NAVIGATE_NEXT].execute();
        logger.debug("--> Exit selectRow");
    }

    /**
     *
     * @param index
     *            .
     * @param originatingObserver
     *            .
     */
    public void selectRow(final int index, final Observer originatingObserver) {
        logger.debug("--> Enter selectRow");
        Runnable doSelectRowObject = new Runnable() {

            @Override
            public void run() {

                try {
                    if (originatingObserver != null) {
                        selectionMonitor.deleteObserver(originatingObserver);
                    }

                    ((ITable<?>) tableControl).selectRowObject(index);
                    // if ((index > -1)) { // && (getVisibleObjects().size() >
                    // index)) {
                    // tableControl.getSelectionModel().setSelectionInterval(index,
                    // index);
                    // } else {
                    // tableControl.getSelectionModel().clearSelection();
                    // }
                    scrollToSelectedRow();

                    if (originatingObserver != null) {
                        selectionMonitor.addObserver(originatingObserver);
                    }
                } catch (Exception e) {
                    logger.warn("Errore nel ripristinare la selezione per la tabella " + widgetId);
                }
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            doSelectRowObject.run();
        } else {
            SwingUtilities.invokeLater(doSelectRowObject);
        }
        logger.debug("--> Exit selectRow");
    }

    /**
     * Seleziona una riga.
     *
     * @param objectToSelect
     *            oggetto della riga da selezionare
     * @param originatingObserver
     *            observe che non viene richiamato
     */
    public void selectRowObject(final T objectToSelect, final Observer originatingObserver) {
        logger.debug("--> Enter selectRowObject");
        int selectedIndex = getRows().indexOf(objectToSelect);
        if (selectedIndex != -1) {
            int timeToDelay = getDelayForSelection();
            try {
                setDelayForSelection(0);
                selectRow(selectedIndex, originatingObserver);
            } finally {
                setDelayForSelection(timeToDelay);
            }
        }
        logger.debug("--> Exit selectRowObject");
    }

    /**
     * @param adjustingMode
     *            the adjustingMode to set
     */
    public void setAdjustingMode(boolean adjustingMode) {
        this.adjustingMode = adjustingMode;
    }

    /**
     * Colonne da aggregare all'apertura.
     *
     * @param columnsName
     *            nome delle colonne
     */
    public void setAggregatedColumns(String[] columnsName) {
        // Il nome delle colonne nel columnModel viene variato per
        // l'internazionalizzazione
        // devo quindi prendere la proprietà del bean e internazionalizzarla
        defaultAggregateColumns = new String[columnsName.length];

        for (int i = 0; i < columnsName.length; i++) {
            String originalName = columnsName[i];
            defaultAggregateColumns[i] = getFieldFaceSource().getFieldFace(originalName, widgetId).getLabelInfo()
                    .getText();
        }

        if (tableControl != null) {
            // tableControl.getAggregateTableModel().setAggregatedColumns(defaultAggregateColumns);
            // tableControl.getAggregateTableModel().aggregate();
            tableControl.putClientProperty(DEFAULT_AGGREGATE_COLUMNS_PROPERTY, defaultAggregateColumns);
            TableUtils.autoResizeAllColumns(tableControl);
        }
    }

    /**
     * @param changeSelectionConstraint
     *            the changeSelectionConstraint to set
     */
    @SuppressWarnings("unchecked")
    public void setChangeSelectionConstraint(Constraint changeSelectionConstraint) {
        this.changeSelectionConstraint = changeSelectionConstraint;
        if (tableControl != null) {
            ((ITable<T>) tableControl).setChangeSelectionConstraint(this.changeSelectionConstraint);
        }
    }

    /**
     * Imposta il tempo di attesa prima di lanciare l'evento di selezione della tabella. Il tempo di default impostato è
     * 0.
     *
     * @param delay
     *            tempo in millisecondi
     */
    public void setDelayForSelection(int delay) {
        this.delayForSelection = delay;
    }

    /**
     *
     * @param editable
     *            false per rendere le celle editabili non editabili.
     */
    public void setEditable(boolean editable) {
        ((ITable<?>) tableControl).setEditable(editable);
    }

    /**
     * @param hierarchicalTableComponentFactory
     *            the hierarchicalTableComponentFactory to set
     */
    public void setHierarchicalTableComponentFactory(
            HierarchicalTableComponentFactory hierarchicalTableComponentFactory) {
        this.hierarchicalTableComponentFactory = hierarchicalTableComponentFactory;
    }

    /**
     * @param layoutView
     *            the layoutView to set
     */
    public void setLayoutView(DefaultTableLayoutView layoutView) {
        this.layoutView = layoutView;
    }

    /**
     * Setta la visibilita del numero righe.
     *
     * @param visibleRowNumbers
     *            true per visualizzare il numero di righe.
     */
    public void setNumberRowVisible(boolean visibleRowNumbers) {
        overlayTable.setNumberRowVisible(visibleRowNumbers);
    }

    /**
     * Aggiorna/inserisce pioù righe nella tabella
     *
     * @param object
     *            oggetto da aggiornare/aggiungere
     */
    public void setObject(T object) {
        if (tableControl == null) {
            init();
        }
        String preference = TableUtils.getTablePreferenceByName(tableControl);
        tableModel.setObject(object);
        TableUtils.setTablePreferenceByName(tableControl, preference);
        // tableModel.fireTableStructureChanged();
        tableModel.fireTableDataChanged();
    }

    /**
     * Aggiorna/inserisce più righe nella tabella
     *
     * @param objects
     *            oggetti da aggiornare/aggiungere
     */
    public void setObjects(List<T> objects) {
        for (T object : objects) {
            setObject(object);
        }
    }

    /**
     * @param overlayTable
     *            the overlayTable to set
     */
    public void setOverlayTable(JideEmptyOverlayTableScrollPane overlayTable) {
        this.overlayTable = overlayTable;
    }

    /**
     * @param propertyCommandExecutor
     *            the propertyCommandExecutor to set
     */
    public void setPropertyCommandExecutor(ActionCommandExecutor propertyCommandExecutor) {
        this.propertyCommandExecutor = propertyCommandExecutor;
    }

    /**
     * setta le righe nella tabella.
     *
     * @param newRows
     *            righe da settare
     */
    public void setRows(final Collection<T> newRows) {
        setRows(newRows, true);
    }

    /**
     * setta le righe nella tabella e seleziona la prima.
     *
     * @param newRows
     *            righe da settare
     * @param scrollToFirstRow
     *            true per selezionare la prima riga.
     */
    public void setRows(final Collection<T> newRows, final boolean scrollToFirstRow) {
        logger.debug("--> Enter setRows");
        if (tableControl == null) {
            init();
        }
        Runnable task = new Runnable() {
            @Override
            public void run() {
                adjustingMode = true;
                String preference = TableUtils.getTablePreferenceByName(tableControl);
                String sortableTablePreference = "";
                try {
                    sortableTablePreference = TableUtils.getSortableTablePreference((SortableTable) tableControl);
                } catch (Exception ex) {
                    logger.error("-->errore nel salvare le condizioni di sorting", ex);
                }

                tableModel.setRows(newRows);
                tableModel.fireTableDataChanged();
                if (!sortableTablePreference.isEmpty()) {
                    TableUtils.setSortableTablePreference((SortableTable) tableControl, sortableTablePreference);
                }
                TableUtils.setTablePreferenceByName(tableControl, preference);
                if (newRows.size() > 0 && scrollToFirstRow) {
                    // Seleziona la riga nel modello. A me serve la prima riga
                    // della griglia
                    selectRow(TableModelWrapperUtils.getActualRowAt(tableControl.getModel(), 0), null);
                } else {
                    selectionMonitor.setValue(null);
                }

                adjustingMode = false;
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeLater(task);
        }
        logger.debug("--> Exit setRows");
    }

    /**
     * @param summaryCalculatorFactory
     *            The summaryCalculatorFactory to set.
     */
    public void setSummaryCalculatorFactory(SummaryCalculatorFactory summaryCalculatorFactory) {
        this.summaryCalculatorFactory = summaryCalculatorFactory;
    }

    /**
     *
     * @param tableHeader
     *            tableHeader da settare alla tabella
     */
    public void setTableHeader(ITableHeader tableHeader) {
        printCommand.setTableHeader(tableHeader);
        exportPDFCommand.setTableHeader(tableHeader);
        openReportCommand.setTableHeader(tableHeader);
    }

    /**
     * @param tableSelectionColumnsOptionPane
     *            the tableSelectionColumnsOptionPane to set
     */
    public void setTableSelectionColumnsOptionPane(
            AbstractTableSelectionColumnsOptionPane tableSelectionColumnsOptionPane) {
        this.tableSelectionColumnsOptionPane = tableSelectionColumnsOptionPane;
    }

    /**
     * @param tableType
     *            The tableType to set.
     */
    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    /**
     * Esco dalla modalità editazione.
     */
    public void stopCellEditor() {
        TableCellEditor editor = getTable().getCellEditor();
        if (editor != null) {
            editor.stopCellEditing();
        }
    }

    /**
     * Toglie la selezione delle righe.
     */
    public void unSelectAll() {
        Runnable doUnselectAll = new Runnable() {
            @Override
            public void run() {
                tableControl.getSelectionModel().clearSelection();
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            doUnselectAll.run();
        } else {
            SwingUtilities.invokeLater(doUnselectAll);
        }
    }
}
