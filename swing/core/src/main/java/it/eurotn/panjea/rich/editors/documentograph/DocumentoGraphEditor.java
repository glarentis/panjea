package it.eurotn.panjea.rich.editors.documentograph;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.panjea.anagrafica.rich.commands.OpenAreeDocumentoCommand;
import it.eurotn.panjea.documenti.graph.node.DocumentoNode;
import it.eurotn.panjea.documenti.graph.util.DocumentoGraph;
import it.eurotn.panjea.rich.editors.documentograph.toolbar.cell.ToolbarDocumentoCellGraph;
import it.eurotn.panjea.rich.editors.documentograph.toolbar.graph.ToolbarDocumentoGraph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.progress.ProgressMonitor;

import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.swing.SimpleScrollPane;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxCellTracker;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import com.mxgraph.view.mxGraph;

public class DocumentoGraphEditor extends AbstractEditor {

	private class BredCrumbBarSelectionListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {

			if (e.getClickCount() != 2) {
				return;
			}

			DocumentoGraph documentoGraphOpened = new DocumentoGraph();
			documentoGraphOpened.setIdDocumento(((DocumentoNode) breadCrumbBar.getSelectedValue()).getIdDocumento());
			LifecycleApplicationEvent event = new OpenEditorEvent(documentoGraphOpened);
			Application.instance().getApplicationContext().publishEvent(event);
		}
	}

	private class GraphMouseListener extends MouseAdapter {

		private OpenAreeDocumentoCommand openAreeDocumentoCommand = new OpenAreeDocumentoCommand();

		@Override
		public void mouseClicked(MouseEvent e) {

			if (e.getClickCount() != 2) {
				return;
			}

			Object cell = graphComponent.getCellAt(e.getX(), e.getY());

			if (cell != null && cell instanceof mxCell && ((mxCell) cell).getValue() instanceof DocumentoNode) {
				DocumentoNode documentoNode = (DocumentoNode) ((mxCell) cell).getValue();
				Documento documento = new Documento();
				documento.setId(documentoNode.getIdDocumento());
				openAreeDocumentoCommand.addParameter(OpenAreeDocumentoCommand.PARAM_ID_DOCUMENTO, documento.getId());
				openAreeDocumentoCommand.execute();
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			if (e.getWheelRotation() < 0) {
				graphComponent.zoomIn();
			} else {
				graphComponent.zoomOut();
			}
		}
	}

	private class GraphOutlineListener extends MouseAdapter {

		@Override
		public void mouseExited(MouseEvent e) {
			graphOutline.setVisible(false);
		}
	}

	private class RootPanelComponentListener implements ComponentListener {
		@Override
		public void componentHidden(ComponentEvent e) {
		}

		@Override
		public void componentMoved(ComponentEvent e) {
		}

		@Override
		public void componentResized(ComponentEvent e) {
			layeredPanel.setBounds(0, 0, e.getComponent().getWidth() - GRAPH_BORDER, e.getComponent().getHeight()
					- GRAPH_BORDER - toolBarGraph.getControl().getHeight());
			graphComponent.setBounds(GRAPH_BORDER, 0, e.getComponent().getWidth() - GRAPH_BORDER, e.getComponent()
					.getHeight() - GRAPH_BORDER - toolBarGraph.getControl().getHeight());
		}

		@Override
		public void componentShown(ComponentEvent e) {
		}
	}

	private DocumentoGraph documentoGraph;

	private IDocumentiBD documentiBD;

	private JLayeredPane layeredPanel = new JLayeredPane();

	public static final int GRAPH_BORDER = 25;

	private ToolbarDocumentoCellGraph toolbar = new ToolbarDocumentoCellGraph();

	private mxGraphLayout layout;

	private JPanel rootPanel;

	private mxGraphComponent graphComponent;

	private RootPanelComponentListener rootPanelComponentListener;
	private GraphMouseListener graphMouseListener;
	private GraphOutlineListener graphOutlineListener;

	private ToolbarDocumentoGraph toolBarGraph;

	private mxGraphOutline graphOutline;

	private JList breadCrumbBar;
	private BredCrumbBarSelectionListener bredCrumbBarSelectionListener;

	private boolean loadAllNodes = Boolean.FALSE;

	private mxCell cellaDocumentoDiPartenza;

	private Map<String, Object> normalCellStyle;
	private Map<String, Object> selectedCellStyle;

	private static final String NORMAL_CELL_STYLE_NAME = "normal";
	private static final String SELECTED_CELL_STYLE_NAME = "selected";

	{
		normalCellStyle = new HashMap<String, Object>();
		normalCellStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		normalCellStyle.put(mxConstants.STYLE_ROUNDED, true);
		normalCellStyle.put(mxConstants.STYLE_STROKEWIDTH, 1);
		normalCellStyle.put(mxConstants.STYLE_STROKECOLOR, "gray");

		selectedCellStyle = new HashMap<String, Object>();
		selectedCellStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		selectedCellStyle.put(mxConstants.STYLE_ROUNDED, true);
		selectedCellStyle.put(mxConstants.STYLE_STROKEWIDTH, 2);
		selectedCellStyle.put(mxConstants.STYLE_STROKECOLOR, "black");
	}

	/**
	 * Aggiunge un nodo alla bredCrumbBar. Se il nodo esiste viene rimosso e aggiunto in coda alla lista.
	 *
	 * @param documentoNode
	 *            nodo da aggiungere
	 */
	private void addNodeToBreadCrumbBar(DocumentoNode documentoNode) {
		DefaultListModel model = (DefaultListModel) breadCrumbBar.getModel();

		// cerco all'interno della lista se esiste già il nodo
		int idxNode = model.indexOf(documentoNode);
		if (idxNode >= 0) {
			model.removeElementAt(idxNode);
		}

		model.addElement(documentoNode);
	}

	/**
	 * Crea il grafgico per il nodo di riferimento.
	 *
	 * @return grafico creato
	 */
	public JComponent createGraph() {
		DocumentoNode documentoNode = documentiBD.createNode(documentoGraph, loadAllNodes);
		final mxGraph graph = new mxGraph() {
			@Override
			public String convertValueToString(Object cell) {
				if (cell instanceof mxCell) {
					Object value = ((mxCell) cell).getValue();

					if (value instanceof DocumentoNode) {
						return ((DocumentoNode) value).getHTMLDescription();
					}
				}
				return super.convertValueToString(cell);
			}

		};
		graph.setHtmlLabels(true);
		graph.setCellsResizable(false);
		graph.setCellsEditable(false);
		graph.setCellsDisconnectable(false);
		graph.setCellsDeletable(false);
		graph.setCellsBendable(false);
		graph.setConnectableEdges(false);
		graph.getStylesheet().putCellStyle(NORMAL_CELL_STYLE_NAME, normalCellStyle);
		graph.getStylesheet().putCellStyle(SELECTED_CELL_STYLE_NAME, selectedCellStyle);

		if (graphComponent == null) {
			graphComponent = new mxGraphComponent(graph);
			getRootPanel().addComponentListener(rootPanelComponentListener);
			graphComponent.addMouseWheelListener(getGraphMouseListener());
			graphComponent.getGraphControl().addMouseListener(getGraphMouseListener());
			graphOutline = new mxGraphOutline(graphComponent);
			graphOutline.addMouseListener(getGraphOutlineListener());
			graphOutline.setVisible(false);
			graphOutline.setBounds(0, 0, 200, 200);
		} else {
			graphComponent.setGraph(graph);
		}
		graphComponent.setConnectable(false);

		// creo i nodi
		createNodesGraph(documentoNode, new HashMap<Integer, Object>(), graph);

		// applico il layout
		layout = GraphLayoutsFactory.getLayout(GraphLayoutsFactory.HIERARCHICAL_HORIZONTAL_LAYOUT, graph);

		graph.getModel().beginUpdate();

		try {
			layout.execute(graph.getDefaultParent());

			Object[] cells = graph.getChildVertices(graph.getDefaultParent());
			for (int i = 0; i < cells.length; i++) {
				graph.updateCellSize(cells[i]);
			}
		} finally {
			mxMorphing morph = new mxMorphing(graphComponent, 20, 1.2, 20);
			morph.addListener(mxEvent.DONE, new mxIEventListener() {
				@Override
				public void invoke(Object sender, mxEventObject evt) {
					graph.getModel().endUpdate();
					graphComponent.scrollCellToVisible(cellaDocumentoDiPartenza, true);
				}
			});
			morph.startAnimation();
		}

		// Aggiungo il listener per visualizzare la toolbar agganciata alla cella sopra il mouse, altrimenti la
		// nascondo.
		new mxCellTracker(graphComponent, Color.RED) {

			private static final long serialVersionUID = 230975584619345930L;

			@Override
			public void mouseMoved(MouseEvent e) {
				Object cell = graphComponent.getCellAt(e.getX(), e.getY());

				if (cell != null && cell instanceof mxCell && ((mxCell) cell).getValue() instanceof DocumentoNode) {
					if (!toolbar.getControl().isVisible() && graph.getView().getState(cell) != null) {
						mxCellState state = graph.getView().getState(cell);

						mxPoint pointState = state.getOrigin();
						double scala = graph.getView().getScale();

						double xMX = (pointState.getPoint().getX()) * scala;
						double yMX = (pointState.getPoint().getY()) * scala;

						Point point = new mxPoint(xMX, yMX).getPoint();

						SwingUtilities.convertPointToScreen(point, graphComponent.getGraphControl());
						SwingUtilities.convertPointFromScreen(point, layeredPanel);

						toolbar.getControl().setBounds(point.x - 25, point.y, 25, 200);
						toolbar.getControl().setVisible(true);
						toolbar.update((mxCell) cell, graphComponent, layout);
					}
				} else {
					toolbar.getControl().setVisible(false);
				}
			}

		};

		graphComponent.setBorder(null);
		return graphComponent;
	}

	/**
	 * Crea nel grafico tutte le celle e i collegamenti del nodo specificato.
	 *
	 * @param documentoNode
	 *            nodo di riferimento
	 * @param mapVertex
	 *            mappa dei nodi già creati
	 * @param graph
	 *            grafico
	 * @return nodo creato
	 */
	private Object createNodesGraph(DocumentoNode documentoNode, Map<Integer, Object> mapVertex, mxGraph graph) {

		// se il nodo risulta già creato prendo quello che c'è altrimenti lo creo
		Object vertex = mapVertex.get(documentoNode.getIdDocumento());
		if (vertex == null) {

			if (documentoGraph.getDocumento().getId().equals(documentoNode.getIdDocumento())) {

				// visto che è il documento di riferimento dell'editor lo aggiungo alla breadcrumbbar
				addNodeToBreadCrumbBar(documentoNode);

				String style = SELECTED_CELL_STYLE_NAME + ";fillColor=" + documentoNode.getColore();
				vertex = graph.insertVertex(graph.getDefaultParent(), null, documentoNode, 20, 20, 250, 150, style);

				cellaDocumentoDiPartenza = (mxCell) vertex;
			} else {
				String style = NORMAL_CELL_STYLE_NAME + ";fillColor=" + documentoNode.getColore();
				vertex = graph.insertVertex(graph.getDefaultParent(), null, documentoNode, 20, 20, 250, 150, style);
			}
			mapVertex.put(documentoNode.getIdDocumento(), vertex);
		}

		// creo ricorsivamente tutti i nodi successivi
		for (Entry<DocumentoNode, String> nextEntry : documentoNode.getNextNodes().entrySet()) {
			Object vertexNext = mapVertex.get(nextEntry.getKey().getIdDocumento());
			if (vertexNext == null) {
				vertexNext = createNodesGraph(nextEntry.getKey(), mapVertex, graph);
			}
			if (!loadAllNodes) {
				graph.insertEdge(graph.getDefaultParent(), null, nextEntry.getValue(), vertex, vertexNext);
			}
		}

		// creo ricorsivamente tutti i nodi precedenti
		for (Entry<DocumentoNode, String> prevEntry : documentoNode.getPreviousNodes().entrySet()) {
			Object vertexPrev = mapVertex.get(prevEntry.getKey().getIdDocumento());
			if (vertexPrev == null) {
				vertexPrev = createNodesGraph(prevEntry.getKey(), mapVertex, graph);
			}
			graph.insertEdge(graph.getDefaultParent(), null, prevEntry.getValue(), vertexPrev, vertex);
		}

		// creo i nodi dei documenti di destinazione linkati
		for (DocumentoNode documentoNodeLink : documentoNode.getLinkNodesDestinazione()) {
			Object vertexLink = mapVertex.get(documentoNodeLink.getIdDocumento());
			if (vertexLink == null) {
				vertexLink = createNodesGraph(documentoNodeLink, mapVertex, graph);
			}
			graph.insertEdge(graph.getDefaultParent(), null, "collegato a", vertex, vertexLink);
		}

		// creo i nodi dei documenti di origine linkati
		for (DocumentoNode documentoNodeLink : documentoNode.getLinkNodesOrigine()) {
			Object vertexLink = mapVertex.get(documentoNodeLink.getIdDocumento());
			if (vertexLink == null) {
				vertexLink = createNodesGraph(documentoNodeLink, mapVertex, graph);
			}
			graph.insertEdge(graph.getDefaultParent(), null, "collegato a", vertexLink, vertex);
		}

		return vertex;

	}

	@Override
	public void dispose() {
		rootPanel.removeComponentListener(rootPanelComponentListener);

		toolbar.dispose();

		graphOutline.removeMouseListener(graphOutlineListener);

		breadCrumbBar.removeMouseListener(bredCrumbBarSelectionListener);

		super.dispose();
	}

	/**
	 * @return Returns the bredCrumbBarSelectionListener.
	 */
	public BredCrumbBarSelectionListener getBredCrumbBarSelectionListener() {
		if (bredCrumbBarSelectionListener == null) {
			bredCrumbBarSelectionListener = new BredCrumbBarSelectionListener();
		}

		return bredCrumbBarSelectionListener;
	}

	@Override
	public JComponent getControl() {
		return getRootPanel();
	}

	/**
	 * @return Returns the graphComponent.
	 */
	public mxGraphComponent getGraphComponent() {
		return graphComponent;
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
	 * @return Returns the graphOutline.
	 */
	public mxGraphOutline getGraphOutline() {
		return graphOutline;
	}

	/**
	 * @return Returns the graphOutlineListener.
	 */
	public GraphOutlineListener getGraphOutlineListener() {
		if (graphOutlineListener == null) {
			graphOutlineListener = new GraphOutlineListener();
		}

		return graphOutlineListener;
	}

	@Override
	public String getId() {
		return "documentoGraphEditor";
	}

	/**
	 * @return Returns the layout.
	 */
	public mxGraphLayout getLayout() {
		return layout;
	}

	/**
	 * @return pannello principale
	 */
	private JPanel getRootPanel() {
		if (rootPanel == null) {
			rootPanel = getComponentFactory().createPanel(new BorderLayout());
			rootPanel.add(layeredPanel, BorderLayout.CENTER);

			JPanel barsPanel = getComponentFactory().createPanel(new BorderLayout());
			toolBarGraph = new ToolbarDocumentoGraph(this);
			barsPanel.add(toolBarGraph.getControl(), BorderLayout.NORTH);

			breadCrumbBar = new JList(new DefaultListModel());
			breadCrumbBar.setVisibleRowCount(1);
			breadCrumbBar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			breadCrumbBar.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			breadCrumbBar.setCellRenderer(new BreadcrumbDocumentoListRenderer());
			breadCrumbBar.setOpaque(false);
			breadCrumbBar.addMouseListener(getBredCrumbBarSelectionListener());

			JPanel breadCrumbPanel = getComponentFactory().createPanel(new BorderLayout());
			breadCrumbPanel.add(new SimpleScrollPane(breadCrumbBar), BorderLayout.CENTER);
			barsPanel.add(breadCrumbPanel, BorderLayout.SOUTH);

			rootPanel.add(barsPanel, BorderLayout.NORTH);

			// devo aggiungere un listener sul resize del pannello principale perchè il layered pane non ha un layout e
			// quindi devo settargli le dimensioni
			rootPanelComponentListener = new RootPanelComponentListener();
		}
		return rootPanel;
	}

	@Override
	public void initialize(Object editorObject) {

		if (toolBarGraph != null) {
			toolBarGraph.resetControl();
		}

		loadAllNodes = Boolean.FALSE;

		documentoGraph = (DocumentoGraph) editorObject;

		layeredPanel.removeAll();
		layeredPanel.setVisible(true);

		createGraph();
		layeredPanel.add(graphComponent, new Integer(0));
		graphComponent.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		graphComponent.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		toolbar.getControl().setVisible(false);
		layeredPanel.add(toolbar.getControl(), new Integer(2));

		layeredPanel.add(graphOutline, new Integer(1));
	}

	@Override
	public void save(ProgressMonitor saveProgressTracker) {
		throw new UnsupportedOperationException("SAVE....operazione non supportata.");
	}

	/**
	 * @param documentiBD
	 *            The documentiBD to set.
	 */
	public void setDocumentiBD(IDocumentiBD documentiBD) {
		this.documentiBD = documentiBD;
	}

	/**
	 * @param layout
	 *            The layout to set.
	 */
	public void setLayout(mxGraphLayout layout) {
		this.layout = layout;
	}

	/**
	 * @param loadAllNodes
	 *            The loadAllNodes to set.
	 */
	public void setLoadAllNodes(boolean loadAllNodes) {
		this.loadAllNodes = loadAllNodes;
	}

}
