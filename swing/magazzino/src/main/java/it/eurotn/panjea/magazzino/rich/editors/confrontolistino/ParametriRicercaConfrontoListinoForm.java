/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.confrontolistino;

import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.CategorieTreeTablePage;
import it.eurotn.panjea.magazzino.rich.search.ListinoSearchObject;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaConfrontoListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.TipoConfronto;
import it.eurotn.panjea.magazzino.util.parametriricerca.TipoConfronto.EConfronto;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.TableBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.CheckBoxTreeSelectionModel;
import com.jidesoft.swing.SearchableUtils;

/**
 * @author fattazzo
 * 
 */
public class ParametriRicercaConfrontoListinoForm extends PanjeaAbstractForm {

	private final class CategoriaTreeCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 6374529617764128306L;

		@Override
		public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3,
				boolean arg4, int arg5, boolean arg6) {
			JComponent component = (JComponent) super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5,
					arg6);
			component.setOpaque(true);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) arg1;

			if (node.getUserObject() instanceof CategoriaLite) {
				CategoriaLite categoriaLite = (CategoriaLite) node.getUserObject();
				setIcon(getIconSource().getIcon(CategoriaLite.class.getName()));

				String desc;
				if (categoriaLite.getCodice() != null) {
					desc = categoriaLite.getCodice() + " - " + categoriaLite.getDescrizione();
				} else {
					desc = getMessage(CategorieTreeTablePage.PAGE_ID + ".rootNode.label");
				}
				setText(desc);
			}
			return component;
		}
	}

	/**
	 * Tree selection listener per la selezione dei tipi documento.
	 * 
	 * @author Leonardo
	 */
	private class CheckBoxTreeSelectionListener implements TreeSelectionListener {

		/**
		 * Aggiunge ricorsivamente i figli alla lista di categorie passata per riferimento.
		 * 
		 * @param cat
		 *            la categoria da ispezionare
		 * @param cats
		 *            la lista di categorie da riempire
		 */
		private void addChildren(CategoriaLite cat, List<CategoriaLite> cats) {
			for (CategoriaLite kitten : cat.getCategorieFiglie()) {
				cats.add(kitten);
				addChildren(kitten, cats);
			}
		}

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			TreePath[] selected = ((CheckBoxTreeSelectionModel) e.getSource()).getSelectionPaths();

			((ParametriRicercaConfrontoListino) getFormObject()).setTutteCategorie(true);
			getValueModel("categorie").setValue(new ArrayList<CategoriaLite>());

			if (selected != null) {
				List<CategoriaLite> listCategorieSelezionate = new ArrayList<CategoriaLite>();
				for (TreePath selectionTreePath : selected) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionTreePath.getLastPathComponent();

					if (node.getUserObject() instanceof CategoriaLite) {
						CategoriaLite cat = (CategoriaLite) node.getUserObject();
						if (cat.getId().equals(-1)) {
							listCategorieSelezionate.clear();
							((ParametriRicercaConfrontoListino) getFormObject()).setTutteCategorie(true);
						} else {
							((ParametriRicercaConfrontoListino) getFormObject()).setTutteCategorie(false);
							listCategorieSelezionate.add(cat);
							addChildren(cat, listCategorieSelezionate);
						}
					}
				}
				getValueModel("categorie").setValue(listCategorieSelezionate);
			}

		}
	}

	private class ConfrontoBasePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			EConfronto confronto = (EConfronto) evt.getNewValue();

			boolean visible = confronto != null && confronto == EConfronto.LISTINO;
			listinoLabel.setVisible(visible);
			listinoSearch.setVisible(visible);
		}

	}

	private class ReadOnlyChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (treeCategorie != null) {
				boolean readOnly = ((Boolean) evt.getNewValue()).booleanValue();
				treeCategorie.setCheckBoxEnabled(!readOnly);
				if (!readOnly) {
					treeCategorie.setBackground((Color) UIManager.getDefaults().get("JPanel.background"));
				} else {
					treeCategorie.setBackground(UIManager.getDefaults().getColor("TextField.inactiveBackground"));
				}
			}
		}
	}

	private class TipoConfrontoCommandInterceptor implements ActionCommandInterceptor {

		private TipoConfrontoListinoInserimentoForm inserimentoForm = null;

		/**
		 * Costruttore.
		 * 
		 * @param inserimentoForm
		 *            TipoConfrontoCommandInterceptor
		 */
		public TipoConfrontoCommandInterceptor(final TipoConfrontoListinoInserimentoForm inserimentoForm) {
			super();
			this.inserimentoForm = inserimentoForm;
		}

		@Override
		public void postExecution(ActionCommand command) {

		}

		@Override
		public boolean preExecution(ActionCommand command) {
			TipoConfronto tipoConfronto = (TipoConfronto) inserimentoForm.getFormObject();
			return tipoConfronto.getConfronto() != null
					&& !(tipoConfronto.getConfronto() == EConfronto.LISTINO && tipoConfronto.getListino() == null);
		}
	}

	public static final String FORM_ID = "parametriRicercaConfrontoListinoForm";

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD = null;
	private JLabel listinoLabel;
	private JComponent listinoSearch;
	private ActionCommand aggiungiRigaCommand = null;
	private CheckBoxTree treeCategorie = null;
	private DefaultTreeModel treeModel;
	private CheckBoxTreeSelectionListener treeSelectionListener;

	private JScrollPane scrollPaneCategorieComponent;
	private ConfrontoBasePropertyChange confrontoBasePropertyChange;
	private TipoConfrontoCommandInterceptor tipoConfrontoCommandInterceptor = null;

	private ReadOnlyChangeListener readOnlyChangeListener = null;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaConfrontoListinoForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaConfrontoListino(), false, FORM_ID), FORM_ID);
		this.magazzinoAnagraficaBD = RcpSupport.getBean(MagazzinoAnagraficaBD.BEAN_ID);
		this.getFormModel().addPropertyChangeListener("readOnly", getReadOnlyChangeListener());

		// aggiungo la finta proprietà tipi listino per far si che la search text del listino mi selezioni solo quelli
		// normali e non a scaglioni
		ValueModel tipoListinoValueModel = new ValueHolder(ETipoListino.NORMALE);
		DefaultFieldMetadata tipiListinoData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipoListinoValueModel), ETipoListino.class, true, null);
		getFormModel().add("tipoListino", tipoListinoValueModel, tipiListinoData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"left:default, 4dlu, fill:202dlu, 10dlu,left:default, 4dlu, fill:202dlu,fill:default:grow",
				"4dlu,default,2dlu,default,2dlu,default,2dlu,default,4dlu,default,default:grow,2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered());
		builder.setLabelAttributes("r,c");

		builder.setRow(2);
		builder.addHorizontalSeparator("Base di confronto", 1, 7);

		builder.addPropertyAndLabel("confrontoBase.confronto", 1, 4);

		listinoLabel = builder.addLabel("confrontoBase.listino", 5, 4);
		Binding bindingListino = bf.createBoundSearchText("confrontoBase.listino", new String[] { "codice" },
				new String[] { "tipoListino" }, new String[] { ListinoSearchObject.TIPO_LISTINO_KEY });
		((SearchPanel) bindingListino.getControl()).getTextFields().get("codice").setColumns(15);
		listinoSearch = builder.addBinding(bindingListino, 7, 4);

		listinoLabel.setVisible(false);
		listinoSearch.setVisible(false);

		builder.setRow(6);
		builder.addHorizontalSeparator("Listini/Costi di confronto", 1, 3);
		builder.addHorizontalSeparator("Filtri", 5, 3);

		builder.addLabel("confronto", 1, 8);
		final TipoConfrontoListinoInserimentoForm inserimentoForm = new TipoConfrontoListinoInserimentoForm();
		DefaultBeanTableModel<TipoConfronto> tipoConfrontoTableModel = new TipoConfrontoTableModel();
		TableBinding<?> tipoConfrontoBinding = (TableBinding<?>) bf.createTableBinding("confronti", 190,
				tipoConfrontoTableModel, inserimentoForm);
		aggiungiRigaCommand = tipoConfrontoBinding.getAggiungiRigaCommand();
		aggiungiRigaCommand.addCommandInterceptor(getTipoConfrontoCommandInterceptor(inserimentoForm));
		JComponent tipoConfrontoControl = tipoConfrontoBinding.getControl();
		tipoConfrontoControl.setPreferredSize(new Dimension(200, 120));
		builder.addComponent(tipoConfrontoControl, 3, 8, 1, 4);

		builder.nextRow();

		builder.addComponent(createTreeCategorieComponent(), 5, 10, 4, 2);

		builder.nextRow();

		// builder.addHorizontalSeparator("Entità", 1, 5);
		builder.addLabel("entita", 5, 8);
		JComponent components = builder.addBinding(
				bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" }), 7, 8);
		((SearchPanel) components).getTextFields().get("codice").setColumns(5);
		((SearchPanel) components).getTextFields().get("anagrafica.denominazione").setColumns(23);

		getValueModel("confrontoBase.confronto").addValueChangeListener(getConfrontoBasePropertyChange());

		return builder.getPanel();
	}

	/**
	 * Crea il nodo principale dell'albero ed inserisci i figli.
	 * 
	 * @return rootNode
	 */
	public DefaultMutableTreeNode createNode() {
		List<CategoriaLite> list = magazzinoAnagraficaBD.caricaCategorie();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		// aggiungo una categoriaDTO con id-1 er indicare che è la categoria
		// "root"
		CategoriaLite categoriaRoot = new CategoriaLite();
		categoriaRoot.setId(-1);
		root.setUserObject(categoriaRoot);
		for (CategoriaLite categoriaDTO : list) {
			root.add(createNodeForCategoria(categoriaDTO));
		}
		return root;
	}

	/**
	 * Metodo ricorsivo che data una categoria crea il nodo ed aggiunge i figli.<br>
	 * . <b>NB</b>: i figli posso essere categorie.
	 * 
	 * @param categoriaLite
	 *            per il quale creare il nodo
	 * @return nodo per la categoria con i suoi figli
	 */
	public DefaultMutableTreeNode createNodeForCategoria(CategoriaLite categoriaLite) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(categoriaLite);
		for (CategoriaLite categoriaDTOFiglia : categoriaLite.getCategorieFiglie()) {
			node.add(createNodeForCategoria(categoriaDTOFiglia));
		}
		return node;
	}

	/**
	 * @return JComponent
	 */
	public JComponent createTreeCategorieComponent() {
		treeModel = new DefaultTreeModel(createNode());

		treeCategorie = new CheckBoxTree(treeModel) {

			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension(250, 100);
			}
		};
		treeCategorie.setRootVisible(true);
		treeCategorie.setShowsRootHandles(false);
		treeSelectionListener = new CheckBoxTreeSelectionListener();
		treeCategorie.getCheckBoxTreeSelectionModel().addTreeSelectionListener(treeSelectionListener);
		treeCategorie.setCellRenderer(new CategoriaTreeCellRenderer());

		SearchableUtils.installSearchable(treeCategorie);
		scrollPaneCategorieComponent = getComponentFactory().createScrollPane(treeCategorie);
		return scrollPaneCategorieComponent;
	}

	@Override
	public void dispose() {
		getValueModel("confrontoBase.confronto").removeValueChangeListener(getConfrontoBasePropertyChange());
		if (aggiungiRigaCommand != null) {
			aggiungiRigaCommand.removeCommandInterceptor(tipoConfrontoCommandInterceptor);
		}
		this.getFormModel().removePropertyChangeListener("readOnly", getReadOnlyChangeListener());

		super.dispose();
	}

	/**
	 * @return the confrontoBasePropertyChange
	 */
	private ConfrontoBasePropertyChange getConfrontoBasePropertyChange() {
		if (confrontoBasePropertyChange == null) {
			confrontoBasePropertyChange = new ConfrontoBasePropertyChange();
		}
		return confrontoBasePropertyChange;
	}

	/**
	 * @return readOnlyChangeListener
	 */
	private ReadOnlyChangeListener getReadOnlyChangeListener() {
		if (readOnlyChangeListener == null) {
			readOnlyChangeListener = new ReadOnlyChangeListener();
		}
		return readOnlyChangeListener;
	}

	/**
	 * @param inserimentoForm
	 *            TipoConfrontoListinoInserimentoForm
	 * @return TipoConfrontoCommandInterceptor
	 */
	private TipoConfrontoCommandInterceptor getTipoConfrontoCommandInterceptor(
			TipoConfrontoListinoInserimentoForm inserimentoForm) {
		tipoConfrontoCommandInterceptor = new TipoConfrontoCommandInterceptor(inserimentoForm);
		return tipoConfrontoCommandInterceptor;
	}

}
