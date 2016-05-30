/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.statistiche.valorizzazione;

import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.CategorieTreeTablePage;
import it.eurotn.panjea.magazzino.util.CategoriaLite;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.tree.TreeUtils;

import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.SearchableUtils;

/**
 * @author fattazzo
 * 
 */
public class ParametriValorizzazioneCategorieControl extends AbstractControlFactory {

	private class CategoriaTreeRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = -4384749319811218269L;

		public static final String CATEGORIA_ROOT_NODE_LABEL = CategorieTreeTablePage.PAGE_ID + ".rootNode.label";
		public static final String CATEGORIA_ROOT_NODE_ICON = CategorieTreeTablePage.PAGE_ID + ".rootNode.icon";

		IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);

		@Override
		public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3,
				boolean arg4, int arg5, boolean arg6) {
			JComponent component = (JComponent) super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5,
					arg6);
			component.setOpaque(false);

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) arg1;

			if (node.getUserObject() instanceof CategoriaLite) {
				CategoriaLite categoriaDTO = (CategoriaLite) node.getUserObject();
				setIcon(iconSource.getIcon(node.getUserObject().getClass().getName()));
				setText(categoriaDTO.getCodice() + " - " + categoriaDTO.getDescrizione());
			} else {
				setIcon(iconSource.getIcon(CATEGORIA_ROOT_NODE_ICON));
				setText(getMessage(CATEGORIA_ROOT_NODE_LABEL));
			}
			return component;
		}
	}

	private class CheckBoxTreeSelectionListener implements TreeSelectionListener {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			if (treeFill) {
				return;
			}

			TreePath[] selected = treeCategorie.getCheckBoxTreeSelectionModel().getSelectionPaths();

			List<Categoria> listcategorieSelezionate = new ArrayList<Categoria>();

			if (selected != null) {
				for (TreePath treePathSel : selected) {
					visitSelectedNode((DefaultMutableTreeNode) treePathSel.getLastPathComponent(),
							listcategorieSelezionate);
				}
			}

			formModel.getValueModel("categorie").setValue(listcategorieSelezionate);
			formModel.getValueModel("tutteCategorie").setValue(listcategorieSelezionate.size() == listCategorie.size());
		}

		private void visitSelectedNode(DefaultMutableTreeNode node, List<Categoria> listSelected) {

			if (node.getUserObject() instanceof CategoriaLite
					&& ((CategoriaLite) node.getUserObject()).isRoot() == false) {
				Categoria categoria = new Categoria();
				categoria.setId(((CategoriaLite) node.getUserObject()).getId());
				categoria.setVersion(((CategoriaLite) node.getUserObject()).getVersion());
				listSelected.add(categoria);
			} else {
				Enumeration children = node.children();
				while (children.hasMoreElements()) {
					DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children.nextElement();
					if (node.getUserObject() instanceof CategoriaLite
							&& ((CategoriaLite) node.getUserObject()).isRoot() == false) {
						Categoria categoria = new Categoria();
						categoria.setId(((CategoriaLite) childNode.getUserObject()).getId());
						categoria.setVersion(((CategoriaLite) childNode.getUserObject()).getVersion());
						listSelected.add(categoria);
					} else {
						visitSelectedNode(childNode, listSelected);
					}
				}
			}
		}

	}

	private DefaultMutableTreeNode root = null;

	private DefaultTreeModel treeModel;

	private CheckBoxTree treeCategorie = null;

	private TreePath treePath;

	private final boolean treeFill = false;

	private CheckBoxTreeSelectionListener treeSelectionListener = null;

	private final FormModel formModel;

	private List<CategoriaLite> listCategorie;

	public static final String CATEGORIE_TITLE = "parametriValorizzazioneCategorie.categorie";

	private final IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	public ParametriValorizzazioneCategorieControl(IMagazzinoAnagraficaBD magazzinoAnagraficaBD, FormModel formModel) {
		super();
		this.formModel = formModel;
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;

		this.formModel.addPropertyChangeListener(FormModel.READONLY_PROPERTY, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				boolean readOnly = ((Boolean) evt.getNewValue()).booleanValue();
				treeCategorie.setCheckBoxEnabled(!readOnly);
				if (!readOnly) {
					treeCategorie.setBackground((Color) UIManager.getDefaults().get("TextField.background"));
				} else {
					treeCategorie.setBackground(UIManager.getDefaults().getColor("TextField.inactiveBackground"));
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.factory.AbstractControlFactory#createControl()
	 */
	@Override
	protected JComponent createControl() {
		treeModel = createTreeModel();

		treeCategorie = new CheckBoxTree(treeModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension(300, 150);
			}
		};
		treeCategorie.setRootVisible(true);
		treeCategorie.setShowsRootHandles(true);
		treeCategorie.setOpaque(true);
		treePath = new TreePath(root.getPath());
		treeCategorie.getCheckBoxTreeSelectionModel().setSelectionPath(treePath);
		treeCategorie.setCellRenderer(new CategoriaTreeRenderer());

		treeSelectionListener = new CheckBoxTreeSelectionListener();
		treeCategorie.getCheckBoxTreeSelectionModel().addTreeSelectionListener(treeSelectionListener);
		TreeUtils.expandAll(treeCategorie, true);
		SearchableUtils.installSearchable(treeCategorie);

		JScrollPane scrollPane = getComponentFactory().createScrollPane(treeCategorie);
		scrollPane.setBorder(BorderFactory.createTitledBorder(getMessage(CATEGORIE_TITLE)));

		return scrollPane;
	}

	private DefaultMutableTreeNode createNode() {
		listCategorie = magazzinoAnagraficaBD.caricaCategorie();

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();

		for (CategoriaLite categoriaLite : listCategorie) {
			rootNode.add(createNodeForCategoria(categoriaLite));
		}

		return rootNode;
	}

	private DefaultMutableTreeNode createNodeForCategoria(CategoriaLite categoriaLite) {

		DefaultMutableTreeNode node = new DefaultMutableTreeNode(categoriaLite);

		for (CategoriaLite categoriaDTOFiglia : categoriaLite.getCategorieFiglie()) {
			node.add(createNodeForCategoria(categoriaDTOFiglia));
		}

		return node;
	}

	private DefaultTreeModel createTreeModel() {

		root = createNode();

		DefaultTreeModel model = new DefaultTreeModel(root);

		return model;
	}
}
