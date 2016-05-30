/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.magazzino.util.CategoriaLite;

import java.util.List;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

/**
 * Classe per creare il tree delle categorie lite.
 */
public class CategoriaLiteTreeTableFactory {

	/**
	 * Crea il tree node delle categorie.
	 * 
	 * @param categorie
	 *            le categorie con cui creare il tree node
	 * @return DefaultMutableTreeTableNode
	 */
	public DefaultMutableTreeTableNode create(List<CategoriaLite> categorie) {
		DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();

		// aggiungo una categoriaDTO con id-1 er indicare che è la categoria "root"
		CategoriaLite categoriaRoot = new CategoriaLite();
		categoriaRoot.setId(-1);
		root.setUserObject(categoriaRoot);
		for (CategoriaLite categoriaLite : categorie) {
			root.add(createNodeForCategoria(categoriaLite));
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
	private DefaultMutableTreeTableNode createNodeForCategoria(CategoriaLite categoriaLite) {
		DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode(categoriaLite);
		for (CategoriaLite categoriaDTOFiglia : categoriaLite.getCategorieFiglie()) {
			node.add(createNodeForCategoria(categoriaDTOFiglia));
		}
		return node;
	}

}