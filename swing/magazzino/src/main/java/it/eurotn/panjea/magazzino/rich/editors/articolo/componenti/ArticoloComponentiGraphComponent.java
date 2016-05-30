/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Set;

import javax.swing.JScrollPane;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.rich.editors.categoriaarticolo.ArticoloCategoriaDTO;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;
import it.eurotn.panjea.rich.editors.documentograph.GraphLayoutsFactory;

/**
 * @author fattazzo
 *
 */
public class ArticoloComponentiGraphComponent extends mxGraphComponent {

	private class GraphMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			Object cell = getCellAt(e.getX(), e.getY());
			if (cell != null && cell instanceof mxCell) {
				ArticoloCategoriaDTO articoloCategoriaDTO = null;
				mxCell cellClicked = (mxCell) cell;

				if (cellClicked.getValue() instanceof Componente) {
					ArticoloLite articoloLiteCaricato = ((Componente) cellClicked.getValue()).getArticolo();
					Articolo articoloCaricato = magazzinoAnagraficaBD
							.caricaArticolo(articoloLiteCaricato.creaProxyArticolo(), true);
					articoloCategoriaDTO = new ArticoloCategoriaDTO(articoloCaricato, articoloCaricato.getCategoria());
				} else if (cellClicked.getValue() instanceof ArticoloLite) {
					ArticoloLite articoloLiteCaricato = ((ArticoloLite) cellClicked.getValue());
					Articolo articoloCaricato = magazzinoAnagraficaBD
							.caricaArticolo(articoloLiteCaricato.creaProxyArticolo(), true);
					articoloCategoriaDTO = new ArticoloCategoriaDTO(articoloCaricato, articoloCaricato.getCategoria());
				} else if (cellClicked.getValue() instanceof String) {
					Set<Componente> componentiPadri = articoloConfigurazioneDistinta.getDistinte();
					ComponentePadriDialog padriDialog = new ComponentePadriDialog(componentiPadri);
					padriDialog.showDialog();
				}
				if (articoloCategoriaDTO != null) {
					LifecycleApplicationEvent event = new OpenEditorEvent(articoloCategoriaDTO);
					Application.instance().getApplicationContext().publishEvent(event);
				}
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getWheelRotation() < 0) {
				zoomIn();
			} else {
				zoomOut();
			}
		}
	}

	private static final long serialVersionUID = -8052696501535893977L;

	private GraphMouseListener graphMouseListener;

	private mxGraphLayout layout;

	private ArticoloConfigurazioneDistinta articoloConfigurazioneDistinta;

	private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

	{
		setConnectable(false);
		setBorder(null);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		addMouseWheelListener(getGraphMouseListener());
		getGraphControl().addMouseListener(getGraphMouseListener());
	}

	/**
	 * Costruttore.
	 *
	 * @param parammxGraph
	 *            grafico
	 * @param magazzinoAnagraficaBD
	 *            magazzino anagrafica
	 */
	public ArticoloComponentiGraphComponent(final mxGraph parammxGraph,
			final IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
		super(parammxGraph);
		this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
		layout = GraphLayoutsFactory.getLayout(GraphLayoutsFactory.HIERARCHICAL_HORIZONTAL_LAYOUT, graph);
	}

	/**
	 * Applica il layout selezionato.
	 *
	 * @param paramLayout
	 *            layout
	 */
	public void applyLayout(mxGraphLayout paramLayout) {
		layout = paramLayout;
		updateGraph(articoloConfigurazioneDistinta);
	}

	/**
	 * Crea ricorsivamente tutti i nodi per il componente indicato.
	 *
	 * @param vertexPadre
	 *            nodo padre
	 * @param componente
	 *            componente di cui creare il nodo e legarlo a quello padre
	 */
	private void createNodes(Object vertexPadre, Componente componente) {

		// solo i componenti diretti dell'articolo sono modificabili quindi
		// applico uno stile diverso agli altri.
		String style = ArticoloComponentiGraph.COMP_PRIMO_LV_CELL_STYLE_NAME;
		if (!componente.getDistinta().getId().equals(articoloConfigurazioneDistinta.getIdDistinta())) {
			style = ArticoloComponentiGraph.ARTICOLO_NON_MODIFICABILE_CELL_STYLE_NAME;
		}

		Object vertex = graph.insertVertex(graph.getDefaultParent(), null, componente, 20, 20, 250, 50, style);
		graph.insertEdge(graph.getDefaultParent(), null, "", vertexPadre, vertex);

		for (Componente subComponente : componente.getArticolo().getComponenti()) {
			createNodes(vertex, subComponente);
		}
	}

	/**
	 * Crea tutti i nodi delle distinte padre per node dell'articolo di
	 * riferimento.
	 *
	 * @param vertexArticolo
	 *            nodo articolo di riferimento
	 */
	private void createNodesDistintePadri(Object vertexArticolo) {

		Set<Componente> componentiPadri = articoloConfigurazioneDistinta.getDistinte();

		if (componentiPadri != null) {
			if (componentiPadri.size() < 9) {
				for (Componente componentePadre : componentiPadri) {
					Object vertex = graph.insertVertex(graph.getDefaultParent(), null, componentePadre.getDistinta(),
							20, 20, 250, 50, ArticoloComponentiGraph.ARTICOLO_NON_MODIFICABILE_CELL_STYLE_NAME);
					graph.insertEdge(graph.getDefaultParent(), null, "", vertex, vertexArticolo);
				}
			} else {
				Object vertex = graph.insertVertex(graph.getDefaultParent(), null,
						"<HTML>Presenti " + componentiPadri.size()
								+ " elementi.<br>Aprire l'editor per visualizzarli</HTML>",
						20, 20, 250, 50, ArticoloComponentiGraph.ARTICOLO_NON_MODIFICABILE_CELL_STYLE_NAME);
				graph.insertEdge(graph.getDefaultParent(), null, "", vertex, vertexArticolo);
			}
		}
	}

	/**
	 * @return Returns the graphMouseListener.
	 */
	public GraphMouseListener getGraphMouseListener() {
		if (graphMouseListener == null) {
			graphMouseListener = new GraphMouseListener();
		}

		return graphMouseListener;
	}

	/**
	 * Aggiorna il grafico in base all'articolo.
	 *
	 * @param articoloConfigurazioneDistintaParam
	 *            articolo con il quale aggiornare il grafico.
	 */
	public void updateGraph(ArticoloConfigurazioneDistinta articoloConfigurazioneDistintaParam) {

		this.articoloConfigurazioneDistinta = articoloConfigurazioneDistintaParam;

		graph.removeCells();

		graph.setModel(new mxGraphModel());

		Object verticeArticolo = graph.insertVertex(graph.getDefaultParent(), null, articoloConfigurazioneDistinta, 20,
				20, 250, 50, ArticoloComponentiGraph.ARTICOLO_EDITOR_CELL_STYLE_NAME);

		// creo i nodi dei padri
		createNodesDistintePadri(verticeArticolo);

		// creo i nodi dei figli
		for (Componente componente : articoloConfigurazioneDistinta.getComponenti()) {
			createNodes(verticeArticolo, componente);
		}

		graph.getModel().beginUpdate();
		layout.execute(graph.getDefaultParent());
		graph.getModel().endUpdate();
	}
}
