/**
 * 
 */
package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.rich.editors.AbstractTablePageEditor;

import java.util.Collection;

/**
 * @author fattazzo
 * 
 */
public class CodicePagamentoEvasioneAssentePage extends AbstractTablePageEditor<AreaOrdine> {

	public static final String PAGE_ID = "codicePagamentoEvasioneAssentePage";

	/**
	 * Costruttore.
	 */
	public CodicePagamentoEvasioneAssentePage() {
		super(PAGE_ID, new String[] { "documento", "documento.entita", "documento.sedeEntita" }, AreaOrdine.class);
	}

	@Override
	public String getTitle() {
		return "Codice di pagamento non trovato";
	}

	@Override
	public Collection<AreaOrdine> loadTableData() {
		return null;
	}

	@Override
	public void onPostPageOpen() {

	}

	@Override
	public Collection<AreaOrdine> refreshTableData() {
		return null;
	}

	@Override
	public void setFormObject(Object object) {

	}

}
