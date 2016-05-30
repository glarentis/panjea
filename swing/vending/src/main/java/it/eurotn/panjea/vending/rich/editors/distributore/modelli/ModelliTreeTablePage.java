package it.eurotn.panjea.vending.rich.editors.distributore.modelli;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.entity.EntityBase;
import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.panjea.vending.rich.editors.distributore.DistributorePM;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.editors.AbstractTreeTableDialogPageEditor;
import it.eurotn.rich.editors.IPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * @author fattazzo
 */
public class ModelliTreeTablePage extends AbstractTreeTableDialogPageEditor implements PropertyChangeListener {

    public static final String PAGE_ID = "modelliTreeTablePage";

    public static final String MODELLO_ROOT_NODE_LABEL = PAGE_ID + ".rootNode.label";
    public static final String MODLLO_ROOT_NODE_ICON = PAGE_ID + ".rootNode.icon";

    protected IVendingAnagraficaBD vendingAnagraficaBD;

    protected AbstractCommand[] abstractCommands;
    private RefreshModelliCommand refreshModelliCommand;

    private IPageEditor modelloPage;
    private IPageEditor tipoModelloPage;

    private JECCommandGroup nuovoCommandGroup;

    /**
     * Costruttore.
     */
    public ModelliTreeTablePage() {
        super(PAGE_ID);
        setShowTitlePane(false);
    }

    @Override
    protected void configureTreeTable(JXTreeTable treeTable) {
        treeTable.setRootVisible(true);
        treeTable.expandAll();
    }

    /**
     * Crea il nodo principale dell'albero ed inserisci i figli.
     *
     * @return rootNode
     */
    public MutableTreeTableNode createNode() {
        List<Modello> listModelli = vendingAnagraficaBD.caricaModelli();
        List<TipoModello> listTipiModello = vendingAnagraficaBD.caricaTipiModello();
        ModelloTreeTableFactory modelloTree = new ModelloTreeTableFactory();
        return modelloTree.create(listTipiModello, listModelli);
    }

    @Override
    protected TreeTableModel createTreeTableModel() {
        return new ModelliTreeTableModel(createNode());
    }

    @Override
    public void deleteNode(DefaultMutableTreeTableNode node) {
        DefaultTreeTableModel myModel = (DefaultTreeTableModel) this.getTreeTable().getTreeTableModel();
        myModel.removeNodeFromParent(node);
    }

    @Override
    protected boolean doDelete(DefaultMutableTreeTableNode node) {
        Modello modello = (Modello) node.getUserObject();
        vendingAnagraficaBD.cancellaModello(modello.getId());
        DefaultMutableTreeTableNode nodeToSelect = (DefaultMutableTreeTableNode) getTreeTable().getTreeTableModel()
                .getRoot();
        if (node.getParent() != null && !node.getParent().equals(nodeToSelect)) {
            nodeToSelect = (DefaultMutableTreeTableNode) node.getParent();
        }
        getTreeTable().getTreeSelectionModel().setSelectionPath(new TreePath(nodeToSelect));
        return true;
    }

    @Override
    public AbstractCommand[] getCommand() {
        if (abstractCommands == null) {
            abstractCommands = new AbstractCommand[] { getNewCommand(), getPropertyCommand(), getDeleteCommand(),
                    getRefreshModelliCommand() };
        }
        return abstractCommands;
    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        return getDeleteCommand();
    }

    @Override
    public AbstractCommand getEditorLockCommand() {
        return getPropertyCommand();
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getNewCommand();
    }

    /**
     * @return NewModelloCommand
     */
    private JECCommandGroup getNewCommand() {
        if (nuovoCommandGroup == null) {
            nuovoCommandGroup = new JECCommandGroup("newModelliTreeTablePageCommand");
            RcpSupport.configure(nuovoCommandGroup);

            nuovoCommandGroup.add(new NewModelloCommand(this));
            nuovoCommandGroup.add(new NewTipoModelloCommand(this));
        }
        return nuovoCommandGroup;
    }

    /**
     * @return RefreshCategorieCommand
     */
    protected RefreshModelliCommand getRefreshModelliCommand() {
        if (refreshModelliCommand == null) {
            refreshModelliCommand = new RefreshModelliCommand(this);
        }
        return refreshModelliCommand;
    }

    /**
     *
     * @return modello selezionato nel treetable.<br/>
     *         . NULL se non c'è nessuna selezione
     */
    public Modello getSelectedModello() {
        Modello modello = null;
        DefaultMutableTreeTableNode node = getSelectedNode();
        if (node != null && node.getUserObject() instanceof Modello) {
            modello = (Modello) node.getUserObject();
        }
        return modello;
    }

    @Override
    protected TreeCellRenderer getTreeCellRender() {
        return new ModelloTreeCellRenderer();
    }

    @Override
    public void loadData() {
        // Non utilizzato
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    /**
     * apre il dialog per editare un modello.
     *
     * @param modello
     *            modello da editare
     */
    protected void openModelloPage(Modello modello) {
        new ModelloTipoModelloDialog(modello, this, modelloPage).showDialog();
    }

    @Override
    protected void openSelectedNode(DefaultMutableTreeTableNode node) {

        EntityBase entity = (EntityBase) node.getUserObject();
        if (entity != null && !entity.isNew()) {
            if (entity instanceof Modello) {
                openModelloPage((Modello) entity);
            }
            if (entity instanceof TipoModello) {
                openTipoModelloPage((TipoModello) entity);
            }
        }
    }

    /**
     * apre il dialog per editare un modello.
     *
     * @param modello
     *            modello da editare
     */
    protected void openTipoModelloPage(TipoModello tipoModello) {
        new ModelloTipoModelloDialog(tipoModello, this, tipoModelloPage).showDialog();
    }

    @Override
    public void postSetFormObject(Object object) {
        if (object instanceof DistributorePM && StringUtils.isBlank(((DistributorePM) object).getSource())) {
            DistributorePM distributorePM = (DistributorePM) object;

            TreeTableModel model = getTreeTable().getTreeTableModel();
            selectNode((DefaultMutableTreeTableNode) model.getRoot(), distributorePM);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object object = evt.getNewValue();
        if (IPageLifecycleAdvisor.OBJECT_CHANGED.equals(evt.getPropertyName())
                && (object instanceof Modello || object instanceof TipoModello)) {
            DefaultMutableTreeTableNode node = getSelectedNode();

            // potrei aver selezionato un tipo modello e aver creato un modello quindi se i due oggetti non
            // corrispendono riaggiorno la tree table
            if (node != null && !((IDefProperty) object).isNew()
                    && node.getUserObject().getClass().equals(object.getClass())) {
                node.setUserObject(object);
            } else {
                getRefreshModelliCommand().execute();
            }
        }
    }

    @Override
    public void refreshData() {
        // Non utilizzato
    }

    @Override
    protected void selectionChanged(DefaultMutableTreeTableNode node) {
        if (node == null) {
            return;
        }

        TipoModello tipoModello = null;
        Modello modello = null;
        if (node.getUserObject() instanceof Modello && !((Modello) node.getUserObject()).isNew()) {
            modello = (Modello) node.getUserObject();
            tipoModello = modello.getTipoModello();
        }
        if (node.getUserObject() instanceof TipoModello) {
            tipoModello = (TipoModello) node.getUserObject();
        }

        DistributorePM distributorePMtmp = new DistributorePM(tipoModello, modello, null, PAGE_ID);
        firePropertyChange(IPageEditor.OBJECT_CHANGED, null, distributorePMtmp);

        // la pagina articolo se riceve una categoria senza articoli ne prepara uno
        // nuovo e prende il focus. Devo riprendermelo.
        getTreeTable().requestFocusInWindow();
    }

    /**
     * Seleziona il nodo che contiene il distributorePM speficifato.
     *
     * @param node
     *            nodo di partenza
     * @param distributorePM
     *            distributore da selezionare
     */
    public void selectNode(DefaultMutableTreeTableNode node, DistributorePM distributorePM) {

        // se nel PM il modello è presente cerco di selezionare quello, altrimenti seleziono il tipo modello
        Object userObj = ObjectUtils.defaultIfNull(distributorePM.getModello(), distributorePM.getTipoModello());

        if (node.getUserObject().equals(userObj)) {
            TreePath treePath = new TreePath(
                    ((DefaultTreeTableModel) getTreeTable().getTreeTableModel()).getPathToRoot(node));
            getTreeTable().expandPath(treePath);
            getTreeTable().getTreeSelectionModel().clearSelection();
            getTreeTable().getTreeSelectionModel().setSelectionPath(treePath);
            getTreeTable().scrollPathToVisible(treePath);
            return;
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                selectNode((DefaultMutableTreeTableNode) node.getChildAt(i), distributorePM);
            }
        }
    }

    @Override
    public void setFormObject(Object object) {
        // Non utilizzato
    }

    /**
     * @param modelloPage
     *            the modelloPage to set
     */
    public void setModelloPage(IPageEditor modelloPage) {
        this.modelloPage = modelloPage;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        enable(!readOnly);
    }

    /**
     * @param tipoModelloPage
     *            the tipoModelloPage to set
     */
    public void setTipoModelloPage(IPageEditor tipoModelloPage) {
        this.tipoModelloPage = tipoModelloPage;
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }
}