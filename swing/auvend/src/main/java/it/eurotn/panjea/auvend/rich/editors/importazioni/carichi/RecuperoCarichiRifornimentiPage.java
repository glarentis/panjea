package it.eurotn.panjea.auvend.rich.editors.importazioni.carichi;

import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.rich.forms.ImportazioneCarichiRifornimentiForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class RecuperoCarichiRifornimentiPage extends FormBackedDialogPageEditor {
	public static final String PAGE_ID = "recuperoCarichiRifornimentiPage";

	private IAuVendBD auVendBD;

	/**
	 * 
	 * Costruttore.
	 */
	public RecuperoCarichiRifornimentiPage() {
		super(PAGE_ID, new ImportazioneCarichiRifornimentiForm());
	}

	/**
	 * @return Returns the auVendBD.
	 */
	public IAuVendBD getAuVendBD() {
		return auVendBD;
	}

	@Override
	public void loadData() {
	}

	@Override
	public void onPostPageOpen() {
	}

	@Override
	public boolean onPrePageOpen() {
		return true;
	}

	@Override
	public void refreshData() {
	}

	/**
	 * @param auVendBD
	 *            The auVendBD to set.
	 */
	public void setAuVendBD(IAuVendBD auVendBD) {
		this.auVendBD = auVendBD;
	}

}
