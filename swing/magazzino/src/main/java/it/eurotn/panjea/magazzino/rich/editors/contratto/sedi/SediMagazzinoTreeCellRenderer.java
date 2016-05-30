/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.contratto.sedi;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.SedeAnagraficaLite;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.image.IconSource;

/**
 * Visualizza la descrizione dell'entita nei nodi che la contengono come userobject, la descrizione della sede negli
 * altri.
 * 
 * @author fattazzo
 * 
 */
public class SediMagazzinoTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {

		IconSource iconSource = (IconSource) Application.services().getService(IconSource.class);

		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		label.setBackground(UIManager.getColor("JPanel.background"));
		Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
		if (userObject instanceof EntitaLite) {
			label.setText(((EntitaLite) userObject).getCodice() + " - "
					+ ((EntitaLite) userObject).getAnagrafica().getDenominazione());

			label.setIcon(iconSource.getIcon(((EntitaLite) userObject).getClass().getName()));
			label.setOpaque(true);
		} else {
			if (userObject instanceof SedeMagazzinoLite) {
				SedeAnagraficaLite sede = ((SedeMagazzinoLite) userObject).getSedeEntita().getSede();
				label.setText(sede.getDescrizione() + (sede.getIndirizzo() != null ? " - " + sede.getIndirizzo() : ""));
				label.setIcon(iconSource.getIcon(SedeMagazzinoLite.class.getName()));

				if (sel) {
					label.setBackground(new Color(197, 203, 214));
				}
			}
		}

		return label;
	}
}
