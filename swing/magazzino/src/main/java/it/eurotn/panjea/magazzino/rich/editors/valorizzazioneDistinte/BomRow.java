package it.eurotn.panjea.magazzino.rich.editors.valorizzazioneDistinte;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.mrp.domain.Bom;

import java.math.BigDecimal;

import com.jidesoft.grid.DefaultExpandableRow;

public class BomRow extends DefaultExpandableRow {

	private Bom bom;

	/**
	 *
	 * @param bom
	 *            bom della riga
	 */
	public BomRow(final Bom bom) {
		super();
		this.bom = bom;
		creaNodo(bom);
	}

	private void creaNodo(Bom nodo) {
		for (Bom figlio : nodo.getFigli()) {
			BomRow row = new BomRow(figlio);
			addChild(row);
		}
	}

	@Override
	public Class<?> getCellClassAt(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return ArticoloLite.class;
		case 1:
			return BigDecimal.class;
		case 2:
			return BigDecimal.class;
		case 3:
			return String.class;
		case 4:
			return BigDecimal.class;
		case 5:
			return Double.class;
		default:
			break;
		}
		return super.getCellClassAt(columnIndex);
	}

	@Override
	public Object getValueAt(int column) {
		switch (column) {
		case 0:
			return bom.getArticolo();
		case 1:
			return bom.getCosto();
		case 2:
			return bom.getCostoCopia();
		case 3:
			return bom.getFormulaMolt();
		case 4:
			return bom.getMoltiplicatore();
		case 5:
			return bom.getQtaAttrezzaggioArticolo() + bom.getQtaAttrezzaggioDistinta();
		default:
			throw new IllegalArgumentException("numero di colonna non previsto. N: " + column);
		}
	}
}
