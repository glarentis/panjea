package it.eurotn.panjea.magazzino.rich.editors.articolo;

import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.AttributoMagazzino;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import java.math.BigDecimal;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class AttributiArticoloTableModel extends DefaultBeanEditableTableModel<AttributoArticolo> {

	private static final long serialVersionUID = -3589834069288540860L;

	/**
	 * Costruttore.
	 *
	 */
	public AttributiArticoloTableModel() {
		super("AttributiArticoloTableModel", new String[] { "tipoAttributo.nome", "valore", "formula",
				"inserimentoInRiga", "riga", "ordine", "stampa", "obbligatorio", "updatable" }, AttributoArticolo.class);
	}

	@Override
	public Class<?> getCellClassAt(int row, int column) {
		if (column == 1) {
			AttributoMagazzino attributoMagazzino = getObject(row);
			switch (attributoMagazzino.getTipoAttributo().getTipoDato()) {
			case BOOLEANO:
				return Boolean.class;
			case NUMERICO:
				return BigDecimal.class;
			default:
				return String.class;
			}
		}
		return super.getCellClassAt(row, column);
	}

	@Override
	protected Object getColumnValue(Object row, int column) {
		if (column == 1) {
			AttributoArticolo attributoMagazzino = (AttributoArticolo) row;
			switch (attributoMagazzino.getTipoAttributo().getTipoDato()) {
			case BOOLEANO:
				return attributoMagazzino.getValoreTipizzato(Boolean.class);
			case NUMERICO:
				return attributoMagazzino.getValoreTipizzato(BigDecimal.class);
			default:
				return attributoMagazzino.getValoreTipizzato(String.class);
			}
		}
		return super.getColumnValue(row, column);
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int col) {
		if (col == 1) {
			AttributoMagazzino attributoMagazzino = getObject(row);
			return new NumberWithDecimalConverterContext(attributoMagazzino.getTipoAttributo().getNumeroDecimali());
		}
		return super.getConverterContextAt(row, col);
	}

	@Override
	public EditorContext getEditorContextAt(int row, int col) {
		switch (col) {
		case 1:
			AttributoMagazzino attributoMagazzino = getObject(row);
			return new EditorContext("numeroDecimaliEditorContext", attributoMagazzino.getTipoAttributo()
					.getNumeroDecimali());
		case 2:
			SearchContext searchContext = new SearchContext("codice");
			return searchContext;
		default:
			return super.getEditorContextAt(row, col);
		}
	}

	@Override
	protected boolean isAllowInsert() {
		return false;
	}

	@Override
	protected boolean isAllowRemove() {
		return false;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column != 0;
	}

	@Override
	protected Object setColumnValue(Object row, Object value, int column) {
		if (column == 1) {
			AttributoArticolo attributoMagazzino = (AttributoArticolo) row;
			switch (attributoMagazzino.getTipoAttributo().getTipoDato()) {
			case BOOLEANO:
				attributoMagazzino.setValore((Boolean) value);
				break;
			case NUMERICO:
				attributoMagazzino.setValore((BigDecimal) value);
				break;
			default:
				attributoMagazzino.setValore((String) value);
				break;
			}
			return attributoMagazzino;
		}
		return super.setColumnValue(row, value, column);
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		super.setValueAt(value, row, column);
	}

}