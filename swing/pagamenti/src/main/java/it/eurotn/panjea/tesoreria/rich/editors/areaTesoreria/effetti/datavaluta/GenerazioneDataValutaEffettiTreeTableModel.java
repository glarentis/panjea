package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.effetti.datavaluta;

import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti.RaggruppamentoEffetti;
import it.eurotn.panjea.tesoreria.domain.Effetto;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.richclient.util.RcpSupport;

import ca.odell.glazedlists.GroupingList;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.ExpandableRow;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.grid.TreeTableModel;

public class GenerazioneDataValutaEffettiTreeTableModel extends TreeTableModel<ExpandableRow> implements StyleModel,
ContextSensitiveTableModel {

	private static final ConverterContext CONVERTER_CONTEXT = new ConverterContext("", 2);
	private static final DataValutaCellStyle DATA_VALUTA_CELL_STYLE = new DataValutaCellStyle();

	private static final long serialVersionUID = 4110349344131247519L;

	protected AreaEffetti areaEffetti;
	protected RaggruppamentoEffetti raggruppamento;
	protected boolean dirty;

	/**
	 *
	 * @param areaEffetti
	 *            areaEffetti contenente gli effetti per assegnare la data valuta
	 */
	public GenerazioneDataValutaEffettiTreeTableModel(final AreaEffetti areaEffetti) {
		super();
		this.areaEffetti = areaEffetti;
		dirty = true;
	}

	/**
	 * Crea le righe del tree table
	 */
	public void createRow() {
		if (raggruppamento == null) {
			raggruppamento = RaggruppamentoEffetti.DATA_VALUTA;
		}
		createRow(raggruppamento);
	}

	/**
	 * Crea le righe del tree table.
	 *
	 * @param nuovoRaggruppamento
	 *            raggruppamento per gli effetti
	 *
	 */
	public void createRow(RaggruppamentoEffetti nuovoRaggruppamento) {
		if (this.raggruppamento == nuovoRaggruppamento && !dirty) {
			return;
		}

		((ExpandableRow) getRoot()).removeAllChildren();
		this.raggruppamento = nuovoRaggruppamento;
		dirty = false;

		GroupingList<Effetto> effettiRaggruppati = areaEffetti.getEffettiRaggrupati(nuovoRaggruppamento);

		Map<Date, List<Effetto>> effettiPerDataValuta = new TreeMap<Date, List<Effetto>>();
		// creo la mappa
		for (List<Effetto> list : effettiRaggruppati) {
			Date key = list.get(0).getDataValuta();
			effettiPerDataValuta.put(key, list);
		}

		// crea il tree node dalla mappa ordinata per data valuta.Form
		for (Entry<Date, List<Effetto>> dataValutaEntry : effettiPerDataValuta.entrySet()) {
			DataValutaRow dataValutaRow = new DataValutaRow(dataValutaEntry.getValue());
			addRow(dataValutaRow);
		}

	}

	@Override
	public CellStyle getCellStyleAt(int row, int column) {
		if (getRowAt(row) instanceof DataValutaRow) {
			return DATA_VALUTA_CELL_STYLE;
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {
		return RcpSupport.getMessage("gestioneDataValutaEffettiTableColumn" + column);
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		switch (j) {
		case 3:
			return CONVERTER_CONTEXT;

		default:
			return null;
		}
	}

	@Override
	public boolean isCellStyleOn() {
		return true;
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
		super.setValueAt(arg0, arg1, arg2);
		fireTableDataChanged();
		dirty = true;
	}

}
