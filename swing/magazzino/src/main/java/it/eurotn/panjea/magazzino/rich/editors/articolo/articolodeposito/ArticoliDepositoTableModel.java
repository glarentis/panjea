/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.articolo.articolodeposito;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloDeposito;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

/**
 * @author leonardo
 */
public class ArticoliDepositoTableModel extends DefaultBeanEditableTableModel<ArticoloDeposito> {

	private static final String ARTICOLI_DEPOSITO_TABLE_MODEL = "articoliDepositoTableModel";
	private static final long serialVersionUID = -2285784718432235589L;

	private Articolo articolo;

	private static ConverterContext qtaContext;
	private static EditorContext qtaEditorContext;

	private static final SearchContext CODICE_DEPOSITO_EDITOR_CONTEXT = new SearchContext("codice");
	private static final ConverterContext CODICE_DEPOSITO_CONVERTER_CONTEXT = new ConverterContext(
			"depositoCodiceContext", "codice");

	private int numeroDecimaliQta;

	static {
		qtaContext = new NumberWithDecimalConverterContext();
		qtaContext.setUserObject(2);

		qtaEditorContext = new EditorContext("qtaEditorContext", 2);
	}

	/**
	 * Costruttore.
	 */
	public ArticoliDepositoTableModel() {
		super(ARTICOLI_DEPOSITO_TABLE_MODEL, new String[] { "deposito", "scorta" }, ArticoloDeposito.class);
	}

	@Override
	protected ArticoloDeposito createNewObject() {
		ArticoloDeposito articoloDeposito = new ArticoloDeposito();
		articoloDeposito.setScorta(0.0);
		articoloDeposito.setArticolo(articolo);
		return articoloDeposito;
	}

	@Override
	public ConverterContext getConverterContextAt(int row, int column) {
		switch (column) {
		case 0:
			return CODICE_DEPOSITO_CONVERTER_CONTEXT;
		case 1:
			qtaContext.setUserObject(numeroDecimaliQta);
			return qtaContext;
		default:
			return super.getConverterContextAt(row, column);
		}
	}

	@Override
	public EditorContext getEditorContextAt(int row, int col) {
		switch (col) {
		case 0:
			return CODICE_DEPOSITO_EDITOR_CONTEXT;
		case 1:
			qtaEditorContext.setUserObject(numeroDecimaliQta);
			return qtaEditorContext;
		default:
			break;
		}
		return super.getEditorContextAt(row, col);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return true;
	}

	@Override
	protected boolean isRowObjectChanged(Object value, int row, int column) {
		Object oldValue = getValueAt(row, column);
		if (value instanceof DepositoLite) {
			DepositoLite oldDeposito = (DepositoLite) oldValue;
			DepositoLite newDeposito = (DepositoLite) value;

			return newDeposito != null && oldDeposito != null
					&& newDeposito.getCodice().equals(oldDeposito.getCodice());
		}
		return super.isRowObjectChanged(value, row, column);
	}

	/**
	 * @param articolo
	 *            The rigaArticolo to set.
	 */
	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
		setNumeroDecimaliQta(articolo);
	}

	/**
	 * @param paramArticolo
	 *            riga articolo
	 */
	private void setNumeroDecimaliQta(Articolo paramArticolo) {
		this.numeroDecimaliQta = 2;
		if (paramArticolo != null && paramArticolo.getNumeroDecimaliQta() != null) {
			this.numeroDecimaliQta = paramArticolo.getNumeroDecimaliQta();
		}
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		super.setValueAt(value, row, column);
	}

}
