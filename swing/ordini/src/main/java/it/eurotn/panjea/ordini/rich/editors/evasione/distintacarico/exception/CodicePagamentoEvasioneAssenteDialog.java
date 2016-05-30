/**
 * 
 */
package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;

import java.util.Set;

/**
 * @author fattazzo
 * 
 */
public class CodicePagamentoEvasioneAssenteDialog extends CodicePagamentoAssenteDialog {

	/**
	 * Costruttore.
	 * 
	 * @param areeOrdine
	 *            aree ordini
	 */
	public CodicePagamentoEvasioneAssenteDialog(final Set<AreaOrdine> areeOrdine) {
		super(areeOrdine);
		setTitlePaneTitle("Non è possibile recuperare il codice pagamento dalle sedi dei seguenti ordini.");
	}

}
