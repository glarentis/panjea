/**
 *
 */
package it.eurotn.panjea.bi.rich.editors.dashboard.filtri.commands;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.pivot.PivotField;

/**
 * @author fattazzo
 *
 */
public class FiltriDashboardTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 6998420622586017090L;

	private static final Icon CATEGORIA_BI_ICON = RcpSupport.getIcon("categoriaBi.icon");
	private static final Icon FIELD_BI_ICON = RcpSupport.getIcon("fieldBi.icon");

	/**
	 * Costruttore.
	 */
	public FiltriDashboardTreeCellRenderer() {
		super();
		setLeafIcon(FIELD_BI_ICON);
		setClosedIcon(CATEGORIA_BI_ICON);
		setOpenIcon(CATEGORIA_BI_ICON);
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		if (((DefaultMutableTreeNode) value).getUserObject() instanceof PivotField) {
			PivotField field = (PivotField) ((DefaultMutableTreeNode) value).getUserObject();
			label.setText(field.getTitle());
		}

		return label;
	}
}
