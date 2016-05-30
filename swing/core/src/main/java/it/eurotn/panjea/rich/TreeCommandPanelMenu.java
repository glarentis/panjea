/**
 *
 */
package it.eurotn.panjea.rich;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.tree.TreeUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.SimpleScrollPane;
import com.jidesoft.tooltip.ExpandedTipUtils;

import it.eurotn.panjea.rich.bd.ISicurezzaBD;
import it.eurotn.panjea.rich.bd.SicurezzaBD;
import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.command.DynamicCommandGroup;

/**
 * Pannello utilizzato per inserire nel men� generale i comandi con un tree. La creazione dei comandi avviene tramite
 * <code>ApplicationEvent</code> <code>LoginEvent</code> per verificare l'autorizzazione dei commands contenuti
 *
 * @author Aracno
 * @version 1.0, 04/dic/06
 *
 */
public class TreeCommandPanelMenu extends AbstractMenuPanel {

    /**
     *
     * Classe che gestisce il renderer delle celle della TreeView se il valore del ramo � istanza di
     * <code>ActionCommand</code> restituisco il pulsante creato dall'action del valore altrimenti ritorno il controllo
     * creato dal <code>DefaultTreeCellRenderer</code>.
     *
     * @author Aracno
     * @version 1.0, 04/dic/06
     *
     */
    private class CustomCellRenderer extends DefaultTreeCellRenderer {

        /**
         * Comment for <code>serialVersionUID</code>.
         */
        private static final long serialVersionUID = 6639381067581421473L;

        @Override
        public Component getTreeCellRendererComponent(JTree paramTree, Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {
            JLabel c = (JLabel) super.getTreeCellRendererComponent(paramTree, value, selected, expanded, leaf, row,
                    hasFocus);
            IconNode node = (IconNode) value;
            // setto l'icona
            if (node.getCommand() instanceof ActionCommand) {
                ActionCommand command = node.getCommand();
                AbstractButton button = command.createButton();
                c.setText(button.getText());
                c.setIcon(button.getIcon());

                return c;
            } else {
                if (node.getUserObject() instanceof String) {
                    IconNodeRenderer defaultRenderer = new IconNodeRenderer();
                    return defaultRenderer.getTreeCellRendererComponent(paramTree, value, selected, expanded, leaf, row,
                            hasFocus);
                }
            }
            return c;
        }
    }

    class IconNodeRenderer extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = -1625907159823194783L;

        private final Icon closedFolderIcon = RcpSupport.getIcon("tree.closedFolder.icon");
        private final Icon openFolderIcon = RcpSupport.getIcon("tree.openFolder.icon");

        {
            setClosedIcon(closedFolderIcon);
            setOpenIcon(openFolderIcon);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree paramTree, Object value, boolean sel, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(paramTree, value, sel, expanded, leaf, row, hasFocus);

            Icon icon = ((IconNode) value).getIcon();

            if (icon == null) {
                @SuppressWarnings("unchecked")
                Hashtable<String, Icon> treeIcons = (Hashtable<String, Icon>) paramTree
                        .getClientProperty("JTree.icons");
                String name = ((IconNode) value).getIconName();
                if ((treeIcons != null) && (name != null)) {
                    icon = treeIcons.get(name);
                    if (icon != null) {
                        setIcon(icon);
                    }
                }
            } else {
                setIcon(icon);
            }

            return this;
        }
    }

    protected JTree tree;

    private List<Object> commands = new ArrayList<Object>();

    private Hashtable<String, Icon> icons = new Hashtable<String, Icon>();

    private Set<String> utenteMenuDisableCommands = null;

    /**
     * Costruttore.
     */
    public TreeCommandPanelMenu() {
        super();
        // setto come non singleton perch� ho la necessit� per la sicurezza che
        // i controlli vengano ricreati ogni volta (il tree va ricreato per
        // verificare
        // se il command � authorized)
        setSingleton(false);
    }

    @Override
    protected JComponent createControl() {
        SimpleScrollPane scrollPane = null;
        try {
            TreeModel model = createTreeModel();
            createJTree(model);
            JPanel panel = getComponentFactory().createPanel(new BorderLayout());
            panel.add(tree, BorderLayout.CENTER);
            customizeMainPanel(panel);
            scrollPane = new SimpleScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return scrollPane;
    }

    /**
     * Crea il tree del menu.
     *
     * @param treeModel
     *            modello
     */
    private void createJTree(TreeModel treeModel) {
        tree = new JTree(treeModel);
        tree.setName(getTitle() + "Menu");
        tree.setRootVisible(false);
        tree.setRowHeight(0);
        // for (int i=0; i<tree.getRowCount(); i++) tree.expandRow(i);
        tree.setCellRenderer(new CustomCellRenderer());
        tree.putClientProperty("JTree.icons", icons);
        tree.setShowsRootHandles(true);
        tree.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tree.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                TreePath path = tree.getSelectionPath();
                if (path != null) {
                    IconNode node = (IconNode) path.getLastPathComponent();
                    if (node.getCommand() != null) {
                        node.getCommand().execute();
                        tree.clearSelection();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
        tree.setBorder(new EmptyBorder(3, 3, 3, 3));
        SearchableUtils.installSearchable(tree);
        TreeUtils.expandAll(tree, true);
        ExpandedTipUtils.install(tree);
    }

    /**
     * Crea la lista dei nodi.
     *
     * @param dynamicCommandGroup
     *            dynamicCommandGroup
     * @return nodi creati
     */
    private List<IconNode> createMenuCommandGroupTreeNode(DynamicCommandGroup dynamicCommandGroup) {

        Map<String, List<ActionCommand>> map = dynamicCommandGroup.getMapItem();
        Set<String> gruppi = map.keySet();
        List<IconNode> listNodi = new ArrayList<IconNode>();

        for (String gruppo : gruppi) {
            IconNode gruppoNode = new IconNode(getMessageSource().getMessage("menu.gruppo." + gruppo + ".title",
                    new Object[] {}, Locale.getDefault()));
            gruppoNode.setIconName("menu.gruppo." + gruppo + ".icon");
            Icon icon = getIconSource().getIcon("menu.gruppo." + gruppo + ".icon");
            if (icon != null) {
                icons.put("menu.gruppo." + gruppo + ".icon", icon);
            }

            for (ActionCommand command : map.get(gruppo)) {
                IconNode child = new IconNode(command.getText());
                child.setCommand(command);
                gruppoNode.add(child);
            }

            listNodi.add(gruppoNode);
        }

        return listNodi;
    }

    /**
     * Crea i nodi per i comandi specificati.
     *
     * @return nodo principale
     */
    private IconNode createNodes() {

        Map<Object, IconNode> mapNodes = new HashMap<Object, IconNode>();
        IconNode rootNode = new IconNode(this.getId());
        String prevGroup = null;
        icons = new Hashtable<String, Icon>();
        for (Object command : this.commands) {

            if (command instanceof DynamicCommandGroup) {
                List<IconNode> listNode = createMenuCommandGroupTreeNode((DynamicCommandGroup) command);

                for (IconNode node : listNode) {
                    // mapNodes.put(node.getUserObject(), node);
                    rootNode.add(node);
                }
            } else if (command instanceof String && !getUtenteMenuDisableCommands().contains(command)) {
                String commandString = (String) command;
                if (commandString.startsWith("group:")) {
                    prevGroup = commandString;
                    if (!mapNodes.containsKey(commandString)) {
                        // se non esite nella mappa il gruppo creo un nodo e lo
                        // inserisco
                        String[] tokenGruppo = commandString.split(":");
                        String idNodeI18N = "menu.gruppo";
                        for (int i = 1; i < tokenGruppo.length; i++) {
                            idNodeI18N = idNodeI18N + "." + tokenGruppo[i];
                        }
                        IconNode node = new IconNode(getMessageSource().getMessage(idNodeI18N + ".title",
                                new Object[] {}, Locale.getDefault()));
                        node.setIconName(idNodeI18N + ".icon");
                        if (getIconSource().getIcon(idNodeI18N + ".icon") != null) {
                            icons.put(idNodeI18N + ".icon", getIconSource().getIcon(idNodeI18N + ".icon"));
                        }
                        mapNodes.put(commandString, node);
                        rootNode.add(node);

                        // se il nodo � legato a un gruppo padre lo inserisco
                        // come gruppo figlio
                        String gruppoPadre = new String();
                        for (int i = 0; i < tokenGruppo.length - 1; i++) {
                            if (gruppoPadre.length() > 0) {
                                gruppoPadre = gruppoPadre + ":";
                            }
                            gruppoPadre = gruppoPadre + tokenGruppo[i];
                        }
                        if (mapNodes.containsKey(gruppoPadre)) {
                            IconNode nodePadre = mapNodes.get(gruppoPadre);
                            // MAIL NPE
                            if (nodePadre == null) {
                                logger.error("--> Errore, nodo padre null!");
                            } else {
                                nodePadre.add(node);
                                mapNodes.put(gruppoPadre, nodePadre);
                            }
                        }
                    }
                } else {
                    IconNode node = mapNodes.get(prevGroup);
                    if (node != null) {
                        ActionCommand actionCommand = (ActionCommand) Application.instance().getActiveWindow()
                                .getCommandManager().getCommand(commandString);
                        org.springframework.util.Assert.notNull(actionCommand,
                                "Il comando non esiste " + commandString);
                        if (actionCommand.isAuthorized()) {
                            IconNode child = new IconNode(actionCommand.getText());
                            child.setCommand(actionCommand);
                            node.add(child);
                            mapNodes.put(prevGroup, node);
                        }
                    }
                }
            }
        }
        removeNodeWithoutChildren(rootNode);
        return rootNode;

    }

    /**
     * @return tree model
     */
    protected TreeModel createTreeModel() {
        IconNode root = createNodes();
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
    }

    /**
     * @param panel
     *            panelto customize
     */
    protected void customizeMainPanel(JPanel panel) {

    }

    /**
     * @return comandi del pannello
     */
    public List<Object> getCommands() {
        return commands;
    }

    /**
     * @return Returns the utenteMenuDisableCommand.
     */
    public Set<String> getUtenteMenuDisableCommands() {
        if (utenteMenuDisableCommands == null) {
            ISicurezzaBD sicurezzaBD = RcpSupport.getBean(SicurezzaBD.BEAN_ID);
            Utente utente = sicurezzaBD.caricaUtente(PanjeaSwingUtil.getUtenteCorrente().getUserName());
            utenteMenuDisableCommands = utente.getMenuDisableCommands();
        }

        return utenteMenuDisableCommands;
    }

    /**
     * per la TreeCommandPanelMenu vengono scorsi i command e viene verificato che almento uno di questi sia autorizzato
     * appena trova element esce dal metodo restituendo true.
     *
     * @return <code>false</code> se non ci sono elementi
     */
    @Override
    public boolean hasElements() {
        for (Object object : commands) {
            if (object instanceof DynamicCommandGroup) {
                // se il command corrente � un DynamicCommandGroup si desume che
                // il CommandPanel ha elementi al suo
                // interno
                return true;
            } else {
                String commandString = (String) object;
                if (!commandString.startsWith("group:") && !getUtenteMenuDisableCommands().contains(commandString)) {
                    // recupera il command dal contesto e ne verifica
                    // l'autorizzazione
                    ActionCommand actionCommand = (ActionCommand) Application.instance().getActiveWindow()
                            .getCommandManager().getCommand(commandString);
                    org.springframework.util.Assert.notNull(actionCommand, "Il comando non esiste " + commandString);
                    if (actionCommand.isAuthorized()) {
                        return true;
                    }
                }

            }

        }
        return false;
    }

    /**
     * metodo ricorsivo incaricato di rimuovere dal root node di una JTree tutti i group node che non hanno command node
     * al loro interno.
     *
     * @param currentNode
     *            nodo di riferimento
     */
    @SuppressWarnings("unchecked")
    private void removeNodeWithoutChildren(IconNode currentNode) {
        logger.debug("--> Enter removeNodeWithoutChildren");
        Enumeration<IconNode> childs = currentNode.children();
        List<IconNode> iconNodesToRemove = new ArrayList<IconNode>();
        IconNode iconNode = null;
        while (childs.hasMoreElements()) {
            iconNode = childs.nextElement();
            // se il node contiene dei child li scorre ricorsivamente altrimenti
            // se non contiene un ActionCommand viene aggiunto ad una List per
            // la sua successiva rimozione
            if (iconNode.getChildCount() != 0) {
                removeNodeWithoutChildren(iconNode);
            } else if (iconNode.getCommand() == null) {
                iconNodesToRemove.add(iconNode);
            }
        }
        // rimozioni di tutti i node dal proprio parent
        for (IconNode iconNodeToRemove : iconNodesToRemove) {
            iconNodeToRemove.removeFromParent();
        }

        logger.debug("--> Exit removeNodeWithoutChildren");
    }

    /**
     * @param commands
     *            comandi delle tree
     */
    public void setCommands(List<Object> commands) {
        this.commands = commands;
    }
}
