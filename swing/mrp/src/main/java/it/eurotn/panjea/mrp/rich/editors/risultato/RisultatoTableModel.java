package it.eurotn.panjea.mrp.rich.editors.risultato;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.mrp.domain.RisultatoMrpFlat;
import it.eurotn.panjea.mrp.rich.renderer.ArticoloRisultatoFlatCellRenderer;
import it.eurotn.panjea.mrp.rich.renderer.GiacenzaMrpRisultatoRenderer;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.rich.factory.navigationloader.NavigationLoaderContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.grid.TableModelWrapperUtils;

public class RisultatoTableModel extends DefaultBeanTableModel<RisultatoMrpFlat> implements StyleModel {
	private static final long serialVersionUID = 8199348147918555910L;
	private static final CellStyle ORDINE_MATCH_CELL_STYLE;
	private static final CellStyle ORDINE_CONFLITTO_CELL_STYLE;
	private static final CellStyle ORDINE_RITARDO_CELL_STYLE;
	private static final CellStyle ORDINE_PRODUZIONE_CELL_STYLE;
	private static final SearchContext FORNITORE_EDITOR_CONTEXT;
	public static final NavigationLoaderContext CONFIGURAZIONE_CONTEXT = new NavigationLoaderContext(
			"configurazioneDistintaArticolo");
	static {
		ORDINE_MATCH_CELL_STYLE = new CellStyle();
		ORDINE_MATCH_CELL_STYLE.setIcon(null);
		ORDINE_MATCH_CELL_STYLE.setFontStyle(-1);
		ORDINE_MATCH_CELL_STYLE.setBackground(Color.GREEN);

		ORDINE_CONFLITTO_CELL_STYLE = new CellStyle();
		ORDINE_CONFLITTO_CELL_STYLE.setIcon(null);
		ORDINE_CONFLITTO_CELL_STYLE.setFontStyle(-1);
		ORDINE_CONFLITTO_CELL_STYLE.setBackground(Color.YELLOW);

		ORDINE_RITARDO_CELL_STYLE = new CellStyle();
		ORDINE_RITARDO_CELL_STYLE.setIcon(null);
		ORDINE_RITARDO_CELL_STYLE.setFontStyle(1);
		ORDINE_RITARDO_CELL_STYLE.setForeground(Color.RED);

		ORDINE_PRODUZIONE_CELL_STYLE = new CellStyle();
		ORDINE_PRODUZIONE_CELL_STYLE.setIcon(null);
		ORDINE_PRODUZIONE_CELL_STYLE.setFontStyle(1);
		ORDINE_PRODUZIONE_CELL_STYLE.setForeground(Color.GRAY);

		FORNITORE_EDITOR_CONTEXT = new SearchContext("anagrafica.denominazione");
		FORNITORE_EDITOR_CONTEXT.setSearchObjectClassKey(EntitaLite.class);
	}

	private Integer idAreaOrdine;

	/**
	 * Costruttore.
	 */
	public RisultatoTableModel() {
		super("risultatoTableModel", new String[] { "articolo", "deposito", "fornitore", "dataDocumento",
				"dataConsegna", "qtaR", "qtaCalcolata", "qtaInArrivo", "tipoAreaOrdineDaGenerare.tipoDocumento",
				"minOrdinabile", "lottoRiordino", "giacenza", "scorta", "ordine", "leadTime", "distinta",
				"configurazioneDistinta", "numRiga", "ordinamento" }, RisultatoMrpFlat.class);
	}

	@Override
	public CellStyle getCellStyleAt(int row, int column) {
		int actualRow = TableModelWrapperUtils.getActualRowAt(this, row);
		if (actualRow == -1) {
			return null;
		}

		RisultatoMrpFlat riga = getObject(row);
		boolean ordineProduzione = riga.getOrdine().getTipoAreaOrdine().isOrdineProduzione();
		if (ordineProduzione) {
			return ORDINE_PRODUZIONE_CELL_STYLE;
		}

		if (riga.getConflitti() != null) {
			return ORDINE_CONFLITTO_CELL_STYLE;
		}

		if (riga.getDataDocumento() != null){
			Date dataOggi=DateUtils.truncate(Calendar.getInstance().getTime(), Calendar.DAY_OF_MONTH);
			Date dataDocumento=DateUtils.truncate(riga.getDataDocumento(), Calendar.DAY_OF_MONTH);
			if (dataDocumento.before(dataOggi)) {
				return ORDINE_RITARDO_CELL_STYLE;
			}
		}

		if (riga.getIdAreaOrdine() != null && riga.getIdAreaOrdine().equals(idAreaOrdine)) {
			return ORDINE_MATCH_CELL_STYLE;
		}
		return null;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
			int actualRow = TableModelWrapperUtils.getActualRowAt(this, row);
			if (actualRow == -1) {
				return null;
			}
			RisultatoMrpFlat riga = getObject(row);
			return new NumberWithDecimalConverterContext(riga.getArticolo().getNumeroDecimaliQta());
		default:
			break;
		}
		return super.getConverterContextAt(row, column);
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		// column = TableModelWrapperUtils.getActualColumnAt(this,
		// convertColumnIndexToModel(column));
		switch (column) {
		case 0:
			return ArticoloRisultatoFlatCellRenderer.ARTICOLO_RISULTATO_FLAT_CONTEXT;
		case 11:
			return GiacenzaMrpRisultatoRenderer.GIACENZA_RISULTATO_FLAT_CONTEXT;
		case 2:
			return FORNITORE_EDITOR_CONTEXT;
		default:
			break;
		}
		return super.getEditorContextAt(row, column);
	}

	@Override
	public NavigationLoaderContext[] getNavigationLoadersContextAt(int row, int column) {
		if (column == 16) {
			return new NavigationLoaderContext[] { CONFIGURAZIONE_CONTEXT };
		}
		return super.getNavigationLoadersContextAt(row, column);
	}

	@Override
	public boolean isCellStyleOn() {
		return true;
	}

	/**
	 * @param idAreaOrdine
	 *            The idAreaOrdine to set.
	 */
	public void setIdAreaOrdine(Integer idAreaOrdine) {
		this.idAreaOrdine = idAreaOrdine;
	}
}
