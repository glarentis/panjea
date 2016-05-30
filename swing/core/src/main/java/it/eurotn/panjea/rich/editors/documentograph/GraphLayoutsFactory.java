package it.eurotn.panjea.rich.editors.documentograph;

import javax.swing.SwingConstants;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.view.mxGraph;

public final class GraphLayoutsFactory {

	public static final String CIRCLE_LAYOUT = "com.mxgraph.layout.mxCircleLayout";
	public static final String ORGANIC_LAYOUT = "com.mxgraph.layout.mxOrganicLayout";
	public static final String HIERARCHICAL_HORIZONTAL_LAYOUT = "com.mxgraph.layout.hierarchical.mxHierarchicalLayout.HORIZONTAL";
	public static final String HIERARCHICAL_VERTICAL_LAYOUT = "com.mxgraph.layout.hierarchical.mxHierarchicalLayout.VERTICAL";

	private static String[] layoutsName = new String[] { HIERARCHICAL_HORIZONTAL_LAYOUT, HIERARCHICAL_VERTICAL_LAYOUT };

	/**
	 * Resituisce il layout in base al nome.
	 * 
	 * @param layoutName
	 *            nome del layout
	 * @param graph
	 *            grafico sul quale associare il layout
	 * @return istanza del layout caricato
	 */
	public static mxGraphLayout getLayout(String layoutName, mxGraph graph) {

		mxGraphLayout layout = null;
		if (CIRCLE_LAYOUT.equals(layoutName)) {
			layout = new mxCircleLayout(graph);
		} else if (ORGANIC_LAYOUT.equals(layoutName)) {
			layout = new mxOrganicLayout(graph);
		} else if (HIERARCHICAL_HORIZONTAL_LAYOUT.equals(layoutName)) {
			layout = new mxHierarchicalLayout(graph, SwingConstants.WEST);
			((mxHierarchicalLayout) layout).setInterRankCellSpacing(100D);
		} else if (HIERARCHICAL_VERTICAL_LAYOUT.equals(layoutName)) {
			layout = new mxHierarchicalLayout(graph);
			((mxHierarchicalLayout) layout).setInterRankCellSpacing(100D);

		} else {
			throw new UnsupportedOperationException("Tipo di layout non gestito: " + layoutName);
		}

		return layout;

	}

	/**
	 * @return layouts disponibili
	 */
	public static String[] getLayoutsName() {
		return layoutsName;
	}

	/**
	 * Costruttore.
	 */
	private GraphLayoutsFactory() {
		super();
	}

}
