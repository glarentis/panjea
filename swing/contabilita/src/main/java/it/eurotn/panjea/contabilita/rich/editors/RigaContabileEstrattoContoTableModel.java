package it.eurotn.panjea.contabilita.rich.editors;

import it.eurotn.panjea.anagrafica.rich.table.renderer.NoteCellRenderer;
import it.eurotn.panjea.contabilita.domain.RigaContabileEstrattoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.rich.converter.ColorConverter;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Color;
import java.math.BigDecimal;

import javax.swing.BorderFactory;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.StyleModel;

public class RigaContabileEstrattoContoTableModel extends DefaultBeanTableModel<RigaContabileEstrattoConto> implements
		StyleModel {

	/**
	 * il sotto conto per il quale espongo l'estratto. Utilizzato per creare lo stile sulla cella saldo.
	 */
	private SottoConto sottoconto;
	private final ColorConverter colorConverter = new ColorConverter();

	private static final long serialVersionUID = -6971923108008422202L;

	private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();
	{
		TOTALE_CONTEXT.setUserObject(2);
	}

	/**
	 * Costruttore.
	 */
	public RigaContabileEstrattoContoTableModel() {
		super(RisultatiRicercaEstrattoContoTablePage.PAGE_ID, new String[] { "statoAreaContabile", "dataRegistrazione",
				"dataDocumento", "numeroDocumento", "protocollo", "tipoDocumento", "entita", "documentiCollegati",
				"note", "noteAreaContabile", "importoDare", "importoAvere", "progressivoImporto", "entita.codice",
				"entita.anagrafica.denominazione", "pagina", "riga" }, RigaContabileEstrattoConto.class);
	}

	@Override
	public CellStyle getCellStyleAt(int row, int column) {
		CellStyle style = null;
		if (column == 12) {
			BigDecimal saldo = (BigDecimal) getValueAt(row, 12);
			if (sottoconto.isStileSaldoEnabled(saldo)) {
				style = new CellStyle();
				style.setBorder(BorderFactory.createLineBorder(
						(Color) colorConverter.fromString(sottoconto.getStileSaldo().getBackGroundColor(), null), 1));
				// style.setForeground((Color)
				// colorConverter.fromString(sottoconto.getStileSaldo().getBackGroundColor(),
				// null));
			}
		}
		return style;
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		switch (j) {
		case 10:
		case 11:
		case 12:
			return TOTALE_CONTEXT;
		default:
			return null;
		}
	}

	@Override
	public EditorContext getEditorContextAt(int i, int j) {
		switch (j) {
		case 7:
		case 8:
		case 9:
			EditorContext context = NoteCellRenderer.NOTE_CONTEXT;
			context.setUserObject(NoteCellRenderer.VISUALIZZA_NOTA_INLINE);
			return context;
		default:
			return null;
		}
	}

	@Override
	public boolean isCellStyleOn() {
		return sottoconto != null;
	}

	/**
	 * @param sottoconto
	 *            The sottoconto to set.
	 */
	public void setSottoconto(SottoConto sottoconto) {
		this.sottoconto = sottoconto;
	}

}
