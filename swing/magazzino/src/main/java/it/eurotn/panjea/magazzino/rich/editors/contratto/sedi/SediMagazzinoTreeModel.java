/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.contratto.sedi;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * @author fattazzo
 * 
 */
public class SediMagazzinoTreeModel extends DefaultTreeModel {

	private static final long serialVersionUID = 1L;

	private Map<Integer, SedeMagazzinoLite> sediAggiunte = null;

	/**
	 * Costruttore di default.
	 * 
	 * @param node
	 *            il nodo con cui creare il tree
	 */
	public SediMagazzinoTreeModel(final TreeNode node) {
		super(node);
		sediAggiunte = new HashMap<Integer, SedeMagazzinoLite>();
		Enumeration<DefaultMutableTreeNode> enumClienti = node.children();
		while (enumClienti.hasMoreElements()) {
			DefaultMutableTreeNode nodeEntita = enumClienti.nextElement();
			Enumeration<DefaultMutableTreeNode> enumSedi = nodeEntita.children();
			while (enumSedi.hasMoreElements()) {
				DefaultMutableTreeNode nodeSede = enumSedi.nextElement();
				SedeMagazzinoLite sedeMagazzinoLite = (SedeMagazzinoLite) nodeSede.getUserObject();
				sediAggiunte.put(sedeMagazzinoLite.getId(), sedeMagazzinoLite);
			}
		}
	}

	/**
	 * Aggiunge una sede magazzino al contratto e la inserisce nel modello creando anche il nodo entità se necessario.
	 * 
	 * @param sedeMagazzinoLite
	 *            la sede da aggiungere al tree
	 */
	public boolean aggiungiSede(SedeMagazzinoLite sedeMagazzinoLite) {
		boolean added = false;
		if (sedeMagazzinoLite.isNew() || sediAggiunte.get(sedeMagazzinoLite.getId()) == null) {

			// creo il nodo per la sede
			DefaultMutableTreeNode sedeNode = new DefaultMutableTreeNode(sedeMagazzinoLite);

			// cerco se nel modello esiste gia' l'entita' della sede
			DefaultMutableTreeNode entitaNode = getNodeForEntita(sedeMagazzinoLite.getSedeEntita().getEntita());

			if (entitaNode == null) {
				entitaNode = new DefaultMutableTreeNode(sedeMagazzinoLite.getSedeEntita().getEntita());
				entitaNode.add(sedeNode);

				insertNodeInto(entitaNode, (DefaultMutableTreeNode) getRoot(),
						((DefaultMutableTreeNode) getRoot()).getChildCount());
			} else {
				entitaNode.add(sedeNode);
			}

			setRoot((DefaultMutableTreeNode) getRoot());
			sediAggiunte.put(sedeMagazzinoLite.getId(), sedeMagazzinoLite);
			added = true;
		}
		return added;
	}

	/**
	 * Cerca nel modello il nodo dell'entita'. Se non viene trovato restituisce <code>null</code>.
	 * 
	 * @param entita
	 *            Entita' da ricercare
	 * @return Nodo trovato
	 */
	@SuppressWarnings("unchecked")
	private DefaultMutableTreeNode getNodeForEntita(EntitaLite entita) {
		if (((DefaultMutableTreeNode) getRoot()).getChildCount() > 0) {

			for (Enumeration e = ((DefaultMutableTreeNode) getRoot()).children(); e.hasMoreElements();) {
				DefaultMutableTreeNode entitaNode = (DefaultMutableTreeNode) e.nextElement();
				EntitaLite entitaTrovata = (EntitaLite) entitaNode.getUserObject();

				// verifico che siano uguali id e classe
				if (entitaTrovata.getClass().getName().equals(entita.getClass().getName())
						&& entitaTrovata.getId().equals(entita.getId())) {
					return entitaNode;
				}
			}
		}
		return null;
	}

	/**
	 * Indica se al contratto è associata l'intera entità.
	 * 
	 * @param entitaLite
	 *            entita
	 * @return <code>true</code> se l'entità è associata
	 */
	public boolean isEntitaAssociata(EntitaLite entitaLite) {

		boolean entitaAssociata = false;

		DefaultMutableTreeNode entitaNode = getNodeForEntita(entitaLite);

		if (entitaNode == null) {
			entitaAssociata = false;
		} else {
			// prendo in considerazione solo il primo figlio. Se la sede contenuta ha di nullo
			// significa che l'entità è associata
			SedeMagazzinoLite sede = (SedeMagazzinoLite) ((DefaultMutableTreeNode) entitaNode.getChildAt(0))
					.getUserObject();

			entitaAssociata = sede.isNew();
		}

		return entitaAssociata;
	}

	/**
	 * Rimuove dalla tree la sede magazzino.
	 * 
	 * @param sedeNode
	 *            il nodo che contiene la sede magazzino
	 */
	public void rimuoviSede(DefaultMutableTreeNode sedeNode) {
		SedeMagazzinoLite sedeMagazzinoLite = (SedeMagazzinoLite) sedeNode.getUserObject();
		sediAggiunte.remove(sedeMagazzinoLite.getId());

		// controllo se il nodo entita ha solo una sede
		if (sedeNode.getParent().getChildCount() == 1) {
			// cancello il nodo dell'entita
			removeNodeFromParent((MutableTreeNode) sedeNode.getParent());
		} else {
			// cancello solo il nodo della sede selezionata
			removeNodeFromParent(sedeNode);
		}
	}
}
