package it.eurotn.panjea.sicurezza.rich.editors;

import it.eurotn.panjea.sicurezza.domain.Permesso;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.CheckBoxTree;

public class PermessiCheckBoxTree extends CheckBoxTree {

    private static Logger logger = Logger.getLogger(PermessiCheckBoxTree.class);

    private static final long serialVersionUID = -3662350579369200371L;

    /**
     * Costruttore.
     *
     * @param treeModel
     *            modello del tree
     */
    public PermessiCheckBoxTree(final TreeModel treeModel) {
        super(treeModel);
        this.setRootVisible(false);
        this.setDigIn(true);
        this.setShowsRootHandles(true);
        this.setCheckBoxEnabled(false);
        this.setOpaque(false);
        installRenderer();
    }

    /**
     * Crea tutti i nodi del tree.
     *
     * @param listAvailablePermessi
     *            lista dei permessi disponibili
     * @param listRuoloPermessi
     *            lista dei permessi associati al ruolo
     */
    public void createNodes(List<Permesso> listAvailablePermessi, List<Permesso> listRuoloPermessi) {

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("ROOT");

        String currentModulo = null;
        String currentSottoModulo = null;
        Integer currentPermesso = null;
        DefaultMutableTreeNode nodeModulo = null;
        DefaultMutableTreeNode nodeSottoModulo = null;

        List<DefaultMutableTreeNode> listNodeToUnselect = new ArrayList<DefaultMutableTreeNode>();

        for (Permesso permesso : listAvailablePermessi) {

            if (currentModulo == null || !currentModulo.equals(permesso.getModulo())) {
                nodeModulo = new DefaultMutableTreeNode(permesso.getModulo());
                rootNode.add(nodeModulo);

                currentModulo = permesso.getModulo();
            }

            if (currentSottoModulo == null || !currentSottoModulo.equals(permesso.getSottoModulo())) {
                nodeSottoModulo = new DefaultMutableTreeNode(permesso.getSottoModulo());
                nodeModulo.add(nodeSottoModulo);

                currentSottoModulo = permesso.getSottoModulo();
            }

            if (currentPermesso == null || !currentPermesso.equals(permesso.getId())) {
                DefaultMutableTreeNode nodePermesso = new DefaultMutableTreeNode(permesso);
                nodeSottoModulo.add(nodePermesso);

                currentPermesso = permesso.getId();

                if (!listRuoloPermessi.contains(permesso)) {
                    listNodeToUnselect.add(nodePermesso);

                }
            }
        }

        this.setModel(new DefaultTreeModel(rootNode));

        getCheckBoxTreeSelectionModel().setSelectionPath(new TreePath(getModel().getRoot()));

        for (DefaultMutableTreeNode nodeToRemove : listNodeToUnselect) {
            getCheckBoxTreeSelectionModel().removeSelectionPath(new TreePath(nodeToRemove.getPath()));
        }
    }

    /**
     * @return lista dei permessi selezionati
     */
    public List<Permesso> getPermessiSelezionati() {
        TreePath[] selected = this.getCheckBoxTreeSelectionModel().getSelectionPaths();

        Set<Permesso> setPermessiSelezionati = new HashSet<Permesso>();

        if (selected != null) {
            for (TreePath treePath : selected) {
                visitSelectedNode((DefaultMutableTreeNode) treePath.getLastPathComponent(), setPermessiSelezionati);
            }
        }

        return new ArrayList<Permesso>(setPermessiSelezionati);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(400, 400);
    }

    /**
     * Installa il renderer sul tree.
     */
    private void installRenderer() {
        this.setCellRenderer(new DefaultTreeCellRenderer() {

            private static final long serialVersionUID = -8813682511205137384L;

            @Override
            public Color getBackground() {
                return null;
            }

            @Override
            public Color getBackgroundNonSelectionColor() {
                return null;
            }

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {

                JLabel c = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

                if (node.getUserObject() instanceof String) {
                    c.setText((String) node.getUserObject());
                    if (node.getParent() == PermessiCheckBoxTree.this.getModel().getRoot()) {
                        c.setIcon(RcpSupport.getIcon("plugins"));
                    } else {
                        c.setIcon(RcpSupport.getIcon("modulo"));
                    }

                } else if (node.getUserObject() instanceof Permesso) {
                    Permesso permesso = (Permesso) node.getUserObject();
                    c.setText(permesso.getDescrizione());

                    // setto l'icona
                    if (node.getUserObject() != null) {
                        c.setIcon(RcpSupport.getIcon(Permesso.class.getName()));
                    }
                }

                c.setOpaque(false);
                return c;
            }
        });
    }

    /**
     * Visita ricorsivamente i nodi aggiornando la lista dei permessi.
     *
     * @param node
     *            nodo da visitare
     * @param listSelected
     *            lista dei permessi
     */
    private void visitSelectedNode(DefaultMutableTreeNode node, Set<Permesso> listSelected) {
        if (node.getUserObject() instanceof Permesso) {
            logger.debug("--> Il nodo e' di tipo permesso: " + ((Permesso) node.getUserObject()).getCodice());
            listSelected.add((Permesso) node.getUserObject());
        } else {
            if (node.getUserObject() instanceof String) {
                logger.debug("--> Il nodo e' di tipo String: " + (String) node.getUserObject());
                @SuppressWarnings("rawtypes")
                Enumeration children = node.children();
                while (children.hasMoreElements()) {
                    DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children.nextElement();
                    if (childNode.getUserObject() instanceof Permesso) {
                        listSelected.add((Permesso) childNode.getUserObject());
                    } else {
                        visitSelectedNode(childNode, listSelected);
                    }
                }
            }
        }
    }
}