/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;

/**
 * @author fattazzo
 * 
 */
public class ArticoloComponentiGraphOutline extends mxGraphOutline {

	private class GraphOutlineListener extends MouseAdapter {

		@Override
		public void mouseExited(MouseEvent e) {
			setVisible(false);
		}
	}

	private static final long serialVersionUID = -4307040475097933002L;

	private GraphOutlineListener graphOutlineListener;

	/**
	 * Costruttore.
	 * 
	 * @param parammxGraphComponent
	 *            GraphComponent
	 */
	public ArticoloComponentiGraphOutline(final mxGraphComponent parammxGraphComponent) {
		super(parammxGraphComponent);
		setVisible(false);
		setBounds(0, 0, 200, 200);
		graphOutlineListener = new GraphOutlineListener();
		addMouseListener(graphOutlineListener);
	}

	/**
	 * Dispose delle risorse
	 */
	public void dispose() {
		removeMouseListener(graphOutlineListener);
	}

}
