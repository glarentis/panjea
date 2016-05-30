package it.eurotn.panjea.vending.rich.editors.distributore.modelli;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.vending.domain.Modello;

public class ModelloTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = -8813682511205137384L;

    private static final Icon ICON = RcpSupport.getIcon("tabelle");
    private static final Icon ICON_MODELLO = RcpSupport.getIcon(Modello.class.getName());

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {
        JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
        EntityBase entity = (EntityBase) node.getUserObject();
        label.setIcon(null);
        if (entity == null || entity.isNew()) {
            // il nodo è il root
            label.setText("Modelli distributori");
            label.setIcon(ICON_MODELLO);
        } else {
            label.setText(ObjectConverterManager.toString(entity));
            label.setIcon(entity instanceof Modello ? ICON_MODELLO : ICON);
        }

        return label;
    }
}