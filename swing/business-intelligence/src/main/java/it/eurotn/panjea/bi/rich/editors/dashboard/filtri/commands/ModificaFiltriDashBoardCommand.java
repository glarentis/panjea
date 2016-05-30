/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.dashboard.filtri.commands;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIDomain;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasure;
import it.eurotn.panjea.bi.rich.editors.dashboard.filtri.DashboardFiltriPage;
import it.eurotn.rich.command.JECCommandGroup;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.config.DefaultCommandButtonConfigurer;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.pivot.PivotField;
import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.TreeSearchable;
import com.jidesoft.tree.TreeUtils;

/**
 * @author fattazzo
 *
 */
public class ModificaFiltriDashBoardCommand extends ActionCommand {

	private class CancelCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public CancelCommand() {
			super("cancelCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			popup.hidePopup();
		}

	}

	private class SelezionaFitriCommand extends ActionCommand {

		/**
		 * Costruttore.
		 */
		public SelezionaFitriCommand() {
			super("selezionaFitriCommand");
			RcpSupport.configure(this);
		}

		@Override
		protected void doExecuteCommand() {
			TreePath[] selectionPaths = checkBoxTree.getCheckBoxTreeSelectionModel().getSelectionPaths();

			List<PivotField> fields = getSelectedFields(selectionPaths);

			dashboardFiltriPage.setFilter(fields);

			popup.hidePopup();
		}

		/**
		 * Restituisce tutti i fields selezionati nella checkbox.
		 *
		 * @param selectionPaths
		 *            selection paths
		 * @return fileds selezionati
		 */
		private List<PivotField> getSelectedFields(TreePath[] selectionPaths) {
			List<PivotField> fieldsSelected = new ArrayList<PivotField>();
			if (selectionPaths != null) {
				for (int i = 0; i < selectionPaths.length; i++) {
					TreePath path = selectionPaths[i];
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

					if (node.isLeaf()) {
						fieldsSelected.add((PivotField) node.getUserObject());
					} else {
						// non faccio una procedure ricorsiva tanto sò che ho solo 2 livelli
						@SuppressWarnings("rawtypes")
						Enumeration children = node.children();
						while (children.hasMoreElements()) {
							DefaultMutableTreeNode child = (DefaultMutableTreeNode) children.nextElement();
							if (child.isLeaf()) {
								fieldsSelected.add((PivotField) child.getUserObject());
							}
						}
					}
				}
			}

			return fieldsSelected;
		}

	}

	public static final String COMMAND_ID = "modificaFiltriDashBoardCommand";

	private DashboardFiltriPage dashboardFiltriPage;

	private CheckBoxTree checkBoxTree;

	private JidePopup popup;

	// button creato dal command. Devo tenermi l'istanza perchè al popup devo settarglielo come owner prima di aprirlo
	private AbstractButton buttonCommand;

	/**
	 * Costruttore.
	 *
	 * @param dashboardFiltriPage
	 *            dashboard dei filtri
	 */
	public ModificaFiltriDashBoardCommand(final DashboardFiltriPage dashboardFiltriPage) {
		super(COMMAND_ID);
		RcpSupport.configure(this);

		this.dashboardFiltriPage = dashboardFiltriPage;

		initControls();
	}

	private void applyCheckBoxTreeModel() {
		Map<String, List<PivotField>> categorieFields = new HashMap<String, List<PivotField>>();

		if (dashboardFiltriPage.getDataModel() != null) {
			for (PivotField pivotField : dashboardFiltriPage.getDataModel().getFields()) {
				Colonna colonna = AnalisiBIDomain.getColonna(pivotField.getName());
				if (!(colonna instanceof ColumnMeasure)) {
					String categoria = StringUtils.capitalize(colonna.getTabella().getAlias());
					@SuppressWarnings("unchecked")
					List<PivotField> fields = (List<PivotField>) ObjectUtils.defaultIfNull(
							categorieFields.get(categoria), new ArrayList<PivotField>());
					fields.add(pivotField);
					categorieFields.put(categoria, fields);
				}
			}
		}

		List<DefaultMutableTreeNode> selectedNodes = new ArrayList<DefaultMutableTreeNode>();

		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();

		for (Entry<String, List<PivotField>> entry : categorieFields.entrySet()) {
			DefaultMutableTreeNode categoriaNode = new DefaultMutableTreeNode(entry.getKey());

			for (PivotField pivotField : entry.getValue()) {
				DefaultMutableTreeNode pivotNode = new DefaultMutableTreeNode(pivotField);
				categoriaNode.add(pivotNode);

				if (pivotField.getSelectedPossibleValues() != null) {
					selectedNodes.add(pivotNode);
				}
			}
			rootNode.add(categoriaNode);
		}

		TreeModel model = new DefaultTreeModel(rootNode);
		checkBoxTree.setModel(model);

		// seleziono in automatico tutti i nodi che sono già impostati come filtri
		for (DefaultMutableTreeNode node : selectedNodes) {
			checkBoxTree.getCheckBoxTreeSelectionModel().addSelectionPath(new TreePath(node.getPath()));
		}
	}

	@Override
	public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer buttonConfigurer) {
		// uso il DefaultCommandButtonConfigurer per non avere l'ombra sul pulsante che su di un'icona 12*12 non va bene
		AbstractButton button = super.createButton(faceDescriptorId, buttonFactory,
				new DefaultCommandButtonConfigurer());
		buttonCommand = button;
		return button;
	}

	@Override
	protected void doExecuteCommand() {

		applyCheckBoxTreeModel();
		TreeUtils.expandAll(checkBoxTree, true);

		popup.setOwner(buttonCommand);
		popup.showPopup();
	}

	private void initControls() {

		checkBoxTree = new CheckBoxTree() {
			private static final long serialVersionUID = -6171919808419233707L;

			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension(300, 400);
			}
		};
		checkBoxTree.setRootVisible(false);
		checkBoxTree.setShowsRootHandles(true);
		checkBoxTree.setCellRenderer(new FiltriDashboardTreeCellRenderer());
		new TreeSearchable(checkBoxTree) {
			@Override
			protected String convertElementToString(Object paramObject) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) ((TreePath) paramObject).getLastPathComponent();

				if (node.getUserObject() instanceof PivotField) {
					return ((PivotField) node.getUserObject()).getTitle();
				}
				return super.convertElementToString(paramObject);
			}
		};

		JECCommandGroup commandGroup = new JECCommandGroup();
		commandGroup.add(new SelezionaFitriCommand());
		commandGroup.add(new CancelCommand());
		JPanel toolbarPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.RIGHT));
		toolbarPanel.add(commandGroup.createToolBar());

		JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
		rootPanel.add(new JScrollPane(checkBoxTree), BorderLayout.CENTER);
		rootPanel.add(toolbarPanel, BorderLayout.SOUTH);

		popup = new JidePopup();
		popup.setMovable(false);
		popup.setAttachable(true);
		popup.getContentPane().add(rootPanel);
		popup.setDefaultFocusComponent(checkBoxTree);
		popup.setPreferredPopupSize(new Dimension(300, 400));
	}

	@Override
	protected void onButtonAttached(AbstractButton button) {
		popup.setOwner(button);
		super.onButtonAttached(button);
	}

}
