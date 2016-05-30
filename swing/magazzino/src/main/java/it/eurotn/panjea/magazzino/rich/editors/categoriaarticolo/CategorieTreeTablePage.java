package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.rich.dialog.DefaultTitledPageApplicationDialog;
import it.eurotn.rich.editors.AbstractTreeTableDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.settings.Settings;

/**
 * @author fattazzo
 */
public class CategorieTreeTablePage extends AbstractTreeTableDialogPageEditor implements PropertyChangeListener {

	private static Logger logger = Logger.getLogger(CategorieTreeTablePage.class);

	public static final String PAGE_ID = "categorieTreeTablePage";

	public static final String CATEGORIA_ROOT_NODE_LABEL = PAGE_ID + ".rootNode.label";
	public static final String CATEGORIA_ROOT_NODE_ICON = PAGE_ID + ".rootNode.icon";

	protected IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	protected AbstractCommand[] abstractCommands;
	private NewCategoriaCommand newCategoriaCommand;
	private RefreshCategorieCommand refreshCategorieCommand;

	private boolean enableCommand;

	private CategoriaPage categoriaPage;

	private ArticoloCategoriaDTO articoloCategoriaDTO;

	/**
	 * Costruttore.
	 */
	public CategorieTreeTablePage() {
		super(PAGE_ID);
		setShowTitlePane(false);
	}

	/**
	 * Costruttore per cambiare l'id della page.
	 * 
	 * @param pageId
	 *            l'id della page
	 */
	protected CategorieTreeTablePage(final String pageId) {
		super(pageId);
		setShowTitlePane(false);
	}

	@Override
	protected void configureTreeTable(JXTreeTable treeTable) {
		logger.debug("--> Enter configureTreeTable");

		// visualizzo il root perchè selezionandolo devo caricare gli articoli
		// di tutte le categorie
		treeTable.setRootVisible(true);
	}

	/**
	 * Crea il nodo principale dell'albero ed inserisci i figli.
	 * 
	 * @return rootNode
	 */
	public MutableTreeTableNode createNode() {
		List<CategoriaLite> list = magazzinoAnagraficaBD.caricaCategorie();
		CategoriaLiteTreeTableFactory categorieTree = new CategoriaLiteTreeTableFactory();
		return categorieTree.create(list);
	}

	@Override
	protected TreeTableModel createTreeTableModel() {
		return new CategoriaLiteTreeTableModel(createNode());
	}

	@Override
	public void deleteNode(DefaultMutableTreeTableNode node) {
		DefaultTreeTableModel myModel = (DefaultTreeTableModel) this.getTreeTable().getTreeTableModel();
		myModel.removeNodeFromParent(node);
	}

	@Override
	protected boolean doDelete(DefaultMutableTreeTableNode node) {
		CategoriaLite categoriaLite = (CategoriaLite) node.getUserObject();
		getMagazzinoAnagraficaBD().cancellaCategoria(categoriaLite.getId());
		DefaultMutableTreeTableNode nodeToSelect = (DefaultMutableTreeTableNode) getTreeTable().getTreeTableModel()
				.getRoot();
		if (node.getParent() != null && !node.getParent().equals(nodeToSelect)) {
			nodeToSelect = (DefaultMutableTreeTableNode) node.getParent();
		}
		getTreeTable().getTreeSelectionModel().setSelectionPath(new TreePath(nodeToSelect));
		return true;
	}

	/**
	 * @return Returns the categoriaPage.
	 */
	public CategoriaPage getCategoriaPage() {
		return categoriaPage;
	}

	@Override
	public AbstractCommand[] getCommand() {
		if (abstractCommands == null) {
			abstractCommands = new AbstractCommand[] { getNewCommand(), getPropertyCommand(), getDeleteCommand(),
					getRefreshCategorieCommand() };
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
	 * @return Returns the magazzinoAnagraficaBD.
	 */
	public IMagazzinoAnagraficaBD getMagazzinoAnagraficaBD() {
		return magazzinoAnagraficaBD;
	}

	/**
	 * Crea un commandGroup contenente due comandi per aggiungere o una categoria root o una categoria figlia.
	 * 
	 * @return commandgroup con i comandi per la nuova categoria.
	 */
	private NewCategoriaCommand getNewCommand() {
		if (newCategoriaCommand == null) {
			newCategoriaCommand = new NewCategoriaCommand(this);
		}
		return newCategoriaCommand;
	}

	/**
	 * @return RefreshCategorieCommand
	 */
	protected RefreshCategorieCommand getRefreshCategorieCommand() {
		if (refreshCategorieCommand == null) {
			refreshCategorieCommand = new RefreshCategorieCommand(this);
		}
		return refreshCategorieCommand;
	}

	/**
	 * 
	 * @return categoria selezionata nel treetable.<br/>
	 *         . NULL se non c'è nessuna selezione
	 */
	public CategoriaLite getSelectedCategoria() {
		CategoriaLite categoriaLite = null;
		DefaultMutableTreeTableNode node = getSelectedNode();
		if (node != null) {
			categoriaLite = (CategoriaLite) node.getUserObject();
		}
		return categoriaLite;
	}

	@Override
	protected TreeCellRenderer getTreeCellRender() {
		return new CategoriaLiteTreeCellRenderer();
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	/**
	 * apre il dialog per editare una categoria.
	 * 
	 * @param categoria
	 *            categoria da editare
	 */
	protected void openCategoriaPage(Categoria categoria) {
		DefaultTitledPageApplicationDialog titledPageApplicationDialog = new DefaultTitledPageApplicationDialog(
				categoria, this, categoriaPage);
		titledPageApplicationDialog.showDialog();
	}

	@Override
	protected void openSelectedNode(DefaultMutableTreeTableNode node) {
		if (getSelectedCategoria() != null && (getSelectedCategoria()).getCodice() != null) {
			Categoria categoria = getSelectedCategoria().createCategoria();
			if (categoria.getId() != null) {
				categoria = magazzinoAnagraficaBD.caricaCategoria(categoria, true);
				openCategoriaPage(categoria);
			}
		}
	}

	@Override
	public void postSetFormObject(Object object) {
		if (object != null && object instanceof ArticoloCategoriaDTO) {

			ArticoloCategoriaDTO articoloCategoriaDTOTmp = (ArticoloCategoriaDTO) object;
			// se ho un articolo e categoria provengo da una searchObject, quindi
			// devo solamente selezionare l'articolo.
			// Rimuovo il listener perchè devo solamente selezionare la categoria e non fare niente visto che le altre
			// pagine riceveranno l'evento della seach object e si comporteranno di coneguenza
			if (articoloCategoriaDTOTmp.getCategoria() != null && articoloCategoriaDTOTmp.getArticolo() != null) {
				TreeTableModel model = getTreeTable().getTreeTableModel();
				getTreeTable().removeTreeSelectionListener(getTreeListSelectionListener());
				selectNode((DefaultMutableTreeTableNode) model.getRoot(), articoloCategoriaDTOTmp.getCategoria()
						.getCategoriaLite());
				getTreeTable().addTreeSelectionListener(getTreeListSelectionListener());
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent propertychangeevent) {
		if (IPageLifecycleAdvisor.OBJECT_CHANGED.equals(propertychangeevent.getPropertyName())) {
			// Ho modificato una categoria nella finestra CategoriaPage
			Categoria categoria = (Categoria) propertychangeevent.getNewValue();
			DefaultMutableTreeTableNode selectNode = getSelectedNode();

			if (selectNode != null) {
				CategoriaLite selectCategoriaLite = (CategoriaLite) selectNode.getUserObject();
				if (selectCategoriaLite.createCategoria().equals(categoria)) {
					// Aggiorno la categoria selezionata.
					CategoriaLite categoriaLite = new CategoriaLite(categoria);
					selectNode.setUserObject(categoriaLite);
				} else {
					// nuova categoria
					DefaultMutableTreeTableNode newNode = new DefaultMutableTreeTableNode(
							(new CategoriaLite(categoria)));
					((DefaultTreeTableModel) getTreeTable().getTreeTableModel()).insertNodeInto(newNode, selectNode,
							selectNode.getChildCount());
					getTreeTable().expandPath(getTreeTable().getPathForRow(getTreeTable().getSelectedRow()));
				}
				// getTreeTable().getTreeTableModel().valueForPathChanged(
				// getTreeTable().getPathForRow(getTreeTable().getSelectedRow()), selectCategoriaLite);
			}
		}
	}

	@Override
	public void refreshData() {
	}

	@Override
	public void restoreState(Settings settings) {
		getTreeTable().removeTreeSelectionListener(getTreeListSelectionListener());
		super.restoreState(settings);

		if (articoloCategoriaDTO != null) {
			// se ho un articolo e categoria provengo da una searchObject, quindi
			// devo solamente selezionare l'articolo.
			// Rimuovo il listener perchè devo solamente selezionare la categoria e non fare niente visto che le altre
			// pagine riceveranno l'evento della seach object e si comporteranno di coneguenza.
			if (articoloCategoriaDTO.getCategoria() != null && articoloCategoriaDTO.getArticolo() != null) {
				TreeTableModel model = getTreeTable().getTreeTableModel();
				selectNode((DefaultMutableTreeTableNode) model.getRoot(), articoloCategoriaDTO.getCategoria()
						.getCategoriaLite());
			}
		}
		getTreeTable().addTreeSelectionListener(getTreeListSelectionListener());
	}

	@Override
	protected void selectionChanged(DefaultMutableTreeTableNode node) {
		if (node != null) {
			CategoriaLite categoriaLite = (CategoriaLite) node.getUserObject();

			if (categoriaLite != null) {
				ArticoloCategoriaDTO articoloCategoriaDTOTmp = new ArticoloCategoriaDTO(null,
						categoriaLite.createCategoria());
				firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, articoloCategoriaDTOTmp);
			}

			// se ho una categoria con id valido e non ho categorie
			// figlie allora posso abilitare il delete command
			enableCommand = categoriaLite != null && categoriaLite.getId() != null
					&& categoriaLite.getId().intValue() != -1 && node.children() != null
					&& !node.children().hasMoreElements();
			getDeleteCommand().setEnabled(enableCommand);
			// la pagina articolo se riceve una categoria senza articoli ne prepara uno
			// nuovo e prende il focus. Devo riprendermelo.
			getTreeTable().requestFocusInWindow();
		}
	}

	/**
	 * Seleziona il nodo (e suoi figli) selezionando quello che contiene la categoria speficifata.
	 * 
	 * @param node
	 *            nodo di partenza
	 * @param categoria
	 *            categoria da selezionare
	 */
	public void selectNode(DefaultMutableTreeTableNode node, CategoriaLite categoria) {
		if (node.getUserObject().equals(categoria)) {
			TreePath treePath = new TreePath(
					((DefaultTreeTableModel) getTreeTable().getTreeTableModel()).getPathToRoot(node));
			getTreeTable().expandPath(treePath);
			getTreeTable().getTreeSelectionModel().clearSelection();
			getTreeTable().getTreeSelectionModel().setSelectionPath(treePath);
			getTreeTable().scrollPathToVisible(treePath);
			return;
		} else {
			for (int i = 0; i < node.getChildCount(); i++) {
				selectNode((DefaultMutableTreeTableNode) node.getChildAt(i), categoria);
			}
		}
	}

	/**
	 * @param categoriaPage
	 *            The categoriaPage to set.
	 */
	public void setCategoriaPage(CategoriaPage categoriaPage) {
		this.categoriaPage = categoriaPage;
	}

	@Override
	public void setFormObject(Object object) {
		articoloCategoriaDTO = null;
		if (object != null && object instanceof ArticoloCategoriaDTO) {
			articoloCategoriaDTO = (ArticoloCategoriaDTO) object;
		}
	}

	/**
	 * @param magazzinoAnagraficaBD
	 *            The magazzinoAnagraficaBD to set.
	 */
	public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		enable(!readOnly);
	}
}