package it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo;

import it.eurotn.panjea.magazzino.util.CategoriaLite;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.util.RcpSupport;

public class CategoriaLiteTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final int LABEL_WIDTH_OVERSIZE = 50;
	private static final long serialVersionUID = -8813682511205137384L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		IconSource iconSource = (IconSource) ApplicationServicesLocator.services().getService(IconSource.class);
		JLabel c = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
		// setto l'icona
		CategoriaLite categoriaLite = (CategoriaLite) node.getUserObject();
		if (categoriaLite != null && categoriaLite.getId() != null && categoriaLite.getId() != -1) {
			c.setIcon(iconSource.getIcon(node.getUserObject().getClass().getName()));
			c.setText(categoriaLite.getCodice() + " - " + categoriaLite.getDescrizione());

			Dimension retDimension = c.getPreferredSize();

			// HACK se non ridimensiono la label e il nuovo text è più
			// grande di quello di prima questo mi
			// viene
			// tagliato e aggiunti 3 punti in fondo
			if (retDimension != null) {
				retDimension = new Dimension(retDimension.width + LABEL_WIDTH_OVERSIZE, retDimension.height);
			}
			c.setPreferredSize(retDimension);
		} else {
			// il nodo è il root
			c.setIcon(iconSource.getIcon(CategorieTreeTablePage.CATEGORIA_ROOT_NODE_ICON));
			c.setText(RcpSupport.getMessage(CategorieTreeTablePage.CATEGORIA_ROOT_NODE_LABEL));
		}

		return c;
	}
}