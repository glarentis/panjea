package it.eurotn.panjea.magazzino.rich.editors.valorizzazioneDistinte;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.TreeTableModel;

public class ValorizzazioneDistinteTreeTableModel extends TreeTableModel<BomRow> {
	private static final ConverterContext DECIMALIPREZZOCONTEXT = new NumberWithDecimalConverterContext();
	private static final long serialVersionUID = 4518231381225338283L;

	/**
	 *
	 * @param boms
	 *            lista di bom da visualizzare
	 */
	public ValorizzazioneDistinteTreeTableModel(final Collection<Bom> boms) {
		List<BomRow> result = new ArrayList<>();
		for (Bom bomDTO : boms) {
			BomRow row = new BomRow(bomDTO);
			result.add(row);
		}
		setOriginalRows(result);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return ArticoloLite.class;
		case 1:
			return BigDecimal.class;
		case 2:
			return BigDecimal.class;
		case 3:
			return BigDecimal.class;
		case 4:
			return String.class;
		case 5:
			return BigDecimal.class;
		case 6:
			return Double.class;
		default:
			break;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "Articolo";
		case 1:
			return "Costo ";
		case 2:
			return "Costo copia";
		case 3:
			return "Formula";
		case 4:
			return "Moltiplicatore";
		case 5:
			return "Qta Att.";
		default:
			break;
		}
		return super.getColumnName(columnIndex);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		DECIMALIPREZZOCONTEXT.setUserObject(4);
		switch (column) {
		case 1:
		case 2:
		case 4:
			return DECIMALIPREZZOCONTEXT;
		default:
			break;
		}
		return super.getConverterContextAt(row, column);
	}
}
