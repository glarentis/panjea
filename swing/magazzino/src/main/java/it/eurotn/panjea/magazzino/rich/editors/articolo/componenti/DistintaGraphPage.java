/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

/**
 * @author fattazzo
 *
 */
public class DistintaGraphPage extends AbstractDialogPage implements IPageLifecycleAdvisor {

    private class RootPanelComponentListener implements ComponentListener {
        @Override
        public void componentHidden(ComponentEvent e) {
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentResized(ComponentEvent e) {
            graphPanel.setBounds(0, 0, e.getComponent().getWidth() - GRAPH_BORDER,
                    e.getComponent().getHeight() - GRAPH_BORDER);
            graphComponent.setBounds(GRAPH_BORDER, 0, e.getComponent().getWidth() - GRAPH_BORDER,
                    e.getComponent().getHeight() - GRAPH_BORDER);
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }
    }

    public static final int GRAPH_BORDER = 25;

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;
    private JPanel rootPanel;

    private JLayeredPane graphPanel = new JLayeredPane();

    private RootPanelComponentListener rootPanelComponentListener;

    private ArticoloComponentiGraph graph;
    private ArticoloComponentiGraphComponent graphComponent;
    private ArticoloComponentiGraphOutline graphOutline;

    private ToolbarComponentiGraph toolBarGraph;

    private ValueHolder articoloConfigurazioneDistintaValueHolder;

    /**
     * Costruttore.
     *
     * @param pageId
     */
    protected DistintaGraphPage() {
        super("distintaGraphPage");
    }

    @Override
    protected JComponent createControl() {
        return getRootPanel();
    }

    /**
     * Crea il grafgico per il nodo di riferimento.
     *
     * @return grafico creato
     */
    private JComponent createGraph() {
        graph = new ArticoloComponentiGraph();

        if (graphComponent == null) {
            graphComponent = new ArticoloComponentiGraphComponent(graph, magazzinoAnagraficaBD);
            getRootPanel().addComponentListener(rootPanelComponentListener);
            graphOutline = new ArticoloComponentiGraphOutline(graphComponent);
        } else {
            graphComponent.setGraph(graph);
        }

        return graphComponent;
    }

    @Override
    public void dispose() {
        if (rootPanel != null) {
            rootPanel.removeComponentListener(rootPanelComponentListener);
        }

        if (graphOutline != null) {
            graphOutline.dispose();
        }
    }

    /**
     * @return pannello principale
     */
    private JPanel getRootPanel() {
        if (rootPanel == null) {
            rootPanel = getComponentFactory().createPanel(new BorderLayout());

            rootPanelComponentListener = new RootPanelComponentListener();

            createGraph();
            graphPanel.add(graphComponent, new Integer(0));

            rootPanel.add(graphPanel, BorderLayout.CENTER);

            JPanel barsPanel = getComponentFactory().createPanel(new BorderLayout());
            toolBarGraph = new ToolbarComponentiGraph(graphComponent, graphOutline);
            barsPanel.add(toolBarGraph.getControl(), BorderLayout.NORTH);
            rootPanel.add(barsPanel, BorderLayout.NORTH);

            graphPanel.add(graphOutline, new Integer(1));

        }
        return rootPanel;
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {
        loadData();
    }

    @Override
    public void restoreState(Settings arg0) {
    }

    @Override
    public void saveState(Settings arg0) {
    }

    @Override
    public void setFormObject(Object object) {
        articoloConfigurazioneDistintaValueHolder = (ValueHolder) object;
        articoloConfigurazioneDistintaValueHolder.addValueChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                graphComponent.updateGraph(
                        (ArticoloConfigurazioneDistinta) articoloConfigurazioneDistintaValueHolder.getValue());
            }
        });
    }

    /**
     * @param magazzinoAnagraficaBD
     *            the magazzinoAnagraficaBD to set
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

}
