package it.eurotn.panjea.sicurezza.rich.editors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.JideTabbedPane;

import it.eurotn.panjea.sicurezza.rich.pm.UtentePM;
import it.eurotn.rich.form.PanjeaAbstractForm;

public class UtenteMenuDiableCommandsForm extends PanjeaAbstractForm {

    private class CheckBoxTreeSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent event) {

            if (getFormModel().isReadOnly()) {
                return;
            }

            Collection<String> disableCommands = getDisableCommands();

            getFormModel().getValueModel("utente.menuDisableCommands").setValue(disableCommands);
        }

    }

    private class FormObjectPropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            updateMenuControls();
        }

    }

    private class MenuRenderer extends DefaultTreeCellRenderer {

        private static final long serialVersionUID = 8462774423946051788L;

        private Map<String, Icon> iconCache = new HashMap<String, Icon>();

        /*
         * (non-Javadoc)
         *
         * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
         * java.lang.Object, boolean, boolean, boolean, int, boolean)
         */
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {
            JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            String name = "";
            if (((DefaultMutableTreeNode) value).getUserObject() != null) {
                name = getValueName((String) ((DefaultMutableTreeNode) value).getUserObject());
            }

            Icon icon = iconCache.get(name);
            if (icon == null) {
                icon = RcpSupport.getIcon(name + ".icon");
                iconCache.put(name, icon);
            }

            if (name.contains("Command")) {
                label.setText(RcpSupport.getMessage(name + ".label"));
            } else {
                label.setText(RcpSupport.getMessage(name + ".title"));
            }

            label.setIcon(icon);
            return label;
        }

        /**
         * Restituisce il nome del comando per la ricerca sui file properties.
         *
         * @param value
         *            valore
         * @return nome comando
         */
        private String getValueName(String value) {

            if (value.contains(":")) {
                value = "menu." + value.replace("group", "gruppo").replaceAll(":", ".");
            }

            return value;
        }

    }

    private class ReadOnlyPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            Boolean readOnly = (Boolean) evt.getNewValue();

            for (CheckBoxTree checkBoxTree : checkBoxTrees) {
                checkBoxTree.setEnabled(!readOnly);
            }

        }

    }

    public static final String FORM_ID = "utenteMenuDiableCommandsForm";

    private Map<String, List<String>> mapCommands = new LinkedHashMap<String, List<String>>();

    private MenuRenderer menuRenderer = new MenuRenderer();

    private JideTabbedPane jideTabbedPane;

    private List<CheckBoxTree> checkBoxTrees = new ArrayList<CheckBoxTree>();

    private FormObjectPropertyChange formObjectPropertyChange;
    private ReadOnlyPropertyChangeListener readOnlyPropertyChangeListener;

    /**
     * Costruttore.
     *
     * @param formModel
     *            form model
     */
    public UtenteMenuDiableCommandsForm(final FormModel formModel) {
        super(formModel, FORM_ID);
    }

    /**
     * Crea la {@link CheckBoxTree} per la selezione dei comandi da abilitare e disabilitare.
     *
     * @param values
     *            lista dei comandi da visualizzare
     * @param utenteDisbleCommands
     *            lista dei comadi disabilitati per l'utente
     * @return {@link CheckBoxTree} creata
     */
    private CheckBoxTree createCheckBoxTree(List<String> values, Set<String> utenteDisbleCommands) {

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        DefaultMutableTreeNode groupNode = null;

        for (String item : values) {

            if (item.startsWith("group")) {
                groupNode = new DefaultMutableTreeNode(item);
                rootNode.add(groupNode);
            } else {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(item);
                groupNode.add(childNode);
            }
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        CheckBoxTree checkBoxTree = new CheckBoxTree(treeModel);
        checkBoxTree.setCellRenderer(menuRenderer);
        checkBoxTree.setRootVisible(true);
        checkBoxTree.getCheckBoxTreeSelectionModel().addTreeSelectionListener(new CheckBoxTreeSelectionListener());
        checkBoxTree.setEnabled(false);
        checkBoxTrees.add(checkBoxTree);

        checkBoxTree.getCheckBoxTreeSelectionModel().setSelectionPath(new TreePath(rootNode.getPath()));

        List<DefaultMutableTreeNode> listNodeToUnselect = new ArrayList<DefaultMutableTreeNode>();
        fillUnselectNode(rootNode, listNodeToUnselect, utenteDisbleCommands);

        for (DefaultMutableTreeNode nodeToRemove : listNodeToUnselect) {
            checkBoxTree.getCheckBoxTreeSelectionModel().removeSelectionPath(new TreePath(nodeToRemove.getPath()));
        }

        for (int i = checkBoxTree.getRowCount() - 1; i >= 0; i--) {
            checkBoxTree.expandRow(i);
        }

        return checkBoxTree;
    }

    @Override
    protected JComponent createFormControl() {

        loadApplicationCommands();

        jideTabbedPane = new JideTabbedPane();
        updateMenuControls();

        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(jideTabbedPane, BorderLayout.CENTER);

        formObjectPropertyChange = new FormObjectPropertyChange();
        addFormObjectChangeListener(formObjectPropertyChange);

        readOnlyPropertyChangeListener = new ReadOnlyPropertyChangeListener();
        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, readOnlyPropertyChangeListener);

        return rootPanel;
    }

    /**
     * Crea i controlli per la visualizzazione dei menu e comandi.
     *
     * @param values
     *            lista di comandi da visualizzare
     * @param utenteDisbleCommands
     *            comandi disabilitati per l'utente
     * @return controlli creati
     */
    private JComponent createTabComponent(List<String> values, Set<String> utenteDisbleCommands) {

        CheckBoxTree checkBoxTree = createCheckBoxTree(values, utenteDisbleCommands);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(checkBoxTree), BorderLayout.CENTER);

        JLabel removeLabel = new JLabel(
                "<html>Selezionare le voci del menu da rimuovere. <b>Dopo aver salvato riavviare Panjea per applicare le modifiche al menu.</b><html>");
        GuiStandardUtils.attachBorder(removeLabel);
        panel.add(removeLabel, BorderLayout.NORTH);

        GuiStandardUtils.attachBorder(panel);

        return panel;
    }

    @Override
    public void dispose() {

        removeFormObjectChangeListener(formObjectPropertyChange);
        formObjectPropertyChange = null;

        super.dispose();
    }

    /**
     * Avvalora la lista dei nodi da non selezionare in base a quelli definiti dall'utente.
     *
     * @param root
     *            nodo di partenza
     * @param listNodeToUnselect
     *            lista dei nodi da non selezionare
     * @param utenteDiableCommands
     *            lista dei comandi disabilitati per l'utente
     */
    public void fillUnselectNode(DefaultMutableTreeNode root, List<DefaultMutableTreeNode> listNodeToUnselect,
            Set<String> utenteDiableCommands) {

        if (utenteDiableCommands != null && utenteDiableCommands.contains(root.getUserObject())) {
            listNodeToUnselect.add(root);
        }
        @SuppressWarnings("rawtypes")
        Enumeration children = root.children();
        if (children != null) {
            while (children.hasMoreElements()) {
                fillUnselectNode((DefaultMutableTreeNode) children.nextElement(), listNodeToUnselect,
                        utenteDiableCommands);
            }
        }
    }

    /**
     * Restituisce i comandi attualmente disabilitati nelle vari chackbox.
     *
     * @return lista di comandi disabilitati
     */
    private Collection<String> getDisableCommands() {

        Set<String> selectedCommands = new TreeSet<String>();
        for (CheckBoxTree checkBox : checkBoxTrees) {

            selectedCommands.addAll(visitSelectedNode((DefaultMutableTreeNode) checkBox.getModel().getRoot(),
                    new TreeSet<String>(), checkBox));
        }

        List<String> allCommands = new ArrayList<String>();
        for (List<String> commands : mapCommands.values()) {
            allCommands.addAll(commands);
        }

        List<String> disableCommands = new ArrayList<String>(allCommands);
        Collections.copy(disableCommands, allCommands);
        disableCommands.removeAll(selectedCommands);

        return new TreeSet<String>(disableCommands);
    }

    /**
     * Crea il {@link TreePath} per il nodo specificato.
     *
     * @param node
     *            nodo
     * @return {@link TreePath} creato
     */
    public TreePath getPath(TreeNode node) {
        List<TreeNode> list = new ArrayList<TreeNode>();

        // Add all nodes to list
        while (node != null) {
            list.add(node);
            node = node.getParent();
        }
        Collections.reverse(list);

        // Convert array of nodes to TreePath
        return new TreePath(list.toArray());
    }

    /**
     * Carica tutti i comandi per i menu definiti nell'applicazione.
     */
    private void loadApplicationCommands() {

        File file = new File("panjea-menu-context.xml");

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setValidating(false);
        Document doc = null;
        try {
            docBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder docBuilder;
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(file);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento del file con la definizione dei comandi", e);
            throw new RuntimeException("errore durante il caricamento del file con la definizione dei comandi", e);
        }

        NodeList rootNode = doc.getElementsByTagName("bean");

        for (int s = 0; s < rootNode.getLength(); s++) {
            Node node = rootNode.item(s);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;

                // nome del plugin
                String plugin = element.getAttribute("id");

                NodeList nodeCommands = element.getElementsByTagName("value");

                for (int c = 0; c < nodeCommands.getLength(); c++) {
                    List<String> commands = mapCommands.get(plugin);
                    if (commands == null) {
                        commands = new ArrayList<String>();
                    }
                    commands.add(nodeCommands.item(c).getTextContent());
                    mapCommands.put(plugin, commands);
                }
            }

        }
    }

    @Override
    public void revert() {
        super.revert();
        updateMenuControls();
    }

    /**
     * Aggiorna la lista di menu selezionati o deselazionati per l'utente caricato.
     */
    private void updateMenuControls() {

        UtentePM utente = (UtentePM) getFormModel().getFormObject();
        Set<String> utenteDiableCommands = utente.getUtente().getMenuDisableCommands();

        jideTabbedPane.removeAll();
        checkBoxTrees.clear();

        for (Entry<String, List<String>> entry : mapCommands.entrySet()) {

            JComponent tabComponent = createTabComponent(entry.getValue(), utenteDiableCommands);

            jideTabbedPane.addTab(RcpSupport.getMessage(entry.getKey() + ".title"),
                    RcpSupport.getIcon(entry.getKey() + ".icon"), tabComponent);
        }
    }

    /**
     * Scorre ricorsivamente la tree check di tipi documento aggiungendo ad una lista tutti i figli trovati.
     *
     * @param node
     *            il nodo da cui partire
     * @param listSelected
     *            lista di elementi per riferimento a cui aggiungo tutti gli elementi
     * @return lista di oggetti selezionati
     */
    private Set<String> visitAllNode(DefaultMutableTreeNode node, Set<String> listSelected) {

        if (node.getUserObject() != null) {
            listSelected.add((String) node.getUserObject());
        }

        @SuppressWarnings("unchecked")
        Enumeration<Object> children = node.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children.nextElement();
            listSelected = visitAllNode(childNode, listSelected);
        }
        return listSelected;
    }

    /**
     * Scorre ricorsivamente la tree check solamente per i nodi parzialmente o totalmente selezionati.
     *
     * @param root
     *            nodo di partenza
     * @param listNodeUserObject
     *            lista di tutti i nodi selezionati
     * @param checkBox
     *            checkbox di riferimento
     * @return lista di oggetti selezionati
     */
    public Set<String> visitSelectedNode(DefaultMutableTreeNode root, Set<String> listNodeUserObject,
            CheckBoxTree checkBox) {

        // se il nodo è parzialmente selezionato aggiungo solo lui altrimenti devo aggiungere anche
        // tutti i suoi nodi
        // collegati
        if (checkBox.getCheckBoxTreeSelectionModel().isPartiallySelected(getPath(root))) {
            if (root.getUserObject() != null) {
                listNodeUserObject.add((String) root.getUserObject());
            }
        } else if (checkBox.getCheckBoxTreeSelectionModel().isPathSelected(getPath(root))) {
            listNodeUserObject.addAll(visitAllNode(root, listNodeUserObject));
        }

        @SuppressWarnings("unchecked")
        Enumeration<Object> children = root.children();
        while (children.hasMoreElements()) {
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children.nextElement();
            listNodeUserObject = visitSelectedNode(childNode, listNodeUserObject, checkBox);
        }

        return listNodeUserObject;
    }

}
