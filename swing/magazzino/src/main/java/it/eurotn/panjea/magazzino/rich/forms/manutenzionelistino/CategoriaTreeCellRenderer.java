package it.eurotn.panjea.magazzino.rich.forms.manutenzionelistino;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.CategorieTreeTablePage;
import it.eurotn.panjea.magazzino.util.CategoriaLite;

public class CategoriaTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 6374529617764128306L;

    @Override
    public Component getTreeCellRendererComponent(JTree arg0, Object arg1, boolean arg2, boolean arg3, boolean arg4,
            int arg5, boolean arg6) {
        JComponent component = (JComponent) super.getTreeCellRendererComponent(arg0, arg1, arg2, arg3, arg4, arg5,
                arg6);
        component.setOpaque(true);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) arg1;

        if (node.getUserObject() instanceof CategoriaLite) {
            CategoriaLite categoriaLite = (CategoriaLite) node.getUserObject();
            setIcon(RcpSupport.getIcon(CategoriaLite.class.getName()));

            String desc;
            if (categoriaLite.getCodice() != null) {
                desc = categoriaLite.getCodice() + " - " + categoriaLite.getDescrizione();
            } else {
                desc = RcpSupport.getMessage(CategorieTreeTablePage.PAGE_ID + ".rootNode.label");
            }
            setText(desc);
        }
        return component;
    }
}