package it.eurotn.rich.editors.table;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.ExclusiveCommandGroup;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.progress.BusyIndicator;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.constraint.Constraint;

import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.event.DockableFrameAdapter;
import com.jidesoft.docking.event.DockableFrameEvent;

import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.AbstractTablePageEditor.NewCommand;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.IToolbarPageCommands;
import it.eurotn.rich.form.PanjeaAbstractForm;

public class EditFrame<T> extends DockableFrame implements Observer, Constraint {

    public final class EditFrameDockableAdapter extends DockableFrameAdapter {
        @Override
        public void dockableFrameDocked(DockableFrameEvent arg0) {
            super.dockableFrameDocked(arg0);
            BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());
        }

        @Override
        public void dockableFrameFloating(DockableFrameEvent arg0) {
            super.dockableFrameFloating(arg0);
            BusyIndicator.showAt(Application.instance().getActiveWindow().getControl());
        }

        @Override
        public void dockableFrameHidden(DockableFrameEvent arg0) {
            super.dockableFrameHidden(arg0);
            BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());
            tableWidget.removeSelectionObserver(EditFrame.this);
        }

        @Override
        public void dockableFrameShown(DockableFrameEvent arg0) {
            super.dockableFrameShown(arg0);

            if (editMode == EEditPageMode.POPUP && !arg0.getDockableFrame().isShowing()) {
                BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());
            }
            update(null, tableWidget.getSelectedObject());
            tableWidget.addSelectionObserver(EditFrame.this);
        }
    }

    private final class EditViewToggleCommand extends JideToggleCommand {

        /**
         * Costruttore.
         *
         * @param commandId
         *            id del comando
         */
        private EditViewToggleCommand(final String commandId) {
            super(commandId);
            RcpSupport.configure(this);
            setSelected(editMode == EEditPageMode.POPUP);
        }

        @Override
        protected void onDeselection() {
            super.onDeselection();
            if (editMode != EEditPageMode.DETAIL) {
                setEditMode(EEditPageMode.DETAIL);
                viewFrame();
            }
        }

        @Override
        protected void onSelection() {
            super.onSelection();
            if (editMode != EEditPageMode.POPUP) {
                setEditMode(EEditPageMode.POPUP);
                viewFrame();
            }
        }
    }

    private final class NewEditFormCommand extends ApplicationWindowAwareCommand {
        private final IPageEditor page;

        /**
         * Costruttore. xcadasfsd
         *
         * @param commandId
         *            command id
         * @param pageEditor
         *            pagina
         */
        private NewEditFormCommand(final String commandId, final IPageEditor pageEditor) {
            super(commandId);
            this.page = pageEditor;
        }

        @Override
        protected void doExecuteCommand() {
            viewFrame();
            page.onNew();
            setCurrentPage(getTableManagedObject(page.getPageObject()));
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(page.getPageEditorId() + ".newCommand");
        }

        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(!enabled);
        }
    }

    public class UndoCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand actioncommand) {
            EditFrame.this.update(null, EditFrame.this.tableWidget.getSelectedObject());
            if (!tableWidget.getRows().isEmpty()) {
                tableWidget.getTable().requestFocusInWindow();
            }
            getQuickDefaultCommand().execute();
        }

        @Override
        public boolean preExecution(ActionCommand actioncommand) {
            return true;
        }

    }

    private static final long serialVersionUID = 5385444966542950581L;

    public static final String EDIT_FRAME_ID = "editFrame";

    private static final String EDIT_VIEW_TYPE_COMMAND = "editViewTypeCommand";

    private static final String QUICK_ACTION_GROUP = "quickActionGroup";

    public static final String QUICK_ACTION_DEFAULT = "quickActionDefault";

    public static final String QUICK_ACTION_EDIT = "quickActionEdit";

    public static final String QUICK_ACTION_INSERT = "quickActionInsert";
    public static final String DEFAULT_OBJECT_CLASS_NAME = "defaultObjectClassName";
    private JideToggleCommand editViewTypeCommand;

    protected EEditPageMode editMode;
    private final Map<String, IPageEditor> editPages;

    private IPageEditor currentEditPage;

    protected JideTableWidget<T> tableWidget;

    protected AbstractTablePageEditor<T> pageEditor;

    private AbstractQuickAction<T> quickAction;

    private ExclusiveCommandGroup commandGroup;

    private CommandFaceDescriptor editFaceDescriptor;
    private CommandFaceDescriptor insertFaceDescriptor;
    private CommandFaceDescriptor defaultFaceDescriptor;

    private EditFrameChangeSelectionConstraint changeSelectionConstraint;

    private final JPanel rootPanel = new JPanel(new CardLayout());

    private final String startQuickAction;

    private Map<String, JideToggleCommand> quickActionCommands;

    private JideToggleCommand quickDefaultCommand;

    private JideToggleCommand quickLockCommand;
    private JideToggleCommand quickInsertCommand;
    private Map<String, AbstractCommand> detailNewCommands;

    /**
     * Costruttore di default per il frame del dettaglio.
     *
     * @param editView
     *            modalità di visualizzazione
     * @param pageEditor
     *            pagina che visualizza/modifica il bean
     * @param startQuickAction
     *            azione di default
     */
    public EditFrame(final EEditPageMode editView, final AbstractTablePageEditor<T> pageEditor,
            final String startQuickAction) {
        super();
        setTabTitle("");
        setKey(EDIT_FRAME_ID);
        setInitMode(DockContext.STATE_FRAMEDOCKED);
        setInitSide(DockContext.DOCK_SIDE_SOUTH);
        setEditMode(editView);
        pageEditor.getTable().setChangeSelectionConstraint(this);
        this.editPages = pageEditor.getEditPages();
        this.currentEditPage = pageEditor.getEditPages().values().iterator().next();
        this.tableWidget = pageEditor.getTable();
        this.tableWidget.addSelectionObserver(EditFrame.this);
        this.pageEditor = pageEditor;
        setAvailableButtons(DockableFrame.BUTTON_CLOSE);
        this.setFloatable(true);
        this.startQuickAction = startQuickAction;
        init();

        setDefaultEscapeAction(ESCAPE_ACTION_DO_NOTING);
    }

    /**
     * Cambia lo stato del form ( editazione o readonly ) in base alla quick action corrente.
     */
    private void applyQuickActionEditForm() {
        if (!pageEditor.isReadOnly()) {
            quickAction.executeAction();
        }
    }

    /**
     * @return crea la selection constraint da utilizzare nell'edit frame
     */
    public EditFrameChangeSelectionConstraint createEditFrameChangeSelectionConstraint() {
        return new EditFrameChangeSelectionConstraint();
    }

    /**
     * Restituisce il command group che contiene i comandi delle quick action.
     *
     * @return command group
     */
    private CommandGroup createQuickActionsCommandGroup() {

        if (commandGroup == null) {
            commandGroup = new ExclusiveCommandGroup(QUICK_ACTION_GROUP);
            RcpSupport.configure(commandGroup);

            commandGroup.add(getQuickDefaultCommand());
            commandGroup.add(getQuickLockCommand());
            commandGroup.add(getQuickInsertCommand());

            RcpSupport.configure(getQuickDefaultCommand());
            RcpSupport.configure(getQuickLockCommand());
            RcpSupport.configure(getQuickInsertCommand());

            editFaceDescriptor = new CommandFaceDescriptor(null, getQuickLockCommand().getIcon(),
                    getQuickLockCommand().getText());
            insertFaceDescriptor = new CommandFaceDescriptor(null, getQuickInsertCommand().getIcon(),
                    getQuickInsertCommand().getText());
            defaultFaceDescriptor = new CommandFaceDescriptor(null, getQuickDefaultCommand().getIcon(),
                    getQuickDefaultCommand().getText());

            getQuickDefaultCommand().setSelected(true);
            if (QUICK_ACTION_EDIT.equals(startQuickAction)) {
                getQuickLockCommand().setSelected(true);
            }
            if (QUICK_ACTION_INSERT.equals(startQuickAction)) {
                getQuickInsertCommand().setSelected(true);
            }

            quickActionCommands = new HashMap<>();
            quickActionCommands.put(QUICK_ACTION_DEFAULT, getQuickDefaultCommand());
            quickActionCommands.put(QUICK_ACTION_EDIT, getQuickLockCommand());
            quickActionCommands.put(QUICK_ACTION_INSERT, getQuickInsertCommand());
        }

        return commandGroup;
    }

    /**
     * Esegue la dispose del frame.
     */
    @Override
    public void dispose() {

        detailNewCommands.clear();

        quickAction = null;

        for (final Entry<String, IPageEditor> entry : editPages.entrySet()) {

            final IPageEditor editPage = entry.getValue();

            editPage.removePropertyChangeListener(this.pageEditor);
        }
        editPages.clear();
        tableWidget.removeSelectionObserver(this);
        tableWidget = null;

        currentEditPage = null;
        pageEditor = null;

        super.dispose();
    }

    /**
     * @return the changeSelectionConstraint
     */
    private EditFrameChangeSelectionConstraint getChangeSelectionConstraint() {
        if (changeSelectionConstraint == null) {
            changeSelectionConstraint = createEditFrameChangeSelectionConstraint();
        }

        return changeSelectionConstraint;
    }

    /**
     * @return the currentEditPage
     */
    public IPageEditor getCurrentEditPage() {
        return currentEditPage;
    }

    protected ActionCommandInterceptor getDeleteCommandInterceptor(IPageEditor editPage) {
        return new DeleteActionCommandInterceptor<>(tableWidget, editPage, this);
    }

    /**
     *
     * @return command per aprire il detail.
     */
    public Map<String, AbstractCommand> getDetailNewCommands() {
        return detailNewCommands;
    }

    /**
     * @return the editView
     */
    public EEditPageMode getEditMode() {
        return editMode;
    }

    /**
     * Restituisce il comando per determinare la gestione della pagina di editazione. Se il pulsante sarà deselezionato
     * la pagina risulterà essere una detail, altrimenti sarà una popup.
     *
     * @return comando
     */
    public JideToggleCommand getEditViewTypeCommand() {
        if (editViewTypeCommand == null) {
            editViewTypeCommand = new EditViewToggleCommand(EDIT_VIEW_TYPE_COMMAND);
        }

        return editViewTypeCommand;
    }

    /**
     * @return getter of quickAction
     */
    public AbstractQuickAction<T> getQuickAction() {
        return quickAction;
    }

    /**
     *
     * @param commandKey
     *            ket del comando
     * @return command per selezionare le quickAction
     */
    public JideToggleCommand getQuickActionCommand(String commandKey) {
        return quickActionCommands.get(commandKey);
    }

    /**
     *
     * @return command che ritorna la quickAction di default.
     */
    public JideToggleCommand getQuickDefaultCommand() {
        if (quickDefaultCommand == null) {
            quickDefaultCommand = new JideToggleCommand(QUICK_ACTION_DEFAULT) {
                @Override
                protected void onSelection() {
                    super.onSelection();
                    quickAction = new DefaultQuickAction<>(tableWidget, EditFrame.this);
                    commandGroup.setFaceDescriptor(defaultFaceDescriptor);
                    applyQuickActionEditForm();
                }
            };
        }

        return quickDefaultCommand;
    }

    /**
     *
     * @return comando per inserimento veloce
     */
    public JideToggleCommand getQuickInsertCommand() {
        if (quickInsertCommand == null) {
            quickInsertCommand = new JideToggleCommand(QUICK_ACTION_INSERT) {
                @Override
                protected void onSelection() {
                    super.onSelection();
                    quickAction = new InsertQuickAction<>(tableWidget, EditFrame.this);
                    commandGroup.setFaceDescriptor(insertFaceDescriptor);
                    applyQuickActionEditForm();
                }
            };
        }

        return quickInsertCommand;
    }

    /**
     *
     * @return @return comando per modifica veloce
     */
    public JideToggleCommand getQuickLockCommand() {
        if (quickLockCommand == null) {
            quickLockCommand = new JideToggleCommand(QUICK_ACTION_EDIT) {
                @Override
                protected void onSelection() {
                    super.onSelection();
                    quickAction = new LockQuickAction<>(tableWidget, EditFrame.this);
                    commandGroup.setFaceDescriptor(editFaceDescriptor);
                    applyQuickActionEditForm();
                }
            };
        }

        return quickLockCommand;
    }

    /**
     *
     * @param editPage
     *            editPage con il command da associare
     * @return interceptor per l'azione save
     */
    protected ActionCommandInterceptor getSaveCommandInterceptor(IPageEditor editPage) {
        return new SaveActionCommandInterceptor<>(tableWidget, editPage, this);
    }

    /**
     *
     * @param object
     *            oggetto gestito dal form
     * @return oggetto gestito dalla tabella
     */
    @SuppressWarnings("unchecked")
    public T getTableManagedObject(Object object) {
        return (T) object;
    }

    /**
     * nasconde il frame
     */
    public void hideFrame() {
        getDockingManager().hideFrame(getKey());
    }

    /**
     * Inizializza il frame.
     */
    protected void init() {

        addDockableFrameListener(new EditFrameDockableAdapter());

        List<AbstractCommand> commandEditPageGroup = new ArrayList<>();
        detailNewCommands = new HashMap<>();

        if (!editPages.containsKey(DEFAULT_OBJECT_CLASS_NAME)) {

            for (final Entry<String, AbstractCommand> command : pageEditor.getNewCommands().entrySet()) {

                @SuppressWarnings("rawtypes")
                final AbstractCommand detailNewCommand = new NewEditFormCommand(command.getValue().getId(),
                        ((NewCommand) command.getValue()).getPage());
                RcpSupport.configure(detailNewCommand);
                detailNewCommands.put(command.getKey(), detailNewCommand);
            }
        }

        for (final Entry<String, IPageEditor> entry : editPages.entrySet()) {

            final IPageEditor editPage = entry.getValue();
            editPage.addPropertyChangeListener(this.pageEditor);

            if (!detailNewCommands.values().isEmpty()) {
                editPage.getEditorNewCommand().setVisible(false);
                if (editPage instanceof IToolbarPageCommands) {
                    ((IToolbarPageCommands) editPage)
                            .setExternalCommandStart(detailNewCommands.values().toArray(new AbstractCommand[0]));
                }
            }

            if (editPage instanceof IToolbarPageCommands) {
                // aggiungo i comandi delle quick action
                commandEditPageGroup.add(createQuickActionsCommandGroup());
                // aggiungo il tipo di vista dell'edit frame
                commandEditPageGroup.add(getEditViewTypeCommand());
                // aggiungo i comandi di navigazione
                for (final AbstractCommand navCommand : tableWidget.getNavigationCommands()) {
                    commandEditPageGroup.add(navCommand);
                }
                ((IToolbarPageCommands) editPage).setExternalCommandLineEnd(
                        commandEditPageGroup.toArray(new AbstractCommand[commandEditPageGroup.size()]));
            }

            commandEditPageGroup = new ArrayList<>();

            ((ActionCommand) editPage.getEditorSaveCommand())
                    .addCommandInterceptor(getSaveCommandInterceptor(editPage));

            ((ActionCommand) editPage.getEditorDeleteCommand())
                    .addCommandInterceptor(getDeleteCommandInterceptor(editPage));

            ((ActionCommand) editPage.getEditorUndoCommand()).addCommandInterceptor(new UndoCommandInterceptor());

            editPage.getEditorLockCommand().setEnabled(false);
            editPage.getEditorUndoCommand().setEnabled(false);
            editPage.getEditorDeleteCommand().setEnabled(false);

            rootPanel.add(((DialogPage) editPage).getControl(), entry.getKey());
        }

        getContentPane().add(rootPanel);
        this.setPreferredSize(rootPanel.getPreferredSize());
        getContentPane().setPreferredSize(rootPanel.getPreferredSize());

        tableWidget.addSelectionObserver(this);
        this.setDefaultCloseAction(CLOSE_ACTION_TO_HIDE);
    }

    /**
     * Setta la pagina attiva in base all'oggetto ricevuto.
     *
     * @param object
     *            oggetto gestito dalla pagina
     */
    public void setCurrentPage(Object object) {
        if (editPages.containsKey(DEFAULT_OBJECT_CLASS_NAME)) {
            currentEditPage = editPages.get(DEFAULT_OBJECT_CLASS_NAME);
        } else {
            if (editPages.get(object.getClass().getName()) != null) {
                currentEditPage = editPages.get(object.getClass().getName());
                final CardLayout cl = (CardLayout) (rootPanel.getLayout());
                cl.show(rootPanel, object.getClass().getName());
            }
        }
    }

    /**
     * @param paramEditMode
     *            the paramEditMode to set
     */
    public void setEditMode(EEditPageMode paramEditMode) {
        setEditMode(paramEditMode, null);
    }

    /**
     * @param paramEditMode
     *            the paramEditMode to set
     * @param source
     *            se avvalorato significa che è stato il toggle command per la modifica della vista a chiamare il
     *            metodo, quindi non aggiorno il suo stato.
     */
    public void setEditMode(EEditPageMode paramEditMode, EditViewToggleCommand source) {
        this.editMode = paramEditMode;
        setAvailable(true);

        if (source == null) {
            // il cambio di editMode non è avvenuto dal pulsante.
            // sincronizzo lo stato del pulsante per il cambio di editMode
            getEditViewTypeCommand().setSelected(paramEditMode == EEditPageMode.POPUP);
        }

        setShowTitleBar(this.editMode == EEditPageMode.POPUP);

        switch (this.editMode) {
        case POPUP:
            setDefaultEscapeAction(DockableFrame.ESCAPE_ACTION_TO_HIDE);
            getContext().setCurrentMode(DockContext.MODE_FLOATABLE);
            int positionx;
            int positiony;
            final Container mainWindow = Application.instance().getActiveWindow().getControl();
            final Point topLeft = mainWindow.getLocationOnScreen();
            final Dimension parentSize = mainWindow.getSize();
            final Dimension mySize = getPreferredSize();

            if (parentSize.width > mySize.width) {
                positionx = ((parentSize.width - mySize.width) / 2) + topLeft.x;
            } else {
                positionx = topLeft.x;
            }

            if (parentSize.height > mySize.height) {
                positiony = ((parentSize.height - mySize.height) / 2) + topLeft.y;
            } else {
                positiony = topLeft.y;
            }

            final Rectangle rectangle = new java.awt.Rectangle(getContentPane().getPreferredSize());
            rectangle.setLocation(positionx, positiony);
            rectangle.height = getContentPane().getPreferredSize().height + 50;
            rectangle.width = getContentPane().getPreferredSize().width + 50;
            if (getDockingManager() != null) {
                setVisible(true);
                getDockingManager().floatFrame(getKey(), rectangle, true);
            }
            break;
        case DETAIL:
            setDefaultEscapeAction(DockableFrame.ESCAPE_ACTION_DO_NOTING);
            if (getDockingManager() != null) {
                getDockingManager().dockFrame(getKey(), DockContext.DOCK_SIDE_SOUTH, 0);
            }
            break;
        default:
            throw new UnsupportedOperationException();
        }
    }

    /**
     * @param quickAction
     *            setter of quickAction
     */
    public void setQuickAction(AbstractQuickAction<T> quickAction) {
        this.quickAction = quickAction;
    }

    @Override
    public boolean shouldVetoHiding() {
        if (getCurrentEditPage().getEditorUndoCommand().isEnabled()) {
            // Se sono in modifica faccio uin UNDO per rimuovere il lock
            // sull'oggetto
            getCurrentEditPage().getEditorUndoCommand().execute();
        }
        return super.shouldVetoHiding();
    }

    @Override
    public boolean test(Object arg0) {
        // if (!getCurrentEditPage().isDirty()) {
        // getCurrentEditPage().getForm().getFormModel().setReadOnly(true);
        // return true;
        // } else {
        boolean changeSel = true;
        if (getCurrentEditPage().isDirty()) {
            changeSel = getChangeSelectionConstraint().test(getCurrentEditPage());

            if (changeSel) {
                // getCurrentEditPage().getForm().getFormModel().setReadOnly(true);
                final AbstractQuickAction<T> oldQuickAction = getQuickAction();

                setQuickAction(new DefaultQuickAction<>(tableWidget, this));
                getCurrentEditPage().getEditorSaveCommand().execute();
                setQuickAction(oldQuickAction);
            }
        }
        return changeSel;
    }

    @Override
    public void update(Observable observable, Object obj) {
        Object formObject = null;
        if (obj != null) {
            setCurrentPage(obj);
            formObject = PanjeaSwingUtil.cloneObject(obj);
        } else {
            if (getCurrentEditPage() instanceof FormBackedDialogPage) {
                ((PanjeaAbstractForm) ((FormBackedDialogPage) getCurrentEditPage()).getBackingFormPage())
                        .getNewFormObjectCommand().execute();
            }
            formObject = getCurrentEditPage().getPageObject();
        }

        getCurrentEditPage().preSetFormObject(formObject);
        getCurrentEditPage().setFormObject(formObject);
        getCurrentEditPage().postSetFormObject(formObject);

        pageEditor.updateCommandsForReadOnly();
        if (obj == null && getCurrentEditPage() instanceof FormBackedDialogPage) {
            ((PanjeaAbstractForm) ((FormBackedDialogPage) getCurrentEditPage()).getBackingFormPage()).getFormModel()
                    .setReadOnly(true);
        }
    }

    /**
     * visualizza il frame.
     */
    public void viewFrame() {
        getDockingManager().showFrame(getKey(), true);
    }
}
