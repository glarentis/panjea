package it.eurotn.panjea.magazzino.rich.forms.statistiche.valorizzazione;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.tree.TreeUtils;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.GroupingList;

import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.SearchableUtils;

public class ParametriValorizzazioneDepositiControl extends
		AbstractControlFactory {

	private class CheckBoxTreeSelectionListener implements
			TreeSelectionListener {

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			if (treeFill) {
				return;
			}

			TreePath[] selected = treeDepositi.getCheckBoxTreeSelectionModel()
					.getSelectionPaths();

			List<Deposito> listDepositiSelezionati = new ArrayList<Deposito>();

			if (selected != null) {
				for (TreePath treePathChanged : selected) {
					visitSelectedNode(
							(DefaultMutableTreeNode) treePathChanged
									.getLastPathComponent(),
							listDepositiSelezionati);
				}
			}

			formModel.getValueModel("depositi").setValue(
					listDepositiSelezionati);
			if (formModel.hasValueModel("tuttiDepositi")) {
				formModel.getValueModel("tuttiDepositi").setValue(
						listDepositiSelezionati.size() == depositiAzienda
								.size());
			}
		}

		private void visitSelectedNode(DefaultMutableTreeNode node,
				List<Deposito> listSelected) {

			if (node.getUserObject() instanceof Deposito) {
				listSelected.add((Deposito) node.getUserObject());
			} else {
				Enumeration children = node.children();
				while (children.hasMoreElements()) {
					DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children
							.nextElement();
					if (node.getUserObject() instanceof Deposito) {
						listSelected.add((Deposito) childNode.getUserObject());
					} else {
						visitSelectedNode(childNode, listSelected);
					}
				}
			}
		}

	}

	private class DepositoTreeRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = -4384749319811218269L;

		@Override
		public Component getTreeCellRendererComponent(JTree arg0, Object arg1,
				boolean arg2, boolean arg3, boolean arg4, int arg5, boolean arg6) {
			JComponent component = (JComponent) super
					.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4,
							arg5, arg6);

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) arg1;

			if (node.getUserObject() instanceof SedeAzienda) {
				SedeAzienda sedeAzienda = (SedeAzienda) node.getUserObject();
				setText(sedeAzienda.getSede().getDescrizione()
						+ " - "
						+ sedeAzienda.getSede().getDatiGeografici()
								.getDescrizioneLocalita() + " - "
						+ sedeAzienda.getSede().getIndirizzo());
			} else if (node.getUserObject() instanceof Deposito) {
				Deposito deposito = (Deposito) node.getUserObject();
				setText(deposito.getDescrizione());
			} else if (node.getUserObject() instanceof TipoDeposito) {
				TipoDeposito tipoDeposito = (TipoDeposito) node.getUserObject();
				setText(tipoDeposito.getCodice());
			}
			component.setOpaque(true);
			return component;
		}

	}

	public static final String DEPOSITI_TITLE = "parametriValorizzazioneDepositi.depositi";

	private DefaultMutableTreeNode root = null;

	private DefaultTreeModel treeModel;

	private CheckBoxTree treeDepositi = null;

	private TreePath treePath;

	private final boolean treeFill = false;

	private CheckBoxTreeSelectionListener treeSelectionListener = null;

	private final FormModel formModel;

	private final List<Deposito> depositiAzienda;

	public ParametriValorizzazioneDepositiControl(IAnagraficaBD anagraficaBD,
			AziendaCorrente aziendaCorrente, FormModel formModel) {
		super();
		this.formModel = formModel;

		depositiAzienda = anagraficaBD.caricaDepositi();
	}

	@Override
	protected JComponent createControl() {

		treeModel = createTreeModel();
		treeDepositi = new CheckBoxTree(treeModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension(400, 120);
			}
		};
		treeDepositi.setRootVisible(false);
		treeDepositi.setShowsRootHandles(true);
		treeDepositi.setOpaque(false);
		treeSelectionListener = new CheckBoxTreeSelectionListener();
		treeDepositi.getCheckBoxTreeSelectionModel().addTreeSelectionListener(
				treeSelectionListener);
		treePath = new TreePath(root.getPath());
		treeDepositi.getCheckBoxTreeSelectionModel().setSelectionPath(treePath);
		treeDepositi.setCellRenderer(new DepositoTreeRenderer());
		TreeUtils.expandLevels(treeDepositi, 1, true);
		SearchableUtils.installSearchable(treeDepositi);

		JScrollPane scrollPane = getComponentFactory().createScrollPane(
				treeDepositi);
		scrollPane.setBorder(BorderFactory
				.createTitledBorder(getMessage(DEPOSITI_TITLE)));

		formModel.getValueModel("depositi").setValue(depositiAzienda);
		if (formModel.hasValueModel("tuttiDepositi")) {
			formModel.getValueModel("tuttiDepositi").setValue(true);
		}
		return scrollPane;
	}

	private DefaultTreeModel createTreeModel() {
		root = new DefaultMutableTreeNode();

		DefaultTreeModel model = new DefaultTreeModel(root);

		Comparator<Deposito> comparatorDeposito = new Comparator<Deposito>() {
			@Override
			public int compare(Deposito o1, Deposito o2) {
				if (o1.getSedeDeposito().equals(o2.getSedeDeposito())) {
					return o1.getTipoDeposito().getId()
							.compareTo(o2.getTipoDeposito().getId());
				}
				return o1.getSedeDeposito().getId()
						.compareTo(o2.getSedeDeposito().getId());
			}
		};

		EventList<Deposito> eventList = GlazedLists.eventList(depositiAzienda);
		GroupingList<Deposito> groupingList = new GroupingList<Deposito>(
				eventList, comparatorDeposito);
		SedeAzienda sedeAziendaCorrente = null;
		DefaultMutableTreeNode sedeNode = null;
		for (List<Deposito> list : groupingList) {
			SedeAzienda sede = list.get(0).getSedeDeposito();
			if (!sede.equals(sedeAziendaCorrente)) {
				sedeNode = new DefaultMutableTreeNode(sede);
				root.add(sedeNode);
				sedeAziendaCorrente = sede;
			}
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(list
					.get(0).getTipoDeposito());
			sedeNode.add(parentNode);

			for (Deposito deposito : list) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(
						deposito);
				parentNode.add(node);
			}
		}

		return model;
	}

	public List<Deposito> getDepositiAzienda() {
		return depositiAzienda;
	}

}
