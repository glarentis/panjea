package it.eurotn.panjea.bi.rich.editors.analisi;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.text.HtmlPane;

import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.jidesoft.pivot.PivotTablePane;
import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.TreeSearchable;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIDomain;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.analisi.tabelle.ColumnMeasure;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Tabella;
import it.eurotn.panjea.bi.rich.editors.analisi.renderer.TreeRenderer;

@SuppressWarnings("serial")
public class AnalisiPivotChooserFieldsPanel extends JideSplitPane {

    private class TreeActionListener extends MouseAdapter {

        private void assigneArea(PivotField field, int area) {
            if (field.getAreaType() == area) {
                field.setAreaType(PivotField.AREA_NOT_ASSIGNED);
            } else {
                field.setAreaType(area);
            }
        }

        private void clickColumn(int xpoint, Colonna colonna) {
            if (xpoint > 40 && xpoint < 85) {
                PivotField field = pivotDataModel.getField(colonna.getKey());
                if (xpoint < 55) {
                    assigneArea(field, PivotField.AREA_COLUMN);
                } else if (xpoint < 70) {
                    assigneArea(field, PivotField.AREA_ROW);
                } else {
                    assigneArea(field, PivotField.AREA_FILTER);
                }
            }
        }

        private void clickColumnMeasure(int xpoint, ColumnMeasure colonna) {
            int startLabel = 56;
            if (StringUtils.isEmpty(colonna.getSezione())) {
                startLabel = 40;
            }
            int funzione = (xpoint - startLabel) / 16;
            if (funzione > AnalisiBIDomain.FUNZIONI_AGGREGAZIONE.length) {
                return;
            }

            String key = colonna.getKey();
            int endString = key.indexOf(colonna.getTabella().getAlias()) - 1;
            if (endString < 0) {
                endString = 0;
            }
            String functionToReplace = key.substring(0, endString);
            key = key.replace(functionToReplace, (AnalisiBIDomain.FUNZIONI_AGGREGAZIONE[funzione]));
            PivotField field = pivotDataModel.getField(key);
            if (field != null) {
                assigneArea(field, PivotField.AREA_DATA);
            }
        }

        @Override
        public void mouseClicked(MouseEvent me) {
            TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
            if (tp != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
                if (node.getUserObject() instanceof ColumnMeasure) {
                    clickColumnMeasure(me.getX(), (ColumnMeasure) node.getUserObject());
                } else if (node.getUserObject() instanceof Colonna) {
                    clickColumn(me.getX(), (Colonna) node.getUserObject());
                }
            }
            pivotTablePane.fieldsUpdated();
            pivotTablePane.autoResizeAllColumns();
            refreshFiltri();
            tree.repaint();
        }
    }

    private HtmlPane filtriPane;
    final JTree tree;
    private DefaultMutableTreeNode dimensioni = new DefaultMutableTreeNode("DIMENSIONI");
    private DefaultMutableTreeNode misure = new DefaultMutableTreeNode("MISURE");

    private IPivotDataModel pivotDataModel;

    private PivotTablePane pivotTablePane;

    private TreeRenderer treeRender;

    /**
     * costruttore
     *
     * @param pivotTablePane
     *            table pane
     */
    public AnalisiPivotChooserFieldsPanel(final PivotTablePane pivotTablePane) {
        super(JSplitPane.VERTICAL_SPLIT);
        setOneTouchExpandable(true);
        this.pivotTablePane = pivotTablePane;

        DefaultMutableTreeNode rootNode = createRootNode();
        tree = new JTree(rootNode);
        treeRender = new TreeRenderer();
        DefaultMutableTreeNode dimensioniNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        DefaultMutableTreeNode misureNode = (DefaultMutableTreeNode) rootNode.getChildAt(1);
        TreePath treePath = new TreePath(dimensioniNode.getPath());
        tree.expandPath(treePath);
        treePath = new TreePath(misureNode.getPath());
        tree.expandPath(treePath);

        TreeSearchable searchable = new TreeSearchable(tree) {
            @Override
            protected String convertElementToString(Object paramObject) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) ((TreePath) paramObject).getLastPathComponent();
                Object userObject = node.getUserObject();
                if (userObject instanceof Colonna) {
                    return ((Colonna) userObject).getTitle();
                } else if (userObject instanceof Tabella) {
                    return ((Tabella) userObject).getTitle();
                }
                return node.getUserObject().toString();
            }
        };
        searchable.setRecursive(true);
        searchable.setRepeats(true);

        tree.setCellRenderer(treeRender);
        tree.setRootVisible(false);

        tree.addMouseListener(new TreeActionListener());

        // setLayout(new BorderLayout());
        JScrollPane jsp = new JScrollPane(tree, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addPane(jsp);

        filtriPane = new HtmlPane();
        JScrollPane jspFiltri = new JScrollPane(filtriPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addPane(jspFiltri);
    }

    private DefaultMutableTreeNode createRootNode() {
        Collection<Colonna> colonneDomain = AnalisiBIDomain.getColonne().values();

        MultiValuedMap<Tabella, Colonna> colonneDimensioniMap = MultiMapUtils.newSetValuedHashMap();
        MultiValuedMap<Tabella, String> sezioniMisureMap = MultiMapUtils.newSetValuedHashMap();
        MultiValuedMap<String, Colonna> colonneSezioneMap = MultiMapUtils.newSetValuedHashMap();

        for (Colonna colonna : colonneDomain) {
            if (colonna instanceof ColumnMeasure) {
                sezioniMisureMap.put(colonna.getTabella(), colonna.getSezione());
                colonneSezioneMap.put(colonna.getSezione() + colonna.getTabella().getAlias(), colonna);
            } else {
                colonneDimensioniMap.put(colonna.getTabella(), colonna);
            }
        }

        // Creo nodi dimensioni
        Map<Tabella, Collection<Colonna>> colonneDimensioniTree = colonneDimensioniMap.asMap();

        for (Tabella tabella : colonneDimensioniTree.keySet()) {
            DefaultMutableTreeNode tabellaNode = new DefaultMutableTreeNode(tabella);

            List<Colonna> colonne = new ArrayList<>(tabella.getColonne().values());
            Collections.sort(colonne);
            for (Colonna colonna : colonne) {
                DefaultMutableTreeNode colonnaNode = new DefaultMutableTreeNode(colonna);
                tabellaNode.add(colonnaNode);
            }
            dimensioni.add(tabellaNode);
        }

        // Creo nodi misure
        Map<Tabella, Collection<String>> sezioniMisureTree = sezioniMisureMap.asMap();
        Map<String, Collection<Colonna>> colonneSezioneMisureTree = colonneSezioneMap.asMap();
        for (Entry<Tabella, Collection<String>> tabellaEntry : sezioniMisureTree.entrySet()) {
            DefaultMutableTreeNode tabellaNode = new DefaultMutableTreeNode(tabellaEntry.getKey());
            misure.add(tabellaNode);

            for (String sezione : tabellaEntry.getValue()) {
                // se ho una sola sezione aggiungo direttamente i nodi
                DefaultMutableTreeNode sezioneNode = null;
                if (tabellaEntry.getValue().size() == 1) {
                    sezioneNode = tabellaNode;
                } else {
                    sezioneNode = new DefaultMutableTreeNode(sezione);
                    tabellaNode.add(sezioneNode);
                }
                Set<Colonna> colonneSezioneSet = (Set<Colonna>) colonneSezioneMisureTree
                        .get(sezione + tabellaEntry.getKey().getAlias());
                Set<String> titoloColonneAggiunte = new HashSet<>();
                List<Colonna> colonneSezione = new ArrayList<>(colonneSezioneSet);
                Collections.sort(colonneSezione);
                for (Colonna colonna : colonneSezione) {
                    if (!titoloColonneAggiunte.contains(colonna.getTitle())) {
                        DefaultMutableTreeNode colonnaNode = new DefaultMutableTreeNode(colonna);
                        sezioneNode.add(colonnaNode);
                        titoloColonneAggiunte.add(colonna.getTitle());
                    }
                }
            }
        }

        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        root.add(dimensioni);
        root.add(misure);
        return root;
    }

    /**
     * refresh del testo dei filtri.
     */
    private void refreshFiltri() {
        StringBuilder sbFiltroFields = new StringBuilder("<HTML>");
        List<PivotField> fields = new ArrayList<PivotField>();

        fields.addAll(Arrays.asList(pivotDataModel.getRowFields()));
        fields.addAll(Arrays.asList(pivotDataModel.getColumnFields()));
        fields.addAll(Arrays.asList(pivotDataModel.getFilterFields()));

        for (PivotField pivotField : fields) {
            if (pivotField.getSelectedPossibleValues() != null) {
                StringBuilder sbFiltroField = new StringBuilder("<BR/><B>");
                sbFiltroField.append(pivotField.getTitle());
                sbFiltroField.append(":");
                sbFiltroField.append("</B><I>");
                int limitSizeRow = 30;
                for (Object valueFilter : pivotField.getSelectedPossibleValues()) {
                    sbFiltroField.append(ObjectUtils.defaultIfNull(valueFilter, "").toString());
                    if (sbFiltroField.length() > limitSizeRow) {
                        sbFiltroField.append("<br/>");
                        limitSizeRow += 30;
                    } else {
                        sbFiltroField.append(",");
                    }
                }
                sbFiltroField.append("</I>");
                sbFiltroFields.append(sbFiltroField);
            } else if (pivotField.getFilter() != null) {
                sbFiltroFields.append("<B>");
                sbFiltroFields.append(pivotField.getTitle());
                sbFiltroFields.append(":");
                sbFiltroFields.append("</B>");
                sbFiltroFields.append(pivotField.getFilter().getName().replace("|", ""));
                sbFiltroFields.append("</I><br/>");
            }

        }
        sbFiltroFields.append("</HTML>");
        filtriPane.setText(sbFiltroFields.toString());
        filtriPane.setVisible(true);
    }

    /**
     *
     * @param pivotDataModel
     *            set dataModel
     */
    public void setPivotDataModel(IPivotDataModel pivotDataModel) {
        this.pivotDataModel = pivotDataModel;
        treeRender.setPivotDataModel(pivotDataModel);
        refreshFiltri();
    }
}
