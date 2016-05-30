/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti;

import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;

import com.jidesoft.converter.ObjectConverterManager;
import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

/**
 * @author fattazzo
 *
 */
public class ArticoloComponentiGraph extends mxGraph {

	public static final String COMP_PRIMO_LV_CELL_STYLE_NAME = "compPrimoLv";
	public static final String ARTICOLO_EDITOR_CELL_STYLE_NAME = "articoloEditor";
	public static final String ARTICOLO_NON_MODIFICABILE_CELL_STYLE_NAME = "articoloNonModificabile";

	{
		setHtmlLabels(true);
		setCellsResizable(false);
		setCellsEditable(false);
		setCellsLocked(true);
		setCellsDisconnectable(false);
		setCellsDeletable(false);
		setCellsBendable(false);
		setCellsSelectable(false);
		setConnectableEdges(false);
		getStylesheet().putCellStyle(COMP_PRIMO_LV_CELL_STYLE_NAME, new ComponentePrimoLivelloCellStyle());
		getStylesheet().putCellStyle(ARTICOLO_EDITOR_CELL_STYLE_NAME, new ArticoloEditorCellStyle());
		getStylesheet().putCellStyle(ARTICOLO_NON_MODIFICABILE_CELL_STYLE_NAME, new ArticoloNonModificabileCellStyle());
	}

	/**
	 * Costruttore.
	 */
	public ArticoloComponentiGraph() {
		super();
	}

	@Override
	public String convertValueToString(Object cell) {
		if (cell instanceof mxCell) {
			Object value = ((mxCell) cell).getValue();
			if (value instanceof Componente) {
				Componente componente = (Componente) value;
				StringBuilder sb = new StringBuilder(200);
				sb.append("<b>");
				sb.append(componente.getArticolo().getCodice());
				sb.append("<br>");
				sb.append(componente.getArticolo().getDescrizione());
				// sb.append(ObjectConverterManager.toString(componente.getArticolo()));
				sb.append("</b><br> Formula qta: ");
				sb.append(componente.getFormula());
				return sb.toString();
			} else if (value instanceof ArticoloConfigurazioneDistinta) {
				ArticoloConfigurazioneDistinta articolo = (ArticoloConfigurazioneDistinta) value;
				StringBuilder sb = new StringBuilder(200);
				sb.append("<b>");
				sb.append(articolo.getCodice());
				sb.append("<br>");
				sb.append(articolo.getDescrizione());
				// sb.append(ObjectConverterManager.toString(componente.getArticolo()));
				return sb.toString();// ObjectConverterManager.toString(value);
			}
			return ObjectConverterManager.toString(value);
		}
		return super.convertValueToString(cell);
	}

}
