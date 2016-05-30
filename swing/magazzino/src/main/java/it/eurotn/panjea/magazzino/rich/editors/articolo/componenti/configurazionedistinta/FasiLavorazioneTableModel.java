package it.eurotn.panjea.magazzino.rich.editors.articolo.componenti.configurazionedistinta;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazione;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

public class FasiLavorazioneTableModel extends DefaultBeanEditableTableModel<FaseLavorazioneArticolo> {

	private static final long serialVersionUID = 5785933161833578437L;
	private static final SearchContext FASE_CODICE_EDITOR_CONTEXT = new SearchContext("codice");
	private static final ConverterContext NUMBERQTA_CONTEXT = new NumberWithDecimalConverterContext(6);
	private static final EditorContext NUMBERQTA_EDITORCONTEXT = new EditorContext("DecimalNumberEditorContext", 6);

	static {
		FASE_CODICE_EDITOR_CONTEXT.setSearchObjectClassKey(FaseLavorazione.class);
	}

	/**
	 * Costruttore.
	 */
	public FasiLavorazioneTableModel() {
		super("fasiLavorazione", new String[] { "faseLavorazione", "descrizione", "qtaAttrezzaggio", "ordinamento" },
				FaseLavorazioneArticolo.class);
	}

	@Override
	public void addObject(FaseLavorazioneArticolo faseLavorazioneArticolo) {
		faseLavorazioneArticolo.setOrdinamento((getRowCount() + 1) * 10);
		super.addObject(faseLavorazioneArticolo);
	}

	@Override
	public ConverterContext getConverterContextAt(int i, int j) {
		if (j == 2) {
			return NUMBERQTA_CONTEXT;
		}
		return super.getConverterContextAt(i, j);
	}

	@Override
	public EditorContext getEditorContextAt(int i, int j) {
		if (j == 0) {
			return FASE_CODICE_EDITOR_CONTEXT;
		}
		if (j == 2) {
			return NUMBERQTA_EDITORCONTEXT;
		}
		return super.getEditorContextAt(i, j);
	}

	@Override
	protected boolean isAllowInsert() {
		return true;
	}

	@Override
	protected boolean isAllowRemove() {
		return true;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return true;
	}

	/**
	 * @param numeroDecimaliQta
	 *            the numeroDecimaliQta to set
	 */
	public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
		NUMBERQTA_CONTEXT.setUserObject(numeroDecimaliQta);
		NUMBERQTA_EDITORCONTEXT.setUserObject(numeroDecimaliQta);
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		super.setValueAt(value, row, column);
	}
}