package it.eurotn.rich.editors;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.config.CommandFaceDescriptor;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.support.DialogPageUtils;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.PopupMenuMouseListener;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.StyledLabelBuilder;

import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.control.table.CopyCommand;
import it.eurotn.rich.control.table.PrintCommand;
import it.eurotn.rich.control.table.SelectAllCommand;
import it.eurotn.rich.settings.support.PanjeaTableMemento;

/**
 * Classe che gestisce i dati in una TreeTable.
 *
 * @author giangi,leonardo
 */
public abstract class AbstractTreeTableDialogPageEditor extends AbstractDialogPage
        implements IPageLifecycleAdvisor, IEditorCommands, Focussable {

    private class DeleteCommand extends ApplicationWindowAwareCommand {

        private final String pageId;

        /**
         * Costruttore.
         *
         * @param pageId
         *            pageId
         */
        public DeleteCommand(final String pageId) {
            super(DELETE_COMMAND);
            this.pageId = pageId;
            setEnabled(false);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            AbstractTreeTableDialogPageEditor.this.onDelete();
        }

        @Override
        public String getSecurityControllerId() {
            return pageId + ".controller";
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(pageId + "." + DELETE_COMMAND);
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
                getTreeTable().expandAll();
                setFaceDescriptor(expandDescriptor);
            } else {
                getTreeTable().collapseAll();
                setFaceDescriptor(collapseDescriptor);
            }
            this.expandTree = !expandTree;
        }
    }

    /**
     * Key Listener per associare all'invio l'operazione chiamata dal metodo openNode().
     *
     * @author Leonardo
     */
    private class OpenOnEnterKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                openNode();
            }
        }
    }

    /**
     * Comando che apre il nodo selezionato nella treetable.
     *
     * @author fattazzo
     *
     */
    protected class PropertyCommand extends ApplicationWindowAwareCommand {

        private final String pageId;

        /**
         * Costruttore.
         *
         * @param pageId
         *            pageId
         */
        public PropertyCommand(final String pageId) {
            super(PROPERTY_COMMAND);
            this.pageId = pageId;
            setEnabled(false);
            RcpSupport.configure(this);
        }

        @Override
        public void doExecuteCommand() {
            AbstractTreeTableDialogPageEditor.this.openNode();
        }

        @Override
        public String getSecurityControllerId() {
            return pageId + ".controller";
        }
    }

    /**
     * Listener per creare il context popup per la treetable.
     *
     * @author Leonardo
     */
    private class TreePopupMouseListener extends PopupMenuMouseListener {

        @Override
        protected JPopupMenu getPopupMenu() {
            return createPopupContextMenu();
        }
    }

    /**
     * List selection listener che valuta la selezione recuperando il node selezionato e passandolo
     * al metodo selection changed.
     *
     * @author Leonardo
     */
    protected class TreeTableListSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent event) {
            LOGGER.debug("--> selezionato path " + event.getPath());
            DefaultMutableTreeTableNode node = null;
            TreePath selPath = event.getPath();
            // controllo per eventi di selezione su cambio contenuto tabella
            if (selPath == null) {
                AbstractTreeTableDialogPageEditor.LOGGER.debug("--> nulla di selezionato");
                getDeleteCommand().setEnabled(false);
            } else {
                node = (DefaultMutableTreeTableNode) selPath.getLastPathComponent();
                getDeleteCommand().setEnabled(node.getUserObject() != null);
                if (node.getUserObject() != null) {
                    AbstractTreeTableDialogPageEditor.LOGGER
                            .debug("--> nodo selezionato tipo " + node.getUserObject().getClass().getName());
                }
            }
            getPropertyCommand().setEnabled(node != null && AbstractTreeTableDialogPageEditor.this.isEnable());
            getDeleteCommand().setEnabled(node != null && AbstractTreeTableDialogPageEditor.this.isEnable());
            selectionChanged(node);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(AbstractTreeTableDialogPageEditor.class);

    private static final String PROPERTY_COMMAND = "propertyCommand";

    private static final String DELETE_COMMAND = "deleteCommand";

    private String pageId = "";

    protected JXTreeTable treeTable;
    // commands
    private CommandGroup tableCommandGroup;
    private SelectAllCommand selectAllCommand;
    private CopyCommand copyCommand;
    private PrintCommand printCommand;
    private ExpandCommand expandCommand;

    private PropertyCommand propertyCommand = null;
    private DeleteCommand deleteCommand = null;
    private AbstractCommand[] commandBar;

    private boolean enable = true;
    /**
     * Valore booleano che indica se includere la titlePane nella creazione dei controlli.
     */
    private boolean showTitlePane = true;
    private OpenOnEnterKeyListener openOnEnterKeyListener;
    private TreePopupMouseListener treePopupMouseListener;
    private TreeTableListSelectionListener treeTableListSelectionListener;
    private final LockableUI lockableUI = new LockableUI();

    private DefaultOverlayable overlayTable;

    /**
     * Costruttore.
     *
     * @param pageId
     *            pageId
     */
    public AbstractTreeTableDialogPageEditor(final String pageId) {
        super(pageId, true);
        this.pageId = pageId;
        lockableUI.setLockedCursor(Cursor.getDefaultCursor());
    }

    /**
     * Aggiunge un AbstractHighlighter alla JXTable, metodo di accesso alla JXTable.
     *
     * @param highlighter
     *            l'highlighter da aggiungere alla tabella.
     */
    protected void addHighlighter(AbstractHighlighter highlighter) {
        (getTreeTable()).addHighlighter(highlighter);
    }

    /**
     * Metodo da implementare nelle classi derivate per accedere e configurare la tree table.
     *
     * @param treeTableParam
     *            la tree da configurare
     */
    protected void configureTreeTable(JXTreeTable treeTableParam) {
    }

    /**
     * Restituisce il pannello in cui vengono aggiunti i commands in calce alla tabella.
     *
     * @return JPanel
     */
    protected JPanel createButtonPanel() {
        JPanel buttonPanelComponent = getComponentFactory().createPanel(new BorderLayout());
        if (commandBar != null) {
            buttonPanelComponent.add(getButtonBar(), BorderLayout.LINE_END);
        }
        return buttonPanelComponent;
    }

    @Override
    protected JComponent createControl() {
        // pannello principale
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder());
        commandBar = getCommand(); // Inizializzo i comandi da inserire nella
        // buttonBar

        getObjectConfigurer().configure(this, pageId);

        // schema pannelli seguenti
        // ____________________
        // |____HEAD TITLE_____| north
        // |____HEAD CONTROL |
        // | |
        // | TREETABLE | center
        // |______________________|
        // |____CUSTOM CTRL__| \ / north
        // |____FILTER CTRL____| | south | \ south _ north -- west
        // |____BUTTON CTRL__| / \ / south -- east

        // head title aggiunto sul pannello principale a north
        if (!StringUtils.isBlank(getTitle()) && (showTitlePane)) {
            panel.add(DialogPageUtils.createTitlePane(this).getControl(), BorderLayout.PAGE_START);
        }

        // table aggiunta al pannello principale a center
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBorder(BorderFactory.createEmptyBorder());

        // se ci sono gli head control li metto sopra la tabella
        if (getHeadControl() != null) {
            panelTable.add(getHeadControl(), BorderLayout.PAGE_START);
        }

        panelTable.add(createTreeTable(), BorderLayout.CENTER);

        overlayTable = new DefaultOverlayable(panelTable);

        // se ho un messaggio aggiungo l'overlay alla tabella
        if (getOverlayMessage() != null) {
            // e aggiungo il messaggio
            overlayTable
                    .addOverlayComponent(StyledLabelBuilder.createStyledLabel("{" + getOverlayMessage() + ":f:gray}"));
        }

        panel.add(overlayTable, BorderLayout.CENTER);

        // pannello south aggiunto a south successivamente
        JPanel panelSouth = getComponentFactory().createPanel(new BorderLayout());

        // pannello custom control aggiunto a north del panel south
        if (getCustomControl() != null) {
            panelSouth.add(getCustomControl(), BorderLayout.PAGE_START);
        }

        // pannello per inserire filter control e button control a south del
        // panel south

        panelSouth.add(createButtonPanel(), BorderLayout.PAGE_END);

        panel.add(panelSouth, BorderLayout.PAGE_END);
        panel.setPreferredSize(new Dimension(100, 150));
        JPanel dummy = new JPanel(new BorderLayout());
        dummy.setBorder(BorderFactory.createEmptyBorder());
        dummy.add(panel, BorderLayout.CENTER);
        return dummy;
    }

    /**
     * Create the context popup menu, if any, for this table. The default operation is to create the
     * popup from the command group if one has been specified. If not, then null is returned.
     *
     * @return popup menu to show, or null if none
     */
    protected JPopupMenu createPopupContextMenu() {
        return getTableCommandGroup().createPopupMenu();
    }

    /**
     * Creo la TreeTable di default contenuta in uno scroll pane.
     *
     * @return tabella creata in uno scroll pane
     */
    private JComponent createTreeTable() {
        LOGGER.debug("--> Enter createTreeTable");
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder());
        if (treeTable == null) {
            treeTable = (JXTreeTable) ((PanjeaComponentFactory) getComponentFactory()).createTreeTable();

            (treeTable).getActionMap().remove("find");
            treeTable.setColumnControlVisible(true);
            treeTable.setAutoscrolls(true);
            treeTable.setName(getId() + "TableWidget");

            treeTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            treeTable.setTreeTableModel(createTreeTableModel());

            // quando viene attivato un structure changed,serve per evitare che
            // vengano ricreate le
            // colonne di default per il table model e quindi vanificare il
            // memento per colonne visualizzate
            treeTable.setAutoCreateColumnsFromModel(false);

            // supporto per double click
            treeTable.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent event) {
                    if ((event.getButton() == MouseEvent.BUTTON1) && (event.getClickCount() == 2)) {
                        openNode();
                    }
                }
            });
            openOnEnterKeyListener = new OpenOnEnterKeyListener();
            treeTable.addKeyListener(openOnEnterKeyListener);

            treePopupMouseListener = new TreePopupMouseListener();
            // Add the context menu listener
            treeTable.addMouseListener(treePopupMouseListener);
        }
        TreeCellRenderer cellRenderer = getTreeCellRender();
        if (cellRenderer != null) {
            treeTable.setTreeCellRenderer(cellRenderer);
        }

        treeTable.addTreeSelectionListener(getTreeListSelectionListener());

        configureTreeTable(treeTable);

        JScrollPane scrollPane = getComponentFactory().createScrollPane(treeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        rootPanel.add(scrollPane, BorderLayout.CENTER);
        return rootPanel;
    }

    /**
     * Metodo da definire per creare il treeTableModel.
     *
     * @return TreeTableModel
     */
    protected abstract TreeTableModel createTreeTableModel();

    /**
     * Rimuove dal model della treeTable il node ricevuto come parametro e seleziona il nodo
     * precedente o successivo.
     *
     * @param node
     *            il node da rimuovere
     */
    public void deleteNode(DefaultMutableTreeTableNode node) {
        DefaultTreeTableModel myModel = (DefaultTreeTableModel) this.getTreeTable().getTreeTableModel();
        DefaultMutableTreeTableNode parentNode = (DefaultMutableTreeTableNode) node.getParent();
        int currIdx = -1;
        if (parentNode != null) {
            currIdx = myModel.getIndexOfChild(parentNode, node);
        }
        TreeTableNode nextToBeSelected = null;
        if (parentNode != null && parentNode.getChildCount() > currIdx) {
            nextToBeSelected = parentNode.getChildAt(currIdx);
        }
        if (nextToBeSelected != null && currIdx > 0) {
            nextToBeSelected = parentNode.getChildAt(currIdx - 1);
        } else {
            nextToBeSelected = node.getParent();
        }
        myModel.removeNodeFromParent(node);
        getTreeTable().getTreeSelectionModel().setSelectionPath(new TreePath(myModel.getPathToRoot(nextToBeSelected)));
    }

    /**
     * Cancella il nodo selezionato.
     */
    public void deleteSelectedNode() {
        if (getTreeTable() != null && getTreeTable().getTreeSelectionModel() != null) {
            TreePath selPath = getTreeTable().getTreeSelectionModel().getSelectionPath();
            if (selPath != null) {
                DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) selPath.getLastPathComponent();
                deleteNode(node);
                return;
            }
        }
        LOGGER.warn("--> Nessun nodo selezionato da eliminare");
    }

    /**
     * A causa di problemi di memory leak sulla tree (model, view, nodes, ecc...) rimuovo listeners,
     * reinizializzo vuoto il model,sulla classe derivata mi devo preoccupare di rimuovere il
     * vincolo gerarchico degli elementi della tree, il tutto per rilasciare gli oggetti del tree al
     * GarbageCollector.
     */
    @Override
    public void dispose() {

        if (treeTable != null) {
            treeTable.removeKeyListener(openOnEnterKeyListener);
            treeTable.removeTreeSelectionListener(getTreeListSelectionListener());
            treeTable.removeMouseListener(treePopupMouseListener);

            ((DefaultTreeTableModel) treeTable.getTreeTableModel()).setRoot(new DefaultMutableTreeTableNode());
            treeTable.setTreeTableModel(new DefaultTreeTableModel());

            expandCommand = null;
        }
    }

    /**
     * Do delete.
     *
     * @param node
     *            node
     * @return boolean
     */
    protected boolean doDelete(DefaultMutableTreeTableNode node) {
        return false;
    }

    /**
     * Abilita o disabilita tutti i controlli della pagina.
     *
     * @param enabled
     *            <code>true</code> per abilitare i controlli, <code>false</code> altrimenti
     */
    public void enable(boolean enabled) {
        if (commandBar != null) {
            for (AbstractCommand element : commandBar) {
                element.setEnabled(enabled);
            }
        }
        this.enable = enabled;
    }

    /**
     * Button bar dove vengono aggiunti i commands come ad esempio espandi, nuovo, salva, ecc. Per
     * aggiungere commands nella button bar devo sovrascrivere sulla classe derivata getCommand()
     *
     * @return la button bar o null se getCommand ritorna null.
     */
    public JComponent getButtonBar() {
        if (commandBar != null) {
            JComponent panel = ((JECCommandGroup) getCommandGroup()).createToolBar();
            panel.setBorder(BorderFactory.createEmptyBorder());
            return panel;
        } else {
            return null;
        }
    }

    /**
     * I comandi che vengono aggiunti al command group verranno visualizzati come buttonBar sotto la
     * tablella.
     *
     * @return commandGroup dei comandi
     */
    public abstract AbstractCommand[] getCommand();

    /**
     * Se vengono aggiunti dei command viene creato il commandGroup da aggiungere alla button bar.
     *
     * @return CommandGroup
     */
    private CommandGroup getCommandGroup() {
        JECCommandGroup commandGroup = new JECCommandGroup();
        if (commandBar != null) {
            for (AbstractCommand element : commandBar) {
                commandGroup.add(element);
            }
        } else {
            commandGroup = null;
        }
        return commandGroup;
    }

    /**
     * @return CopyCommand
     */
    private ActionCommand getCopyCommand() {
        if (copyCommand == null) {
            copyCommand = new CopyCommand(getTreeTable());
        }
        return copyCommand;
    }

    /**
     * Metodo da sovrascrivere nel caso in cui si debba aver bisogno di mettere dei controlli tra la
     * tabella e i pulsanti.
     *
     * @return controls
     *
     */
    public JComponent getCustomControl() {
        return null;
    }

    /**
     * @return delete command
     */
    public ApplicationWindowAwareCommand getDeleteCommand() {
        if (deleteCommand == null) {
            deleteCommand = new DeleteCommand(pageId);
        }
        return deleteCommand;
    }

    /*
     * Default non registro nessun comando.
     */
    @Override
    public AbstractCommand getEditorDeleteCommand() {
        return null;
    }

    /*
     * Default non registro nessun comando.
     */
    @Override
    public AbstractCommand getEditorLockCommand() {
        return null;
    }

    /*
     * Default non registro nessun comando.
     */
    @Override
    public AbstractCommand getEditorNewCommand() {
        return null;
    }

    /*
     * Default non registro nessun comando.
     */
    @Override
    public AbstractCommand getEditorSaveCommand() {
        return null;
    }

    /*
     * Default non registro nessun comando.
     */
    @Override
    public AbstractCommand getEditorUndoCommand() {
        return null;
    }

    /**
     * Command per chiamare la expand/collapse sulla treeTable.
     *
     * @return AbstractCommand
     */
    public AbstractCommand getExpandCommand() {
        if (expandCommand == null) {
            expandCommand = new ExpandCommand();
        }
        return expandCommand;
    }

    /**
     * Metodo da sovrascrivere nel caso in cui si debba aver bisogno di mettere dei controlli sopra
     * la tabella.
     *
     * @return head control
     */
    public JComponent getHeadControl() {
        return null;
    }

    /**
     * Imposta la colonna di default su cui eseguire il filtro di evidenziazione delle righe che
     * hanno un riscontro sul pattern specificato.
     *
     * @return il valore di default ? la colonna selezionata oppure la 0 se non c'? nessuna colonna.
     */
    protected int getHighlightFilterColumn() {
        return getTreeTable().getSelectedColumn() != -1 ? getTreeTable().getSelectedColumn() : 0;
    }

    /**
     * Se ritorna un valore diverso da null viene stampato il messaggio <br/>
     * sopra i componenti della pagina quando la tabella non ha dati.<br/>
     * <b>NB:</b> il messaggio deve essere internazionalizzato
     *
     * @return Il messaggio da aggiungere come overlay alla tabella
     */
    public String getOverlayMessage() {
        return null;
    }

    private ActionCommand getPrintCommand() {
        if (printCommand == null) {
            printCommand = new PrintCommand(getTreeTable());
        }
        return printCommand;
    }

    /**
     * @return property command
     */
    public ApplicationWindowAwareCommand getPropertyCommand() {
        if (propertyCommand == null) {
            propertyCommand = new PropertyCommand(pageId);
        }
        return propertyCommand;
    }

    private ActionCommand getSelectAllCommand() {
        if (selectAllCommand == null) {
            selectAllCommand = new SelectAllCommand(getTreeTable());
        }
        return selectAllCommand;
    }

    /**
     * Recupera dalla selezione sulla tabella il nodo corrente o null se non è attiva.
     *
     * @return DefaultMutableTreeTableNode
     */
    protected DefaultMutableTreeTableNode getSelectedNode() {
        LOGGER.debug("--> Enter getSelectedNode");
        DefaultMutableTreeTableNode node = null;
        if (getTreeTable().getSelectionModel().getMinSelectionIndex() != -1) {
            TreePath treePath = getTreeTable().getPathForRow(getTreeTable().getSelectedRow());

            node = (DefaultMutableTreeTableNode) treePath.getLastPathComponent();
        }
        LOGGER.debug("--> Exit getSelectedNode");
        return node;
    }

    /**
     * Recupera dalla selezione sulla tabella tutti i nodi selezionati.
     *
     * @return List<DefaultMutableTreeTableNode>
     */
    protected List<DefaultMutableTreeTableNode> getSelectedNodes() {
        LOGGER.debug("--> Enter getSelectedNodes");
        List<DefaultMutableTreeTableNode> nodes = new ArrayList<DefaultMutableTreeTableNode>();
        int selectedRowsNumber = getTreeTable().getSelectedRowCount();
        if (selectedRowsNumber > 0) {
            int[] selRows = getTreeTable().getSelectedRows();
            for (int selRow : selRows) {
                TreePath treePath = getTreeTable().getPathForRow(selRow);
                if (treePath != null) {
                    nodes.add((DefaultMutableTreeTableNode) treePath.getLastPathComponent());
                }
            }
        }
        LOGGER.debug("--> Exit getSelectedNodes " + nodes.size());
        return nodes;
    }

    private CommandGroup getTableCommandGroup() {
        if (tableCommandGroup == null) {

            if (getCommandGroup() != null) {
                tableCommandGroup = getCommandGroup();
            } else {
                tableCommandGroup = new CommandGroup();
            }

            // aggiungo i pulsanti di gestione della tabella
            tableCommandGroup.addSeparator();
            tableCommandGroup.add(getSelectAllCommand());
            tableCommandGroup.add(getCopyCommand());
            tableCommandGroup.add(getPrintCommand());
        }
        return tableCommandGroup;
    }

    /**
     * Rendered per le colonne tabella, per definire tipo in rapporto alla relativa visualizzazione.
     *
     * @return TreeCellRenderer
     */
    protected abstract TreeCellRenderer getTreeCellRender();

    protected TreeTableListSelectionListener getTreeListSelectionListener() {
        if (treeTableListSelectionListener == null) {
            treeTableListSelectionListener = new TreeTableListSelectionListener();
        }
        return treeTableListSelectionListener;
    }

    /**
     * @return tree table
     */
    public JXTreeTable getTreeTable() {
        return treeTable;
    }

    /**
     * @see it.eurotn.rich.editors.IPageLifecycleAdvisor#componentFocusGained()
     */
    @Override
    public void grabFocus() {
        LOGGER.debug("---> Enter componentFocusGained per treeTableDialogPage " + getId());
        if (getTreeTable() != null) {
            if (getTreeTable().getRowCount() > 0) {
                getTreeTable().requestFocusInWindow();
            } else {
                getTreeTable().getTableHeader().requestFocusInWindow();
            }
        }
    }

    /**
     * @return is enable
     */
    public boolean isEnable() {
        return this.enable;
    }

    /**
     * Blocca o sbocca la pagina impedendone qualsiasi operazione.
     *
     * @param lock
     *            lock
     */
    public void lockPage(boolean lock) {
        lockableUI.setLocked(lock);
    }

    /**
     * @return deleted object
     */
    public Object onDelete() {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(
                getMessage("abstractObjectTable.delete.confirm.title"),
                getMessage("abstractObjectTable.delete.confirm.message")) {
            @Override
            protected void onConfirm() {
                TreePath[] selPaths = getTreeTable().getTreeSelectionModel().getSelectionPaths();
                if (selPaths != null) {
                    // sel path può arrivare null
                    for (TreePath selPath : selPaths) {
                        if (selPath != null) {
                            DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) selPath
                                    .getLastPathComponent();
                            if (doDelete(node)) {
                                deleteNode(node);
                                publishApplicationEvent(LifecycleApplicationEvent.DELETED, node.getUserObject());
                            }
                        }
                    }
                }
            }
        };
        confirmationDialog.setPreferredSize(new Dimension(250, 50));
        confirmationDialog.setResizable(false);
        confirmationDialog.showDialog();
        return null;
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    /**
     * Metodo di comodo per chiamare openSelectedNode solo se la selezione e' presente.
     */
    protected void openNode() {
        LOGGER.debug("--> Enter openNode");
        DefaultMutableTreeTableNode node = getSelectedNode();
        if (node != null) {
            openSelectedNode(node);
        }
        LOGGER.debug("--> Exit openNode");
    }

    /**
     * Metodo chiamato sul double click e sull'enter da implementare per l'esecuzione di modifica
     * del nodo selezionato che viene passato come parametro del metodo.
     *
     * @param node
     *            l'oggetto selezionato sulla treetable
     */
    protected abstract void openSelectedNode(DefaultMutableTreeTableNode node);

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    protected void publishApplicationEvent(String evt, Object obj) {
        PanjeaLifecycleApplicationEvent event = new PanjeaLifecycleApplicationEvent(evt, obj);
        Application.instance().getApplicationContext().publishEvent(event);
    }

    /**
     * Rimuove un AbstractHighlighter alla JXTable, metodo di accesso alla JXTable.
     *
     * @param highlighter
     *            l'highlighter da rimuovere dalla tabella.
     */
    protected void removeHighlighter(AbstractHighlighter highlighter) {
        (getTreeTable()).removeHighlighter(highlighter);
    }

    @Override
    public void restoreState(Settings settings) {
        LOGGER.debug("--> Enter restoreState ripristina le informazioni della treeTable " + this.pageId + ".treeTable");
        PanjeaTableMemento tableMemento = new PanjeaTableMemento(getTreeTable(), this.pageId + ".treeTable");
        tableMemento.restoreState(settings);
        LOGGER.debug("--> Exit restoreState informazioni ripristinate della treeTable " + this.pageId + ".treeTable");
    }

    @Override
    public void saveState(Settings settings) {
        LOGGER.debug("--> Enter saveState salva le informazioni della treeTable " + this.pageId + ".treeTable");
        PanjeaTableMemento tableMemento = new PanjeaTableMemento(getTreeTable(), this.pageId + ".treeTable");
        tableMemento.saveState(settings);
        LOGGER.debug("--> Exit saveState informazioni salvate della treeTable " + this.pageId + ".treeTable");
    }

    /**
     * Metodo chiamato sul cambio della selezione della tree table, viene passato il node che e'
     * l'oggetto selezionato della tree table o null se non c'e' selezione.
     *
     * @param node
     *            il node selezionato o null
     */
    protected abstract void selectionChanged(DefaultMutableTreeTableNode node);

    /**
     * @param visible
     *            set overlay visible/invisible
     */
    public void setOverlayVisible(boolean visible) {
        if (getOverlayMessage() != null) {
            overlayTable.setOverlayVisible(visible);
        }
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Imposta la selezione alla riga specificata.
     *
     * @param row
     *            row
     */
    protected void setSelectedNode(int row) {
        LOGGER.debug("--> Enter setSelectedNode");
        ListSelectionModel listSelectionModel = getTreeTable().getSelectionModel();
        listSelectionModel.setAnchorSelectionIndex(row);
        LOGGER.debug("--> Exit setSelectedNode");
    }

    /**
     * @param showTitlePane
     *            The showTitlePane to set.
     */
    public void setShowTitlePane(boolean showTitlePane) {
        this.showTitlePane = showTitlePane;
    }

    /**
     * Metodo per settare il modello dei dati per il treeTable.
     *
     * @param root
     *            il root node da assegnare alla treeTable
     */
    public void setTableData(TreeTableNode root) {
        ((DefaultTreeTableModel) treeTable.getTreeTableModel()).setRoot(root);
    }
}
