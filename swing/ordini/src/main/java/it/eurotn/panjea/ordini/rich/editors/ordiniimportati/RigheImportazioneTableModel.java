package it.eurotn.panjea.ordini.rich.editors.ordiniimportati;

import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.StyleTableModel;

public class RigheImportazioneTableModel extends DefaultBeanTableModel<RigaOrdineImportata> implements StyleTableModel {

	public enum DatiErrore {
		PREZZO_ZERO, PREZZO_DIVERSO, PAGAMENTO_DIVERSO, NON_ASSORTIMENTO
	}

	public enum DatiMancanti {
		ARTICOLI, ENTITA, PAGAMENTI, AGENTI
	}

	public static final CellStyle DATI_COMMERCIALI_DIVERSI_CELL_STYLE = new CellStyle();
	public static final CellStyle ORDINE_BLOCCATO_CELL_STYLE = new CellStyle();

	{
		DATI_COMMERCIALI_DIVERSI_CELL_STYLE.setBackground(Color.YELLOW);
		ORDINE_BLOCCATO_CELL_STYLE.setForeground(Color.RED);
	}

	private static final long serialVersionUID = -8519966193850139696L;;

	private ConverterContext context = new NumberWithDecimalConverterContext();
	private Map<DatiMancanti, Collection<String>> datiMancanti;
	private Map<DatiErrore, Collection<RigaOrdineImportata>> datiErrore;

	/**
	 * Costruttore di default.
	 * 
	 * @param id
	 *            id tabella
	 */
	public RigheImportazioneTableModel(final String id) {
		super(id, new String[] { "selezionata", "ordine.agente", "ordine.entita", "ordine.sedeEntita",
				"ordine.tipoDocumento", "ordine.pagamento", "ordine.numero", "ordine.data", "articolo", "numeroRiga",
				"omaggio", "qta", "scostamentoPrezzo", "totaleRiga", "totaleRigaDeterminato", "percProvvigione",
				"attributi" }, RigaOrdineImportata.class);
	}

	@Override
	public CellStyle getCellStyleAt(int row, int col) {
		CellStyle cellStyle = null;
		RigaOrdineImportata riga = getElementAt(row);

		switch (col) {
		case 5:
			if (!riga.getOrdine().isPagamentoStandard()) {
				cellStyle = DATI_COMMERCIALI_DIVERSI_CELL_STYLE;
			}
			break;
		case 12:
			if (riga.getScostamentoPrezzo() == null) {
				cellStyle = DATI_COMMERCIALI_DIVERSI_CELL_STYLE;
			} else if (riga.getScostamentoPrezzo().compareTo(BigDecimal.ZERO) != 0) {
				cellStyle = DATI_COMMERCIALI_DIVERSI_CELL_STYLE;
			}
			break;
		default:
			cellStyle = null;
		}
		if (riga.getOrdine().isSedeBloccata()) {
			cellStyle = ORDINE_BLOCCATO_CELL_STYLE;
		}
		return cellStyle;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 11:
			context.setUserObject(3);
			return context;
		case 12:
		case 13:
		case 14:
		case 15:
			context.setUserObject(2);
			return context;
		default:
			return null;
		}
	}

	/**
	 * @return dati errore da modificare per la creazione degli ordini
	 */
	public Map<DatiErrore, Collection<RigaOrdineImportata>> getDatiErrore() {
		return datiErrore;
	}

	/**
	 * @return dati mancanti per confermare l'importazione
	 */
	public Map<DatiMancanti, Collection<String>> getDatiMancanti() {
		return datiMancanti;
	}

	@Override
	public EditorContext getEditorContextAt(int row, int column) {
		switch (column) {
		case 0:
			return StatoRigaOrdineImportataGroupCellRenderer.STATO_RIGA_ORDINE_IMPORTATA_CONTEXT;
		case 9:
			return NumeroRigaCellRenderer.NUMERO_RIGA_CONTEXT;
		case 14:
			return PrezzoDeterminatoCellRenderer.PREZZO_DETERMINATO_CONTEXT;
		default:
			return super.getEditorContextAt(row, column);
		}
	}

	/**
	 * @return restituisce gli indici delle colonne che non possono essere raggruppate
	 */
	public int[] getNonGroupableColumns() {
		return new int[] { 0, 8, 9, 10, 11, 12, 13, 14 };
	}

	/**
	 * Inizializza i dati di errore e i dati mancanti.
	 */
	public void initDatiMancanti() {
		datiMancanti = new HashMap<DatiMancanti, Collection<String>>();
		datiMancanti.put(DatiMancanti.ARTICOLI, new HashSet<String>());
		datiMancanti.put(DatiMancanti.ENTITA, new HashSet<String>());
		datiMancanti.put(DatiMancanti.PAGAMENTI, new HashSet<String>());
		datiMancanti.put(DatiMancanti.AGENTI, new HashSet<String>());

		datiErrore = new HashMap<DatiErrore, Collection<RigaOrdineImportata>>();
		datiErrore.put(DatiErrore.PREZZO_ZERO, new HashSet<RigaOrdineImportata>());
		datiErrore.put(DatiErrore.PREZZO_DIVERSO, new HashSet<RigaOrdineImportata>());
		datiErrore.put(DatiErrore.PAGAMENTO_DIVERSO, new HashSet<RigaOrdineImportata>());
		datiErrore.put(DatiErrore.NON_ASSORTIMENTO, new HashSet<RigaOrdineImportata>());

		for (RigaOrdineImportata riga : getObjects()) {
			if (riga.getArticolo() == null) {
				datiMancanti.get(DatiMancanti.ARTICOLI).add(riga.getCodiceArticolo());
			}
			if (riga.getOrdine().getAgente() == null && riga.getOrdine().getCodiceAgente() != null) {
				datiMancanti.get(DatiMancanti.AGENTI).add(riga.getOrdine().getCodiceAgente());
			}
			if (riga.getOrdine().getEntita() == null) {
				datiMancanti.get(DatiMancanti.ENTITA).add(riga.getOrdine().getCodiceEntita());
			}
			if (riga.getOrdine().getPagamento() == null) {
				datiMancanti.get(DatiMancanti.PAGAMENTI).add(riga.getOrdine().getCodicePagamento());
			}
			if (riga.getPrezzoUnitarioDeterminato().compareTo(BigDecimal.ZERO) == 0) {
				datiErrore.get(DatiErrore.PREZZO_ZERO).add(riga);
			}
			if (riga.getScostamentoPrezzo() == null || riga.getScostamentoPrezzo().compareTo(BigDecimal.ZERO) != 0) {
				datiErrore.get(DatiErrore.PREZZO_DIVERSO).add(riga);
			}
			if (!riga.getOrdine().isPagamentoStandard()) {
				datiErrore.get(DatiErrore.PAGAMENTO_DIVERSO).add(riga);
			}
			if (!riga.isAssortimento()) {
				datiErrore.get(DatiErrore.NON_ASSORTIMENTO).add(riga);
			}
		}
	}

	@Override
	public boolean isCellStyleOn() {
		return true;
	}

}
