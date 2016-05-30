/**
 * 
 */
package it.eurotn.panjea.ordini.rich.editors.evasione.distintacarico.exception;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

import java.awt.Dimension;
import java.util.Set;

/**
 * @author fattazzo
 * 
 */
public class CodicePagamentoAssenteDialog extends PanjeaTitledPageApplicationDialog {

	private Set<AreaOrdine> areeOrdine;

	/**
	 * Costruttore.
	 * 
	 * @param areeOrdine
	 *            aree ordine da gestire
	 */
	public CodicePagamentoAssenteDialog(final Set<AreaOrdine> areeOrdine) {
		super(new CodicePagamentoEvasioneAssentePage());
		this.areeOrdine = areeOrdine;
		setPreferredSize(new Dimension(800, 300));
		setTitlePaneTitle("Codice pagamento mancante per i seguenti ordini.");
	}

	@Override
	protected Object[] getCommandGroupMembers() {
		return new Object[] { getFinishCommand() };
	};

	@Override
	protected String getFinishCommandId() {
		return "okCommand";
	}

	@Override
	protected String getTitle() {
		return "Codice di pagamento assente";
	};

	@Override
	protected void onAboutToShow() {
		((CodicePagamentoEvasioneAssentePage) getDialogPage()).setRows(areeOrdine);
	};

	@Override
	protected boolean onFinish() {
		return true;
	}

}
