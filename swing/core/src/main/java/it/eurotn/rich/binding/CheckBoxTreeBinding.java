/*
 * Copyright 2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package it.eurotn.rich.binding;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.binding.convert.ConversionExecutor;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.core.ReflectiveVisitorHelper;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.form.binding.support.CustomBinding;
import org.springframework.richclient.util.Assert;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.swing.CheckBoxTree;
import com.jidesoft.swing.DefaultOverlayable;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.GroupingList;
import it.eurotn.rich.converter.PanjeaCompositeConverter;
import it.eurotn.rich.converter.PanjeaConverter;

/**
 * Binding for the JIDE check box tree using the ListBinding as a guide.
 *
 * @author Jonny Wray
 *
 */
public class CheckBoxTreeBinding extends CustomBinding {

    private class DefaultTreeBindingCellRenderer extends DefaultTreeCellRenderer {

        protected class IconVisitor {
            /**
             *
             * @param oggetto
             *            ogg. da visitare
             * @return stringa che descrive l
             */
            @SuppressWarnings("unused")
            Icon visit(Enum<?> oggetto) {
                return getIconSource().getIcon(oggetto.getClass().getName() + "#" + ((Enum<?>) oggetto).name());
            }

            /**
             *
             * @param oggetto
             *            ogg. da visitare
             * @return stringa che descrive l
             */
            @SuppressWarnings("unused")
            Icon visit(Object oggetto) {
                return getIconSource().getIcon(oggetto.getClass().getName());
            }

            /**
             *
             * @param label
             *            oggetto di tipo Stringas
             * @return stringa che descrive l
             */
            @SuppressWarnings("unused")
            Icon visit(String label) {
                return null;
            }
        }

        protected class ValuesVisitor {
            /**
             *
             * @param oggetto
             *            ogg. da visitare
             * @return stringa che descrive l
             */
            @SuppressWarnings("unused")
            String visit(Object oggetto) {
                return ObjectConverterManager.toString(oggetto);
            }

            /**
             *
             * @param label
             *            oggetto di tipo Stringas
             * @return stringa che descrive l
             */
            @SuppressWarnings("unused")
            String visit(String label) {
                String message = RcpSupport.getMessage(label);
                if (message.isEmpty()) {
                    message = formModel.getFieldFace(label).getLabelInfo().getText();
                }
                return message;
            }
        }

        private static final long serialVersionUID = -2472095721881450615L;

        private final ReflectiveVisitorHelper visitorHelper = new ReflectiveVisitorHelper();

        private Object visitor = new ValuesVisitor();
        private Object iconVisitor = new IconVisitor();

        private Color selectionColor;
        private Color nonSelectionColor;

        {
            selectionColor = UIManager.getColor("TextField.selectionBackground");
            nonSelectionColor = UIManager.getColor("Panel.background");
            setClosedIcon(CLOSED_FOLDER_ICON);
            setOpenIcon(OPEN_FOLDER_ICON);
        }

        @Override
        public Component getTreeCellRendererComponent(JTree jtree, Object obj, boolean flag, boolean flag1,
                boolean flag2, int i, boolean flag3) {
            Object userObject = ((DefaultMutableTreeNode) obj).getUserObject();
            JLabel comp = (JLabel) super.getTreeCellRendererComponent(jtree, obj, flag, flag1, flag2, i, flag3);
            Icon icon = (Icon) visitorHelper.invokeVisit(iconVisitor, userObject);
            if (icon != null) {
                setIcon(icon);
            }
            setText(visitorHelper.invokeVisit(visitor, userObject).toString());
            setOpaque(true);
            // comp.setOpaque(true);

            setBackground(new Color(nonSelectionColor.getRGB()));
            if (flag) {
                setBackground(new Color(selectionColor.getRGB()));
            }

            return comp;
        }
    }

    private class ScambioVisualizzazioneCommand extends ApplicationWindowAwareCommand {

        /**
         * Costruttore.
         */
        public ScambioVisualizzazioneCommand() {
            super("ScambioVisualizzazioneCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            Collection<?> collection = ((Collection<?>) selectedItemHolder.getValue());
            if (collection != null && !collection.isEmpty()) {
                ObjectConverter converter = ObjectConverterManager
                        .getConverter(collection.iterator().next().getClass());
                if (converter != null && scambioVisualizzazione) {
                    ((PanjeaCompositeConverter<?>) converter).scambiaVisualizzazione();
                    tree.setModel(createModel());
                }
            }
        }

    }

    private final class TreeBindingSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {

            TreePath[] selected = tree.getCheckBoxTreeSelectionModel().getSelectionPaths();

            List<Object> listElementiSelezionati = new ArrayList<Object>();

            if (selected != null) {
                for (TreePath treePath : selected) {
                    Object userObject = ((DefaultMutableTreeNode) treePath.getLastPathComponent()).getUserObject();
                    if (!(userObject instanceof String)) {
                        listElementiSelezionati.add(userObject);
                    } else {
                        listElementiSelezionati = visitSelectedNode(
                                (DefaultMutableTreeNode) treePath.getLastPathComponent(), listElementiSelezionati);
                    }
                }
            }
            ConversionExecutor conversioneExecutor = getConversionService()
                    .getConversionExecutor(listElementiSelezionati.getClass(), getPropertyType());
            controlValueChanged(conversioneExecutor.execute(listElementiSelezionati));
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
        private List<Object> visitSelectedNode(DefaultMutableTreeNode node, List<Object> listSelected) {
            if (node.isLeaf()) {
                listSelected.add(node.getUserObject());
            } else {
                @SuppressWarnings("unchecked")
                Enumeration<Object> children = node.children();
                while (children.hasMoreElements()) {
                    DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) children.nextElement();
                    listSelected = visitSelectedNode(childNode, listSelected);
                }
            }
            return listSelected;
        }
    }

    private final static Icon CLOSED_FOLDER_ICON = RcpSupport.getIcon("tree.closedFolder.icon");
    private final static Icon OPEN_FOLDER_ICON = RcpSupport.getIcon("tree.openFolder.icon");

    private boolean scambioVisualizzazione = false;

    // private final SelectedItemChangeHandler selectedItemChangeHandler = new SelectedItemChangeHandler();
    private CheckBoxTree tree;
    private ValueModel selectedItemHolder;
    private TreeCellRenderer renderer;

    private String[] groupableProperty;

    private DefaultOverlayable overlayable;

    private TreeModel treeModel;

    /**
     *
     * Costruttore.
     *
     * @param tree
     *            control tree
     * @param formModel
     *            formModel
     * @param formPropertyPath
     *            path
     */
    public CheckBoxTreeBinding(final CheckBoxTree tree, final FormModel formModel, final String formPropertyPath) {
        super(formModel, formPropertyPath, null);
        this.tree = tree;
    }

    /**
     *
     * @return treeModel per il componente.
     */
    private TreeModel createModel() {
        if (treeModel != null) {
            return treeModel;
        }
        Assert.notNull(selectedItemHolder, "Settare la collection di oggetti da selezionare");
        // la collection deve essere ordinata per le proprietà da raggruppare
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(formPropertyPath);
        int currentPropertyIndex = 0;
        fillModel((Collection<?>) selectedItemHolder.getValue(), rootNode, currentPropertyIndex);
        return new DefaultTreeModel(rootNode);
    }

    @Override
    protected JComponent doBindControl() {
        JComponent componentBinding = tree;
        TreeModel model = createModel();
        tree.setModel(model);
        tree.getCheckBoxTreeSelectionModel().addTreeSelectionListener(new TreeBindingSelectionListener());
        if (renderer != null) {
            tree.setCellRenderer(renderer);
        } else {
            tree.setCellRenderer(new DefaultTreeBindingCellRenderer());
        }
        tree.setOpaque(false);

        if (scambioVisualizzazione) {
            overlayable = new DefaultOverlayable(tree, new ScambioVisualizzazioneCommand().createButton(),
                    DefaultOverlayable.TOP);
            // overlayable
            // .addOverlayComponent(new ScambioVisualizzazioneCommand().createButton(), SwingConstants.NORTH, 0);
            overlayable.setOverlayVisible(true);
            componentBinding = overlayable;
        }
        return new JScrollPane(componentBinding);
    }

    @Override
    protected void enabledChanged() {
        tree.setEnabled(isEnabled());
    }

    /**
     *
     * @param elementi
     *            elementi da raggruppare e aggiungere al parent
     * @param parent
     *            nodo padre
     * @param currentPropertyIndex
     *            indice della proprietà da raggruppare
     */
    private void fillModel(Collection<?> elementi, DefaultMutableTreeNode parent, int currentPropertyIndex) {
        // Raggruppo per la proprietà
        @SuppressWarnings({ "rawtypes", "unchecked" })
        GroupingList<?> groupList = new GroupingList(GlazedLists.eventList(elementi),
                new BeanComparator(groupableProperty[currentPropertyIndex]));
        for (Collection<?> gruppo : groupList) {
            DefaultMutableTreeNode nodoGruppo = null;
            Object groupObject = null;
            try {
                groupObject = groupableProperty[currentPropertyIndex] + "."
                        + BeanUtils.getProperty(gruppo.iterator().next(), groupableProperty[currentPropertyIndex]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            nodoGruppo = new DefaultMutableTreeNode(groupObject);
            parent.add(nodoGruppo);
            if (currentPropertyIndex < groupableProperty.length - 1) {
                fillModel(gruppo, nodoGruppo, currentPropertyIndex + 1);
            } else {
                List<Object> gruppoList = new ArrayList<Object>(gruppo);

                // se esiste un converter e il gruppo non è vuoto, lo riordino
                if (gruppo != null && !gruppo.isEmpty()) {
                    ObjectConverter converter = ObjectConverterManager
                            .getConverter(gruppo.iterator().next().getClass());
                    if (converter != null) {
                        scambioVisualizzazione = converter instanceof PanjeaCompositeConverter<?>;
                        Collections.sort(gruppoList, ((PanjeaConverter) converter).getComparator());
                    }
                }
                for (Object object : gruppoList) {
                    nodoGruppo.add(new DefaultMutableTreeNode(object));
                }
            }
            parent.add(nodoGruppo);
        }
    }

    @Override
    protected void readOnlyChanged() {
        tree.setEnabled(!isReadOnly());
    }

    /**
     * @param groupableProperty
     *            The groupableProperty to set.
     */
    public void setGroupableProperty(String[] groupableProperty) {
        this.groupableProperty = groupableProperty;
    }

    public void setModel(TreeModel treeModelParam) {
        this.treeModel = treeModelParam;
    }

    /**
     * @param renderer
     *            The renderer to set.
     */
    public void setRenderer(TreeCellRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Visualizza o nasconde l'overlay per lo scambio di visualizzazione degli oggetti.
     *
     * @param visible
     *            <code>true</code> visualizza l'overlay, <code>false</code> altrimenti
     */
    public void setScambioVisualizzazioneVisible(boolean visible) {
        overlayable.setOverlayVisible(visible);
    }

    /**
     * @param selectedItemHolder
     *            The selectedItemHolder to set.
     */
    public void setSelectedItemHolder(ValueModel selectedItemHolder) {
        this.selectedItemHolder = selectedItemHolder;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void valueModelChanged(Object obj) {
        @SuppressWarnings("unchecked")
        Collection<Object> elementiSelezionati = (Collection<Object>) obj;
        tree.getCheckBoxTreeSelectionModel().clearSelection();
        if (obj == null) {
            return;
        }
        for (Object object : elementiSelezionati) {
            DefaultMutableTreeNode theNode = null;
            for (Enumeration e = ((DefaultMutableTreeNode) tree.getModel().getRoot()).depthFirstEnumeration(); e
                    .hasMoreElements() && theNode == null;) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
                if (node.getUserObject().equals(object)) {
                    tree.getCheckBoxTreeSelectionModel().addSelectionPath(new TreePath(node.getPath()));
                }
            }
        }
    }
}
