package it.eurotn.panjea.magazzino.rich.editors.inventarioarticolo;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.tree.TreeUtils;
import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.GroupingList;

import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.SearchableUtils;

public class DepositiInventarioArticoloControl extends AbstractControlFactory {

	private class DepositoTreeRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = -4384749319811218269L;

		@Override
		public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3,
				boolean arg4, int arg5, boolean arg6) {
			JComponent component = (JComponent) super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5,
					arg6);

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) arg1;

			if (node.getUserObject() instanceof SedeAzienda) {
				SedeAzienda sedeAzienda = (SedeAzienda) node.getUserObject();
				setText(sedeAzienda.getSede().getDescrizione() + " - "
						+ sedeAzienda.getSede().getDatiGeografici().getDescrizioneLocalita() + " - "
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

	private DefaultMutableTreeNode root = null;

	private DefaultTreeModel treeModel;

	private CheckBoxTree treeDepositi = null;

	private TreePath treePath;

	private final List<Deposito> depositiAzienda;

	public static final String DEPOSITI_TITLE = "parametriValorizzazioneDepositi.depositi";

	/**
	 * Costruttore.
	 * 
	 */
	public DepositiInventarioArticoloControl() {
		super();

		IMagazzinoDocumentoBD magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
		depositiAzienda = magazzinoDocumentoBD.caricaDepositiPerInventari();
	}

	@Override
	protected JComponent createControl() {

		treeModel = createTreeModel();

		treeDepositi = new CheckBoxTree(treeModel);
		treeDepositi.setRootVisible(false);
		treeDepositi.setShowsRootHandles(true);
		treeDepositi.setOpaque(false);
		treePath = new TreePath(root.getPath());
		treeDepositi.getCheckBoxTreeSelectionModel().setSelectionPath(treePath);
		treeDepositi.setCellRenderer(new DepositoTreeRenderer());
		TreeUtils.expandLevels(treeDepositi, 1, true);
		SearchableUtils.installSearchable(treeDepositi);

		JScrollPane scrollPane = getComponentFactory().createScrollPane(treeDepositi);
		scrollPane.setBorder(BorderFactory.createTitledBorder(getMessage(DEPOSITI_TITLE)));

		return scrollPane;
	}

	/**
	 * @return modello del tree
	 */
	private DefaultTreeModel createTreeModel() {
		root = new DefaultMutableTreeNode();

		DefaultTreeModel model = new DefaultTreeModel(root);

		Comparator<Deposito> comparatorDeposito = new Comparator<Deposito>() {
			@Override
			public int compare(Deposito o1, Deposito o2) {
				if (o1.getSedeDeposito().equals(o2.getSedeDeposito())) {
					return o1.getTipoDeposito().getId().compareTo(o2.getTipoDeposito().getId());
				}
				return o1.getSedeDeposito().getId().compareTo(o2.getSedeDeposito().getId());
			}
		};

		EventList<Deposito> eventList = GlazedLists.eventList(depositiAzienda);
		GroupingList<Deposito> groupingList = new GroupingList<Deposito>(eventList, comparatorDeposito);
		SedeAzienda sedeAziendaCorrente = null;
		DefaultMutableTreeNode sedeNode = null;
		for (List<Deposito> list : groupingList) {
			SedeAzienda sede = list.get(0).getSedeDeposito();
			if (!sede.equals(sedeAziendaCorrente)) {
				sedeNode = new DefaultMutableTreeNode(sede);
				root.add(sedeNode);
				sedeAziendaCorrente = sede;
			}
			DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(list.get(0).getTipoDeposito());
			sedeNode.add(parentNode);

			for (Deposito deposito : list) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(deposito);
				parentNode.add(node);
			}
		}

		return model;
	}

	/**
	 * @return lista dei depositi selezionati
	 */
	public List<DepositoLite> getDepositiSelezionati() {

		TreePath[] selected = treeDepositi.getCheckBoxTreeSelectionModel().getSelectionPaths();

		List<DepositoLite> listDepositiSelezionati = new ArrayList<DepositoLite>();

		if (selected != null) {
			for (TreePath treePathChanged : selected) {
				visitSelectedNode((DefaultMutableTreeNode) treePathChanged.getLastPathComponent(),
						listDepositiSelezionati);
			}
		}

		return listDepositiSelezionati;
	}

	/**
	 * Visita ricorsivamente il nodo aggiornando la lista con i depositi selezionati.
	 * 
	 * @param node
	 *            nodo
	 * @param listSelected
	 *            lista depositi selezionati
	 */
	private void visitSelectedNode(DefaultMutableTreeNode node, List<DepositoLite> listSelected) {

		if (node.getUserObject() instanceof Deposito) {
			listSelected.add(((Deposito) node.getUserObject()).creaLite());
		} else {
			Enumeration<?> children = node.children();
			while (children.hasMoreElements()) {
				DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children.nextElement();
				if (node.getUserObject() instanceof Deposito) {
					listSelected.add(((Deposito) childNode.getUserObject()).creaLite());
				} else {
					visitSelectedNode(childNode, listSelected);
				}
			}
		}
	}

}
