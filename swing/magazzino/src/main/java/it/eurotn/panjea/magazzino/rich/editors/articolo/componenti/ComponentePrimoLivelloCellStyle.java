/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti;

import java.util.HashMap;

import com.mxgraph.util.mxConstants;

/**
 * @author fattazzo
 * 
 */
public class ComponentePrimoLivelloCellStyle extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	/**
	 * Costruttore.
	 */
	public ComponentePrimoLivelloCellStyle() {
		super();
		put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		put(mxConstants.STYLE_ROUNDED, true);
		put(mxConstants.STYLE_STROKEWIDTH, 2);
		put(mxConstants.STYLE_STROKECOLOR, "black");
		put(mxConstants.STYLE_FONTCOLOR, "black");
		put(mxConstants.STYLE_FILLCOLOR, "#CCFF99");
	}

}
