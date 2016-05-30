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
public class ArticoloEditorCellStyle extends HashMap<String, Object> {

	private static final long serialVersionUID = -5016546745596668662L;

	/**
	 * Costruttore.
	 */
	public ArticoloEditorCellStyle() {
		super();
		put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
		put(mxConstants.STYLE_ROUNDED, true);
		put(mxConstants.STYLE_STROKEWIDTH, 1);
		put(mxConstants.STYLE_STROKECOLOR, "black");
		put(mxConstants.STYLE_FONTCOLOR, "black");
		put(mxConstants.STYLE_FILLCOLOR, "#FFCC99");
	}

}
