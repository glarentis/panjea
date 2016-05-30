package it.eurotn.rich.editors;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingWorker;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.dialog.support.DialogPageUtils;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.Memento;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DialogFloatingContainer;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockableHolderPanel;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.TableColumnChooser;

import it.eurotn.locking.IDefProperty;
import it.eurotn.locking.ILock;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.rich.command.GlueActionCommand;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideEmptyOverlayTableScrollPane;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

/**
 * Editor che visualizza i dati attraverso una TableWidget.
 *
 * @author fattazzo
 */
public abstract class AbstractTablePageEditor<T> extends AbstractDialogPage
        implements IPageEditor, IEditorListener, Memento, PropertyChangeListener, Observer {

    private class DeleteCommand extends ActionCommand {

        /**
         * Costruttore di default.
         */
        public DeleteCommand() {
            super(DELETE_COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            if (editFrame == null) {
                return;
            }
            if (tableWidget.getSelectedObject() == null) {
                return;
            }
            if (!editFrame.getCurrentEditPage().getEditorDeleteCommand().isEnabled()) {
                return;
            }

            if (!readOnly) {
                editFrame.setCurrentPage(tableWidget.getSelectedObject());
                editFrame.getCurrentEditPage().setFormObject(tableWidget.getSelectedObject());
                editFrame.getCurrentEditPage().getEditorDeleteCommand().execute();
            } else {
                // cambio lo stato dei command della pagina di editazione perchè
                // settando un nuovo
                // oggetto la formbacked li riabilita
                updateCommandsForReadOnly();
            }
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(pageId + "." + DELETE_COMMAND_ID);
        }
    }

    private class EditFrameDockingManager extends DefaultDockingManager {

        /**
         * Costruttore.
         *
         * @param rootpanecontainer
         *            rootpanecontainer
         * @param container
         *            container
         */
        public EditFrameDockingManager(final RootPaneContainer rootpanecontainer, final Container container) {
            super(rootpanecontainer, container);
        }

        @Override
        protected DialogFloatingContainer createDialogFloatingContainer(InternalEventManager internaleventmanager,
                Window window) {
            // usato dal tool di test per assegnare un nome alla edit frame
            DialogFloatingContainer dialog = super.createDialogFloatingContainer(internaleventmanager, window);
            dialog.setTitle(AbstractTablePageEditor.this.getId() + "EditFrame");
            return dialog;
        }

    }

    private class EditFramePropertyChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("docked".equals(evt.getPropertyName()) || "floated".equals(evt.getPropertyName())) {
                updateTableCommands();
            }
        }
    }

    private class LoadDataSwingWorker extends TableDataSwingWorker<T> {

        /**
         * Costruttore.
         *
         * @param tablePage
         *            table page
         */
        public LoadDataSwingWorker(final AbstractTablePageEditor<T> tablePage) {
            super(tablePage);
        }

        @Override
        public Collection<T> getData() {
            return loadTableData();
        }
    }

    public class LockCommand extends ActionCommand {

        /**
         * Costruttore di default.
         */
        public LockCommand() {
            super(EDIT_COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            if (editFrame == null) {
                return;
            }
            if (!editFrame.getCurrentEditPage().getEditorLockCommand().isAuthorized()) {
                return;
            }
            T formObject = tableWidget.getSelectedObject();

            if (formObject == null) {
                return;
            }

            editFrame.setCurrentPage(formObject);
            editFrame.getCurrentEditPage().preSetFormObject(formObject);
            editFrame.getCurrentEditPage().setFormObject(formObject);
            editFrame.getCurrentEditPage().postSetFormObject(formObject);

            editFrame.viewFrame();

            if (!readOnly) {
                editFrame.getCurrentEditPage().getEditorLockCommand().execute();
            } else {
                // cambio lo stato dei command della pagina di editazione perchè
                // settando un nuovo
                // oggetto la formbacked li riabilita
                updateCommandsForReadOnly();
            }
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(pageId + "." + EDIT_COMMAND_ID);
        }
    }

    public class NewCommand extends ActionCommand {

        private final IPageEditor page;
        private final String commandId;

        /**
         * Costruttore di default.
         *
         * @param commandId
         *            id del comando
         * @param page
         *            pagina di riferimento
         */
        public NewCommand(final String commandId, final IPageEditor page) {
            super(commandId);
            RcpSupport.configure(this);
            this.page = page;
            this.commandId = commandId;
        }

        @Override
        protected void doExecuteCommand() {
            if (editFrame.getCurrentEditPage().getEditorNewCommand().isEnabled()) {
                editFrame.getCurrentEditPage().getEditorUndoCommand().execute();
                editFrame.viewFrame();
                editFrame.setCurrentPage(page.getPageObject());
                editFrame.getCurrentEditPage().getEditorNewCommand().execute();
            }
        }

        /**
         * @return the page
         */
        public IPageEditor getPage() {
            return page;
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(pageId + "." + commandId);
        }
    }

    private class RefreshCommand extends ActionCommand {

        /**
         * Costruttore di default.
         */
        public RefreshCommand() {
            super(REFRESH_COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            onRefresh();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(pageId + "." + REFRESH_COMMAND_ID);
        }
    }

    private class RefreshDataSwingWorker extends TableDataSwingWorker<T> {

        /**
         * Costruttore.
         *
         * @param tablePage
         *            table page
         */
        public RefreshDataSwingWorker(final AbstractTablePageEditor<T> tablePage) {
            super(tablePage);
        }

        @Override
        public Collection<T> getData() {
            return refreshTableData();
        }

    }

    private class SaveCommand extends ActionCommand {

        /**
         * Costruttore di default.
         */
        public SaveCommand() {
            super(SAVE_COMMAND_ID);
        }

        @Override
        protected void doExecuteCommand() {
            if (editFrame.getCurrentEditPage().getEditorSaveCommand().isEnabled()) {
                editFrame.getCurrentEditPage().getEditorSaveCommand().execute();
            }
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(pageId + "." + SAVE_COMMAND_ID);
        }
    }

    private class TableControlFactory extends AbstractControlFactory {

        @Override
        protected JComponent createControl() {
            return createTableWidget();
        }

    }

    /**
     * Crea i componenti docked se la pagina è una tabella con la pagina di modifica.
     *
     * @author giangi
     * @version 1.0, 17/apr/2014
     */
    private class TableDockingControlFactory extends AbstractControlFactory {

        @Override
        protected JComponent createControl() {
            holderPanel = new DockableHolderPanel(null) {
                private static final long serialVersionUID = 2114918483793557658L;

                @Override
                protected DockingManager createDockingManager(RootPaneContainer rootpanecontainer) {
                    return new EditFrameDockingManager(rootpanecontainer, this);
                }
            };
            holderPanel.getDockingManager().setFloatable(true);
            holderPanel.getDockingManager().setFloatingContainerType(DockingManager.FLOATING_CONTAINER_TYPE_DIALOG);
            holderPanel.getDockingManager().setEasyTabDock(true);
            holderPanel.getDockingManager().setShowGripper(true);
            holderPanel.getDockingManager().setShowDividerGripper(true);
            holderPanel.getDockingManager().setSensitiveAreaSize(20);
            holderPanel.getDockingManager().setDoubleClickAction(DockingManager.DOUBLE_CLICK_TO_MAXIMIZE);
            holderPanel.getDockingManager().getWorkspace().setVisible(false);
            holderPanel.getDockingManager().setRearrangable(false);
            holderPanel.getDockingManager().resetToDefault();

            JComponent tableComponent = null;
            try {
                tableComponent = createTableWidget();
            } catch (Exception e) {
                // è stato generato un errore generico e questo capita
                // spesso quando il layout salvato è corrotto.
                // Provo a ripristinare le colonne al valore iniziale e riaprire, se l'errore
                // persiste
                // pazienza.
                TableColumnChooser.resetColumnsToDefault(getTable().getTable());
                tableComponent = createTableWidget();
            }

            DockableFrame tableFrame = new DockableFrame();
            tableFrame.setKey(TABELLA_FRAME_ID);
            tableFrame.getContext().setInitSide(DockContext.DOCK_SIDE_NORTH);
            tableFrame.getContext().setInitIndex(0);
            tableFrame.getContentPane().add(tableComponent);
            tableFrame.setAvailableButtons(0);
            tableFrame.setShowContextMenu(false);

            // aggiungo il frame della tabella
            holderPanel.getDockingManager().addFrame(tableFrame);
            tableFrame.setShowTitleBar(false);

            // aggiungo la edit page solo se è stata settata
            initEditFrame();
            if (editFrame != null) {
                holderPanel.getDockingManager().addFrame(editFrame);
                for (AbstractCommand command : newCommands.values()) {
                    // TODO vedi su editor AreaMagazzino, new command di riga articolo ritorna
                    // command.getButtonIn(tableFrame) = null
                    // vedere in maraton se buttons in panjea hanno un nome.
                    AbstractButton button = command.getButtonIn(tableFrame);
                    if (button == null) {
                        System.err.println("command.getButtonIn(tableFrame) null = " + command.getId());
                    }
                    String buttonName = button != null ? button.getName() : command.getId();
                    command.setSecurityControllerId(editFrame.getCurrentEditPage().getPageEditorId() + ".controller");
                    RcpSupport.configure(command);
                    if (button != null) {
                        button.setName(buttonName);
                    }
                }

                String buttonName = getEditorDeleteCommand().getButtonIn(tableFrame).getName();
                getEditorDeleteCommand()
                        .setSecurityControllerId(editFrame.getCurrentEditPage().getPageEditorId() + ".controller");
                RcpSupport.configure(getEditorDeleteCommand());
                getEditorDeleteCommand().getButtonIn(tableFrame).setName(buttonName);

                getEditorLockCommand().setEnabled(false);
                getEditorDeleteCommand().setEnabled(false);

            }
            return holderPanel;
        }

    }

    private class UndoCommand extends ActionCommand {

        /**
         * Costruttore di default.
         */
        public UndoCommand() {
            super(UNDO_COMMAND_ID);
        }

        @Override
        protected void doExecuteCommand() {
            if (editFrame.getCurrentEditPage() instanceof FormBackedDialogPageEditor) {
                ((FormBackedDialogPageEditor) editFrame.getCurrentEditPage()).getForm().revert();
            } else {
                if (editFrame.getCurrentEditPage() != null
                        && editFrame.getCurrentEditPage().getEditorUndoCommand() != null) {
                    editFrame.getCurrentEditPage().getEditorUndoCommand().execute();
                }
            }
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(pageId + UNDO_COMMAND_ID);
        }
    }

    protected static final String TABELLA_FRAME_ID = "tableFrame";

    private static final String REFRESH_COMMAND_ID = "refreshCommand";

    private static final String EDIT_COMMAND_ID = "lockCommand";

    private static final String DELETE_COMMAND_ID = "deleteCommand";

    private static final String SAVE_COMMAND_ID = "saveCommand";
    private static final String UNDO_COMMAND_ID = "undoCommand";
    private static final String NEW_COMMAND_ID = "newCommand";
    private String pageSecurityEditorId = null;
    private JideTableWidget<T> tableWidget = null;
    private final String pageId;
    // rappresenta le pagine di editazione
    private Map<String, IPageEditor> editPages;

    // modalità di editazione.
    protected EEditPageMode editPageMode = EEditPageMode.DETAIL;

    private ActionCommand autoResizesRowCommand;
    private ActionCommand lockCommand;

    private ActionCommand deleteCommand;
    private ActionCommand refreshCommand;
    private Map<String, AbstractCommand> newCommands;
    /**
     * Valore booleano che indica se includere la titlePane nella creazione dei controlli.
     */
    private boolean showTitlePane = false;
    protected DockableHolderPanel holderPanel;
    private boolean readOnly = false;

    private boolean enableAutoResizeRow = false;

    private AbstractControlFactory controlFactory;

    protected JECCommandGroup toolbarCommandGroup;

    private AbstractCommand saveCommand;

    private AbstractCommand undoCommand;

    private final JideEmptyOverlayTableScrollPane overlayTable = new JideEmptyOverlayTableScrollPane();

    private EditFramePropertyChangeListener editFramePropertyChangeListener;

    @SuppressWarnings("rawtypes")
    private SwingWorker loadDataSwingWorker;

    @SuppressWarnings("rawtypes")
    private SwingWorker refreshDataSwingWorker;

    private EditFrame<T> editFrame;

    protected JPanel tablePanel;

    /**
     * Se si usa questo costruttore si deve impostare la tableWidget dall'esterno.
     *
     * @param pageId
     *            idpagina.
     */
    protected AbstractTablePageEditor(final String pageId) {
        super(pageId);
        this.pageId = pageId;
    }

    /**
     * Costruttore.
     *
     * @param pageId
     *            id della pagina
     * @param tableModel
     *            table model che verrà applicato alla tabella
     */
    protected AbstractTablePageEditor(final String pageId, final DefaultBeanTableModel<T> tableModel) {
        super(pageId);
        this.pageId = pageId;
        this.tableWidget = new JideTableWidget<T>(pageId + "Widget", tableModel);
        this.tableWidget.setOverlayTable(overlayTable);
    }

    /**
     * Costruttore.
     *
     * @param pageId
     *            id della pagina
     * @param tableWidget
     *            tabella che verrà visualizzata
     */
    protected AbstractTablePageEditor(final String pageId, final JideTableWidget<T> tableWidget) {
        super(pageId);
        this.pageId = pageId;
        this.tableWidget = tableWidget;
        this.tableWidget.setOverlayTable(overlayTable);
    }

    /**
     * Costruttore.
     *
     * @param pageId
     *            id della pagina
     * @param columnsName
     *            Lista delle colonne da visualizzare
     * @param classe
     *            Classe del bean da visualizzare
     */
    protected AbstractTablePageEditor(final String pageId, final String[] columnsName, final Class<T> classe) {
        super(pageId);
        this.pageId = pageId;
        this.tableWidget = new JideTableWidget<T>(pageId + "Widget", columnsName, classe);
        this.tableWidget.setOverlayTable(overlayTable);
    }

    /**
     * Crea i controlli della pagina.
     *
     * @return Controlli creati per la pagina
     */
    @Override
    protected JComponent createControl() {
        if (controlFactory == null) {
            if (editPages == null) {
                this.controlFactory = new TableControlFactory();
            } else {
                this.controlFactory = new TableDockingControlFactory();
            }
        }
        return controlFactory.getControl();
    }

    /**
     * Crea il frame per la pagina di modifica.
     *
     * @return frame creato
     */
    protected EditFrame<T> createEditFrame() {
        return new EditFrame<T>(editPageMode, this, EditFrame.QUICK_ACTION_DEFAULT);
    }

    /**
     * Crea i controlli per il frame della tabella seguendo questo schema.<br>
     * <br>
     * <ul>
     * <li>Tilolo della pagina ( shoTitlePane )</li>
     * <li>Header control ( {@link AbstractTablePageEditor#getHeaderControl()} )</li>
     * <li>Tabella</li>
     * <li>Buttonbar della tabella</li>
     * <li>Footer control ( {@link AbstractTablePageEditor#getFooterControl()} )</li>
     * </ul>
     *
     * @return Controlli creati per la tabella
     */
    protected JComponent createTableWidget() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

        getObjectConfigurer().configure(this, this.pageId);

        // aggiunta del titolo se configurato
        if (!StringUtils.isBlank(getTitle()) && (showTitlePane)) {
            rootPanel.add(DialogPageUtils.createTitlePane(this).getControl(), BorderLayout.PAGE_START);
        }

        JComponent headerControl = getHeaderControl();

        tablePanel = getComponentFactory().createPanel(new BorderLayout());

        if (headerControl != null) {
            tablePanel.add(headerControl, BorderLayout.NORTH);
        }

        if (tableWidget != null) {
            tablePanel.add(tableWidget.getComponent(), BorderLayout.CENTER);
            tableWidget.addSelectionObserver(this);
        }

        JComponent footerControl = getFooterControl();

        if (footerControl != null) {
            tablePanel.add(footerControl, BorderLayout.SOUTH);
        }

        rootPanel.add(tablePanel, BorderLayout.CENTER);

        JComponent toolbar = createToolbar();
        if (toolbar != null) {
            JPanel toolbarPanel = getComponentFactory().createPanel(new BorderLayout());
            toolbarPanel.add(toolbar, BorderLayout.CENTER);
            rootPanel.add(toolbarPanel, BorderLayout.SOUTH);
        }
        rootPanel.setPreferredSize(new Dimension(100, 100));
        rootPanel.setBorder(BorderFactory.createEmptyBorder());

        return rootPanel;
    }

    /**
     * Crea la toolbar della pagina.<br>
     * Nella toolbar saranno presenti di default i comandi Elimina e Modifica se la edit page è configurata. Verranno
     * aggiunti poi tutti gli eventuali comandi definiti nel metodo getCommands.
     *
     * @return toolbar creata
     */
    public JComponent createToolbar() {
        toolbarCommandGroup = new JECCommandGroup();
        if (enableAutoResizeRow) {
            toolbarCommandGroup.add(getAutoResizesRowCommand());
        }
        toolbarCommandGroup.addGlue();

        AbstractCommand[] customCommands = ObjectUtils.defaultIfNull(getCommands(), new AbstractCommand[] {});
        for (AbstractCommand command : customCommands) {
            if (command == null) {
                continue;
            }
            if (command instanceof GlueActionCommand) {
                toolbarCommandGroup.addGlue(true);
            } else {
                toolbarCommandGroup.add(command);
            }
        }

        if (getEditPages() != null && !isIgnoreDetailCommands()) {
            for (AbstractCommand command : getNewCommands().values()) {
                toolbarCommandGroup.add(command);
            }
            toolbarCommandGroup.add(getEditorLockCommand());
            toolbarCommandGroup.add(getEditorDeleteCommand());
            toolbarCommandGroup.add(getRefreshCommand());
        }

        if (toolbarCommandGroup.getMemberCount() == 0) {
            return null;
        } else {
            return toolbarCommandGroup.createToolBar();
        }
    }

    @Override
    public void dispose() {
        if (tableWidget != null) {
            tableWidget.removeSelectionObserver(this);
        }

        if (holderPanel != null) {
            holderPanel.getDockingManager().removeFrame(TABELLA_FRAME_ID);

            if (editPages != null) {
                for (Entry<String, IPageEditor> entry : editPages.entrySet()) {
                    entry.getValue().dispose();
                }
            }

            if (editFrame != null) {
                editFrame.removePropertyChangeListener(editFramePropertyChangeListener);
                editFrame.dispose();
            }

            holderPanel.getDockingManager().removeFrame(EditFrame.EDIT_FRAME_ID);
            holderPanel.getDockingManager().dispose();
            holderPanel = null;
        }

        if (toolbarCommandGroup != null) {
            toolbarCommandGroup.reset();
        }

        if (tableWidget != null) {
            this.tableWidget.dispose();
            this.tableWidget = null;
        }
    }

    /**
     * @return true se abilita il pulsante per visualizzare la tabella in un grafico
     */
    protected boolean enableChartPanel() {
        return true;
    }

    /**
     * @return the autoResizesRowCommand
     */
    public ActionCommand getAutoResizesRowCommand() {
        if (autoResizesRowCommand == null) {
            autoResizesRowCommand = new AutoResizesRowCommand((JideTable) getTable().getTable());
        }
        return autoResizesRowCommand;
    }

    /**
     * Array di comandi che verranno aggiunti alla toobal sotto la tabella.
     *
     * @return array di comandi
     */
    public AbstractCommand[] getCommands() {
        return null;
    }

    /**
     * @return EditFrame
     */
    public EditFrame<T> getEditFrame() {
        return editFrame;
    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        if (deleteCommand == null) {
            deleteCommand = new DeleteCommand();
        }
        return deleteCommand;
    }

    @Override
    public AbstractCommand getEditorLockCommand() {
        if (editPages != null) {
            if (lockCommand == null) {
                lockCommand = new LockCommand();
            }
            return lockCommand;
        } else {
            return null;
        }
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        if (editPages != null) {
            if (getNewCommands() != null && !getNewCommands().isEmpty()) {
                return getNewCommands().values().iterator().next();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        if (editPages != null) {
            if (saveCommand == null) {
                saveCommand = new SaveCommand();
            }
            return saveCommand;
        } else {
            return null;
        }
    }

    @Override
    public AbstractCommand getEditorUndoCommand() {
        if (editPages != null) {
            if (undoCommand == null) {
                undoCommand = new UndoCommand();
            }
            return undoCommand;
        } else {
            return null;
        }
    }

    /**
     * Ritorna una mappa di editPages.<br/>
     * Se esiste una sola pagina la chiave è EditFrame.DEFAULT_OBJECT_CLASS_NAME
     *
     * @return the editPages
     */
    public Map<String, IPageEditor> getEditPages() {
        return editPages;
    }

    /**
     * Restituisce i controlli che verranno visualizzati sotto la tabella.
     *
     * @return controlli da visualizzare. Se ritorna <code>null</code> non verrà visualizzato nessun componente.
     */
    public JComponent getFooterControl() {
        return null;
    }

    /**
     * Restituisce i controlli che verranno visualizzati sopra la tabella.
     *
     * @return controlli da visualizzare. Se ritorna <code>null</code> non verrà visualizzato nessun componente.
     */
    public JComponent getHeaderControl() {
        return null;
    }

    /**
     * Worker utilizzato per il caricamento dei dati.
     *
     * @return worker
     */
    @SuppressWarnings("rawtypes")
    public SwingWorker getLoadDataSwingWorker() {
        return new LoadDataSwingWorker(this);
    }

    /**
     * Ritorna l'eventuale oggetto del quale la pagina dovrà lanciare l'evento
     * {@link IPageLifecycleAdvisor#OBJECT_CHANGED}. Se l'oggetto ritornato sarà uguale a <code>null</code> non verrà
     * rilanciato niente.
     *
     * @param pageObject
     *            oggetto della pagina
     * @return oggetto di cui rilanciare l'evento
     */
    public Object getManagedObject(Object pageObject) {
        return null;
    }

    /**
     * @return command per creare un nuovo oggetto.
     */
    public Map<String, AbstractCommand> getNewCommands() {
        if (newCommands == null) {

            newCommands = new HashMap<String, AbstractCommand>();

            if (getEditPages().containsKey(EditFrame.DEFAULT_OBJECT_CLASS_NAME)) {
                IPageEditor page = getEditPages().get(EditFrame.DEFAULT_OBJECT_CLASS_NAME);
                newCommands.put(EditFrame.DEFAULT_OBJECT_CLASS_NAME,
                        new NewCommand(page.getEditorNewCommand().getId(), page));
            } else {
                for (final Entry<String, IPageEditor> entry : editPages.entrySet()) {
                    if (entry.getValue().getEditorNewCommand() != null
                            && entry.getValue().getEditorNewCommand().isVisible()) {
                        newCommands.put(entry.getKey(), new NewCommand(
                                NEW_COMMAND_ID + ".detail." + entry.getValue().getPageEditorId(), entry.getValue()));
                    }
                }
            }
        }
        return newCommands;
    }

    @Override
    public String getPageEditorId() {
        return getId();
    }

    @Override
    public Object getPageObject() {
        return null;
    }

    /**
     * @return pageSecurityEditorId
     */
    @Override
    public String getPageSecurityEditorId() {
        if (pageSecurityEditorId == null) {
            return getId();
        } else {
            return pageSecurityEditorId;
        }
    }

    /**
     * @return command per ricaricare i dati nella pagina
     */
    public ActionCommand getRefreshCommand() {
        if (refreshCommand == null) {
            refreshCommand = new RefreshCommand();
        }
        return refreshCommand;
    }

    /**
     * Worker utilizzato per il refresh dei dati.
     *
     * @return worker
     */
    @SuppressWarnings("rawtypes")
    public SwingWorker getRefreshDataSwingWorker() {
        return new RefreshDataSwingWorker(this);
    }

    /**
     * Restituisce il table widget.
     *
     * @return table widget
     */
    public JideTableWidget<T> getTable() {
        return this.tableWidget;
    }

    @Override
    public void grabFocus() {
        if (tableWidget != null && tableWidget.getTable() != null) {
            this.tableWidget.getTable().requestFocusInWindow();
        }
    }

    /**
     * Toglie il messaggio di ricerca in corso.
     */
    public void hideSearcInProgressMessage() {
        if (isControlCreated()) {
            overlayTable.stopSearch();
        }
    }

    /**
     * Crea il frame per la pagina di modifica.
     *
     * @return frame creato
     */
    private DockableFrame initEditFrame() {
        editFrame = null;
        if (this.editPages != null) {
            editFrame = createEditFrame();
            editFramePropertyChangeListener = new EditFramePropertyChangeListener();
            editFrame.addPropertyChangeListener(editFramePropertyChangeListener);
            editFrame.setEditMode(editPageMode);
        }
        return editFrame;
    }

    @Override
    public boolean isCommittable() {
        if (editPages != null && editFrame != null && editFrame.getCurrentEditPage() != null) {
            return editFrame.getCurrentEditPage().isCommittable();
        }
        return false;
    }

    @Override
    public boolean isDirty() {
        // NPE MAIL
        if (!isReadOnly() && editPages != null && editFrame != null && editFrame.getCurrentEditPage() != null) {
            return editFrame.getCurrentEditPage().isDirty();
        }
        return false;
    }

    /**
     * @return se <code>true</code> non considera i command configurati nell'eventuale pagina di detail ma inserisce
     *         nella toolbar solo i comandi configurati per la table page.
     */
    public boolean isIgnoreDetailCommands() {
        return false;
    }

    @Override
    public boolean isLocked() {
        if (editPages != null && editFrame != null && editFrame.getCurrentEditPage() != null) {
            return editFrame.getCurrentEditPage().isLocked();
        }
        return false;
    }

    /**
     * @return true se la pagina è in sola lettura (ma abilitata)
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void loadData() {
        try {
            if (loadDataSwingWorker != null) {
                loadDataSwingWorker.cancel(true);
                loadDataSwingWorker = null;
            }
            if (refreshDataSwingWorker != null) {
                refreshDataSwingWorker.cancel(true);
                refreshDataSwingWorker = null;
            }

            loadDataSwingWorker = getLoadDataSwingWorker();
            loadDataSwingWorker.execute();
        } catch (NullPointerException npe) {
            // Se ho una npe e i controlli non sono creati significa che ho chiuso la pagina...non
            // rilancio nessun
            // errore
            if (!isControlCreated()) {
                logger.error("-->errore durante la lodatTableData della pagina " + this.pageId, npe);
            } else {
                throw new RuntimeException("NPE in loadData", npe);
            }
        }
    }

    /**
     * Carica i dati nella table page.
     *
     * @return dati
     */
    public abstract Collection<T> loadTableData();

    @Override
    public Object onDelete() {
        if (editPages != null) {
            return editFrame.getCurrentEditPage().onDelete();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEditorEvent(ApplicationEvent event) {
        logger.debug("--> Enter onEditorEvent");

        PanjeaLifecycleApplicationEvent panjeaEvent = (PanjeaLifecycleApplicationEvent) event;
        if (panjeaEvent.getEventType().equals(LifecycleApplicationEvent.DELETED) && this.tableWidget != null
                && this.tableWidget.getClassObj() == panjeaEvent.getSource().getClass()) {
            // verifico che ci sia almeno una riga nella tabella.
            if (getTable().getRows().isEmpty()) {
                return;
            }
            // oggetto dell'evento
            Object object = panjeaEvent.getSource();

            // source dell'oggetto
            Object sourcePageEditor = panjeaEvent.getSourceContainer();

            // se l'origine dell'evento è il dettaglio della tabella non devo
            // chiamare la remove perche'
            // ci ha gia' pensato la tabella a rimuovere l'elemento
            // Controllo che l'evento non sia generato dalla mia detail o
            // direttamente da un pulsante sull'editor
            // se ho una pagina sorgene di default la considero true...vedo dopo
            // se è vero

            boolean removeObject = false;
            // non so da che pagina arriva. cancello di default
            if (sourcePageEditor == null) {
                removeObject = true;
            } else {
                // se ho una detail controllo se l'evento è stato generato da
                // quella
                if (this.editPages != null) {
                    removeObject = !editFrame.getCurrentEditPage().equals(sourcePageEditor);
                } else {
                    removeObject = !this.equals(sourcePageEditor);
                }
            }
            if (removeObject) {
                getTable().removeRowObject((T) object);
            }
        }
        logger.debug("--> Exit onEditorEvent");
    }

    @Override
    public ILock onLock() {
        if (editPages != null) {
            return editFrame.getCurrentEditPage().onLock();
        }
        return null;
    }

    @Override
    public void onNew() {
        if (editPages != null) {
            editFrame.getCurrentEditPage().onNew();
        }
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    /**
     * Metodo chiamato dal refreshCommand.
     */
    protected void onRefresh() {
        loadData();
    }

    @Override
    public boolean onSave() {
        if (editPages != null) {
            return editFrame.getCurrentEditPage().onSave();
        }
        return false;
    }

    @Override
    public boolean onUndo() {
        if (editPages != null) {
            return editFrame.getCurrentEditPage().onUndo();
        }
        return false;
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
        if (getEditorUndoCommand() != null && getEditorUndoCommand().isEnabled() && isControlCreated()
                && editFrame != null) {
            getEditorUndoCommand().execute();
        }
    }

    /**
     * Processa i dati caricati.
     *
     * @param results
     *            dati
     */
    public void processTableData(Collection<T> results) {
        setRows(results);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (IPageLifecycleAdvisor.OBJECT_CHANGED.equals(event.getPropertyName())) {

            Integer idObject = null;
            if (event.getNewValue() instanceof IDefProperty) {
                // Se il formObject deriva da IDefProperty vado direttamente
                // sulla propriet?
                // altrimenti vado di reflection
                idObject = ((IDefProperty) event.getNewValue()).getId();
                logger.debug("-->L'id trovato tramite IDefProperty risulta: " + idObject);
            } else {
                try {
                    BeanWrapperImpl wrapper = new BeanWrapperImpl(event.getNewValue());
                    idObject = (Integer) wrapper.getPropertyValue("id");
                    logger.debug("-->L'id trovato tramite la reflection risulta: " + idObject);
                } catch (BeansException e) {
                    logger.error("---> Non riesco a trovare tramite la reflection la propriet? id della classe  "
                            + event.getNewValue().getClass());
                    throw e;
                }
            }

            if (idObject != null) {

                Object object = getManagedObject(event.getNewValue());

                if (object != null) {
                    firePropertyChange(event.getPropertyName(), null, object);
                }
            }
        }
    }

    @Override
    public void refreshData() {
        try {
            if (refreshDataSwingWorker != null) {
                refreshDataSwingWorker.cancel(true);
                refreshDataSwingWorker = null;
            }
            if (loadDataSwingWorker != null) {
                loadDataSwingWorker.cancel(true);
                loadDataSwingWorker = null;
            }

            refreshDataSwingWorker = getRefreshDataSwingWorker();
            refreshDataSwingWorker.execute();
        } catch (NullPointerException npe) {
            // Se ho una npe e i controlli non sono creati significa che ho chiuso la pagina...non
            // rilancio nessun
            // errore
            if (!isControlCreated()) {
                logger.error("-->errore durante la lodatTableData della pagina " + this.pageId, npe);
            }
        }
    }

    /**
     * Ricarico i dati nella table page.
     *
     * @return dati
     *
     */
    public abstract Collection<T> refreshTableData();

    @Override
    public void restoreState(Settings settings) {
        try {
            getTable().restoreState(settings);
            if (editPages != null) {
                if (settings.contains(this.pageId + "." + EditFrame.EDIT_FRAME_ID + ".popupMode")) {
                    if (settings.getBoolean(this.pageId + "." + EditFrame.EDIT_FRAME_ID + ".popupMode")) {
                        editFrame.setEditMode(EEditPageMode.POPUP);
                        editPageMode = EEditPageMode.POPUP;
                        editFrame.hideFrame();
                    } else {
                        editFrame.setEditMode(EEditPageMode.DETAIL);
                        editPageMode = EEditPageMode.DETAIL;
                    }
                }
                updateTableCommands();
            }
        } catch (Exception e) {
            logger.warn("-->Impossibile ripristinare lo stato della pagina " + getId());
            System.err.println(e);
        }
    }

    @Override
    public void saveState(Settings settings) {
        try {
            getTable().saveState(settings);

            if (editPages != null) {
                settings.setBoolean(this.pageId + "." + EditFrame.EDIT_FRAME_ID + ".popupMode",
                        editFrame.getEditMode() == EEditPageMode.POPUP);

            }
        } catch (Exception e) {
            logger.warn("-->Impossibile salvare lo stato della pagina " + getId());
        }
    }

    /**
     * @param editPage
     *            the editPage to set
     */
    public void setEditPage(IFormPageEditor editPage) {
        this.editPages = new HashMap<String, IPageEditor>();
        this.editPages.put(EditFrame.DEFAULT_OBJECT_CLASS_NAME, editPage);
        tableWidget.setPropertyCommandExecutor(getEditorLockCommand());
    }

    /**
     * @param editMode
     *            the editView to set
     */
    public void setEditPageMode(EEditPageMode editMode) {
        this.editPageMode = editMode;
    }

    /**
     * @param editPages
     *            the editPages to set
     */
    public void setEditPages(Map<String, IPageEditor> editPages) {
        this.editPages = editPages;
        // ho delle pagine di edit quindi setto l'executor di default sull'edit
        tableWidget.setPropertyCommandExecutor(getEditorLockCommand());
    }

    /**
     * @param enableAutoResizeRow
     *            the enableAutoResizeRow to set
     */
    public void setEnableAutoResizeRow(boolean enableAutoResizeRow) {
        this.enableAutoResizeRow = enableAutoResizeRow;
    }

    /**
     * @param pageSecurityEditorId
     *            the pageSecurityEditorId to set
     */
    public void setPageSecurityEditorId(String pageSecurityEditorId) {
        this.pageSecurityEditorId = pageSecurityEditorId;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        // l'editFrame potrebbe non esistere nel caso in cui ho una table senza dettaglio quindi
        // controllo se è stata
        // mai aggiunta una page di edit
        if (editPages != null) {
            editFrame.getCurrentEditPage().setReadOnly(readOnly);
        }
        updateCommandsForReadOnly();
    }

    /**
     * @param rows
     *            righe da settare alla tabella.
     */
    public void setRows(Collection<T> rows) {
        if (getTable() != null) {
            getTable().setRows(rows);
        }
    }

    /**
     * @param newRows
     *            righe da inserire nella tabella
     * @param scrollToFirstRow
     *            effettua lo scroll sulla prima riga
     */
    public void setRows(Collection<T> newRows, boolean scrollToFirstRow) {
        if (getTable() != null) {
            getTable().setRows(newRows, scrollToFirstRow);
        }
    }

    /**
     * @param showTitlePane
     *            the showTitlePane to set
     */
    public void setShowTitlePane(boolean showTitlePane) {
        this.showTitlePane = showTitlePane;
    }

    /**
     * Setto una nuova table widget
     *
     * @param tableWidgetParam
     *            widget da impostare
     */
    public void setTableWidget(JideTableWidget<T> tableWidgetParam) {
        if (this.tableWidget != null) {
            this.tableWidget.setPropertyCommandExecutor(null);
            tableWidget.removeSelectionObserver(this);
            tableWidget = null;
        }
        this.tableWidget = tableWidgetParam;
        this.tableWidget.setOverlayTable(overlayTable);
        BorderLayout layout = (BorderLayout) tablePanel.getLayout();
        if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
            tablePanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
        }
        tablePanel.add(tableWidget.getComponent(), BorderLayout.CENTER);
    }

    /**
     * Visualizza sulla tabella il messaggio di ricerca in corso.
     */
    public void showSearcInProgressMessage() {
        if (isControlCreated()) {
            overlayTable.startSearch();
        }
    }

    @Override
    public void unLock() {
        if (editPages != null) {
            editFrame.getCurrentEditPage().unLock();
        }
    }

    @Override
    public void update(Observable observable, Object obj) {
        if (editPages != null) {
            getEditorLockCommand().setEnabled(obj != null);
        }
        if (getEditorDeleteCommand() != null) {
            getEditorDeleteCommand().setEnabled(obj != null);
        }
    }

    /**
     * Aggiorna tutti i comandi in base alla proprietà readonly della pagina.
     */
    public void updateCommandsForReadOnly() {
        if (getRefreshCommand() != null) {
            getRefreshCommand().setEnabled(!readOnly);
        }
        if (getEditorLockCommand() != null) {
            getEditorLockCommand().setEnabled(!readOnly);
        }
        if (getEditorDeleteCommand() != null) {
            getEditorDeleteCommand().setEnabled(!readOnly);
        }

        if (editPages != null) {
            for (AbstractCommand command : getNewCommands().values()) {
                command.setEnabled(!readOnly);
            }
        }

        for (AbstractCommand command : toolbarCommandGroup.getCommands()) {
            command.setEnabled(!readOnly);
        }

        getRefreshCommand().setEnabled(true);
    }

    /**
     * Aggiorna lo stato dei pulsanti in base alla editview passata come parametro.
     */
    public void updateTableCommands() {
        switch (editFrame.getEditMode()) {
        case DETAIL:
            getEditorLockCommand().setVisible(false);
            for (AbstractCommand command : newCommands.values()) {
                command.setVisible(false);
            }
            if (!isIgnoreDetailCommands()) {
                getEditorDeleteCommand().setVisible(false);
            }
            break;
        case POPUP:
            getEditorLockCommand().setVisible(true);
            for (AbstractCommand command : newCommands.values()) {
                if (command.isAuthorized()) {
                    command.setVisible(true);
                }
            }
            if (getEditorDeleteCommand().isAuthorized()) {
                getEditorDeleteCommand().setVisible(true);
            }
            break;
        default:
            break;
        }
    }
}
